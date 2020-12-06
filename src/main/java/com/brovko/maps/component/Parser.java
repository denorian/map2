package com.brovko.maps.component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.HeightService;
import com.brovko.maps.services.iface.ExternalService;
import com.brovko.maps.services.impl.*;
import com.brovko.maps.utils.RoundUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Parser {

	public static final int LONGITUDE_START = -125;
	public static final int LONGITUDE_END = 145;
	public static final int LATITUDE_START = 58;
	public static final int LATITUDE_END = -54;

	private float step;
	private int skipCount;

	private HeightRepo heightRepo;
	private HeightService service;
	private RoundUtil roundUtil;
	private Map<ExternalService,ThreadPoolExecutor> externalServiceMap = new HashMap();
	private Map<ExternalService, Long> statisticMap = new HashMap();

	public Parser(
		HeightRepo heightRepo,
		HeightService service,
		RoundUtil roundUtil,
		HeyWhatsThat heyWhatsThat,
		FloodMap floodMap,
		VoteToVid voteToVid,
		Topocoding topocoding,
		CalcMaps calcMaps,
		FreeMapTools freeMapTools,
		@Value("${parser.step}") float step
	) {
		this.heightRepo = heightRepo;
		this.service = service;
		this.roundUtil = roundUtil;
		this.step = step;

		initExternalService(heyWhatsThat);
		initExternalService(floodMap);
		initExternalService(calcMaps);
		initExternalService(freeMapTools);
	}

	public void run() throws InterruptedException {
		for (float i = LATITUDE_START; i > LATITUDE_END; i -= step) {

			float finalLatitude = roundUtil.customRound(i);

			Map<Float, Height> lineLatitudeMap = heightRepo.findHeightsByLatitude(finalLatitude)
				.stream()
				.collect(Collectors.toMap(
					Height::getLongitude,
					Function.identity(),
					(v1, v2) -> v1
				));

			for (float j = LONGITUDE_START; j < LONGITUDE_END; j += step) {
				float finalLongitude = roundUtil.customRound(j);

				if (lineLatitudeMap.containsKey(finalLongitude)) {
					skipCount++;
					continue;
				}

				ExternalService es = getMinQueueExternalService();

				ThreadPoolExecutor minThreadPoolExecutor = externalServiceMap.get(es);

				if (minThreadPoolExecutor.getQueue().size() < 10000) {
					minThreadPoolExecutor.submit(() -> {
						service.getHeightWithExternalService(finalLatitude, finalLongitude, es);
						return null;
					});
				} else {
					System.out.println("lat = " + finalLatitude + " lon = " + finalLongitude);
					printStatistic();
					Thread.sleep(1000);
				}
			}
		}

		System.out.println("shutdown");

		externalServiceMap.values().forEach(
			threadPoolExecutor -> {
				threadPoolExecutor.shutdown();
				try {
					threadPoolExecutor.awaitTermination(24L, TimeUnit.HOURS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		);
	}

	private void initExternalService(ExternalService externalService){
		if(externalService != null){
			externalServiceMap.put(
				externalService,
				(ThreadPoolExecutor) Executors.newFixedThreadPool(externalService.getThreadCount())
			);
		}
	}

	private ExternalService getMinQueueExternalService(){
		int min = Integer.MAX_VALUE;
		ExternalService resExtService = null;
		for (Map.Entry<ExternalService, ThreadPoolExecutor> pair : externalServiceMap.entrySet()) {
			int poolSize = pair.getValue().getQueue().size();

			if(poolSize < min ){
				min = poolSize;
				resExtService = pair.getKey();
			}
		}

		return resExtService;
	}

	private void printStatistic(){
		System.out.println("skipCount = " + skipCount);
		skipCount = 0;

		if (statisticMap.isEmpty()) {
			externalServiceMap.forEach(
				(es, pool) -> {
					statisticMap.put(es, pool.getCompletedTaskCount());
				}
			);
		} else {
			externalServiceMap.forEach(
				(es, pool) -> {
					System.out.println(
						es.getClass().getSimpleName() + " = " + (pool.getCompletedTaskCount() - statisticMap.get(es))
					);
					statisticMap.put(es, pool.getCompletedTaskCount());
				}
			);
		}
	}
}
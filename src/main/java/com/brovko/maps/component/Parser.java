package com.brovko.maps.component;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.HeightService;
import com.brovko.maps.utils.RoundUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Parser {

	public static final int LONGITUDE_START = -125;
	public static final int LONGITUDE_END = 145;
	public static final int LATITUDE_START = 66;
	public static final int LATITUDE_END = -54;

	@Value("${parser.step}")
	private float step;
	@Autowired
	private HeightRepo heightRepo;
	@Autowired
	private HeightService service;
	@Autowired
	private RoundUtil roundUtil;

	public void run() throws InterruptedException {
		ThreadPoolExecutor heyWhatsThatPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
		ThreadPoolExecutor floodMapPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(128);
		ThreadPoolExecutor voteToVidPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

		int totalSpeed = 0;
		int heySpeed = 0;
		int floodSpeed = 0;
		int voteSpeed = 0;
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

				totalSpeed++;

				float finalLongitude = roundUtil.customRound(j);

				if (lineLatitudeMap.containsKey(finalLongitude)) {
					continue;
				}

				if (heyWhatsThatPool.getQueue().size() < 10000) {
					heySpeed++;
					heyWhatsThatPool.submit(() -> {
						Height height = service.getHeight(finalLatitude, finalLongitude);
						return null;
					});
				}
				 else if (floodMapPool.getQueue().size() < 10000){
					floodSpeed++;
					floodMapPool.submit(() -> {
						Height height = service.getHeightFloodMap(finalLatitude, finalLongitude);
						return null;
					});
				} else if(voteToVidPool.getQueue().size() < 10000){
					voteSpeed++;
					voteToVidPool.submit(() -> {
						Height height = service.getHeightVoteToVid(finalLatitude, finalLongitude);
						return null;
					});
				}  else {
					System.out.println("lat=" + finalLatitude + " lon=" + finalLongitude);
					System.out.println("totalSpeed=" + totalSpeed);
					System.out.println("heySpeed=" + heySpeed);
					System.out.println("floodSpeed=" + floodSpeed);
					System.out.println("voteSpeed=" + voteSpeed);
					totalSpeed = 0;
					heySpeed = 0;
					floodSpeed = 0;
					voteSpeed = 0;
					Thread.sleep(3000);
				}
			}
		}

		heyWhatsThatPool.shutdown();
		System.out.println("shutdown");
		while (!heyWhatsThatPool.awaitTermination(24L, TimeUnit.HOURS)) {
			System.out.println("Not yet. Still waiting for termination");
		}
	}
}
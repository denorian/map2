package com.brovko.maps.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;

public class Parser {

	public static final int LONGITUDE_START = -180;
	public static final int LONGITUDE_END = 180;
	public static final int LATITUDE_START = 67;
	public static final int LATITUDE_END = -54;

	private float step;
	private int precision;
	private HeightRepo heightRepo;

	public Parser(HeightRepo heightRepo, float step, int precision) {
		this.step = step;
		this.precision = precision;
		this.heightRepo = heightRepo;
	}

	public void run() throws InterruptedException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(200	);

		int speed = 0;
		for (float i = LATITUDE_START; i > LATITUDE_END; i -= step) {

			float finalI = customRound(i);

			Map<Float, Height> lineLatitudeMap = heightRepo.findHeightsByLatitude(finalI)
				.stream()
				.collect(Collectors.toMap(
					Height::getLongitude,
					Function.identity(),
					(v1, v2) -> v1
				));

			for (float j = LONGITUDE_START; j < LONGITUDE_END; j += step) {
				speed++;
				float finalJ = customRound(j);

				if(lineLatitudeMap.containsKey(finalJ)) {
					continue;
				}


				executor.submit(() -> {
					new HeightComponent(heightRepo).getHeight(finalI, finalJ);
					return null;
				});

				if (executor.getQueue().size() > 40000) {
					System.out.println("lat=" + finalI + " lon=" + finalJ);
					System.out.println("speed=" + speed);
					speed = 0;
					Thread.sleep(1000);
				}
			}
		}

		executor.shutdown();
		System.out.println("shutdown");
		while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
			System.out.println("Not yet. Still waiting for termination");
		}
	}

	private float customRound(float num) {
		return new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP).floatValue();
	}
}
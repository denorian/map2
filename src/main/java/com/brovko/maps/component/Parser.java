package com.brovko.maps.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.brovko.maps.repositories.HeightRepo;

public class Parser {

	public static final int LONGITUDE_START = -180;
	public static final int LONGITUDE_END = 180;
	public static final int LATITUDE_START = 68;
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
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(120);

		int counter = 0;
		for (float i = LATITUDE_START; i > LATITUDE_END; i -= step) {
			for (float j = LONGITUDE_START; j < LONGITUDE_END; j += step) {

				float finalI = customRound(i);
				float finalJ = customRound(j);
				executor.submit(() -> {
					new HeightComponent(heightRepo).getHeight(finalI, finalJ);
					return null;
				});

				if (executor.getQueue().size() > 10000) {
					System.out.println("lat=" + finalI + " lon=" + finalJ);
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
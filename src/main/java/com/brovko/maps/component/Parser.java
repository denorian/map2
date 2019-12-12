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
	
	private double step;
	private int precision;
	private HeightRepo heightRepo;
	
	public Parser(HeightRepo heightRepo,double step, int precision) {
		this.step = step;
		this.precision = precision;
		this.heightRepo = heightRepo;
	}
	
	public void run() throws InterruptedException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(64);
		
		for (double i = LATITUDE_START; i > LATITUDE_END; i -= step) {
			for (double j = LONGITUDE_START; j < LONGITUDE_END; j += step) {
				
				double finalI = customRound(i);
				double finalJ = customRound(j);
				executor.submit(() -> {
					new HeightComponent(heightRepo).getHeight(finalI, finalJ);
					return null;
				});
				
				if(executor.getQueue().size() > 10000) {
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
	
	private double customRound(double num) {
		return new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP).doubleValue();
	}
}
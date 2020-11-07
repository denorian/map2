package com.brovko.maps.component.workers;

import com.brovko.maps.component.Coordinate;
import com.brovko.maps.component.HeightComponent;
import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;

import java.util.LinkedList;

public class HeightWorkerThread extends Thread {
	
	private float latitude;
	private float longitude;
	private LinkedList<Height> list;
	private HeightRepo heightRepo;
	
	public HeightWorkerThread(float latitude, float longitude, LinkedList<Height> list, HeightRepo heightRepo) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.list = list;
		this.heightRepo = heightRepo;
	}
	
	public void run() {
		list.add(new HeightComponent(heightRepo).getHeight(latitude,longitude));
	}
}
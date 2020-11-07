package com.brovko.maps.component;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.First;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class HeightComponent {
	
	private HeightRepo heightRepo;
	
	public static final int ERROR_VALUE = -1000;
	
	public HeightComponent(HeightRepo heightRepo) {
		this.heightRepo = heightRepo;
	}
	
	public Height getHeight(float latitude, float longitude) {
		Height height;
		
		try {
			List<Height> heights = heightRepo.fetchHeight(latitude, longitude);
			height = heights.get(0);
		} catch (Exception e) {
			short heightElevation = (short) First.getHeight(latitude, longitude);
			height = new Height(latitude, longitude, heightElevation);
			heightRepo.save(height);
		}
		
		return height;
	}
}
package com.brovko.maps.services;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.iface.ExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeightService {

	@Autowired
	private HeightRepo heightRepo;

	public Height getHeight( float latitude, float longitude){
		return heightRepo.findHeightByLatitudeAndLongitude(latitude, longitude);
	}

	public Height getHeightWithExternalService(float latitude, float longitude, ExternalService externalService){
		Height height = heightRepo.findHeightByLatitudeAndLongitude(latitude, longitude);

		if (height == null) {
			height = new Height(
				latitude,
				longitude,
				externalService.getHeight(latitude, longitude)
			);

			heightRepo.save(height);
		}

		return height;
	}
}

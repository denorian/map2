package com.brovko.maps.component;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.First;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@AllArgsConstructor
public class HeightComponent {

	private HeightRepo heightRepo;

	public Height getHeight(float latitude, float longitude) {
		Height height = heightRepo.findHeightByLatitudeAndLongitude(latitude, longitude);

		if (height == null) {
			height = new Height(
				latitude,
				longitude,
				First.getHeight(latitude, longitude)
			);

			heightRepo.save(height);
		}

		return height;
	}
}
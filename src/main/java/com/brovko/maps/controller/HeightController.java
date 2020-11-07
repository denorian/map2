package com.brovko.maps.controller;

import com.brovko.maps.component.HeightComponent;
import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api")
public class HeightController {
	@Autowired
	private HeightRepo heightRepo;

	@RequestMapping(value = "/get-height/", method = RequestMethod.GET)
	public ResponseEntity<Height> getHeight(
		@RequestParam(value = "lat") String latitudeString,
		@RequestParam(value = "lon") String longitudeSting
	) {
		float latitude, longitude;
		try {
			latitude = new BigDecimal(Double.parseDouble(latitudeString))
					.setScale(Height.PRECISION, RoundingMode.UP)
					.floatValue();
			
			longitude = new BigDecimal(Double.parseDouble(longitudeSting))
					.setScale(Height.PRECISION, RoundingMode.UP)
					.floatValue();
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Height height = new HeightComponent(heightRepo).getHeight(latitude, longitude);
		
		return new ResponseEntity(height, HttpStatus.OK);
	}
}
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
	public ResponseEntity<?> getHeight(@RequestParam(value = "lat") String latitudeString, @RequestParam(value = "lon") String longitudeSting) {
		double latitude, longitude;
		try {
			latitude = new BigDecimal(Double.parseDouble(latitudeString))
					.setScale(Height.PRECISION, RoundingMode.UP)
					.doubleValue();
			
			longitude = new BigDecimal(Double.parseDouble(longitudeSting))
					.setScale(Height.PRECISION, RoundingMode.UP)
					.doubleValue();
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		Height height = new HeightComponent(heightRepo).getHeight(latitude, longitude);
		
		return new ResponseEntity(height, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get-height123/", method = RequestMethod.GET)
	public String getHeight2(
			@RequestParam(value = "lon") String longitude,
			@RequestParam(value = "lat") String latitude,
			@RequestParam(value = "height") Integer height) {
		
		Height heightModel = new Height(
				Double.parseDouble(latitude),
				Double.parseDouble(longitude),
				height);
		heightRepo.save(heightModel);
		
		return longitude + " " + latitude;
	}
}
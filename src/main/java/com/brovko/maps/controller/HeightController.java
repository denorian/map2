package com.brovko.maps.controller;

import com.brovko.maps.model.Height;
import com.brovko.maps.services.HeightService;
import com.brovko.maps.utils.RoundUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class HeightController {

	@Autowired
	private RoundUtil roundUtil;
	@Autowired
	private HeightService service;

	@RequestMapping(value = "/get-height/", method = RequestMethod.GET)
	public ResponseEntity<Height> getHeight(
		@RequestParam(value = "lat") String latitudeString,
		@RequestParam(value = "lon") String longitudeSting
	) {
		float latitude, longitude;
		try {
			latitude = roundUtil.customRound(Float.parseFloat(latitudeString));
			longitude = roundUtil.customRound(Float.parseFloat(longitudeSting));
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Height height = service.getHeight(latitude, longitude);
		
		return new ResponseEntity(height, HttpStatus.OK);
	}
}
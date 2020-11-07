package com.brovko.maps.controller;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConsoleController {
	@Autowired
	private HeightRepo heightRepo;

	public void getHeight2(
		@RequestParam(value = "lon") String longitude,
		@RequestParam(value = "lat") String latitude,
		@RequestParam(value = "height") Short height
	) {

		Height heightModel = new Height(
			Float.parseFloat(latitude),
			Float.parseFloat(longitude),
			height
		);

		heightRepo.save(heightModel);

	}
}

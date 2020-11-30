package com.brovko.maps.services.impl;

import org.springframework.stereotype.Service;

@Service
public class VoteToVid extends AbstractExternalService {

	@Override
	public String buildQuery(float latitude, float longitude) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("https://votetovid.ru/hgt/");
		stringBuilder.append(latitude);
		stringBuilder.append(",");
		stringBuilder.append(longitude);
		stringBuilder.append("/");

		return stringBuilder.toString();
	}

	@Override
	public short parseResponce(String response) {
		short height = (short) Math.round(Float.parseFloat(response));
		if (height > ERROR_VALUE) {
			return height;
		}

		return ERROR_VALUE;
	}
}

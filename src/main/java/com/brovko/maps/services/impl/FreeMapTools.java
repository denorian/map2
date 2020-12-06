package com.brovko.maps.services.impl;

import org.springframework.stereotype.Service;

@Service
public class FreeMapTools extends AbstractExternalService {

	@Override
	public int getThreadCount() {
		return 1;
	}

	@Override
	public String buildQuery(float latitude, float longitude) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("https://www.freemaptools.com/ajax/elevation-service.php");
		stringBuilder.append("?lat=");
		stringBuilder.append(latitude);
		stringBuilder.append("&lng=");
		stringBuilder.append(longitude);

		return stringBuilder.toString();
	}

	@Override
	public short parseResponce(String response) {
		response = response.replace("\n", "");
		String[] temp = response.trim().split(" ");
		if (temp.length > 2) {
			short height = Short.parseShort(temp[2]);
			if (height > ERROR_VALUE) {
				return height;
			}
		}

		return ERROR_VALUE;
	}
}

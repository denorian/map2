package com.brovko.maps.services.impl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

@Service
public class FloodMap extends AbstractExternalService {

	@Override
	public int getThreadCount() {
		return 144;
	}

	public String buildQuery(float latitude, float longitude) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("https://www.floodmap.net/pro/elevationmap/getelevation.ashx");
		stringBuilder.append("?lat=");
		stringBuilder.append(latitude);
		stringBuilder.append("&lon=");
		stringBuilder.append(longitude);
		stringBuilder.append("&zoom=9");

		return stringBuilder.toString();
	}

	@Override
	public short parseResponce(String response) {
		JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
		String value = (String) jsonObj.get("value");

		if (!value.isEmpty()) {
			short height = Short.parseShort(value);
			if (height > -1) {
 				return height;
			} else {
				return 0;
			}
		}

		return ERROR_VALUE;
	}
}

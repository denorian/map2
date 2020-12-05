package com.brovko.maps.services.impl;

import org.springframework.stereotype.Service;

@Service
public class Topocoding extends AbstractExternalService {
	String topoUrlChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!*()";
	int topoUrlCharsLength = topoUrlChars.length();
	int topoUrlCharsSqrt = (int) Math.floor(Math.sqrt(topoUrlCharsLength));

	@Override
	public String buildQuery(float latitude, float longitude) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("http://topocoding.com/api/altitude_v1.php?key=BGMCCPGOZNKXPZC");
		stringBuilder.append("?id=");
		stringBuilder.append(1);
		stringBuilder.append("&l=");
		stringBuilder.append(topoEncodeCoordinates(latitude, longitude));

		return stringBuilder.toString();
	}

	private String topoEncodeCoordinates(float latitude, float longitude) {
		String result = "";
		int index;

		for (int j = 0; j < 1; j++) {
			double lat = topoParseAngle(latitude);
			double lon = topoParseAngle(longitude);
			if (lat < 0.0)
				lat = 180 + lat;
			lat = lat / 180f;
			lat = lat - (float) Math.floor(lat);
			if (lon < 0.0)
				lon = 360 + lon;
			lon = lon / 360;
			lon = lon - (float) Math.floor(lon);
			for (int i = 0; i < 3; i++) {
				lat = lat * topoUrlCharsLength;
				index = (int) Math.floor(lat);
				lat = lat - index;
				result = result + topoUrlChars.substring(index, index + 1);
				lon = lon * topoUrlCharsLength;
				index = (int) Math.floor(lon);
				lon = lon - index;
				result = result + topoUrlChars.substring(index, index + 1);
			}
			lat = lat * topoUrlCharsSqrt;
			lon = lon * topoUrlCharsSqrt;
			index = (int) Math.floor(lat) * topoUrlCharsSqrt + (int) Math.floor(lon);
			result = result + topoUrlChars.substring(index, index + 1);
		}
		return result;
	}

	private float topoParseAngle(float value) {
		return Math.round(value * 10000) / 10000f;
	}

	@Override
	public short parseResponce(String response) {
		return 0;
	}
}

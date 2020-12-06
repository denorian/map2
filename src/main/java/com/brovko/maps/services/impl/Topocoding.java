package com.brovko.maps.services.impl;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class Topocoding extends AbstractExternalService {
	private String topoUrlChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!*()";
	private int topoUrlCharsLength = topoUrlChars.length();
	private int topoUrlCharsSqrt = (int) Math.floor(Math.sqrt(topoUrlCharsLength));

	@Override
	public short getHeight(float latitude, float longitude) {
		String url = buildQuery(latitude, longitude);
		String reponse = request(url);
		short height = parseResponce(reponse);

		if (height > ERROR_VALUE) {
			return height;
		}

		return ERROR_VALUE;

	}

	public String request(String url){

		String response = "";
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setConnectTimeout(600000);
			connection.setReadTimeout(600000);

			connection.setRequestProperty("Host", "topocoding.com");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Referer", "http://topocoding.com/demo/google.html");
		//	connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");

			connection.connect();

			StringBuilder stringBuilder = new StringBuilder();
			if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append("\n");
				}
				response = stringBuilder.toString();
				reader.close();
			}

		} catch (Exception cause) {
			//	cause.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return response;
	}


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

	@Override
	public int getThreadCount() {
		return 1;
	}
}

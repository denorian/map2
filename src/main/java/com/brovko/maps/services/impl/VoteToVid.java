package com.brovko.maps.services.impl;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class VoteToVid extends AbstractExternalService {

	@Override
	public int getThreadCount() {
		return 1;
	}

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

	public String request(String url){

		String response = "";
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setConnectTimeout(600000);
			connection.setReadTimeout(600000);

			connection.setRequestProperty("Content-Language", "en-US");

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
}

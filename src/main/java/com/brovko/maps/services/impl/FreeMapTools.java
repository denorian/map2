package com.brovko.maps.services.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class FreeMapTools extends AbstractExternalService {

	@Override
	public int getThreadCount() {
		return 8;
	}

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

			connection.setRequestProperty("Host", "www.freemaptools.com");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
			connection.setRequestProperty("Sec-Fetch-Mode", "cors");
			connection.setRequestProperty("Sec-Fetch-Dest", "empty");
			connection.setRequestProperty("Referer", "https://www.freemaptools.com/elevation-finder.htm");

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
		stringBuilder.append("https://www.freemaptools.com/ajax/elevation-service.php");
		stringBuilder.append("?lat=");
		stringBuilder.append(latitude);
		stringBuilder.append("&lng=");
		stringBuilder.append(longitude);

		return stringBuilder.toString();
	}

	@Override
	public short parseResponce(String response) {
		short value = ERROR_VALUE;
		try {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
			JSONObject elevations = (JSONObject) ((JSONArray) jsonObj.get("elevations")).get(0);
			Double rawValue = (Double) elevations.get("elevation");
			value = (short) Math.round(rawValue);
		} catch (Exception ignored) {}

		if(value < 0)
			return ERROR_VALUE;
		else
			return value;
	}
}
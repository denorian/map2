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
public class ElevationApiIO extends AbstractExternalService{

	@Override
	public int getThreadCount() {
		return 1;
	}

	public String buildQuery(float latitude, float longitude) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("https://elevation-api.io/api/elevation");
		stringBuilder.append("?key=wB26f6f3z43hiwLDdbRBOyCHcuK2oc");
		stringBuilder.append("&resolution=90");
		stringBuilder.append("&points=(");
		stringBuilder.append(latitude);
		stringBuilder.append(",");
		stringBuilder.append(longitude);
		stringBuilder.append(")");

		return stringBuilder.toString();
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

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
			connection.setRequestProperty("origin", "https://www.daftlogic.com");
			connection.setRequestProperty("referer", "https://www.daftlogic.com/");
			connection.setRequestProperty("sec-fetch-dest", "empty");
			connection.setRequestProperty("sec-fetch-mode", "cors");
			connection.setRequestProperty("sec-fetch-site", "cross-site");
			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

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
				cause.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return response;
	}

	@Override
	public short parseResponce(String response) {
		short value = ERROR_VALUE;
		try {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
			String resolution = (String) jsonObj.get("resolution");
			if (resolution.equals("5000m"))
				return ERROR_VALUE;

			JSONObject elevations = (JSONObject) ((JSONArray) jsonObj.get("elevations")).get(0);
			Double rawValue = (Double) elevations.get("elevation");
			value = (short) Math.round(rawValue);
		} catch (Exception ignored) {}

		if (value < 0)
			return ERROR_VALUE;
		else
			return value;
	}
}

package com.brovko.maps.services.impl;

import com.brovko.maps.services.SimpleHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CalcMaps extends AbstractExternalService {

	@Override
	public int getThreadCount() {
		return 4;
	}

	public String buildQuery(float latitude, float longitude) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("https://www.calcmaps.com/ajax.php?op=elevation");
		stringBuilder.append("&lat=");
		stringBuilder.append(latitude);
		stringBuilder.append("&lng=");
		stringBuilder.append(longitude);
		stringBuilder.append("&_=1607234011926");

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

			connection.setRequestProperty("Host", "www.calcmaps.com");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("Accept", "*/*");

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
	public short parseResponce(String response) {
		JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
		Double value = (Double) jsonObj.get("elevation");

		short height = (short) Math.round(value);
		if (height > -1) {
			return height;
		} else {
			return 0;
		}
	}
}
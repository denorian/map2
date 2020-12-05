package com.brovko.maps.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient {

	public static String get(String url){
		
		String response = "";
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setConnectTimeout(600000);
			connection.setReadTimeout(600000);

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

package com.brovko.maps.services;

public class First {
	final static int ERROR_VALUE = -1000;
	private static String DOMAIN = "http://www.heywhatsthat.com/";
	private static String RELATIVE_LINK = "bin/elev-landcover.cgi";
	
	private static String getQuery(double latitude, double longitude) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DOMAIN);
		stringBuilder.append(RELATIVE_LINK);
		stringBuilder.append("?lat=");
		stringBuilder.append(latitude);
		stringBuilder.append("&lon=");
		stringBuilder.append(longitude);
		return stringBuilder.toString();
	}
	
	public static int getHeight(double latitude, double longitude) {
		String reponse = SimpleHttpClient.get(getQuery(latitude, longitude));
		String[] temp = reponse.trim().split(" ");
		if (temp.length > 2) {
			int height = Integer.parseInt(temp[2]);
			if (height > ERROR_VALUE) {
				return height;
			}
		} else {
			System.out.println("Query" + getQuery(latitude, longitude));
			System.out.println("reponse" + reponse);
			//System.exit(999);
		}
		return ERROR_VALUE;
	}
}

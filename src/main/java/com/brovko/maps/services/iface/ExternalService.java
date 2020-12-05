package com.brovko.maps.services.iface;

public interface ExternalService {
	String buildQuery(float latitude, float longitude);
	short getHeight(float latitude, float longitude);
	short parseResponce(String response);
	int getThreadCount();
}

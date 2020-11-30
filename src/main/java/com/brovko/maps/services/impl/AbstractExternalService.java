package com.brovko.maps.services.impl;

import com.brovko.maps.services.SimpleHttpClient;
import com.brovko.maps.services.iface.ExternalService;

public abstract class AbstractExternalService implements ExternalService {
	public final static short ERROR_VALUE = -1000;

	public short getHeight(float latitude, float longitude) {
		String url = buildQuery(latitude, longitude);
		String reponse = SimpleHttpClient.get(url);
		short height = parseResponce(reponse);

		if (height > ERROR_VALUE) {
			return height;
		}

		return ERROR_VALUE;
	}
}

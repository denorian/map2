package com.brovko.maps.services.impl;

import org.springframework.stereotype.Service;

@Service
public class HeyWhatsThat extends AbstractExternalService {

	@Override
	public int getThreadCount() {
		return 96;
	}

	@Override
	public String buildQuery(float latitude, float longitude) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("http://www.heywhatsthat.com/bin/elev-landcover.cgi");
		stringBuilder.append("?lat=");
		stringBuilder.append(latitude);
		stringBuilder.append("&lon=");
		stringBuilder.append(longitude);
		stringBuilder.append("&lcs=nlcd,nlcdcanopy,corine,saland");

		return stringBuilder.toString();
	}

	@Override
	public short parseResponce(String response) {
		response = response.replace("\n", "");
		String[] temp = response.trim().split(" ");
		if (temp.length > 2) {
			short height = Short.parseShort(temp[2].replaceAll("\\D+",""));

			if (height > ERROR_VALUE) {
				return height;
			}
		}

		return ERROR_VALUE;
	}
}

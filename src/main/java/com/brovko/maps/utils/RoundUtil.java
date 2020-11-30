package com.brovko.maps.utils;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class RoundUtil {
	@Value("${parser.precision}")
	private int precision;

	public float customRound(float num) {
		return new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP).floatValue();
	}
}

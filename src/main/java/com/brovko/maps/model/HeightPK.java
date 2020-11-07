package com.brovko.maps.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class HeightPK implements Serializable {
	private float latitude;
	private float longitude;
}
package com.brovko.maps.component;

public class Coordinate {
	private float latitude;
	private float longitude;
	
	public Coordinate(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
		return "latitude=" + latitude + ", longitude=" + longitude;
	}
	
	public String getMapKey(){
		return latitude + "_" + longitude;
	}
}
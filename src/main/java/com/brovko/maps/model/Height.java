package com.brovko.maps.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name="height")
public class Height {
	
	public static final int PRECISION = 4;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="latitude")
	private double latitude;
	@Column(name="longitude")
	private double longitude;
	@Column(name="height")
	private int height;
	
	public Height() {
	}
	
	public Height(double latitude, double longitude, int height) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.height = height;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getMapKey(){
		return latitude + "_" + longitude;
	}
	
	@Override
	public String toString() {
		return "Height{" +
				"id=" + id +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", height=" + height +
				'}';
	}
}

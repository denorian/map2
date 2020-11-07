package com.brovko.maps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="height")
@IdClass(HeightPK.class)
public class Height {
	
	public static final int PRECISION = 4;

	@Id
	@Column(name="latitude")
	private float latitude;

	@Id
	@Column(name="longitude")
	private float longitude;

	@Column(name="height", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.ShortType")
	private short height;

	@Override
	public String toString() {
		return "Height{" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				", height=" + height +
				'}';
	}
}

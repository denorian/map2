package com.brovko.maps.repositories;

import com.brovko.maps.model.Height;
import com.brovko.maps.model.HeightPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeightRepo extends CrudRepository<Height, HeightPK> {

	Height findHeightByLatitudeAndLongitude(
		@Param("latitude") float latitude,
		@Param("longitude") float longitude
	);
	
	@Query("SELECT a FROM Height a WHERE a.latitude >= :latitudeStart and a.latitude <= :latitudeEnd and a.longitude >= :longitudeStart and a.longitude <= :longitudeEnd")
	List<Height> fetchTile(
			@Param("latitudeStart") double latitudeStart,
			@Param("latitudeEnd") double latitudeEnd,
			@Param("longitudeStart") double longitudeStart,
			@Param("longitudeEnd") double longitudeEnd
	);
}
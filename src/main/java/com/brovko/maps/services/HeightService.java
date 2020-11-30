package com.brovko.maps.services;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.impl.FloodMap;
import com.brovko.maps.services.impl.HeyWhatsThat;
import com.brovko.maps.services.impl.VoteToVid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeightService {

	@Autowired
	private HeightRepo heightRepo;
	@Autowired
	private HeyWhatsThat heyWhatsThat;
	@Autowired
	private FloodMap floodMap;
	@Autowired
	private VoteToVid voteToVid;

	public Height getHeight( float latitude, float longitude){
		Height height = heightRepo.findHeightByLatitudeAndLongitude(latitude, longitude);

		if (height == null) {
			height = new Height(
				latitude,
				longitude,
				heyWhatsThat.getHeight(latitude, longitude)
			);

			heightRepo.save(height);
		}

		return height;
	}

	public Height getHeightFloodMap( float latitude, float longitude){
		Height height = heightRepo.findHeightByLatitudeAndLongitude(latitude, longitude);

		if (height == null) {
			height = new Height(
				latitude,
				longitude,
				floodMap.getHeight(latitude, longitude)
			);

			heightRepo.save(height);
		}

		return height;
	}

	public Height getHeightVoteToVid( float latitude, float longitude){
		Height height = heightRepo.findHeightByLatitudeAndLongitude(latitude, longitude);

		if (height == null) {
			height = new Height(
				latitude,
				longitude,
				voteToVid.getHeight(latitude, longitude)
			);

			heightRepo.save(height);
		}

		return height;
	}
}

package com.ianclark226.event_venue.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import java.io.IOException;

import com.ianclark226.event_venue.model.Venue;
import com.ianclark226.event_venue.repo.VenueRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService implements IVenueService {
	private final VenueRepo venueRepo;

	@Override
	public Venue addNewVenue(MultipartFile file, String venueType, BigDecimal venuePrice) throws SQLException, IOException {
		Venue venue = new Venue();
		venue.setVenueType(venueType);
		venue.setVenuePrice(venuePrice);
		if(!file.isEmpty()) {
			byte[] photoBytes = file.getBytes();
			Blob photoBlob = new SerialBlob(photoBytes);
			venue.setPhoto(photoBlob);
		}
		return venueRepo.save(venue);
	}

}

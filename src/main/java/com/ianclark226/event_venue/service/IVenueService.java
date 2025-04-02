package com.ianclark226.event_venue.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import com.ianclark226.event_venue.model.Venue;

public interface IVenueService {
	
	Venue addNewVenue(MultipartFile photo, String venueType, BigDecimal venuePrice) throws SQLException, IOException;

}

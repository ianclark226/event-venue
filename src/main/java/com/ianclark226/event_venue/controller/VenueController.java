package com.ianclark226.event_venue.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ianclark226.event_venue.model.Venue;
import com.ianclark226.event_venue.response.VenueResponse;
import com.ianclark226.event_venue.service.IVenueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
public class VenueController {
	
	private final IVenueService venueService;
	
	@PostMapping("/add/new-venue")
	public ResponseEntity<VenueResponse> addNewVenue(@RequestParam("photo") MultipartFile photo, @RequestParam("venueType") String venueType, @RequestParam("venuePrice") BigDecimal venuePrice) throws SQLException, IOException {
		
		Venue savedVenue = venueService.addNewVenue(photo, venueType, venuePrice);
		VenueResponse response = new VenueResponse(savedVenue.getId(), savedVenue.getVenueType(), savedVenue.getVenuePrice());
		
		return ResponseEntity.ok(response);
		
	}

}

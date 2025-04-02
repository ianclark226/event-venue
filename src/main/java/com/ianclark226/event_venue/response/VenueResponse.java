package com.ianclark226.event_venue.response;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class VenueResponse {
	
	public VenueResponse(Long id, String venueType, BigDecimal venuePrice) {
		this.id = id;
		this.venueType = venueType;
		this.venuePrice = venuePrice;
	}

	public VenueResponse(Long id, String venueType, BigDecimal venuePrice, boolean isBooked, byte[] photoBytes,
			List<BookedVenueResponse> bookings) {
		super();
		this.id = id;
		this.venueType = venueType;
		this.venuePrice = venuePrice;
		this.isBooked = isBooked;
		this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
		this.bookings = bookings;
	}

	private Long id;
	
	private String venueType;
	
	private BigDecimal venuePrice;
	
	private boolean isBooked;
	
	private String photo;
	
	private List<BookedVenueResponse> bookings;

}

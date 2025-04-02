package com.ianclark226.event_venue.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookedVenueResponse {
	
	private Long id;
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private String guestName;
	
	private int numOfPlayers;
	
	private int numOfEvents;
	
	private int totalNumOfBoth;
	
	private String bookingConfirmationCode;
	
	private VenueResponse venue;

	public BookedVenueResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
		this.id = id;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.bookingConfirmationCode = bookingConfirmationCode;
	}
	
	

}

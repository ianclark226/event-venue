package com.ianclark226.event_venue.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Venue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String venueType;
	private BigDecimal venuePrice;
	private boolean isBooked = false;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BookedVenue> bookings;
	
	public Venue() {
		this.bookings = new ArrayList<>();
	}
	
	public void addBooking(BookedVenue booking) {
		if(bookings == null) {
			bookings = new ArrayList<>();
		}
		bookings.add(booking);
		booking.setVenue(this);
		isBooked = true;
		String bookingCode = RandomStringUtils.randomNumeric(10);
		booking.setBookingConfirmationCode(bookingCode);
	}

}

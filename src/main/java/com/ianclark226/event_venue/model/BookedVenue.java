package com.ianclark226.event_venue.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedVenue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	
	@Column(name = "check_In")
	private LocalDate checkInDate;
	
	@Column(name = "check_Out")
	private LocalDate checkOutDate;
	
	@Column(name = "organizer_FullName")
	private String organizerFullName;
	
	@Column(name = "organizer_Email")
	private String organizerEmail;
	
	@Column(name = "events")
	private int numOfEvents;
	
	@Column(name = "players")
	private int numOfPlayers;
	
	@Column(name = "total_of_both")
	private int totalNumberOfBoth;
	
	@Column(name = "confirmation_Code")
	private String bookingConfirmationCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	
	@JoinColumn(name = "venue_id")
	private Venue venue;
	
	
	
	public void calculateTotalNumberOfBoth() {
		this.totalNumberOfBoth = this.numOfPlayers + numOfEvents;
	}

	public int getNumOfEvents() {
		return numOfEvents;
	}

	public void setNumOfEvents(int numOfEvents) {
		this.numOfEvents = numOfEvents;
		calculateTotalNumberOfBoth();
	}

	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
		calculateTotalNumberOfBoth();
	}

	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}
	
	
	
	
	

}

package com.ianclark226.event_venue.response;

import com.ianclark226.event_venue.model.Venue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {


    private Long bookingId;


    private LocalDate startDate;


    private LocalDate endDate;


    private String organizerFullName;


    private String organizerEmail;


    private int numOfAdults;


    private int numOfChildren;


    private int numOfTotalParticipates;


    private String bookingConfirmationCode;


    private VenueResponse venue;

    public BookingResponse(Long bookingId, LocalDate startDate, LocalDate endDate, String bookingConfirmationCode) {
        this.bookingId = bookingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}

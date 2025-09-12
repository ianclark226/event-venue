package com.ianclark226.event_venue.service;

import com.ianclark226.event_venue.model.BookedVenue;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long venueId, BookedVenue bookingRequest);

    List<BookedVenue> getAllBookings();

    BookedVenue findByBookingConfirmationCode(String confirmationCode);

    List<BookedVenue> getBookingsByUserEmail(String email);
}

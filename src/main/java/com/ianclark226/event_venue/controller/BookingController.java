package com.ianclark226.event_venue.controller;

import com.ianclark226.event_venue.exception.InvalidBookingRequestException;
import com.ianclark226.event_venue.exception.ResourceNotFoundException;
import com.ianclark226.event_venue.model.BookedVenue;
import com.ianclark226.event_venue.model.Venue;
import com.ianclark226.event_venue.response.BookingResponse;
import com.ianclark226.event_venue.response.VenueResponse;
import com.ianclark226.event_venue.service.BookingService;
import com.ianclark226.event_venue.service.IBookingService;
import com.ianclark226.event_venue.service.IVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService bookingService;
    private final IVenueService venueService;

    @GetMapping("all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedVenue> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedVenue booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")

    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedVenue booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);

        } catch(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedVenue> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedVenue booking: bookings) {
            BookingResponse bookingResponse =getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedVenue booking) {
        Venue theVenue = venueService.getVenueById(booking.getVenue().getId()).get();
        VenueResponse venue = new VenueResponse
                (theVenue.getId(),
                theVenue.getVenueType(),
                theVenue.getVenuePrice());
        return new BookingResponse(
                booking.getBookingId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getOrganizerFullName(),
                booking.getOrganizerEmail(),
                booking.getNumOfAdults(),
                booking.getNumOfChildren(),
                booking.getNumOfTotalParticipates(), booking.getBookingConfirmationCode(), venue);
    }

    @PostMapping("/venue/{venueId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long venueId,
                                         @RequestBody BookedVenue bookingRequest) {

        try {
            String confirmationCode = bookingService.saveBooking(venueId, bookingRequest);
            return ResponseEntity.ok("Venue booked success! Your booking confirmation code is: " + confirmationCode);

        } catch(InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}

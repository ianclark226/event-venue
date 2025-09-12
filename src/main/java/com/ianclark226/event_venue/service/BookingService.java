package com.ianclark226.event_venue.service;

import com.ianclark226.event_venue.exception.InvalidBookingRequestException;
import com.ianclark226.event_venue.exception.ResourceNotFoundException;
import com.ianclark226.event_venue.model.BookedVenue;
import com.ianclark226.event_venue.model.Venue;
import com.ianclark226.event_venue.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{

    private final BookingRepository bookingRepository;
    private final IVenueService venueService;

    public List<BookedVenue> getAllBookingsByVenueId(Long venueId) {
        return bookingRepository.findByVenueId(venueId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long venueId, BookedVenue bookingRequest) {
        if(bookingRequest.getEndDate().isBefore(bookingRequest.getStartDate())) {
            throw new InvalidBookingRequestException("Start Date must come before End Date");
        }
        Venue venue = venueService.getVenueById(venueId).get();
        List<BookedVenue> existingBookings = venue.getBookings();
        boolean venueIsAvailable =  venueIsAvailable(bookingRequest, existingBookings);
        if(venueIsAvailable) {
            venue.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Sorry, This venue is not available for the selected dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean venueIsAvailable(BookedVenue bookingRequest, List<BookedVenue> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                bookingRequest.getStartDate().equals(existingBooking.getStartDate())
                        || bookingRequest.getEndDate().isBefore(existingBooking.getEndDate())

                        || (bookingRequest.getStartDate().isAfter(existingBooking.getStartDate())
                        && bookingRequest.getStartDate().isBefore(existingBooking.getEndDate()))

                        || (bookingRequest.getStartDate().isBefore(existingBooking.getStartDate())
                        && bookingRequest.getEndDate().equals(existingBooking.getEndDate()))

                        || (bookingRequest.getStartDate().isBefore(existingBooking.getStartDate())
                        && bookingRequest.getEndDate().isAfter(existingBooking.getEndDate()))

                        || (bookingRequest.getStartDate().equals(existingBooking.getEndDate())
                        && bookingRequest.getEndDate().equals(existingBooking.getStartDate()))

                        || (bookingRequest.getStartDate().equals(existingBooking.getEndDate())
                        && bookingRequest.getEndDate().equals(bookingRequest.getStartDate()))
        );
    }

    @Override
    public List<BookedVenue> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public BookedVenue findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));
    }

    @Override
    public List<BookedVenue> getBookingsByUserEmail(String email) {
        return bookingRepository.findByOrganizerEmail(email);
    }
}

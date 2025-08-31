package com.ianclark226.event_venue.repo;

import com.ianclark226.event_venue.model.BookedVenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedVenue, Long> {

    BookedVenue findByBookingConfirmationCode(String confirmationCode);

    List<BookedVenue> findByVenueId(Long venueId);
}

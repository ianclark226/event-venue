package com.ianclark226.event_venue.repo;

import com.ianclark226.event_venue.model.BookedVenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookedVenue, Long> {

    Optional<BookedVenue> findByBookingConfirmationCode(String confirmationCode);

    List<BookedVenue> findByVenueId(Long venueId);

    List<BookedVenue> findByOrganizerEmail(String email);
}

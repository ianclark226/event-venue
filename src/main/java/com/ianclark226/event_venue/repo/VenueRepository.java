package com.ianclark226.event_venue.repo;

import com.ianclark226.event_venue.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("SELECT DISTINCT r.venueType FROM Venue r")
    List<String> findDistinctVenueTypes();

    @Query(" SELECT r FROM Venue r " +
            " WHERE r.venueType LIKE %:venueType% " +
            " AND r.id NOT IN (" +
            " SELECT br.venue.id FROM BookedVenue br " +
            " WHERE ((br.startDate <= :endDate) AND (br.endDate >= :startDate))" +
            ")")
    List<Venue> findAvailableVenuesByDateAndType(LocalDate startDate, LocalDate endDate, String venueType);
}

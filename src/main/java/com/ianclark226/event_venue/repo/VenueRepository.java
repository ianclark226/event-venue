package com.ianclark226.event_venue.repo;

import com.ianclark226.event_venue.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("SELECT DISTINCT r.venueType FROM Venue r")
    List<String> findDistinctVenueTypes();
}

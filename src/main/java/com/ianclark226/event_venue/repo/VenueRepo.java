package com.ianclark226.event_venue.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ianclark226.event_venue.model.Venue;

public interface VenueRepo extends JpaRepository<Venue, Long>{

}

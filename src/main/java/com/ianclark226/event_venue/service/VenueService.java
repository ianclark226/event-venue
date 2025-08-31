package com.ianclark226.event_venue.service;

import com.ianclark226.event_venue.exception.InternalServerException;
import com.ianclark226.event_venue.exception.ResourceNotFoundException;
import com.ianclark226.event_venue.model.Venue;
import com.ianclark226.event_venue.repo.VenueRepository;
import com.ianclark226.event_venue.response.VenueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueService implements IVenueService {

    private final VenueRepository venueRepository;

    @Override
    public Venue addNewVenue(MultipartFile file, String venueType, BigDecimal venuePrice) throws SQLException, IOException {
        Venue venue = new Venue();
        venue.setVenueType(venueType);
        venue.setVenuePrice(venuePrice);
        if(!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            venue.setPhoto(photoBlob);
        }
        return venueRepository.save(venue);
    }

    @Override
    public List<String> getAllVenueTypes() {
        return venueRepository.findDistinctVenueTypes();
    }

    @Override
    public List<Venue> getAllVenue() {
        return venueRepository.findAll();
    }

    @Override
    public byte[] getVenuePhotoByVenueId(Long venueId) throws SQLException {
        Optional<Venue> theVenue = venueRepository.findById(venueId);
        if(theVenue.isEmpty()) {
            throw new ResourceNotFoundException("Apologies, Venue not Found.");
        }
        Blob photoBlob = theVenue.get().getPhoto();
        if(photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteVenue(Long venueId) {

        Optional<Venue> theVenue = venueRepository.findById(venueId);
        if(theVenue.isPresent()) {
            venueRepository.deleteById(venueId);
        }
    }

    @Override
    public Venue updateVenue(Long venueId, String venueType, BigDecimal venuePrice, byte[] photoBytes) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        if(venueType != null) venue.setVenueType(venueType);
        if(venuePrice != null) venue.setVenuePrice(venuePrice);
        if(photoBytes != null && photoBytes.length > 0) {
            try {
                venue.setPhoto(new SerialBlob(photoBytes));
            } catch(SQLException ex) {
                throw new InternalServerException("Error updating venue");
            }
        }
            return venueRepository.save(venue);

    }

    @Override
    public Optional<Venue> getVenueById(Long venueId) {
        return Optional.of(venueRepository.findById(venueId).get());
    }

}

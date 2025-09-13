package com.ianclark226.event_venue.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.ianclark226.event_venue.exception.PhotoRetrievalException;
import com.ianclark226.event_venue.exception.PhotoRetrievealException;
import com.ianclark226.event_venue.exception.ResourceNotFoundException;
import com.ianclark226.event_venue.model.BookedVenue;
import com.ianclark226.event_venue.model.Venue;
import com.ianclark226.event_venue.response.BookingResponse;
import com.ianclark226.event_venue.response.VenueResponse;
import com.ianclark226.event_venue.service.BookingService;
import com.ianclark226.event_venue.service.IVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
@CrossOrigin
public class VenueController {
    private final IVenueService venueService;
    private final BookingService bookingService;

    @PostMapping("/add/new-venue")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<VenueResponse> addNewVenue(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("venueType") String venueType,
            @RequestParam("venuePrice") BigDecimal venuePrice) throws SQLException, IOException {

        Venue savedVenue = venueService.addNewVenue(photo, venueType, venuePrice);
        VenueResponse response = new VenueResponse(savedVenue.getId(), savedVenue.getVenueType(), savedVenue.getVenuePrice());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/venue/types")
    public List<String> getVenueTypes() {
        return venueService.getAllVenueTypes();
    }

    @GetMapping("/all-venues")
    public ResponseEntity<List<VenueResponse>> getAllVenues() throws SQLException {
        List<Venue> venues = venueService.getAllVenue();
        List<VenueResponse> venueResponses = new ArrayList<>();

        for(Venue venue : venues) {
            byte[] photoBytes = venueService.getVenuePhotoByVenueId(venue.getId());
            if(photoBytes != null && photoBytes.length > 0) {
                // Might cause an error
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                VenueResponse venueResponse = getVenueResponse(venue);
                venueResponse.setPhoto(base64Photo);
                venueResponses.add(venueResponse);
            }
        }
        return ResponseEntity.ok(venueResponses);
    }

    @DeleteMapping("/delete/venue/{venueId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long venueId) {
        venueService.deleteVenue(venueId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{venueId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long venueId,
                                                     @RequestParam(required = false) String venueType,
                                                     @RequestParam(required = false) BigDecimal venuePrice,
                                                     @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {

        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : venueService.getVenuePhotoByVenueId(venueId);

        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Venue theVenue = venueService.updateVenue(venueId, venueType, venuePrice, photoBytes);
        theVenue.setPhoto(photoBlob);
        VenueResponse venueResponse = getVenueResponse(theVenue);
        return ResponseEntity.ok(venueResponse);

    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<Optional<VenueResponse>> getVenueById(@PathVariable Long venueId) {
        Optional<Venue> theVenue = venueService.getVenueById(venueId);
        return theVenue.map(venue -> {
            VenueResponse venueResponse = getVenueResponse(venue);
            return ResponseEntity.ok(Optional.of(venueResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }

    @GetMapping("/available-venues")
    public ResponseEntity <List<VenueResponse>> getAvailableVenues(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                   @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                   @RequestParam("venueType") String venueType) throws SQLException {

        List<Venue> availableVenues = venueService.getAvailableVenues(startDate, endDate, venueType);
        List<VenueResponse> venueResponses = new ArrayList<>();
        for(Venue venue : availableVenues) {
            byte[] photoBytes = venueService.getVenuePhotoByVenueId(venue.getId());
            if(photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(photoBytes);
                VenueResponse venueResponse = getVenueResponse(venue);
                venueResponse.setPhoto(photoBase64);
                venueResponses.add(venueResponse);
            }
        }

        if(venueResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(venueResponses);
        }
    }

    private VenueResponse getVenueResponse(Venue venue) {
        List<BookingResponse> bookingInfo = Optional.ofNullable(getAllBookingsByVenueId(venue.getId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getBookingId(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getBookingConfirmationCode()))
                .toList();
        byte[] photoBytes = null;
        Blob photoBlob = venue.getPhoto();
        if(photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());

            } catch(SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new VenueResponse(venue.getId(),
                venue.getVenueType(),
                venue.getVenuePrice(),
                venue.isBooked(), photoBytes, bookingInfo);
    }

    private List<BookedVenue> getAllBookingsByVenueId(Long venueId) {
        return bookingService.getAllBookingsByVenueId(venueId);
    }
}

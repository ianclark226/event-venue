package com.ianclark226.event_venue.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class VenueResponse {

    private Long id;
    private String venueType;
    private BigDecimal venuePrice;
    private boolean isBooked;
    private String photo;
    private List<BookingResponse> bookings;

    public VenueResponse(Long id, String venueType, BigDecimal venuePrice) {
        this.id = id;
        this.venueType = venueType;
        this.venuePrice = venuePrice;
    }

    public VenueResponse(Long id, String venueType, BigDecimal venuePrice, boolean isBooked, byte[] photoBytes, List<BookingResponse> bookings) {
        this.id = id;
        this.venueType = venueType;
        this.venuePrice = venuePrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.bookings = bookings;
    }
}

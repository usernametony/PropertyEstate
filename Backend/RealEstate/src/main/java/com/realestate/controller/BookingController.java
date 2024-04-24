package com.realestate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realestate.exception.InvalidBookingRequestException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.model.BookedProperty;
import com.realestate.model.Property;
import com.realestate.response.BookingResponse;
import com.realestate.response.PropertyResponse;
import com.realestate.service.IBookingService;
import com.realestate.service.IPropertyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	private final IBookingService bookingService;
	private final IPropertyService propertyService;
	
	@GetMapping("/all-bookings")
	public ResponseEntity<List<BookingResponse>> getAllBookings(){
		List<BookedProperty> bookings = bookingService.getAllBookings();
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for(BookedProperty booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
			
		}
		return ResponseEntity.ok(bookingResponses);
	}
	
	@PostMapping("/property/{propertyId}/booking")
	public ResponseEntity<?> saveBooking(@PathVariable int propertyId,@RequestBody BookedProperty bookingRequest){
		try {
			String confirmationCode = bookingService.saveBooking(propertyId, bookingRequest);
			return ResponseEntity.ok(
									"Property Visit booked successfully, Your booking confirmation code is :"+confirmationCode
					);
			
		}catch (InvalidBookingRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		}
		
	}
	
	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
		try {
			BookedProperty booking = bookingService.findByBookingConfirmationCode(confirmationCode);
			BookingResponse bookingResponse = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		}catch (ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
	
	
	@GetMapping("/user/{email}/bookings")
	public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email){
		
		List<BookedProperty> bookings = bookingService.getBookingsByUserEmail(email);
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for(BookedProperty booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
		
	}
	
	@DeleteMapping("/booking/{bookingId}/delete")
	public void cancelBooking(@PathVariable int bookingId) {
		bookingService.cancelBooking(bookingId);
	};
	
	private BookingResponse getBookingResponse(BookedProperty booking) {
		Property theProperty = propertyService.getPropertyById(booking.getProperty().getId()).get();
		PropertyResponse property = new PropertyResponse(
				theProperty.getId(),
				theProperty.getName(),
				theProperty.getDescription(),
				theProperty.getArea(),
				theProperty.getConfiguration(),
				theProperty.getPossession_in(),
				theProperty.getFacing(),
				theProperty.getAddress(),
				theProperty.getDealer_name(),
				theProperty.getMeasurements(),
				theProperty.getOrigin(),
				theProperty.getPropertyType(),
				theProperty.getPropertyPrice()
				
				);
		return new BookingResponse(
				booking.getBookingId(),
				booking.getVisitDate(),
				booking.getGuestName(),
				booking.getGuestEmail(),
				booking.getBookingConformationCode(),
				property
				);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

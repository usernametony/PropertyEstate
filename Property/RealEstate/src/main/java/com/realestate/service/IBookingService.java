package com.realestate.service;

import java.util.List;

import com.realestate.model.BookedProperty;

public interface IBookingService {

	void cancelBooking(int propertyId);
	
	List<BookedProperty> getAllBookingsByPropertyId(int propertyId);
	
	String saveBooking(int propertyId,BookedProperty bookingRequest);
	
	BookedProperty findByBookingConfirmationCode(String confirmationCode);
	
	List<BookedProperty> getAllBookings();
	
	List<BookedProperty> getBookingsByUserEmail(String email);
	
	

}

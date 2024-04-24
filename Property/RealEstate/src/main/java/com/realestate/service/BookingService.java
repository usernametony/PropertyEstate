package com.realestate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.realestate.exception.InvalidBookingRequestException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.model.BookedProperty;
import com.realestate.model.Property;
import com.realestate.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
	private final BookingRepository bookingRepository;
	private final IPropertyService propertyService;

	
	@Override
	public List<BookedProperty> getAllBookingsByPropertyId(int propertyId) {
		return bookingRepository.findByPropertyId(propertyId);
	}


	@Override
	public void cancelBooking(int bookingId) {
		bookingRepository.deleteById(bookingId);
		
	}


	@Override
	public String saveBooking(int propertyId, BookedProperty bookingRequest) {
		
		Property property = propertyService.getPropertyById(propertyId).get();
		List<BookedProperty> existingBookings = property.getBookings();
		boolean propertyIsAvailable = propertyIsAvailable(bookingRequest,existingBookings);
		
		if(propertyIsAvailable) {
			property.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		}else {
			throw new InvalidBookingRequestException("Sorry, This property is not available");
		}
		
		return bookingRequest.getBookingConformationCode();
	}


	private boolean propertyIsAvailable(BookedProperty bookingRequest, List<BookedProperty> existingBookings) {
		
		return existingBookings.stream().noneMatch(existingBooking ->
							bookingRequest.getVisitDate().equals(existingBooking.getVisitDate())
		);
	}


	@Override
	public BookedProperty findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode)
				.orElseThrow(()-> new ResourceNotFoundException("No booking found with booking code :"+ confirmationCode));
	}


	@Override
	public List<BookedProperty> getAllBookings() {
		return bookingRepository.findAll();
	}


	@Override
	public List<BookedProperty> getBookingsByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}

}

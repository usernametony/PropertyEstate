package com.realestate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realestate.model.BookedProperty;

public interface BookingRepository extends JpaRepository<BookedProperty, Integer> {
	
	List<BookedProperty> findByPropertyId(int propertyId);
	
	Optional<BookedProperty> findByBookingConfirmationCode(String confirmationCode);
	
	List<BookedProperty> findByGuestEmail(String email);

}

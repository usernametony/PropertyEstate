package com.realestate.model;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Property {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String propertyType;
	@Lob
	private Blob image;
	private BigDecimal propertyPrice;
	private String description;
	private String area;
	private String configuration;
	private String possession_in;
	private String facing;
	private String address;
	private String dealer_name;
	private String measurements;
	private String origin;
	
	private boolean isBooked=false;
	
	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
	
	private List<BookedProperty> bookings;

	public Property() {
		this.bookings = new ArrayList<>();
	}
	
	
	public void addBooking(BookedProperty booking) {
		if(bookings==null) {
			bookings=new ArrayList<>();
		}
		bookings.add(booking);
		booking.setProperty(this);
		isBooked=true;
		String bookingCode = RandomStringUtils.randomNumeric(5);
		booking.setBookingConformationCode(bookingCode);
	}




	

	


	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
 
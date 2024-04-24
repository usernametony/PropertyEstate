package com.realestate.response;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PropertyResponse {

	private int id;
	private String name;
	private String description;
	private String area;
	private String configuration;
	private String possession_in;
	private String facing;
	private String address;
	private String dealer_name;
	private String measurements;
	private String origin;
	
	private String propertyType;
	private BigDecimal propertyPrice;
	
	private String image;
	private boolean isBooked;
	
	private List<BookingResponse>bookings;


	public PropertyResponse(int id, String name, String description, String area, String configuration,
			String possession_in, String facing, String address, String dealer_name, String measurements, String origin,
			 String propertyType, BigDecimal propertyPrice) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.area = area;
		this.configuration = configuration;
		this.possession_in = possession_in;
		this.facing = facing;
		this.address = address;
		this.dealer_name = dealer_name;
		this.measurements = measurements;
		this.origin = origin;
		this.propertyType = propertyType;
		this.propertyPrice = propertyPrice;
		
		
	}
	
	public PropertyResponse(int id, String name, String description, String area, String configuration,
			String possession_in, String facing, String address, String dealer_name, String measurements, String origin,
			String propertyType, BigDecimal propertyPrice, byte[] imageBytes, boolean isBooked,
			List<BookingResponse> bookingInfo) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.area = area;
		this.configuration = configuration;
		this.possession_in = possession_in;
		this.facing = facing;
		this.address = address;
		this.dealer_name = dealer_name;
		this.measurements = measurements;
		this.origin = origin;
		this.propertyType = propertyType;
		this.propertyPrice = propertyPrice;
		this.image = imageBytes !=null ? Base64.encodeBase64String(imageBytes) : null;
		this.isBooked = isBooked;
		this.bookings = bookingInfo;
	}
	


	public void add(PropertyResponse propertyResponse) {
		// TODO Auto-generated method stub
		
	}

	





	





	


	

	


	


	






	








	
	
	
	
	
	
	
	
	
	
	
	

}

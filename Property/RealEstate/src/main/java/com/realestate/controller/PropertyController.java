package com.realestate.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import com.realestate.exception.ImageRetrievalExcetion;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.model.BookedProperty;
import com.realestate.model.Property;
import com.realestate.response.BookingResponse;
import com.realestate.response.PropertyResponse;
import com.realestate.service.BookingService;
import com.realestate.service.IPropertyService;


import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor

public class PropertyController {
	
	private final IPropertyService propertyService;
	private final BookingService bookingService;
	
	@PostMapping("/add/property")
	public ResponseEntity<PropertyResponse>addNewProperty(
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("area") String area,
			@RequestParam("configuration") String configuration,
			@RequestParam("possession_in") String possession_in,
			@RequestParam("facing") String facing,
			@RequestParam("address") String address,
			@RequestParam("dealer_name") String dealer_name,
			@RequestParam("measurements") String measurements,
			@RequestParam("origin") String origin,
				
			@RequestParam("image") MultipartFile image,
			@RequestParam("propertyType") String propertyType,
			@RequestParam("propertyPrice") BigDecimal propertyPrice) throws SQLException, IOException  {
		Property savedProperty=propertyService.addNewProperty(
				name,
				description,
				area,
				configuration,
				possession_in,
				facing,
				address,
				dealer_name,
				measurements,
				origin,
				
				image,
				propertyType,
				propertyPrice);
		
		PropertyResponse response=new PropertyResponse(
				savedProperty.getId(),
				savedProperty.getName(),
				savedProperty.getDescription(),
				savedProperty.getArea(),
				savedProperty.getConfiguration(),
				savedProperty.getPossession_in(),
				savedProperty.getFacing(),
				savedProperty.getAddress(),
				savedProperty.getDealer_name(),
				savedProperty.getMeasurements(),
				savedProperty.getOrigin(),
				
				savedProperty.getPropertyType(),
				savedProperty.getPropertyPrice()
				);
				return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/property/types")
	public List<String>getPropertyType(){
		return propertyService.getAllPropertyTypes();
	}
	
	@GetMapping("/properties")
	public ResponseEntity< List<PropertyResponse>>getAllProperties() throws SQLException  {
		List<Property> properties = propertyService.getAllProperties();
		List<PropertyResponse> propertyResponses=new ArrayList<>();
		for(Property property: properties) {
			byte[] imageBytes= propertyService.getPropertyImageByPropertyId(property.getId());
			if(imageBytes!= null && imageBytes.length >0) {
				String base64Image= Base64.encodeBase64String(imageBytes);
				PropertyResponse propertyResponse= getPropertyResponse(property);
				propertyResponse.setImage(base64Image);
				propertyResponse.add(propertyResponse);
			}
		}
		return ResponseEntity.ok(propertyResponses);
	}
	
	
	@DeleteMapping("/delete/property/{propertyId}")
	public ResponseEntity<Void> deleteProperty(@PathVariable int propertyId){
		propertyService.deleteProperty(propertyId);
		return new ResponseEntity<> (HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/update/{propertyId}")
	public ResponseEntity<PropertyResponse> updateProperty(@PathVariable int propertyId,
															@RequestParam(required = false) String propertyType,
															@RequestParam(required = false) BigDecimal propertyPrice,
															@RequestParam(required = false) MultipartFile image,
															@RequestParam(required = false) String name,
															@RequestParam(required = false) String description,
															@RequestParam(required = false) String area,
															@RequestParam(required = false) String configuration,
															@RequestParam(required = false) String possession_in,
															@RequestParam(required = false) String facing,
															@RequestParam(required = false) String address,
															@RequestParam(required = false) String dealer_name,
															@RequestParam(required = false) String measurements,
															@RequestParam(required = false) String origin
															
															
			
															) throws SQLException, IOException{
		byte[] imageBytes = image !=null && !image.isEmpty() ?
				image.getBytes() : propertyService.getPropertyImageByPropertyId(propertyId);
		Blob imageBlob = imageBytes !=null && imageBytes.length>0 ? new SerialBlob(imageBytes) :null;
		Property theProperty = propertyService.updateProperty(propertyId,
															  propertyType,
															  propertyPrice,
															  imageBytes,
															  name,
															  description,
															  area,
															  configuration,
															  possession_in,
															  facing,
															  address,
															  dealer_name,
															  measurements,
															  origin
															  );
		theProperty.setImage(imageBlob);
		PropertyResponse propertyResponse = getPropertyResponse(theProperty);
		return ResponseEntity.ok(propertyResponse);
	}
	
	@GetMapping("/property/{propertyId}")
	public ResponseEntity<Optional<PropertyResponse>> getPropertyById(@PathVariable int  propertyId){
		Optional<Property> theProperty = propertyService.getPropertyById(propertyId);
		return theProperty.map(property ->{
			PropertyResponse propertyResponse = getPropertyResponse(property);
			return ResponseEntity.ok(Optional.of(propertyResponse));
		}).orElseThrow(()-> new ResourceNotFoundException("Property not found"));
		
	}
	
//	@GetMapping("/available-properties")
//	public ResponseEntity<List<PropertyResponse>> getAvailableProperties(
//			@RequestParam("visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
//			@RequestParam("propertyType") String propertyType
//			
//			) throws SQLException{
//		List<Property> availableProperties = propertyService.getAvailableProperties(visitDate, propertyType);
//		List<PropertyResponse> propertyResponses = new ArrayList<>();
//		for(Property property : availableProperties) {
//			byte[] imageBytes = propertyService.getPropertyImageByPropertyId(property.getId());
//			if(imageBytes !=null && imageBytes.length >0) {
//				String imageBase64 = Base64.encodeBase64String(imageBytes);
//				PropertyResponse propertyResponse = getPropertyResponse(property);
//				propertyResponse.setImage(imageBase64);
//				propertyResponse.add(propertyResponse);
//				
//			}
//		}
//		if(propertyResponses.isEmpty()) {
//			return ResponseEntity.noContent().build();
//		}else {
//			return ResponseEntity.ok(propertyResponses);
//		}
//		
//	}
	
	private PropertyResponse getPropertyResponse(Property property) {
		List<BookedProperty> bookings = getAllBookingsByPropertyId(property.getId());
		List<BookingResponse> bookingInfo = bookings
				.stream()
				.map( booking -> new BookingResponse(booking.getBookingId(),booking.getVisitDate(),booking.getBookingConformationCode())).toList();
		byte[] imageBytes = null;
		Blob imageBlob = property.getImage();
		if(imageBlob!=null) {
			try {
				imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
				
			}catch( SQLException e) {
				throw new ImageRetrievalExcetion("Error retrieving image");
				
			}
				
			
		}
		return new PropertyResponse(
				property.getId(),
				property.getName(),
				property.getDescription(),
				property.getArea(),
				property.getConfiguration(),
				property.getPossession_in(),
				property.getFacing(),
				property.getAddress(),
				property.getDealer_name(),
				property.getMeasurements(),
				property.getOrigin(),
				property.getPropertyType(),
				property.getPropertyPrice(),
				imageBytes,
				property.isBooked(),bookingInfo);
	}

	

	private List<BookedProperty> getAllBookingsByPropertyId(int propertyId) {
		return bookingService.getAllBookingsByPropertyId(propertyId);
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

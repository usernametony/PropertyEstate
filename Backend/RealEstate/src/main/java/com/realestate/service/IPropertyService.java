package com.realestate.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.realestate.model.Property;

public interface IPropertyService {

	Property addNewProperty(String name, String description, String area, String configuration, String possession_in,
			String facing, String address, String dealer_name, String measurements, String origin, MultipartFile image,
			String propertyType, BigDecimal propertyPrice) throws SQLException, IOException;

	List<String> getAllPropertyTypes();
	
	List<Property> getAllProperties();
	
	byte[] getPropertyImageByPropertyId(int id) throws SQLException;
	
	void deleteProperty(int propertyId);
	
	Property updateProperty(int propertyId, String propertyType,BigDecimal propertyPrice, byte[] imageBytes,String name,String description, String area, String configuration, String possession_in,
			String facing, String address, String dealer_name, String measurements, String origin);
	
	Optional<Property> getPropertyById(int propertyId);
	
//	List<Property> getAvailableProperties(LocalDate visitData,String propertyType);



	

	
	
	

}

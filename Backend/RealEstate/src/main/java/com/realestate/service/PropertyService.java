	package com.realestate.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.realestate.exception.InternalServerException;
import com.realestate.exception.ResourseNotFoundException;
import com.realestate.model.Property;
import com.realestate.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService {
	
	private final PropertyRepository propertyRepository;
	private Integer propertyId; 

	@Override
	public Property addNewProperty(String name, String description, String area, String configuration,
			String possession_in, String facing, String address, String dealer_name, String measurements, String origin,
			MultipartFile image, String propertyType, BigDecimal propertyPrice) throws SQLException, IOException {
		
		Property property=new Property();
		property.setName(name);
		property.setDescription(description);
		property.setArea(area);
		property.setConfiguration(configuration);
		property.setPossession_in(possession_in);
		property.setFacing(facing);
		property.setAddress(address);
		property.setDealer_name(dealer_name);
		property.setMeasurements(measurements);
		property.setOrigin(origin);
		property.setPropertyType(propertyType);
		property.setPropertyPrice(propertyPrice);
		if(!image.isEmpty()) {
			byte[] imageBytes=image.getBytes();
			Blob imageBlob=new SerialBlob(imageBytes);
			property.setImage(imageBlob);
			
		}
		return propertyRepository.save(property);
	}

	@Override
	public List<String> getAllPropertyTypes() {
		return propertyRepository.findDistinctPropertyTypes();
	}

	@Override
	public List<Property> getAllProperties() {
		return propertyRepository.findAll();
	}

	@Override
	public byte[] getPropertyImageByPropertyId(int id) throws SQLException {
		Optional<Property> theProperty = propertyRepository.findById(propertyId);
		if(theProperty.isEmpty()) {
			throw new ResourseNotFoundException("Sorry, Property not found");
		}
		Blob imageBlob=theProperty.get().getImage();
		if(imageBlob!=null) {
			return imageBlob.getBytes(1, (int) imageBlob.length());
		}
		return null;
	}

	@Override
	public void deleteProperty(int propertyId) {
		Optional<Property> theProperty = propertyRepository.findById(propertyId);
		if(theProperty.isPresent()) {
			propertyRepository.deleteById(propertyId);
		}

		
	}

	@Override
	public Property updateProperty(int propertyId, String propertyType, BigDecimal propertyPrice, byte[] imageBytes,
			String name, String description, String area, String configuration, String possession_in, String facing,
			String address, String dealer_name, String measurements, String origin) {
		Property property = propertyRepository.findById(propertyId).get();
		if(propertyType !=null) property.setPropertyType(propertyType);
		if(propertyPrice !=null) property.setPropertyPrice(propertyPrice);
		if(imageBytes !=null && imageBytes.length>0 ) {
			try {
				property.setImage(new SerialBlob(imageBytes));
			}catch (SQLException ex) {
				throw new InternalServerException("Fail updating property");
			}
		}
		if(name !=null) property.setName(name);
		if(description !=null) property.setDescription(description);
		if(area !=null) property.setArea(area);
		if(configuration !=null) property.setConfiguration(configuration);
		if(possession_in !=null) property.setPossession_in(possession_in);
		if(facing !=null) property.setFacing(facing);
		if(address !=null) property.setAddress(address);
		if(dealer_name !=null) property.setDealer_name(dealer_name);
		if(measurements !=null) property.setMeasurements(measurements);
		if(origin !=null) property.setOrigin(origin);
		return propertyRepository.save(property);
	}

	@Override
	public Optional<Property> getPropertyById(int propertyId) {
		return Optional.of(propertyRepository.findById(propertyId).get());
	}

//	@Override
//	public List<Property> getAvailableProperties(LocalDate visitData, String propertyType) {
//		return propertyRepository.findAvailablePropertiesByDateAndType(visitData, propertyType) ;
//	}

	

	

	
	
	
	
	
	
	
	
	
}

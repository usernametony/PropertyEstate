package com.realestate.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.realestate.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
	
	@Query("SELECT DISTINCT p.propertyType FROM Property p")
	List<String> findDistinctPropertyTypes();
	
	
	
//	@Query(" SELECT p FROM Property p " +
//            " WHERE p.propertyType LIKE %:propertyType% " +
//            " AND p.id NOT IN (" +
//            "  SELECT bp.property.id FROM BookedProperty bp " +
//            "  WHERE ((bp.visit_date))"+
//            ")")
//	
//	List<Property> findAvailablePropertiesByDateAndType(LocalDate visitDate, String propertyType);

			

}


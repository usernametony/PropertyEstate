package com.realestate.response;

import java.time.LocalDate;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
	
	private Long id;
	private LocalDate visitDate;
	private String guestName;
	private String guestEmail;
	private String bookingConformationCode;
	private PropertyResponse property;
	public BookingResponse(Long bookingId, LocalDate visitDate, String bookingConformationCode) {
		super();
		this.id = id;
		this.visitDate = visitDate;
		this.bookingConformationCode = bookingConformationCode;
	}


	
}

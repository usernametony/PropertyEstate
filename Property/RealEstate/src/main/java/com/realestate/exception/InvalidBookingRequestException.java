package com.realestate.exception;

public class InvalidBookingRequestException extends RuntimeException {
	public InvalidBookingRequestException(String msg) {
		super(msg);
	}

}

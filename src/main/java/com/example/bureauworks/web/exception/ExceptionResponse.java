package com.example.bureauworks.web.exception;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
	
	private Date timestamp;
	
	private int error;		
	
	private String message;		

	public ExceptionResponse(int error, String message) {		
		this.error = error;
		this.message = message;		
		this.timestamp = new Date();
	}
}
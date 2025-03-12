package com.example.bureauworks.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.bureauworks.core.exception.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    
    @ExceptionHandler(BureuWorksException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(Exception ex) {			
		return new ResponseEntity<>(createError(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandlerNullPointer(NullPointerException ex) {			
		return new ResponseEntity<>(createError(HttpStatus.INTERNAL_SERVER_ERROR, ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> entityNotFoundExceptionHandler(Exception ex) {		
		return new ResponseEntity<>(createError(HttpStatus.NOT_FOUND, ex), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> genericExceptionHandler(Exception ex) {		
		return new ResponseEntity<>(createError(HttpStatus.INTERNAL_SERVER_ERROR, ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse> illegalArgumentExceptionHandler(Exception ex) {		
		return new ResponseEntity<>(createError(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
	}
	
	private ExceptionResponse createError(HttpStatus error, Exception ex) {
		ex.printStackTrace();
		return new ExceptionResponse(error.value(), ex.getMessage());
	}
}

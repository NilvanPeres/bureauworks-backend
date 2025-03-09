package com.example.bureauworks.web.exception;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BureuWorksException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = -5465465498454465L;

	public BureuWorksException(String mensagem) {
		super(mensagem);
	}
}

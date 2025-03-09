package com.example.bureauworks.core.exception;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = -3121231939182312L;

	public EntityNotFoundException(String mensagem) {
		super(mensagem);
	}
}

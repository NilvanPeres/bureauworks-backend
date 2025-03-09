package com.example.bureauworks.web.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.example.bureauworks.core.utils.IsNullUtil.isNullOrEmpty;

public abstract class BaseRestController {

	protected <T> ResponseEntity<T> writeResponseBody(Optional<T> body) {
		return body.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	protected <T> ResponseEntity<List<T>> writeResponseBody(List<T> body) {		
		if (!isNullOrEmpty(body)) {
			return ResponseEntity.ok(body);
		}
		return ResponseEntity.noContent().build();
	}

	protected <T> ResponseEntity<T> writeResponseBody(T body) {
		return ResponseEntity.ok(body);
	}
	
	protected <T> ResponseEntity<T> writeResponseBodyCreated(String value) {
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(value).toUri();
		return ResponseEntity.created(location).build();
	}
	
	protected <T> ResponseEntity<T> writeResponseBodyCreated(Integer value) {
		return writeResponseBodyCreated(String.valueOf(value));
	}
}

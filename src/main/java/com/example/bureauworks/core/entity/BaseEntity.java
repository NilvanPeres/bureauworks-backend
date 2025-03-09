package com.example.bureauworks.core.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1L;

    public abstract T getId();
	
	public abstract void setId(T id);

	@JsonIgnore
	@Column(updatable = false)
	@Schema(hidden = true)
	private LocalDateTime inserted;

	@JsonIgnore
	@Schema(hidden = true)
	private LocalDateTime updated;

	@JsonIgnore
	@Schema(hidden = true)
	private boolean deleted = Boolean.FALSE;

	@PrePersist
	private void setInserted() {
		setInserted(LocalDateTime.now());
		setDeleted(false);
	}

	@PreUpdate
	private void setAlteracaoNow() {
		setUpdated(LocalDateTime.now());
		setDeleted(false);
	}
    
}

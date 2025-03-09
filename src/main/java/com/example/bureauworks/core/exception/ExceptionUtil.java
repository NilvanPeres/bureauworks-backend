package com.example.bureauworks.core.exception;

import java.util.List;
import java.util.function.Supplier;

import com.example.bureauworks.core.entity.BaseEntity;
import com.example.bureauworks.core.entity.EntityUtil;
import com.example.bureauworks.web.exception.BureuWorksException;

public class ExceptionUtil {
	private ExceptionUtil () {}
	
	public static <T> T requireField(T value, String message) {
        if (value == null)
            throw new IllegalArgumentException(message);
		if (value instanceof String && ((String) value).trim().isEmpty())
			throw new IllegalArgumentException(message);
		if (value instanceof Integer && ((Integer) value) <= 0)
			throw new IllegalArgumentException(message);
		if (value instanceof List && ((List<?>) value).isEmpty())
			throw new IllegalArgumentException(message);
        return value;
    }
	
	public static <T> T requireEntity(T value, String message) {				
        if (value == null)
            throw new EntityNotFoundException(message);
        
        if (value instanceof BaseEntity entity) {
			if (EntityUtil.isNullOrEmptyEntity((BaseEntity<?>) entity)) {
    			throw new EntityNotFoundException(message);
    		}	
        }
        
        return value;
    }
	
	public static <T> T exception(String message) {
        throw new BureuWorksException(message);
	}

	public static <T> T exception(boolean condition, Supplier<String> message) {
		throw new BureuWorksException(message.get());
	}
}

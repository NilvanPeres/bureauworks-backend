package com.example.bureauworks.core.entity;

import static com.example.bureauworks.core.utils.IsNullUtil.isNullOrEmpty;


import java.io.Serializable;

public class EntityUtil {
	private EntityUtil () {}

	public static <T extends Serializable> boolean isNullOrEmptyEntity(BaseEntity<T> entity) {
		boolean result = (entity == null) || (entity.getId() == null);
		if (!result && entity.getId() instanceof Number numberKey) {
			result = isNullOrEmpty(numberKey);
		}
		return result;
	}

	public static boolean isNullEntity(BaseEntity<Serializable> entity) {
		return (entity == null);
	}
}

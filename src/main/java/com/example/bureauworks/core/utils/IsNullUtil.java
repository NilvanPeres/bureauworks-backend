package com.example.bureauworks.core.utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class IsNullUtil {
	private IsNullUtil () {}

	public static boolean isNull(Object value) {
		return value == null;
	}

	public static boolean isNullOrEmpty(String value) {
		return (value == null) || (value.trim().length() == 0);
	}

	public static boolean isNullOrEmpty(Object value) {
		return (value == null);
	}

	public static <T> boolean isNullOrEmpty(Collection<T> collection) {
		return isNull(collection) || (collection.isEmpty());
	}

	public static boolean isNullOrEmpty(Number number) {
		return isNull(number) || (number.doubleValue() <= 0);
	}

	public static <T> boolean isNullOrEmpty(Map<T, T> map) {
		return isNull(map) || (map.isEmpty());
	}

	public static boolean isNullOrEmpty(File file) {
		return isNull(file) || file.length() == 0;
	}

	public static boolean isNullOrEmpty(Object[] array) {
		return isNull(array) || (array.length == 0);
	}
}

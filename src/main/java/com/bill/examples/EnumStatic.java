package com.bill.examples;

import java.util.HashMap;
import java.util.Map;

public enum EnumStatic {
	Test("test");
	private String value;

	static {
		cache = new HashMap<>();
	}

	private static Map<String, EnumStatic> cache = null;

	private EnumStatic(String value) {
		this.value = value;
		registerCode(value);
	}

	public String getValue() {
		return this.value;
	}

	private void registerCode(String code) {
		getCache().put(code, this);
	}

	public static EnumStatic find(String code) {
		return cache.get(code);
	}

	private static Map<String, EnumStatic> getCache() {
		if (null == cache) {
			cache = new HashMap<>();
		}

		return cache;
	}

}

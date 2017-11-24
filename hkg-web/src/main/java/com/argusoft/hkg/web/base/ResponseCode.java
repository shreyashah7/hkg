package com.argusoft.hkg.web.base;

public enum ResponseCode {
	
	ERROR(3), WARNING(2), FAILURE(1), SUCCESS(0);
	
	private int value;
	
	private ResponseCode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}

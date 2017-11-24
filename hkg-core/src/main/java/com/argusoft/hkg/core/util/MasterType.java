package com.argusoft.hkg.core.util;

public enum MasterType {
	CUSTOM("C"),BUILT_IN("B");
	
	private String value;

	private MasterType(String value) {
		this.value = value;
	}
	
	public String value(){
		return value;
	}
}

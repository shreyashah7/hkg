package com.argusoft.hkg.web.base;

public class PrimaryKey<T> {

	private T primaryKey;

	public T getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(T primaryKey) {
		this.primaryKey = primaryKey;
	}

	public PrimaryKey(T primaryKey) {
		super();
		this.primaryKey = primaryKey;
	}
	
	public PrimaryKey(){
		super();
	}
	
}

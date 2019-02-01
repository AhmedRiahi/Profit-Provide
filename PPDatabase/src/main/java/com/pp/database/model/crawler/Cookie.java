package com.pp.database.model.crawler;

import org.mongodb.morphia.annotations.Embedded;

import com.pp.database.kernel.PPEntity;

@Embedded
public class Cookie {

	private String name;
	private String value;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
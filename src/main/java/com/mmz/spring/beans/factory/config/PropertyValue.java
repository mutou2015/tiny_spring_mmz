package com.mmz.spring.beans.factory.config;

public class PropertyValue {
	
	private final String name;
	
	private final Object value;
	
	private Class tragetType;

	public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
	
	public PropertyValue(String name, Object value,Class tragetType) {
        this.name = name;
        this.value = value;
        this.tragetType = tragetType;
    }

	
	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
	
	public Class getTragetType() {
		return tragetType;
	}
	
	

}

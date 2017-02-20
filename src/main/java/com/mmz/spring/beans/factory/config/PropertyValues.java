package com.mmz.spring.beans.factory.config;

import java.util.ArrayList;
import java.util.List;



public class PropertyValues {
	
	private final List<PropertyValue> propertyValueList =new ArrayList<PropertyValue>();
	
	// 添加属性
	public void addPropertyValue(PropertyValue propertyValue){
		this.propertyValueList.add(propertyValue);
	}
	
	public List<PropertyValue> getPropertyValues() {
		return this.propertyValueList;
	}

}

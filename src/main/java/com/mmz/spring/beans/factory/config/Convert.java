package com.mmz.spring.beans.factory.config;

import java.text.NumberFormat;

public interface Convert {
	
	Class findPorpertyType(String propertyName,Object bean);
	
	 
}

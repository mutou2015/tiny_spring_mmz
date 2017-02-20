package com.mmz.spring.beans.factory;



public interface BeanFactory {
	
	Object getBean(String name) throws Exception;
	
	<T> T getBean(String name, Class<T> requiredType) throws Exception;

}

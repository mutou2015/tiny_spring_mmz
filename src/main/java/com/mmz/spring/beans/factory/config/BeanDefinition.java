package com.mmz.spring.beans.factory.config;



public interface BeanDefinition {
	Object getBean() throws Exception;
	
	Class getBeanClass() throws Exception;
	
	void setBeanClass(Class beanClass);
	
	void setBean(Object bean);
	
	PropertyValues getPropertyValues();
}

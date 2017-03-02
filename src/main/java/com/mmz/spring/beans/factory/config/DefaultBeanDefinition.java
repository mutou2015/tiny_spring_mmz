package com.mmz.spring.beans.factory.config;



public class DefaultBeanDefinition implements BeanDefinition {
	
	private Object bean;
	
	private Class beanClass;
	
	private String beanClassName;
	
	Boolean lazy_init = false;
	
	private PropertyValues propertyValues = new PropertyValues();

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
		try {
			this.beanClass = Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Boolean getLazy_init() {
		return lazy_init;
	}

	public void setLazy_init(Boolean lazy_init) {
		this.lazy_init = lazy_init;
	}
	
	

}

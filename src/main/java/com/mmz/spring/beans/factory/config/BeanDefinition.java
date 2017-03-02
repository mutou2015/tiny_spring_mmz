package com.mmz.spring.beans.factory.config;



public interface BeanDefinition {
	
	
	
	Object getBean() throws Exception;
	
	Class getBeanClass() throws Exception;
	
	void setBeanClass(Class beanClass);
	
	void setBean(Object bean);
	
	PropertyValues getPropertyValues();
	
	void setBeanClassName(String beanClassName);
	
	public Boolean getLazy_init();

	public void setLazy_init(Boolean lazy_init);
}

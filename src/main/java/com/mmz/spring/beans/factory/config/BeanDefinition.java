package com.mmz.spring.beans.factory.config;





public interface BeanDefinition {
	
	String SCOPE_SINGLETON = "singleton";
	
	String SCOPE_PROTOTYPE= "prototype";
	
	Object getBean() throws Exception;
	
	Class getBeanClass() throws Exception;
	
	void setBeanClass(Class beanClass);
	
	void setBean(Object bean);
	
	PropertyValues getPropertyValues();
	
	void setBeanClassName(String beanClassName);
	
	public Boolean getLazy_init();

	public void setLazy_init(Boolean lazy_init);
	
	Boolean isSingleTon();
	
	Boolean isPrototype();
	
	void setScope(String scope);
	
	String getScope();
}

package com.mmz.spring.beans.factory;

import java.util.Map;

import com.mmz.spring.beans.factory.config.BeanDefinition;





public interface BeanFactory extends BeanDefinitionRegistry{
	
	Object getBean(String name) throws Exception;
	
	<T> T getBean(String name, Class<T> requiredType) throws Exception;
	
	 void setBeanDefinitionMap(Map<String, BeanDefinition> beanDefinitionMap);

}

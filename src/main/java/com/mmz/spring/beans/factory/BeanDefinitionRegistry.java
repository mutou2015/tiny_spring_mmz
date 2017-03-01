package com.mmz.spring.beans.factory;



import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.exception.NoSuchBeanDefinitionException;

public interface BeanDefinitionRegistry {
	
	 void registerBeanDefinition(String name, BeanDefinition beanDefinition);
	
	 void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
	 
	 BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

}

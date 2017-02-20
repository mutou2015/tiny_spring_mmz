package com.mmz.spring.beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mmz.spring.beans.factory.config.BeanDefinition;

public class AbstractBeanFactory implements BeanFactory{
	
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	private final List<String> beanDefinitionNames = new ArrayList<String>();

	

	public Object getBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if(beanDefinition!=null){
			Object bean = beanDefinition.getBean();
			if (bean == null) {
				
			}
			return bean;
		}
		else
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		
	}
	
	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		return null;
	}
	
	public <T> T getBean(String name, Class<T> requiredType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

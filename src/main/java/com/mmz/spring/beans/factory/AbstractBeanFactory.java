package com.mmz.spring.beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;




import com.mmz.spring.beans.BeanPostProcessor;
import com.mmz.spring.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory implements BeanFactory{
	
	// 这个beanDefinitionMap可以理解为ioc容器，其中包含的是bean的定义信息，包括Bean,Class,还有PropertyValues属性
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	private final List<String> beanDefinitionNames = new ArrayList<String>();

	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	
	/**
	 * Beanfactroy的核心方法，根据名字获取bean
	 * */
	public Object getBean(String name) throws Exception {
		// 首先获取BeanDefinition，bean的定义信息
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if(beanDefinition!=null){
			// 这里要注意的是，有可能此时BeanDefinition是刚刚加载，还没有把真实的bean实例创建出来
			// 更不用说属性的自动装配
			Object bean = beanDefinition.getBean();
			if (bean == null) {
				// 执行真正的创建bean过程，把beanDefinition作为参数传入，便于回调赋值
				bean=doCreateBean(beanDefinition);
				
			}
			return bean;
		}
		else
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		
	}
	
	
	// 这里提供了doCreateBean的算法架构，但是applyPropertyValues()交给子类去实现，这是模板设计模式
	// spring中大量使用这种设计模式，为了提供不同的实现方式
	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean=createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		// 由子类AutowireCapableBeanFactory实现
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}
	
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}
	
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}
	
	protected Object initializeBean(Object bean, String name) throws Exception {
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}

		// TODO:call initialize method
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
        return bean;
	}
	
	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}
	
	public <T> T getBean(String name, Class<T> requiredType) throws Exception {
		
		return null;
	}
	
	public List getBeansForType(Class type) throws Exception {
		List beans = new ArrayList<Object>();
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}

}

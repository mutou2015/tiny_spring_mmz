package com.mmz.spring.beans;

public interface BeanPostProcessor {
	
	// 前置处理
	Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception;
	
	// 后置处理
	Object postProcessAfterInitialization(Object bean, String beanName) throws Exception;
}

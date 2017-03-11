package com.mmz.spring.beans.factory.config;

import com.mmz.spring.beans.factory.BeanFactory;



public interface BeanFactoryAware {
	
	void setBeanFactory(BeanFactory beanFactory);
}

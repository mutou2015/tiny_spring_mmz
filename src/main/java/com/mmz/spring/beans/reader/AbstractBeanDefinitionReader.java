package com.mmz.spring.beans.reader;

import java.util.HashMap;
import java.util.Map;

import com.mmz.spring.beans.factory.BeanDefinitionRegistry;
import com.mmz.spring.beans.factory.BeanFactory;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.xml.BeanDefinitionParser;
import com.mmz.spring.beans.resource.ResourceLoader;



public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
	
	private BeanDefinitionRegistry beanFactory;
	
    private ResourceLoader resourceLoader;

    private BeanDefinitionParser bdParser;
    
    protected AbstractBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
  

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

	public BeanDefinitionRegistry getRegistry() {
		return beanFactory;
	}

	public void setBeanFactory(BeanDefinitionRegistry beanFactory) {
		this.beanFactory = beanFactory;
	}

	public BeanDefinitionParser getBdParser() {
		return bdParser;
	}

	public void setBdParser(BeanDefinitionParser bdParser) {
		this.bdParser = bdParser;
	}
    
	Map<String,BeanDefinition>  getBeanDefinitionMap(){
		return this.beanFactory.getBeanDefinitionMap();
	};
    
	
}

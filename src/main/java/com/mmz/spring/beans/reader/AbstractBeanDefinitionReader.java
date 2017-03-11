package com.mmz.spring.beans.reader;

import java.util.HashMap;
import java.util.Map;

import com.mmz.spring.beans.factory.BeanDefinitionRegistry;
import com.mmz.spring.beans.factory.BeanFactory;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.xml.BeanDefinitionParser;
import com.mmz.spring.beans.resource.ResourceLoader;



public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
	
	private BeanDefinitionRegistry registry;
	
    private ResourceLoader resourceLoader;

    private BeanDefinitionParser bdParser;
    
    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry beanFactory) {
        this.registry = beanFactory;
    }
    
  

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

	public BeanDefinitionRegistry getRegistry() {
		return registry;
	}

	public void setBeanFactory(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public BeanDefinitionParser getBdParser() {
		return bdParser;
	}

	public void setBdParser(BeanDefinitionParser bdParser) {
		this.bdParser = bdParser;
	}
    
	Map<String,BeanDefinition>  getBeanDefinitionMap(){
		return this.registry.getBeanDefinitionMap();
	};
    
	
}

package com.mmz.spring.beans.reader;

import java.util.HashMap;
import java.util.Map;

import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.ResourceLoader;



public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	private Map<String,BeanDefinition> registry;

    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

	
}

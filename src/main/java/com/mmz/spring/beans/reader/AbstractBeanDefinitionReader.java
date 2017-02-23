package com.mmz.spring.beans.reader;

import java.util.HashMap;
import java.util.Map;

import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.ResourceLoader;



public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	// 这个是ioc容器，也就是name-bean定义map
	private Map<String,BeanDefinition> registry;

    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
    }
    
    /**
     * 获取注册的bean信息(获取ioc容器)
     * */
    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

	
}

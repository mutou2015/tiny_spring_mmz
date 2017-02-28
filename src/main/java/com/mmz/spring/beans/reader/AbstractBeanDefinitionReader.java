package com.mmz.spring.beans.reader;

import java.util.HashMap;
import java.util.Map;

import com.mmz.spring.beans.factory.BeanFactory;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.resource.ResourceLoader;



public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	// 这个是ioc容器，也就是name-bean定义map
	private Map<String,BeanDefinition> registry;
	
	private BeanFactory beanFactory;
	
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanFactory beanFactory) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.beanFactory = beanFactory;
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

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
    
    
	
}

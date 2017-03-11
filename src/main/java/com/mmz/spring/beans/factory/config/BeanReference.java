package com.mmz.spring.beans.factory.config;

/**
 * 引用类型的bean
 * 
 * */

public class BeanReference {
	private String name;

    private Object bean;
    
    private Class type;

    public BeanReference(String name) {
        this.name = name;
    }
    
    public BeanReference(String name,Class type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}
    
    
}

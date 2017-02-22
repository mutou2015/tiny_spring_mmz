package com.mmz.spring.beans.reader;

public interface BeanDefinitionReader {
	void loadBeanDefinitions(String location) throws Exception;
}

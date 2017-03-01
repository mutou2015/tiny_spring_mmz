package com.mmz.spring.beans.resource;

import java.net.URL;



public class ResourceLoader {
	
	public final static String CLASSPATH_ALL_URL_PREFIX="classpath*:";
	/**
	 * 资源加载器 loadBeanDefinitions方法的主要方法体
	 * 把资源转换为URL，返回一个UrlResource实例
	 * */
	public Resource getResource(String location){
        URL resource = this.getClass().getClassLoader().getResource(location);
        return new UrlResource(resource);
    }
	
}

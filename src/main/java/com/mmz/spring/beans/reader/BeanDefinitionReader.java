package com.mmz.spring.beans.reader;

import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.resource.Resource;


/**
 * BeanDefinition(bean定义信息)，是个抽象的概念，具体表现为xml文件，文件系统，甚至是一个类，总之就是记录要被ioc容器接管的bean的信息的集中存放位置
 * 本接口顾名思义，目的在于把外部的文件转换为具体的数据结构加载进程序，是一个变具体为抽象的过程
 * */
public interface BeanDefinitionReader {
	
	/**
	 * 从string类型的path中加载beandefinition
	 * */
	void loadBeanDefinitions(Resource resource) throws Exception;
}

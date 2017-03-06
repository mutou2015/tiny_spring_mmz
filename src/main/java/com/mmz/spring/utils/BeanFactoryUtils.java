package com.mmz.spring.utils;

import com.mmz.spring.beans.factory.BeanFactory;

public class BeanFactoryUtils {
	
	/**
	 * @return 是否需求factorybean本身
	 * */
	public static boolean isFactoryDereference(String name) {
		return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
	}


}

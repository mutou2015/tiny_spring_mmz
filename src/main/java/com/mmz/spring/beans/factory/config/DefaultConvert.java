package com.mmz.spring.beans.factory.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultConvert implements Convert {

	public Class findPorpertyType(String propertyName, Object bean) {
		// 根据属性名获取类型
		try {
			Field declaredField = bean.getClass().getDeclaredField(propertyName);
			return declaredField.getType();
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(propertyName+" is not found in "+bean.getClass());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 根据方法获取参数类型
		Method[] methods = bean.getClass().getDeclaredMethods();
		for(Method method : methods){
			if(("set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1)).equals(method.getName())){
				Class<?>[] paramTypes = method.getParameterTypes();
				return paramTypes[0];
			}
		}
		
		return null;
	}

	

}

package com.mmz.spring.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;






import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.BeanFactoryAware;
import com.mmz.spring.beans.factory.config.BeanReference;
import com.mmz.spring.beans.factory.config.PropertyValue;

public class AutowireCapableBeanFactory extends AbstractBeanFactory {
	
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
		if (bean instanceof BeanFactoryAware) {
			// BeanFactoryAware如果是该类型，则把容器的引用注入，从而让该bean获取容器操作的方法和属性
			// 据说是为了aop服务，暂存疑
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		// 遍历beanDefinition类定义信息中属性列表
		for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
			Object value = propertyValue.getValue();
			// 取出的值如果是引用类型，则去获取该引用类型的实例，递归
			if (value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}
			// 
			try {
				// 获取各属性的set方法
				// 调用getDeclaredMethods方法输出的是自身的public、protected、private方法
				// 调用getMethods方法输出的是自身的public方法和父类Object的public方法。
				Method declaredMethod = bean.getClass().getDeclaredMethod(
						"set" + propertyValue.getName().substring(0, 1).toUpperCase()
								+ propertyValue.getName().substring(1), value.getClass());
				// 实际上setAccessible是启用和禁用访问安全检查的开关,并不是为true就能访问为false就不能访问
				// private 属性方法必须设置为true才能访问，不然报错
				// public method设置true，能提升性能
				declaredMethod.setAccessible(true);
				// 调用set方法为bean注入属性值
				declaredMethod.invoke(bean, value);
			} catch (NoSuchMethodException e) {
				Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
				declaredField.setAccessible(true);
				declaredField.set(bean, value);
			}
		}
	}

}
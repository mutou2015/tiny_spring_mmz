package com.mmz.spring.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mmz.spring.beans.PropertyEditorRegistrySupport;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.BeanFactoryAware;
import com.mmz.spring.beans.factory.config.BeanReference;
import com.mmz.spring.beans.factory.config.DefaultConvert;
import com.mmz.spring.beans.factory.config.PropertyValue;











public class AutowireCapableBeanFactory extends AbstractBeanFactory {
	
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
		if (bean instanceof BeanFactoryAware) {
			// BeanFactoryAware如果是该类型，则把容器的引用注入，从而让该bean获取容器操作的方法和属性
			// 据说是为了aop服务，暂存疑?
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		// 遍历beanDefinition类定义信息中属性列表
		for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
			Object value = propertyValue.getValue();
			// 取出的值如果是引用类型，则尝试根据beanName去Beandefinitions中获取该引用类型的实例，
			// 从这个递归调用可以看出，如果一个bean的属性的引用类型尚未在beanDefinition中setBean(),
			// 也就是所谓的创建实例和自动装配，那么会优先进行这个引用类型的装配，完成之后再继续上一层bean的装配
			if (value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}
			// 
			Class targetType = propertyValue.getTragetType();
			if(targetType==null){
				setConvert(new DefaultConvert(new PropertyEditorRegistrySupport(), null));
				targetType=getConvert().findPorpertyType(propertyValue.getName(), bean);
				
			}
			Object  convertedValue = getConvert().convertIfNecessary(propertyValue.getName(), value,  targetType);
			try {
				// 获取各属性的set方法
				// 调用getDeclaredMethods方法输出的是自身的public、protected、private方法
				// 调用getMethods方法输出的是自身的public方法和父类Object的public方法。
				Method declaredMethod = bean.getClass().getDeclaredMethod(
						"set" + propertyValue.getName().substring(0, 1).toUpperCase()
								+ propertyValue.getName().substring(1), convertedValue.getClass());
				// 实际上setAccessible是启用和禁用访问安全检查的开关,并不是为true就能访问为false就不能访问
				// private 属性方法必须设置为true才能访问，不然报错
				// public method设置true，能提升性能
				declaredMethod.setAccessible(true);
				// 调用set方法为bean注入属性值
				declaredMethod.invoke(bean, convertedValue);
			} catch (NoSuchMethodException e) {
				Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
				declaredField.getType();
				declaredField.setAccessible(true);
				declaredField.set(bean, value);
			}
		}
	}

}

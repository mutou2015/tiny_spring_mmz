package com.mmz.spring.beans.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mmz.spring.beans.BeanPostProcessor;
import com.mmz.spring.beans.BeanWrapper;
import com.mmz.spring.beans.BeanWrapperImpl;
import com.mmz.spring.beans.PropertyEditorRegistrySupport;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.BeanFactoryAware;
import com.mmz.spring.beans.factory.config.BeanReference;
import com.mmz.spring.beans.factory.config.DefaultConvert;
import com.mmz.spring.beans.factory.config.PropertyValue;



public class AutowireCapableBeanFactory extends AbstractBeanFactory {
	
	/** Cache of unfinished FactoryBean instances: FactoryBean name --> BeanWrapper 也是跟循环依赖有关*/
	private final Map<String, BeanWrapper> factoryBeanInstanceCache =
			new ConcurrentHashMap<String, BeanWrapper>(16);
	
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
				
				value = getBean(beanReference.getName(),beanReference.getType());
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
								+ propertyValue.getName().substring(1), targetType);
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

	
	
	/**
	 * 检验bean能否被创建，如果该bean配置了postprocessor，那么返回一个proxy
	 * */
	@Override
	protected Object createBean(String beanName, BeanDefinition mbd,final Object[] args) {
		//resolveBeanClass(mbd, beanName);
		//mbd.prepareMethodOverrides();
		Object beanInstance = doCreateBean(beanName,mbd,args);
		mbd.setBean(beanInstance);
		return beanInstance;
	}

	protected Object doCreateBean(String beanName, BeanDefinition mbd, Object[] args) {
		BeanWrapper instanceWrapper = null;
		if(mbd.isSingleTon()&&beanName!=null){
			instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
		}
		if(instanceWrapper == null){
			try {
				instanceWrapper = createBeanInstance(beanName,mbd,args);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null);
		Class<?> beanType = (instanceWrapper != null ? instanceWrapper.getWrappedClass() : null);
		
		// 相当于populateBean(beanName, mbd, instanceWrapper);
		try {
			applyPropertyValues(bean, mbd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object wrappedBean = initializeBean(beanName,bean,mbd);
		return wrappedBean;
	}
	
	public Object doCreateBean(BeanDefinition mbd) throws Exception {
		
		return doCreateBean(null,mbd,null);
	}
	
	// 这里提供了doCreateBean的算法架构，但是applyPropertyValues()交给子类去实现，这是模板设计模式
			// spring中大量使用这种设计模式，为了提供不同的实现方式
//			public Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
//				Object bean=createBeanInstance(beanDefinition);
//				beanDefinition.setBean(bean);
//				// 由子类AutowireCapableBeanFactory实现
//				applyPropertyValues(bean, beanDefinition);
//				return bean;
//			}
	
	protected Object initializeBean(final String beanName, final Object bean, BeanDefinition mbd) {
		if(bean instanceof BeanNameAware){
			((BeanNameAware) bean).setBeanName(beanName);
		}
		if(bean instanceof BeanFactoryAware){
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		Object wrappedBean = bean;
		try {
			 wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		invokeCustomInitMethod(beanName, wrappedBean, mbd);
		try {
			 wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrappedBean;
	}
	
	private void invokeCustomInitMethod(String beanName, Object bean, BeanDefinition mbd) {
		
		
	}



	/**
	 * 根据类的Class对象创建类的实例
	 * @throws Exception 
	 * */
	protected BeanWrapper createBeanInstance(String beanName,BeanDefinition mbd,Object[] args) throws Exception{
		
//				Class<?> beanClass = resolveBeanClass(mbd, beanName);
//
//				if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
//					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
//							"Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
//				}
//
//				if (mbd.getFactoryMethodName() != null)  {
//					return instantiateUsingFactoryMethod(beanName, mbd, args);
//				}
//
//				
//				boolean resolved = false;
//				boolean autowireNecessary = false;
//				if (args == null) {
//					synchronized (mbd.constructorArgumentLock) {
//						if (mbd.resolvedConstructorOrFactoryMethod != null) {
//							resolved = true;
//							autowireNecessary = mbd.constructorArgumentsResolved;
//						}
//					}
//				}
//				if (resolved) {
//					if (autowireNecessary) {
//						return autowireConstructor(beanName, mbd, null, null);
//					}
//					else {
//						return instantiateBean(beanName, mbd);
//					}
//				}
//
//				
//				Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
//				if (ctors != null ||
//						mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
//						mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
//					return autowireConstructor(beanName, mbd, ctors, args);
//				}

				
				return instantiateBean(beanName, mbd);
	}
	
	protected BeanWrapper instantiateBean(String beanName, BeanDefinition mbd) throws Exception {
		Class clazz = mbd.getBeanClass();
		if(clazz.isInterface())
			throw new RuntimeException("指定的类是个接口");
		Constructor constructorToUse = clazz.getDeclaredConstructor((Class[]) null);
		constructorToUse.setAccessible(true);
		Object instance = constructorToUse.newInstance(null);
		
		return new BeanWrapperImpl(instance);
	}

	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws Exception
			 {

		Object result = existingBean;
		for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			result = beanProcessor.postProcessBeforeInitialization(result, beanName);
			
		}
		return result;
	}
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws Exception
	 {

		Object result = existingBean;
		for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			result = beanProcessor.postProcessAfterInitialization(result, beanName);
			
		}
		return result;
	 }
	
	


	




}

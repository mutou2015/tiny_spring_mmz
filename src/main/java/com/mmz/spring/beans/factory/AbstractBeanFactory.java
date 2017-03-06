package com.mmz.spring.beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;




import com.mmz.spring.beans.BeanPostProcessor;
import com.mmz.spring.beans.PropertyEditorRegistrySupport;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.Convert;
import com.mmz.spring.beans.factory.config.DefaultConvert;
import com.mmz.spring.beans.factory.xml.BeanDefinitionParser;
import com.mmz.spring.beans.factory.xml.ComponentScanBeanDefinitionParser;
import com.mmz.spring.exception.NoSuchBeanDefinitionException;

public abstract class AbstractBeanFactory implements BeanFactory{
	
	// 这个beanDefinitionMap可以理解为ioc容器中的内容，其中包含的是bean的定义信息，包括Bean,Class,还有PropertyValues属性
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	private final List<String> beanDefinitionNames = new ArrayList<String>();

	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	private DefaultConvert convert;
	
	protected static final Object NULL_OBJECT = new Object();
	/** Cache of singleton objects: bean name --> bean instance */
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);
	
	
	/**
	 * Beanfactroy的核心方法，根据名字获取bean
	 * */
	public Object getBean(String name) throws Exception {
		// 首先根据该id获取对应的BeanDefinition，bean的定义信息
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if(beanDefinition!=null){
			// 获取了BeanDefinition之后，如果是第一次获取，BeanDefinition还没有做setbean的操作，所以此时class有，但是bean为空
			// 当然属性的自动装配也就更没有开始
			Object bean = beanDefinition.getBean();
			if (bean == null) {
				// 执行真正的创建bean过程，把beanDefinition作为参数传入，用于在创建之后回调setBean()
				bean=doCreateBean(beanDefinition);
				
			}
			return bean;
		}
		else
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		
	}
	@SuppressWarnings("unchecked")
	protected <T> Object doGetBean(final String name,final Class<T> requiredType){
		final BeanDefinition bd = getBeanDefinition(name);
		Object bean = null;
		if(bd.isSingleTon()){
			
			Object sharedInstance = getSingleTon(name, new ObjectFactory<Object>() {

				public Object getObject() {
					
					return createBean(name, bd);
				}
				
			});
		}
		if(bd.isPrototype()){
			Object prototypeInstance = null;
			prototypeInstance = createBean(name, bd);
			/**
			 * Get the object for the given bean instance, 
			 * either the bean instance itself or its created object in case of a FactoryBean.
			 * */
			//bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
		}
		// Check if required type matches the type of the actual bean instance.
//		if (requiredType != null && bean != null && !requiredType.isAssignableFrom(bean.getClass())) {
//			
//			//return getTypeConverter().convertIfNecessary(bean, requiredType);
//			return null;
//		}
		return (T)bean;		
	}
	
	protected abstract Object createBean(String beanName, BeanDefinition mbd);
			
	
	private Object getSingleTon(String name,ObjectFactory<?> singletonFactory) {
		Object singletonObject = this.singletonObjects.get(name);
		if(singletonObject==null){
			synchronized(this.singletonObjects){
				// 这个会被回调
				singletonObject = singletonFactory.getObject();
			}
			addSingleton(name,singletonObject);
		}
		return singletonObject != null ? singletonObject : NULL_OBJECT;
	}

	private void addSingleton(String beanName,Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
//			this.singletonFactories.remove(beanName);
//			this.earlySingletonObjects.remove(beanName);
//			this.registeredSingletons.add(beanName);
		}
		
	}

	// 这里提供了doCreateBean的算法架构，但是applyPropertyValues()交给子类去实现，这是模板设计模式
	// spring中大量使用这种设计模式，为了提供不同的实现方式
	public Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean=createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		// 由子类AutowireCapableBeanFactory实现
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}
	
	
	
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}
	
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}
	
	
	public void removeBeanDefinition(String beanName) {
		if(beanDefinitionMap.remove(beanName)==null){
			throw new NoSuchBeanDefinitionException("no "+beanName+"  beanDefinition");
		}
				
	}
	
	public BeanDefinition getBeanDefinition(String beanName) {
		
		return beanDefinitionMap.get(beanName);
				
	}
	
	protected Object initializeBean(Object bean, String name) throws Exception {
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}

		// TODO:call initialize method
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
        return bean;
	}
	
	/**
	 * 根据类的Class对象创建类的实例
	 * */
	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}
	
	public <T> T getBean(String name, Class<T> requiredType) throws Exception {
		
		return null;
	}
	
	public List getBeansForType(Class type) throws Exception {
		List beans = new ArrayList<Object>();
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}

	
	public void setBeanDefinitionMap(Map<String, BeanDefinition> beanDefinitionMap) {
		this.beanDefinitionMap = beanDefinitionMap;
	}

	/**
	 * 根据class类型从map中获取对应的beandefinition
	 * */
	public BeanDefinition getBdDefinitionByType(Class clz){
		
		 for (Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
			 try {
				if(entry.getValue().getBeanClass().equals(clz))
					 return entry.getValue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			  
		 }
		return null;
		
	}
	
	public void setConvert(DefaultConvert convert) {
		this.convert = convert;
	}


	public DefaultConvert getConvert() {
		return  this.convert;//new DefaultConvert(new PropertyEditorRegistrySupport(), beanDefinitionMap);
	}


	public Map<String, BeanDefinition> getBeanDefinitionMap() {
		return beanDefinitionMap;
	}

	
	
	
	
	
	

}

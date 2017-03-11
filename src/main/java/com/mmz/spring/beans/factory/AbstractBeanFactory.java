package com.mmz.spring.beans.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;






import com.mmz.spring.beans.BeanPostProcessor;
import com.mmz.spring.beans.BeanWrapper;
import com.mmz.spring.beans.BeanWrapperImpl;
import com.mmz.spring.beans.PropertyEditorRegistrySupport;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.Convert;
import com.mmz.spring.beans.factory.config.DefaultConvert;
import com.mmz.spring.beans.factory.xml.BeanDefinitionParser;
import com.mmz.spring.beans.factory.xml.ComponentScanBeanDefinitionParser;
import com.mmz.spring.exception.NoSuchBeanDefinitionException;
import com.mmz.spring.utils.BeanFactoryUtils;
import com.mmz.spring.utils.StringUtils;



public abstract class AbstractBeanFactory implements BeanFactory{
	
	// 这个beanDefinitionMap可以理解为ioc容器中的内容，其中包含的是bean的定义信息，包括Bean,Class,还有PropertyValues属性
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	private final List<String> beanDefinitionNames = new ArrayList<String>();

	// bean后处理器
	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	private DefaultConvert convert;
	
	protected static final Object NULL_OBJECT = new Object();
	/** Cache of singleton objects: bean name --> bean instance */
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);
	
	/** Cache of singleton objects created by FactoryBeans: FactoryBean name --> object */
	private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);
	
	
	/**
	 * Beanfactroy的核心方法，根据名字获取bean
	 * */
	public Object getBean(String name)  {
		
		return doGetBean(name, null, null);

	}
	
	@SuppressWarnings("unchecked")
	protected <T> Object doGetBean(final String name,final Class<T> requiredType,final Object[] args){
		// 首先根据该id获取对应的BeanDefinition，bean的定义信息
		
		final BeanDefinition bd = getBeanDefinition(name)==null?getBdDefinitionByType(requiredType):getBeanDefinition(name);
		
		if(bd == null)
			throw new RuntimeException("No bean named " + name + " is defined");
		// 截取前缀之外的真是beanName
		final String beanName = transformedBeanName(name);
		Object bean = null;
		if(bd.isSingleTon()){
			
			Object sharedInstance = getSingleTon(name, new ObjectFactory<Object>() {

				public Object getObject() {
					
					return createBean(name, bd,args);
				}
				
			});
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, bd);
		}
		if(bd.isPrototype()){
			Object prototypeInstance = null;
			prototypeInstance = createBean(name, bd,args);
			/**
			 * Get the object for the given bean instance, 
			 * either the bean instance itself or its created object in case of a FactoryBean.
			 * */
			bean = getObjectForBeanInstance(prototypeInstance, name, beanName, bd);
		}
		// Check if required type matches the type of the actual bean instance.
//		if (requiredType != null && bean != null && !requiredType.isAssignableFrom(bean.getClass())) {
//			
//			//return getTypeConverter().convertIfNecessary(bean, requiredType);
//			return null;
//		}
		return (T)bean;		
	}
	
	/**
	 * FactoryBean相关方法，对factorybean的实现类做特殊的返回bean，也就是调用其重写的getObject()
	 * */
	private Object getObjectForBeanInstance(Object sharedInstance, String name, String beanName, BeanDefinition bd) {
		if(!(sharedInstance instanceof FactoryBean)||BeanFactoryUtils.isFactoryDereference(name)){
			return sharedInstance;
		}
		Object bean=null;
		FactoryBean<?> factory = (FactoryBean<?>)sharedInstance;
		if(factory.isSingleton()&&containsSingleton(beanName)){
			synchronized (this.singletonObjects) {
				bean = getFactoryBeanObjectCache();
				if(bean == null){
					bean = doGetObjectFromFactoryBean(factory,beanName);
					factoryBeanObjectCache.put(beanName, bean);
				}
				
			}
		}
		else{
			bean = doGetObjectFromFactoryBean(factory,beanName);
		}
		return (bean != NULL_OBJECT ? bean : null);
	}
	/**
	 * 真正执行FactoryBean的GetObject返回值
	 * */
	private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName){

		Object object = null;
		
			if (System.getSecurityManager() != null) {
				AccessControlContext acc = AccessController.getContext();
				
					try {
						object = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
							public Object run() throws Exception {
									return factory.getObject();
								}
							}, acc);
					} catch (PrivilegedActionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			else {
				try {
					object = factory.getObject();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		// Do not accept a null value for a FactoryBean that's not fully
		// initialized yet: Many FactoryBeans just return null then.
//		if (object == null && isSingletonCurrentlyInCreation(beanName)) {
//			throw new BeanCurrentlyInCreationException(
//					beanName, "FactoryBean which is currently in creation returned null from getObject");
//		}
		return object;
	}
	
	private String transformedBeanName(String name) {
		
		return StringUtils.transformedBeanName(name);
	}
	protected abstract Object createBean(String beanName, BeanDefinition mbd,final Object[] args);
			
	
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
	
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> requiredType) throws Exception {
		
		return (T) doGetBean(name, requiredType, null);
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
	
	public Boolean containsSingleton(String beanname){
		return singletonObjects.containsKey(beanname);
	}
	public Map<String, Object> getFactoryBeanObjectCache() {
		return factoryBeanObjectCache;
	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		this.beanPostProcessors.add(beanPostProcessor);
	}
	
	
	
	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}
	
	
	
	
	

}

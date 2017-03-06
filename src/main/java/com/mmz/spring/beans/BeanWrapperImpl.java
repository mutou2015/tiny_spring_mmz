package com.mmz.spring.beans;

import java.beans.PropertyDescriptor;
import java.security.AccessControlContext;
import java.util.Map;





public class BeanWrapperImpl implements BeanWrapper {

	private boolean defaultEditorsActive = false;
	/**
	 * We'll create a lot of these objects, so we don't want a new logger every time.
	 */
	


	/** The wrapped object */
	private Object object;

	private String nestedPath = "";

	private Object rootObject;

	/**
	 * The security context used for invoking the property methods
	 */
	private AccessControlContext acc;

	/**
	 * Cached introspections results for this object, to prevent encountering
	 * the cost of JavaBeans introspection every time.
	 */
	//private CachedIntrospectionResults cachedIntrospectionResults;

	/**
	 * Map with cached nested BeanWrappers: nested path -> BeanWrapper instance.
	 */
	private Map<String, BeanWrapperImpl> nestedBeanWrappers;

	private boolean autoGrowNestedPaths = false;

	private int autoGrowCollectionLimit = Integer.MAX_VALUE;
	
	public BeanWrapperImpl(Object object) {
		registerDefaultEditors();
		setWrappedInstance(object);
	}
	
	protected void registerDefaultEditors() {
		this.defaultEditorsActive = true;
	}
	
	/**
	 * Switch the target object, replacing the cached introspection results only
	 * if the class of the new object is different to that of the replaced object.
	 * @param object the new target object
	 */
	public void setWrappedInstance(Object object) {
		setWrappedInstance(object, "", null);
	}

	/**
	 * Switch the target object, replacing the cached introspection results only
	 * if the class of the new object is different to that of the replaced object.
	 * @param object the new target object
	 * @param nestedPath the nested path of the object
	 * @param rootObject the root object at the top of the path
	 */
	public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
		
		this.object = object;
		this.nestedPath = (nestedPath != null ? nestedPath : "");
		this.rootObject = (!"".equals(this.nestedPath) ? rootObject : object);
		this.nestedBeanWrappers = null;
		//this.typeConverterDelegate = new TypeConverterDelegate(this, object);
		//setIntrospectionClass(object.getClass());
	}
	
	public Object getWrappedInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class<?> getWrappedClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
		// TODO Auto-generated method stub

	}

	public boolean isAutoGrowNestedPaths() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
		// TODO Auto-generated method stub

	}

	public int getAutoGrowCollectionLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PropertyDescriptor getPropertyDescriptor(String propertyName){
		// TODO Auto-generated method stub
		return null;
	}

}

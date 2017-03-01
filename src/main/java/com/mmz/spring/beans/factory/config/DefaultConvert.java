package com.mmz.spring.beans.factory.config;

import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;

import com.mmz.spring.beans.PropertyEditorRegistrySupport;



public class DefaultConvert  implements Convert  {

	private final PropertyEditorRegistrySupport propertyEditorRegistry;
	
	private final Object targetObject;
	
	
	public DefaultConvert(PropertyEditorRegistrySupport propertyEditorRegistry, Object targetObject) {
		this.propertyEditorRegistry = propertyEditorRegistry;
		this.targetObject = targetObject;
		this.propertyEditorRegistry.registerDefaultEditors();
	}
	
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
	
	/**
	 * Find a default editor for the given type.
	 * @param requiredType the type to find an editor for
	 * @return the corresponding editor, or {@code null} if none
	 */
	private PropertyEditor findDefaultEditor(Class<?> requiredType) {
		PropertyEditor editor = null;
		if (requiredType != null) {
			// No custom editor -> check BeanWrapperImpl's default editors.
			editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
			if (editor == null && !String.class.equals(requiredType)) {
				// No BeanWrapper default editor -> check standard JavaBean editor.
				//editor = BeanUtils.findEditorByConvention(requiredType);
				
			}
		}
		return editor;
	}
	
	public <T> T convertIfNecessary(String propertyName, Object oldValue,
			Class<T> requiredType){
		if(oldValue.getClass().equals(requiredType))
			return (T) oldValue;
		PropertyEditor editor = null;
		if (requiredType != null) {
			// No custom editor -> check BeanWrapperImpl's default editors.
			editor = this.findDefaultEditor(requiredType);
			
		}
		 
		try {
			editor.setValue(oldValue);
		}
		catch (Exception ex) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
//			}
			// Swallow and proceed.
		}
		if(oldValue instanceof String){
			editor.setAsText((String)oldValue);
		}
		
		return (T)editor.getValue();
		
	}



	

}

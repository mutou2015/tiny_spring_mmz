package com.mmz.spring.beans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mmz.spring.beans.factory.BeanFactory;



@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	/**
	 * Specifies the scope to use for the annotated component/bean.
	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
	 */
	String value() default BeanFactory.SCOPE_SINGLETON;

	

}
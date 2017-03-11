package test.entity.beanpostprocessor;

import com.mmz.spring.beans.BeanPostProcessor;

public class TestPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
		System.out.println("before "+beanName+" init");
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		System.out.println("after "+beanName+" init");
		return bean;
	}

}

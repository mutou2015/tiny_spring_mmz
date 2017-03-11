package com.mmz.spring.beans.context;

import java.util.List;

import com.mmz.spring.beans.BeanPostProcessor;
import com.mmz.spring.beans.factory.AbstractBeanFactory;



public abstract class AbstractApplicationContext {
	
	protected AbstractBeanFactory beanFactory;

	public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public void refresh()  {
		loadBeanDefinitions(beanFactory);
		try {
			registerBeanPostProcessors(beanFactory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		onRefresh();
	}

	protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) ;

	protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
		List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
		for (Object beanPostProcessor : beanPostProcessors) {
			beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
		}
	}

	protected void onRefresh(){
		
	}

	
	public Object getBean(String name) {
		return beanFactory.getBean(name);
	}

}

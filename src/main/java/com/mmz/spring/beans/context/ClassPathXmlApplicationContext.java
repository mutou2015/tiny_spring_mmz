package com.mmz.spring.beans.context;

import java.util.Map;

import com.mmz.spring.beans.factory.AbstractBeanFactory;
import com.mmz.spring.beans.factory.AutowireCapableBeanFactory;
import com.mmz.spring.beans.reader.XmlBeanDefinitionReader;
import com.mmz.spring.beans.resource.Resource;
import com.mmz.spring.beans.resource.ResourceLoader;



public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
	
	private String configLocation;

	private XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(this.beanFactory);
	
	public ClassPathXmlApplicationContext(String configLocation) {
		this(configLocation, new AutowireCapableBeanFactory());
	}

	public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory)  {
		super(beanFactory);
		this.configLocation = configLocation;
		refresh();
	}

	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory)  {
		ResourceLoader rl= new ResourceLoader();
		Resource rs=rl.getResource(configLocation);
		try {
			xmlBeanDefinitionReader.loadBeanDefinitions(rs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

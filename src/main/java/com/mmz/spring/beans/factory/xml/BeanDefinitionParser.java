package com.mmz.spring.beans.factory.xml;


import org.w3c.dom.Element;

import com.mmz.spring.beans.factory.config.BeanDefinition;

public interface BeanDefinitionParser {
	
	BeanDefinition parse(Element element);

}

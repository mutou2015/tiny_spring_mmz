package com.mmz.spring.beans.reader;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.BeanReference;
import com.mmz.spring.beans.factory.config.DefaultBeanDefinition;
import com.mmz.spring.beans.factory.config.PropertyValue;
import com.mmz.spring.beans.factory.config.ResourceLoader;



public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{

	protected XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
		super(resourceLoader);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 从路径中加载xml或者其他文件，作为BeanDefinitions类实例定义信息
	 * 
	 * */
	public void loadBeanDefinitions(String location) throws Exception {
		InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
		doLoadBeanDefinitions(inputStream);
		
	}
	
	protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
		// java提供的一种解析xml的工具类 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		// 解析 XML 文档的输入流，得到一个 Document
		Document doc = docBuilder.parse(inputStream);
		// 解析bean
		registerBeanDefinitions(doc);
		inputStream.close();
	}
	// 这个方法感觉有点多余
	public void registerBeanDefinitions(Document doc) {
		// 获取根节点
		Element root = doc.getDocumentElement();

		parseBeanDefinitions(root);
	}

	/**
	 * 
	 * 对xml文件(的根节点)进行真正的解析
	 * 如果一个node是Element，强制转换成Element类型
	 * 
	 * */
	protected void parseBeanDefinitions(Element root) {
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element) {
				Element ele = (Element) node;
				processBeanDefinition(ele);
			}
		}
	}
	/**
	 * 获取元素的id和class属性
	 * */
	protected void processBeanDefinition(Element ele) {
		String name = ele.getAttribute("id");
		String className = ele.getAttribute("class");
		BeanDefinition beanDefinition = new DefaultBeanDefinition();
		processProperty(ele, beanDefinition);
		beanDefinition.setBeanClassName(className);
		getRegistry().put(name, beanDefinition);
	}

	private void processProperty(Element ele, BeanDefinition beanDefinition) {
		NodeList propertyNode = ele.getElementsByTagName("property");
		for (int i = 0; i < propertyNode.getLength(); i++) {
			Node node = propertyNode.item(i);
			if (node instanceof Element) {
				Element propertyEle = (Element) node;
				String name = propertyEle.getAttribute("name");
				String value = propertyEle.getAttribute("value");
				if (value != null && value.length() > 0) {
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
				} else {
					String ref = propertyEle.getAttribute("ref");
					if (ref == null || ref.length() == 0) {
						throw new IllegalArgumentException("Configuration problem: <property> element for property '"
								+ name + "' must specify a ref or value");
					}
					BeanReference beanReference = new BeanReference(ref);
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
				}
			}
		}
	}

}
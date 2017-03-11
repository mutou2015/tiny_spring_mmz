package com.mmz.spring.beans.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mmz.spring.beans.factory.BeanDefinitionRegistry;
import com.mmz.spring.beans.factory.BeanFactory;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.BeanReference;
import com.mmz.spring.beans.factory.config.DefaultBeanDefinition;
import com.mmz.spring.beans.factory.config.PropertyValue;
import com.mmz.spring.beans.factory.xml.BeanDefinitionParserDelegate;
import com.mmz.spring.beans.factory.xml.ComponentScanBeanDefinitionParser;
import com.mmz.spring.beans.resource.Resource;
import com.mmz.spring.beans.resource.ResourceLoader;



public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{

	private ArrayList<Element> beanList = new ArrayList<Element>();
	
	private ArrayList<Element> componentList = new ArrayList<Element>();
	
	private BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
	
	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;
	
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 从string类型的path中加载beandefinition，从BeanDefinitionReader接口继承实现
	 * 调用resourceLoader的getResource方法把文件转换为字节流
	 * */
	public void loadBeanDefinitions(Resource resource) throws Exception {
		InputStream inputStream = resource.getInputStream();
		doLoadBeanDefinitions(inputStream);
		
		
	}
	
	protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
		// java提供的一种解析xml的工具类 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 命名空间开启
		factory.setNamespaceAware(true);
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		// 解析 XML 文档的输入流，得到一个 Document
		Document doc = docBuilder.parse(inputStream);
		// 解析bean
		registerBeanDefinitions(doc);
		
		getRegistry().setBeanDefinitionMap(getBeanDefinitionMap());
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
				if(delegate.isDefaultNamespace(node)){
					parseDefaultElement(ele,delegate);
				}
				else{
					delegate.parseCustomElement(ele,this.getRegistry());
				}	
				
				
			}
		}
		
		
	}
	
	private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		
		// 节点名为bean
		 if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
			processBeanDefinition(ele, delegate);
		 }
		 // 待有空扩展
		
	}
	/**
	 * 获取元素的id和class属性
	 * @throws Exception 
	 * */
	protected  void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate){
		// 从这里开始往beanDefinition写入bean定义信息
		BeanDefinition beanDefinition = new DefaultBeanDefinition();
		processProperty(ele, beanDefinition);
		String name = ele.getAttribute("id");
		String className = ele.getAttribute("class");
		beanDefinition.setBeanClassName(className);
		Boolean lazy_init = (ele.getAttribute("lazy-init").equals("")?"true":ele.getAttribute("lazy-init")).equals("true")?true:false;
		String scope = (ele.getAttribute("scope").equals("")?"singleton":ele.getAttribute("scope"));
		beanDefinition.setScope(scope);
		beanDefinition.setLazy_init(lazy_init);
		this.getRegistry().registerBeanDefinition(name, beanDefinition);
	}
	
	
	
	
	
	protected void createInstanceByLazy(BeanDefinition beanDefinition) throws Exception{
		// 获取懒加载属性
		if(!beanDefinition.getLazy_init())
			getRegistry().doCreateBean(beanDefinition);
	} 
	
	/**
	 * 获取bean的property标签NodeList：propertyNode
	 * 
	 * */
	private void processProperty(Element ele, BeanDefinition beanDefinition)  {
		NodeList propertyNode = ele.getElementsByTagName("property");
		for (int i = 0; i < propertyNode.getLength(); i++) {
			Node node = propertyNode.item(i);
			if (node instanceof Element) {
				Element propertyEle = (Element) node;
				String name = propertyEle.getAttribute("name");
				String value = propertyEle.getAttribute("value");
				String type = propertyEle.getAttribute("type");
				if(value != null && value.length() > 0 && type!=null && !"".equals(type.trim())){
				
					try {
						Class targetType = Class.forName(type); // 尝试获取目标属性
						beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value,targetType));
					} catch (ClassNotFoundException e) {
						
						throw new RuntimeException("property type is not found");
					}
					
				}
				else if (value != null && value.length() > 0) {
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

package com.mmz.spring.beans.factory.xml;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.management.RuntimeErrorException;


import org.w3c.dom.Element;

import com.mmz.spring.beans.annotation.Autowired;
import com.mmz.spring.beans.annotation.Component;
import com.mmz.spring.beans.factory.BeanDefinitionRegistry;
import com.mmz.spring.beans.factory.config.BeanDefinition;
import com.mmz.spring.beans.factory.config.DefaultBeanDefinition;
import com.mmz.spring.beans.resource.Resource;
import com.mmz.spring.beans.resource.ResourceLoader;
import com.mmz.spring.beans.resource.UrlResource;
import com.mmz.spring.exception.NoSuchBeanDefinitionException;
import com.mmz.spring.utils.StringUtils;

public class ComponentScanBeanDefinitionParser implements BeanDefinitionParser {

	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";

	private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";

	private static final String ANNOTATION_CONFIG_ATTRIBUTE = "annotation-config";

	private static final String NAME_GENERATOR_ATTRIBUTE = "name-generator";

	private static final String SCOPE_RESOLVER_ATTRIBUTE = "scope-resolver";

	private static final String SCOPED_PROXY_ATTRIBUTE = "scoped-proxy";

	private static final String EXCLUDE_FILTER_ELEMENT = "exclude-filter";

	private static final String INCLUDE_FILTER_ELEMENT = "include-filter";

	private static final String FILTER_TYPE_ATTRIBUTE = "type";

	private static final String FILTER_EXPRESSION_ATTRIBUTE = "expression";
	
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
	private BeanDefinitionRegistry registry;
	
	public ComponentScanBeanDefinitionParser(BeanDefinitionRegistry registry){
		this.registry = registry;
	}
	
	public BeanDefinition parse(Element element) {
		String[] basePackages = StringUtils.tokenizeToStringArray(
                element.getAttribute(BASE_PACKAGE_ATTRIBUTE),
                ",; \t\n");

		

		return null;
	}
	
	protected void doScan(String... basePackages) {
     
        for (String basePackage : basePackages) {
            //这里是重点,找到候选组件
            Set<BeanDefinition> candidates = null;
			try {
				candidates = findCandidateComponents(basePackage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            for (BeanDefinition candidate : candidates) {
                
//                if (checkCandidate(beanName, candidate)) {
//                    BeanDefinitionHolder definitionHolder = 
//                                            new BeanDefinitionHolder(candidate, beanName);
//                    beanDefinitions.add(definitionHolder);
//                    //注册到工厂中
//                    registerBeanDefinition(definitionHolder, this.registry);
//                }
            }                        
        }
      
    }
	
	public Set<BeanDefinition> findCandidateComponents(String basePackage) throws IOException {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
       
            String packageSearchPath = ResourceLoader.CLASSPATH_ALL_URL_PREFIX +
                    resolveBasePackage(basePackage) + "/" + this.DEFAULT_RESOURCE_PATTERN;
            Set<Class<?>> classes = getResources(basePackage,packageSearchPath);
            for(Class clz:classes){
            	clz.getAnnotation(Component.class);
            	
            }
        
        return candidates;
    }

	private String resolveBasePackage(String basePackage) {
		if(StringUtils.hasLength(basePackage)&&StringUtils.hasText(basePackage)){
			return basePackage.replace(".", "/").replace("*", "");
		}
		throw new RuntimeException("package path scaned cannot be null!");	
		
	}
	
	public Set<Class<?>> getResources(String locationPattern,String basePackage) throws IOException {
		Set<Class<?>> set = new LinkedHashSet<Class<?>>();
		findAndAddClassesInPackageByFile(basePackage,locationPattern,set);
		return set;
	}
	

	
	/**  
     * 以文件的形式来获取包下的所有Class  
     *   
     * @param packageName  
     * @param packagePath  
     * @param classes  
     */  
    public static void findAndAddClassesInPackageByFile(String packageName,   
            String packagePath, Set<Class<?>> classes) {   
        // 获取此包的目录 建立一个File   
        File dir = new File(packagePath);   
        // 如果不存在或者 也不是目录就直接返回   
        if (!dir.exists() || !dir.isDirectory()) {   
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");   
            return;   
        }   
        // 如果存在 就获取包下的所有文件 包括目录   
        File[] dirfiles = dir.listFiles();   
        // 循环所有文件   
        for (File file : dirfiles) {   
            // 如果是目录 则继续扫描   
            if (file.isDirectory()) {   
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(),classes);   
            } else {   
                // 如果是java类文件 去掉后面的.class 只留下类名   
                String className = file.getName().substring(0,   
                        file.getName().length() - 6);   
                try {   
                		classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));     
                     } 
                catch (ClassNotFoundException e) {   
                    throw new RuntimeException("can not load  "+className+".class! ");
                }   
            }   
        }   
    }  
    
    protected void annotationParser(BeanDefinition beanDefinition,Class clz){
    	Annotation[] typeAnnotations = clz.getAnnotations();
    	for(Annotation typeAnt:typeAnnotations){
    		if(typeAnt.annotationType()==Component.class){
    			try {
					
					componentParser(beanDefinition,clz,clz.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchBeanDefinitionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		}
    			
    	}
    	
    	
    }
	
    protected void componentParser(BeanDefinition beanDefinition,Class clz,Object bean) throws IllegalArgumentException, IllegalAccessException, NoSuchBeanDefinitionException, Exception {
    	Field[] fileds = clz.getDeclaredFields();
    	beanDefinition.setBeanClass(clz);
    	beanDefinition.setBeanClassName(clz.getName());
    	
    	for(Field field:fileds){
    		Annotation[] fieldAnnotations = field.getAnnotations();
    		for(Annotation fieldAnt:fieldAnnotations){
    			if(fieldAnt.annotationType()==Autowired.class){
    				if(this.registry.getBeanDefinition(field.getName())==null){
    					componentParser(new DefaultBeanDefinition(),field.getType(),field.getType().newInstance());
    				}
    				try {
    					
    					Method declaredMethod = bean.getClass().getDeclaredMethod(
    							"set" + field.getName().substring(0, 1).toUpperCase()
    									+ field.getName().substring(1), field.getClass());
    					
    					declaredMethod.setAccessible(true);
    					// 调用set方法为bean注入属性值
    					declaredMethod.invoke(bean,this.registry.getBeanDefinition(field.getName()).getBean());
    				} catch (NoSuchMethodException e) {
    					Field declaredField = bean.getClass().getDeclaredField(field.getName());
    					declaredField.getType();
    					declaredField.setAccessible(true);
    					declaredField.set(bean, this.registry.getBeanDefinition(field.getName()).getBean());
    		    	
    				} 
    				
    				
    			}
    		}
    	}
    	this.registry.registerBeanDefinition(clz.getName(), beanDefinition);
    }
    	
	

}
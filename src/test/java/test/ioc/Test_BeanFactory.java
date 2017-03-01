package test.ioc;

import static org.junit.Assert.*;

import org.junit.Test;

import test.entity.Person;

import com.mmz.spring.beans.factory.AutowireCapableBeanFactory;
import com.mmz.spring.beans.factory.BeanFactory;
import com.mmz.spring.beans.reader.BeanDefinitionReader;
import com.mmz.spring.beans.reader.XmlBeanDefinitionReader;
import com.mmz.spring.beans.resource.Resource;
import com.mmz.spring.beans.resource.ResourceLoader;
import com.mmz.spring.beans.resource.UrlResource;

public class Test_BeanFactory {

	@Test
	public void test_ioc() {
		ResourceLoader rl= new ResourceLoader();
		Resource rs=rl.getResource("ioc.xml");
		AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
		BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		try {
			beanDefinitionReader.loadBeanDefinitions(rs);
			Person person = (Person) beanFactory.getBean("person");
			System.out.println(person.getName()+"--"+person.getAge()+"--"+person.getJob().getPosition());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

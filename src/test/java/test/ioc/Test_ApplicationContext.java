package test.ioc;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mmz.spring.beans.context.AbstractApplicationContext;
import com.mmz.spring.beans.context.ClassPathXmlApplicationContext;

import test.entity.Person;

public class Test_ApplicationContext {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("ioc.xml");
		Person p = (Person) ctx.getBean("person");
		System.out.println(p.getName());
	}

}

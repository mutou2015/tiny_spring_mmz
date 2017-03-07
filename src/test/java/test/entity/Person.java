package test.entity;

import com.mmz.spring.beans.annotation.Autowired;
import com.mmz.spring.beans.annotation.Component;


public class Person {
	
	private String name;
	private Integer age;
	
	
	private Job job;
	
	
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	

}

package test.entity;

import com.mmz.spring.beans.annotation.Autowired;
import com.mmz.spring.beans.annotation.Component;

@Component
public class Fruit {
	
	@Autowired
	private Person person;

	@Autowired
	private Quality qualy;
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Quality getQuality() {
		return qualy;
	}
	
	

}

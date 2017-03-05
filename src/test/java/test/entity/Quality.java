package test.entity;

import com.mmz.spring.beans.annotation.Component;

@Component
public class Quality {
	
	private Integer price;
	
	private String producer;

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}
	
	
}

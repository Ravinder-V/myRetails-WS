package com.retail.myretail.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_EMPTY)
public class ProductResponse {

	private long id;
	private String name;
	private PriceBean current_price;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PriceBean getCurrent_price() {
		return current_price;
	}
	public void setCurrent_price(PriceBean current_price) {
		this.current_price = current_price;
	}
	
	
}

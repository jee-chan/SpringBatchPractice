package com.springbatch.demo.domain;

import java.util.Date;

public class Item {
	
	private String name;
	private Integer price;
	private Date expiration_date;
	private Integer amount;
	
	public Item(String name, Integer price, Date expiration_date, Integer amount) {
		this.name = name;
		this.price = price;
		this.expiration_date = expiration_date;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}

package org.finance.domain;

import java.util.Date;

import org.finance.util.Type;

public class LineItem 
extends CalculatedFields{
	
	private String description;
	private Double amount;
	private Type type;
	private Date date;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "LineItem [description=" + description + ", amount=" + amount + ", type=" + type.name() + ", date=" + date.toString()
				+ "] "+super.toString();
	}
	
}

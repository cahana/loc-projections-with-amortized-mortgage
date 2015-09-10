package org.finance.domain;

import org.finance.util.Type;

public class BasicItem {
	
	private String description;
	private Integer dayOfMonth;
	private Type type;
	private Double amount;
	
	
	public Integer getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
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
		return "BasicItem [description=" + description + ", dayOfMonth=" + dayOfMonth + ", type=" + type.name() + ", amount="
				+ amount + "]";
	}
	
	
}

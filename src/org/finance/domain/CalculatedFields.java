package org.finance.domain;

public class CalculatedFields {

	private Double balance;
	private Double accruedInterest;
	private Double runningAccruedInterest;
	
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getAccruedInterest() {
		return accruedInterest;
	}
	public void setAccruedInterest(Double accruedInterest) {
		this.accruedInterest = accruedInterest;
	}
	public Double getRunningAccruedInterest() {
		return runningAccruedInterest;
	}
	public void setRunningAccruedInterest(Double runningAccruedInterest) {
		this.runningAccruedInterest = runningAccruedInterest;
	}
	@Override
	public String toString() {
		return "CalculatedFields [balance=" + balance + ", accruedInterest=" + accruedInterest + ", runningAccruedInterest="
				+ runningAccruedInterest + "]";
	}
}

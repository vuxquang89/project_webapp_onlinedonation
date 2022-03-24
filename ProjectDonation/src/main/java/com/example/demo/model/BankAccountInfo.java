package com.example.demo.model;

public class BankAccountInfo {

	private int id;
	private String accountName;
	public BankAccountInfo(int id, String accountName) {
		super();
		this.id = id;
		this.accountName = accountName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	
	
}

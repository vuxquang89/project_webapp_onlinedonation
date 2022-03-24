package com.example.demo.model;

public class UserCriteria {

	private String keySearch;
	private int limitItem;
	private int userStatus;
	private int current;
	private String id;
	private String action;
	
	
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public String getKeySearch() {
		return keySearch;
	}
	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}
	public int getLimitItem() {
		return limitItem;
	}
	public void setLimitItem(int limitItem) {
		this.limitItem = limitItem;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}

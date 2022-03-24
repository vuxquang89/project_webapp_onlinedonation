package com.example.demo.model;

import java.util.List;

public class ResponseBodyUser {

	private int status;
	private int totalRecords;
	private String message;
	private String error;
	private List<AppUser> users;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<AppUser> getUsers() {
		return users;
	}
	public void setUsers(List<AppUser> users) {
		this.users = users;
	}
	
}

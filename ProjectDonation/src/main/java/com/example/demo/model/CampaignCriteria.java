package com.example.demo.model;

public class CampaignCriteria {
	
	
	private int campaignStatus;
	private String keySearch;
	private String search;
	private int limitItem;
	private int currentItem;
	private int current;
	private String id;
	private String userId;
	private String campaignId;
	private Float amount;
	private String action;
	private String fromDate;
	
	private String q;//get key search from select2 library

	
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	
}

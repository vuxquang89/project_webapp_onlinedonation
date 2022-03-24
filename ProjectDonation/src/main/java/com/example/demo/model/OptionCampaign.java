package com.example.demo.model;

public class OptionCampaign {

	private int campaignStatus;
	private int limitItem;
	
	public OptionCampaign(int campaignStatus, int limitItem) {
	
		this.campaignStatus = campaignStatus;
		this.limitItem = limitItem;
	}
	public int getCampaignStatus() {
		return campaignStatus;
	}
	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
	}
	public int getLimitItem() {
		return limitItem;
	}
	public void setLimitItem(int limitItem) {
		this.limitItem = limitItem;
	}
	
	
}

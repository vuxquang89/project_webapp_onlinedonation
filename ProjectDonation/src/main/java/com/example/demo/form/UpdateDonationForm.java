package com.example.demo.form;

public class UpdateDonationForm {

	private Integer userId;
	private Integer campaignId;
	private Float amount;
	private String dateTime;
	private String content;
	private String result;
	
	public UpdateDonationForm() {}
	
	public UpdateDonationForm(Integer userId, Integer campaignId, Float amount, String dateTime) {
		
		this.userId = userId;
		this.campaignId = campaignId;
		this.amount = amount;
		this.dateTime = dateTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}

package com.example.demo.form;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class CreateCampaignForm {

	private int campaignId;
	private String campaignName;
	private String summary;
	private String content;
	private String urlPicture;
	private Float goal;
	private String dateCreated;
	private String dateStart;
	private String dateEnd;
	private int bankId;
	private MultipartFile file;
	
	public CreateCampaignForm() {}
	
	public CreateCampaignForm(String campaignName, String summary, String content, String urlPicture, Float goal,
			String dateCreated, String dateStart, String dateEnd, int bankId) {
		this.campaignName = campaignName;
		this.summary = summary;
		this.content = content;
		this.urlPicture = urlPicture;
		this.goal = goal;
		this.dateCreated = dateCreated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.bankId = bankId;
	}

	
	public CreateCampaignForm(int campaignId, String campaignName, String summary, String content, String urlPicture,
			Float goal, String dateCreated, String dateStart, String dateEnd, int bankId) {
		
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.summary = summary;
		this.content = content;
		this.urlPicture = urlPicture;
		this.goal = goal;
		this.dateCreated = dateCreated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.bankId = bankId;
	}

	
	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrlPicture() {
		return urlPicture;
	}

	public void setUrlPicture(String urlPicture) {
		this.urlPicture = urlPicture;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Float getGoal() {
		return goal;
	}

	public void setGoal(Float goal) {
		this.goal = goal;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	
	
}
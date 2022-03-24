package com.example.demo.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class Campaign {

	private int campaignId;
	private int bankId;
	private String name;
	private String summary;
	private String content;
	private String urlAvatar;
	private String hisUrl;
	private int status;
	private Float amount;
	private Float goal;
	private String dateCreated;
	private String dateStart;
	private String dateEnd;
	private MultipartFile file;
	
	private int countDonation;
	private Float avg;
	private Long countDay;
	
	
	public Campaign() {}
	
	public Campaign(int campaignId, int bankId, String name, String summary, String content, String urlAvatar,
			String hisUrl, int  status, Float amount, Float goal, String dateCreated, String dateStart, String dateEnd) {
		this.campaignId = campaignId;
		this.bankId = bankId;
		this.name = name;
		this.summary = summary;
		this.content = content;
		this.urlAvatar = urlAvatar;
		this.hisUrl = hisUrl;
		this.status = status;
		this.amount = amount;
		this.goal = goal;
		this.dateCreated = dateCreated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	
	public Campaign(int campaignId, String name, String urlAvatar,
			int status, Float amount, Float goal, String dateEnd,
			int countDonation, Float avg, Long countDay) {		
		this.campaignId = campaignId;		
		this.name = name;		
		this.urlAvatar = urlAvatar;		
		this.status = status;
		this.amount = amount;
		this.goal = goal;		
		this.dateEnd = dateEnd;		
		this.countDonation = countDonation;
		this.avg = avg;
		this.countDay = countDay;
	}

	public Campaign(int campaignId, String name, String content, String summary, String urlAvatar, int status, Float amount, Float goal,
			String dateCreated, int countDonation, Float avg, Long countDay, int bankId) {
		
		this.campaignId = campaignId;
		this.name = name;
		this.content = content;
		this.summary = summary;
		this.urlAvatar = urlAvatar;
		this.status = status;
		this.amount = amount;
		this.goal = goal;
		this.dateCreated = dateCreated;		
		this.countDonation = countDonation;
		this.avg = avg;
		this.countDay = countDay;
		this.bankId = bankId;
	}
	
	public Campaign(int campaignId, String name, String urlAvatar, int status, Float amount, Float goal,
			String dateCreated, int countDonation, Float avg) {
		
		this.campaignId = campaignId;
		this.name = name;
		this.urlAvatar = urlAvatar;
		this.status = status;
		this.amount = amount;
		this.goal = goal;
		this.dateCreated = dateCreated;		
		this.countDonation = countDonation;
		this.avg = avg;
	}
	
	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	
	public int getCountDonation() {
		return countDonation;
	}

	public void setCountDonation(int countDonation) {
		this.countDonation = countDonation;
	}
	
	public Float getAvg() {
		return avg;
	}

	
	public Long getCountDay() {
		return countDay;
	}

	public void setCountDay(Long countDay) {
		this.countDay = countDay;
	}

	public void setAvg(Float avg) {
		this.avg = avg;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUrlAvatar() {
		return urlAvatar;
	}

	public void setUrlAvatar(String urlAvatar) {
		this.urlAvatar = urlAvatar;
	}

	public String getHisUrl() {
		return hisUrl;
	}

	public void setHisUrl(String hisUrl) {
		this.hisUrl = hisUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
}

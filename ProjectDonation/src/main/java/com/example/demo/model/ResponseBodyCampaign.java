package com.example.demo.model;

import java.util.List;

public class ResponseBodyCampaign {

	private int status;
	private int totalRecords;
	private int limit;
	private String message;
	private String error;
	private boolean show;
	private List<Campaign> campaigns;
	private List<Donation> donations;
	
	private List<Donor> results;
	
	private Donation donation;
	private Campaign campaign;

	
	
	public List<Donor> getResults() {
		return results;
	}

	public void setResults(List<Donor> results) {
		this.results = results;
	}

	public Donation getDonation() {
		return donation;
	}

	public void setDonation(Donation donation) {
		this.donation = donation;
	}

	public Campaign getCampaign() {
		return campaign;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
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

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<Donation> getDonations() {
		return donations;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
	
}

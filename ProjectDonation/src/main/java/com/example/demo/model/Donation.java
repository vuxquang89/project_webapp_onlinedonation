package com.example.demo.model;

public class Donation {

	private int userId;
	private String username;
	private int donationId;
	private int campaignId;
	private int campaignStatus;
	private String campaignName;
	private Float amount;
	private Float goal;
	private String dateTime;
	private String fullName;
	private String phone;
	
	private Integer countDonation;
	private Float totalAmount;
	private String firstLetterName;
	
	private int bankId;
	private String bank_name;
	private String bank_account_name;
	private String bank_account_number;
	private String bank_trading_name;
	
	public Donation(Integer countDonation, Float totaAmount) {
		this.countDonation = countDonation;
		this.totalAmount = totaAmount;
	}
	
	public Donation(int userId, int donationId, int campaignId, String campaignName, Float amount, String dateTime) {
		
		this.userId = userId;
		this.donationId = donationId;
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.amount = amount;
		this.dateTime = dateTime;
	}
	public Donation(int donationId, int userId, String userName, int campaignId, Float amount, String fullName, String dateTime, int campaignStatus) {
		this.donationId = donationId;
		this.userId = userId;
		this.username = userName;
		this.campaignId = campaignId;
		this.amount = amount;
		this.fullName = fullName;
		this.dateTime = dateTime;
		this.campaignStatus = campaignStatus;
	}
	public Donation(int userId, int campaignId, String campaignName, Float amount, String dateTime) {
		
		this.userId = userId;
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.amount = amount;
		this.dateTime = dateTime;		
	}
	public Donation(int campaignId, int campaignStatus, String campaignName, Float amount, Float goal, int bankId, String bank_account_name,
			String bank_trading_name) {
		
		this.campaignId = campaignId;
		this.campaignStatus = campaignStatus;
		this.campaignName = campaignName;
		this.amount = amount;
		this.goal = goal;
		this.bankId = bankId;
		this.bank_account_name = bank_account_name;
		this.bank_trading_name = bank_trading_name;
	}
	public Donation(int campaignId, int bankId, String bank_name, String bank_account_name, String bank_account_number,
			String bank_trading_name) {
	
		this.campaignId = campaignId;
		this.bankId = bankId;
		this.bank_name = bank_name;
		this.bank_account_name = bank_account_name;
		this.bank_account_number = bank_account_number;
		this.bank_trading_name = bank_trading_name;
	}
	
	public Donation(int userId, int campaignId, Float amount, String fullName, String phone) {
		
		this.userId = userId;
		this.campaignId = campaignId;
		this.amount = amount;
		this.fullName = fullName;
		this.phone = phone;
	}

	public Donation(int userId, int campaignId, Float amount, String dateTime, String fullName, String phone) {
		
		this.userId = userId;
		this.campaignId = campaignId;
		this.amount = amount;
		this.dateTime = dateTime;
		this.fullName = fullName;
		this.phone = phone;
	}

	public Donation(int userId, int campaignId, Float amount, String dateTime, String fullName, String firstLetterName, String phone) {
		
		this.userId = userId;
		this.campaignId = campaignId;
		this.amount = amount;
		this.dateTime = dateTime;
		this.fullName = fullName;
		this.firstLetterName = firstLetterName;
		this.phone = phone;
	}
	
	public Donation(int userId, int campaignId, String dateTime, String fullName, String phone, Float totalAmount) {
		
		this.userId = userId;
		this.campaignId = campaignId;
		this.dateTime = dateTime;
		this.fullName = fullName;
		this.phone = phone;
		this.totalAmount = totalAmount;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	
	public int getDonationId() {
		return donationId;
	}
	public void setDonationId(int donationId) {
		this.donationId = donationId;
	}
	
	public Integer getCountDonation() {
		return countDonation;
	}

	public void setCountDonation(Integer countDonation) {
		this.countDonation = countDonation;
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getFirstLetterName() {
		return firstLetterName;
	}

	public void setFirstLetterName(String firstLetterName) {
		this.firstLetterName = firstLetterName;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}


	public String getBank_name() {
		return bank_name;
	}


	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}


	public String getBank_account_name() {
		return bank_account_name;
	}


	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}


	public String getBank_account_number() {
		return bank_account_number;
	}


	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}


	public String getBank_trading_name() {
		return bank_trading_name;
	}


	public void setBank_trading_name(String bank_trading_name) {
		this.bank_trading_name = bank_trading_name;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public Float getGoal() {
		return goal;
	}
	public void setGoal(Float goal) {
		this.goal = goal;
	}
	public int getCampaignStatus() {
		return campaignStatus;
	}
	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}

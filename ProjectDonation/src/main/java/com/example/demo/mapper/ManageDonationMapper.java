package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.Donation;

public class ManageDonationMapper implements RowMapper<Donation>{

	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {		
		int campaignId = rs.getInt("cp_id");
		int bankId = rs.getInt("bank_id");
		int status = rs.getInt("cp_status");
		String bankTradingName = rs.getString("bank_trading_name");
		String campaignName = rs.getString("cp_name");
		Float amount = rs.getFloat("cp_amount");
		Float goal = rs.getFloat("cp_goal");
		String bankAccountName = rs.getString("bank_account_name");
		
		return new Donation(campaignId, status, campaignName, amount, goal, bankId, bankAccountName, bankTradingName);
		
	}

}

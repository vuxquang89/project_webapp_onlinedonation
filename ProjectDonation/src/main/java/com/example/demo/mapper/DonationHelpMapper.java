package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.Donation;

public class DonationHelpMapper implements RowMapper<Donation>{

	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		int campaignId = rs.getInt("cp_id");
		int bankId = rs.getInt("bank_id");
		String bank_name = rs.getString("bank_name");
		String bank_account_name = rs.getString("bank_account_name");
		String bank_account_number = rs.getString("bank_account_number");
		String bank_trading_name = rs.getString("bank_trading_name");
		
		return new Donation(campaignId, bankId, bank_name, bank_account_name, bank_account_number, bank_trading_name);		
	}

}

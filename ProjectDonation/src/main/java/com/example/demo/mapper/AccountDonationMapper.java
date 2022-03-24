package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.lib.Libraries;
import com.example.demo.model.Donation;

public class AccountDonationMapper implements RowMapper<Donation>{

	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
		int donationId = rs.getInt("donation_id");
		int userId = rs.getInt("user_id");
		int campaignId = rs.getInt("cp_id");
		String campaignName = rs.getString("cp_name");
		String date = Libraries.convertDateToString(rs.getDate("donation_date"));
		Float amount = rs.getFloat("donation_amount");
		//String username = rs.getString("user_username");
		return new Donation(userId, donationId, campaignId, campaignName, amount, date);
	}

}

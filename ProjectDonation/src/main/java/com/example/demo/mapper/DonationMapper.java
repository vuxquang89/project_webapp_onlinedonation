package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.lib.Libraries;
import com.example.demo.model.Donation;

public class DonationMapper implements RowMapper<Donation>{

	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
		int userId = rs.getInt("user_id");
		int campaignId = rs.getInt("cp_id");
		String date = rs.getDate("donation_date").toString();
		Float amount = rs.getFloat("donation_amount");
		String fullName = rs.getString("user_fullname");
		String firstLetterName = Libraries.getFirstChar(fullName);		
		String phone = Libraries.hiddenPhone(rs.getString("user_phone"));
		return new Donation(userId, campaignId, amount, date, fullName, firstLetterName, phone);
	}

}

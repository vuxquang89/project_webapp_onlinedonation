package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.jdbc.core.RowMapper;

import com.example.demo.lib.Libraries;
import com.example.demo.model.Donation;

public class InfoDonationMapper implements RowMapper<Donation>{

	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
		int campaignStatus = rs.getInt("CP_STATUS");
		int donationId = rs.getInt("donation_id");
		int userId = rs.getInt("user_id");
		int campaignId = rs.getInt("cp_id");
		String date = Libraries.convertDateToString(rs.getDate("donation_date"));
		Float amount = rs.getFloat("donation_amount");
		String fullName = rs.getString("user_fullname");
		String userName = rs.getString("user_username");
		return new Donation(donationId, userId, userName, campaignId, amount, fullName, date, campaignStatus);
	}

}

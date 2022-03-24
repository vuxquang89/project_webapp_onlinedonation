package com.example.demo.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.lib.Libraries;
import com.example.demo.model.Campaign;

public class CampaignNewsMapper implements RowMapper<Campaign>{

	@Override
	public Campaign mapRow(ResultSet rs, int rowNum) throws SQLException {
		int campaignId = rs.getInt("cp_id");		
		String name = rs.getString("cp_name");		
		String urlAvatar = rs.getString("cp_picture");		
		int status = rs.getInt("cp_status");
		Float amount = rs.getFloat("cp_amount");
		Float goal = rs.getFloat("cp_goal");	
		String dateCreated = Libraries.convertDateToString(rs.getDate("cp_date_created"));	
		
		Long countDay = Libraries.simpleDate(String.valueOf(rs.getDate("cp_date_end")));
		int countDonation = rs.getInt("cp_count");
		Float avg = amount / goal * 100;
		avg = (float)Math.round(avg * 10) / 10;
		return new Campaign(campaignId, name, urlAvatar, status, amount, goal, dateCreated, countDonation, avg, countDay);
		      
	}

}

package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.lib.Libraries;
import com.example.demo.model.Campaign;

public class CampaignMapper implements RowMapper<Campaign> {

	public static final String BASE_SQL = "select * from campaign";
	
	@Override
	public Campaign mapRow(ResultSet rs, int rowNum) throws SQLException {
		int campaignId = rs.getInt("cp_id");
		int bankId = rs.getInt("bank_id");
		String name = rs.getString("cp_name");
		String summary = rs.getString("cp_summary");
		String content = rs.getString("cp_content");
		String urlAvatar = rs.getString("cp_picture");
		String hisUrl = rs.getString("cp_picture");
		int status = rs.getInt("cp_status");
		Float amount = rs.getFloat("cp_amount");
		Float goal = rs.getFloat("cp_goal");
		String dateCreated = Libraries.convertDateToString(rs.getDate("cp_date_created"));
		String dateStart = Libraries.convertDateToString(rs.getDate("cp_date_start"));
		String dateEnd = Libraries.convertDateToString(rs.getDate("cp_date_end"));
		
		return new Campaign(campaignId, bankId, name, summary, content, urlAvatar, hisUrl, 
				status, amount, goal, dateCreated, dateStart, dateEnd);
	}
	
}

package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.Donor;

public class DonorMapper implements RowMapper<Donor>{

	@Override
	public Donor mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer id = rs.getInt("user_id");
		String text = rs.getString("user_username");
		return new Donor(id, text);
	}

}

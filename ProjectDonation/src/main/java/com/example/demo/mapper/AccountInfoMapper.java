package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.form.AccountInfoForm;



public class AccountInfoMapper implements RowMapper<AccountInfoForm>{

	@Override
	public AccountInfoForm mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		String email = rs.getString("user_email");		
		String h_email = rs.getString("user_email");		
		String fullName = rs.getString("user_fullname");		
		String phone = rs.getString("user_phone");
		String h_phone = rs.getString("user_phone");		
		String avatar = rs.getString("user_avatar");
		
		return new AccountInfoForm(fullName, email, h_email, phone, h_phone, avatar);
	}

	
}

package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.BankAccountInfo;

public class BankAccountMapper implements RowMapper<BankAccountInfo> {

public static final String BASE_SQL = "select b.bank_id, b.bank_account_name from bank b";
	
	@Override
	public BankAccountInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("bank_id");
		String accountName = rs.getString("bank_account_name");
		
		return new BankAccountInfo(id, accountName);
	}
}

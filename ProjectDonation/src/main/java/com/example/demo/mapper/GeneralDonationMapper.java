package com.example.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.Donation;

/**
 * 
 * @author Vux8x
 * tổng số tiền quyên góp của nhà hảo tâm
 * số lượt quyên góp của nhà hảo tâm
 */
public class GeneralDonationMapper implements RowMapper<Donation>{

	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer countDonation = rs.getInt("count_donation");
		Float totalAmount = rs.getFloat("total_donation_amount");
		return new Donation(countDonation, totalAmount);
	}

}

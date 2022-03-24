package com.example.demo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.BankAccountMapper;
import com.example.demo.model.BankAccountInfo;

@Repository
@Transactional
public class BankAccountDAO extends JdbcDaoSupport {
	
	@Autowired
	public BankAccountDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	public List<BankAccountInfo> getBankAccount(){
		String sql = BankAccountMapper.BASE_SQL;
		
		Object[] params = new Object[] {};
		BankAccountMapper mapper = new BankAccountMapper();
		List<BankAccountInfo> list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

}

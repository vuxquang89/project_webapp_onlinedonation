package com.example.demo.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.form.UpdateDonationForm;
import com.example.demo.lib.Libraries;
import com.example.demo.mapper.AccountDonationMapper;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.mapper.DateDonationMapper;
import com.example.demo.mapper.DonationHelpMapper;
import com.example.demo.mapper.DonationMapper;
import com.example.demo.mapper.DonationTotalMapper;
import com.example.demo.mapper.DonorMapper;
import com.example.demo.mapper.GeneralDonationMapper;
import com.example.demo.mapper.InfoDonationMapper;
import com.example.demo.mapper.ManageDonationMapper;
import com.example.demo.model.Campaign;
import com.example.demo.model.Donation;
import com.example.demo.model.Donor;

@Repository
@Transactional
public class DonationDAO extends JdbcDaoSupport {

	@Autowired
	public DonationDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	public int getCountDonation(int campaignId) {
		String sqlString = "select count(cp_id) from donation where cp_id = ?";
		Object[] params = new Object[] {campaignId};
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(value == null)
			return 0;
		return value;
	}
	
	/*get top donation*/
	public List<Donation> getTopDonations(int campaignId){
		String sqlString = "SELECT TOP 10 u.USER_ID, d.CP_ID, u.USER_FULLNAME, u.USER_PHONE, d.DONATION_AMOUNT, d.DONATION_DATE "
				+ "FROM DONATION d, APP_USER u WHERE d.USER_ID = u.USER_ID AND d.CP_ID = ? order by d.DONATION_DATE desc";
		Object[] params = new Object[] {campaignId};
		DonationMapper donationMapper = new DonationMapper();
		List<Donation> donations = this.getJdbcTemplate().query(sqlString, params, donationMapper);
		return donations;
	}
	
	/*get top total donation*/
	public List<Donation> getTopTotalDonations(int campaignId){
		String sqlString = "SELECT TOP 10 u.USER_ID, d.CP_ID, u.USER_FULLNAME, u.USER_PHONE, sum(d.DONATION_AMOUNT) TOTAL_AMOUNT "
				+ "FROM DONATION d, APP_USER u "
				+ "WHERE d.USER_ID = u.USER_ID AND d.CP_ID = ? "
				+ "GROUP BY u.USER_ID, d.CP_ID, u.USER_FULLNAME, u.USER_PHONE "
				+ "order by TOTAL_AMOUNT desc";
		Object[] params = new Object[] {campaignId};
		DonationTotalMapper donationMapper = new DonationTotalMapper();
		List<Donation> totalDonations = this.getJdbcTemplate().query(sqlString, params, donationMapper);
		return totalDonations;
	}
	
	/*get load more donation*/
	public List<Donation> getLoadMore(int campaignId, int begin, int end){
		String sqlString = "WITH LoadMoreDonations AS("
				+ "				SELECT u.USER_ID, d.CP_ID, u.USER_FULLNAME, u.USER_PHONE, d.DONATION_AMOUNT, d.DONATION_DATE ,ROW_NUMBER()  OVER (ORDER BY d.DONATION_DATE desc) AS 'RowNumber' "
				+ "				FROM DONATION d, APP_USER u WHERE d.USER_ID = u.USER_ID AND d.CP_ID = ?"					
				+ "				) "
				+ "SELECT * FROM LoadMoreDonations WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {campaignId, begin, end};
		DonationMapper donationMapper = new DonationMapper();
		List<Donation> donations = this.getJdbcTemplate().query(sqlString, params, donationMapper);
		return donations;
	}
	
	/*get list donation with campaign status*/
	public List<Donation> getDonations(int status, int begin, int end){
		String sqlString = "WITH LoadDonations AS("
				+ "SELECT c.CP_ID,c.CP_STATUS, c.CP_NAME, c.CP_AMOUNT, c.CP_GOAL, b.BANK_ID, b.BANK_ACCOUNT_NAME, b.BANK_TRADING_NAME ,ROW_NUMBER() OVER (ORDER BY c.CP_ID) AS 'RowNumber' "
				+ "FROM CAMPAIGN c, BANK b WHERE b.BANK_ID = c.BANK_ID AND c.CP_STATUS = ?	"
				+ ")"
				+ "SELECT * FROM LoadDonations WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {status, begin, end};
		ManageDonationMapper mDonationMapper = new ManageDonationMapper();
		List<Donation> totalDonations = this.getJdbcTemplate().query(sqlString, params, mDonationMapper);
		return totalDonations;
	}
	
	/*get list info donation*/
	public List<Donation> getInfoDonations(int id, int begin, int end){
		String sqlString = "WITH LoadInfoDonations AS("
				+ "SELECT c.CP_STATUS, d.DONATION_ID, d.CP_ID, u.USER_ID, u.USER_USERNAME, u.USER_FULLNAME, d.DONATION_AMOUNT, d.DONATION_DATE ,ROW_NUMBER() OVER (ORDER BY d.DONATION_DATE desc) AS 'RowNumber' "
				+ "FROM DONATION d, APP_USER u, CAMPAIGN c WHERE d.USER_ID = u.USER_ID and c.CP_ID = d.CP_ID and d.CP_ID = ? "
				+ ")"
				+ "SELECT * FROM LoadInfoDonations WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {id, begin, end};
		InfoDonationMapper iDonationMapper  = new InfoDonationMapper();
		List<Donation> infoDonations = this.getJdbcTemplate().query(sqlString, params, iDonationMapper);
		return infoDonations;
	}
	
	/*get date donation of account*/
	public String getDonationDate(String username) {
		String sqlString = "SELECT top 1 CONVERT(VARCHAR(25), d.DONATION_DATE, 105) DONATION_DATE FROM DONATION d, APP_USER u WHERE d.USER_ID = u.USER_ID and u.USER_USERNAME = ?";
		Object[] params = new Object[] {username};
		DateDonationMapper dateDonationMapper = new DateDonationMapper();		
		String date = this.getJdbcTemplate().queryForObject(sqlString, params, dateDonationMapper);
		return date;
	}
	
	/*get total donation amount and count donation of donor*/
	public Donation getGeneralDonation(String username) {
		String sqlString = "SELECT count(d.USER_ID) count_donation, sum(d.DONATION_AMOUNT) total_donation_amount "
				+ "from DONATION d, APP_USER u where d.USER_ID = u.USER_ID and u.USER_USERNAME = ?";
		Object[] params = new Object[] {username};
		GeneralDonationMapper generalDonationMapper = new GeneralDonationMapper();
		Donation generalDonation = this.getJdbcTemplate().queryForObject(sqlString, params, generalDonationMapper);
		return generalDonation;
	}
	
	/*delete general donation with username*/
	public boolean deleteGeneralDonation(String username, int donationId) {
		String sqlString = "UPDATE DONATION SET DONATION_STATUS = 0 WHERE DONATION_ID = ? AND USER_ID = (select user_id from APP_USER where user_username = ?)";
		Object[] params = new Object[] {donationId, username};
		
		Integer rs = this.getJdbcTemplate().update(sqlString, params);
		
		if(rs < 0 || rs == null)
			return false;
		return true;
	}
	
	/*get list donation of account */
	public List<Donation> getAccountDonations(String username, String fromDate, int begin, int end){
		LocalDate fDate = Libraries.convertStringToDate(fromDate);
		
		String sqlString = "WITH LoadAccountDonations AS("
				+ "SELECT d.DONATION_ID, c.cp_name, d.CP_ID, u.USER_ID, u.USER_FULLNAME, d.DONATION_AMOUNT, d.DONATION_DATE ,ROW_NUMBER() OVER (ORDER BY d.DONATION_DATE desc) AS 'RowNumber' "
				+ "FROM DONATION d, APP_USER u, CAMPAIGN c WHERE d.USER_ID = u.USER_ID and c.CP_ID = d.CP_ID and u.user_username = ? and d.DONATION_DATE >= ? and d.DONATION_STATUS = 1 "
				+ ")"
				+ "SELECT * FROM LoadAccountDonations WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {username, fDate, begin, end};
		AccountDonationMapper aDonationMapper  = new AccountDonationMapper();
		List<Donation> aDonations = this.getJdbcTemplate().query(sqlString, params, aDonationMapper);
		return aDonations;
	}
	
	/*get list donation of account with param username*/
	public List<Donation> getAccountDonations(String username){
		
		String sqlString = "SELECT d.DONATION_ID, c.cp_name, d.CP_ID, u.USER_ID, u.USER_FULLNAME, d.DONATION_AMOUNT, d.DONATION_DATE "
				+"FROM DONATION d, APP_USER u, CAMPAIGN c WHERE d.USER_ID = u.USER_ID and c.CP_ID = d.CP_ID and u.user_username = ? and d.DONATION_STATUS = 1 "
				+"ORDER BY d.DONATION_DATE desc";
		Object[] params = new Object[] {username};
		AccountDonationMapper aDonationMapper  = new AccountDonationMapper();
		List<Donation> aDonations = this.getJdbcTemplate().query(sqlString, params, aDonationMapper);
		return aDonations;
	}
	
	/*get search list donation with account */
	public List<Donation> getSearchAccountDonations(String username, String fromDate, String keySearch, int begin, int end){
		LocalDate fDate = Libraries.convertStringToDate(fromDate);
		
		String sqlString = "WITH LoadSearchAccountDonations AS("
				+ "SELECT d.DONATION_ID, c.cp_name, d.CP_ID, u.USER_ID, u.USER_FULLNAME, d.DONATION_AMOUNT, d.DONATION_DATE ,ROW_NUMBER() OVER (ORDER BY d.DONATION_DATE desc) AS 'RowNumber' "
				+ "FROM DONATION d, APP_USER u, CAMPAIGN c WHERE d.USER_ID = u.USER_ID and c.CP_ID = d.CP_ID and u.user_username = ? and d.DONATION_DATE >= ? and c.cp_name like ? and d.DONATION_STATUS = 1"
				+ ")"
				+ "SELECT * FROM LoadSearchAccountDonations WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {username, fDate, "%"+keySearch+"%", begin, end};
		AccountDonationMapper aDonationMapper  = new AccountDonationMapper();
		List<Donation> aDonations = this.getJdbcTemplate().query(sqlString, params, aDonationMapper);
		return aDonations;
	}
	
	/*get count search account donation*/
	public int getCountSearchAccountDonation(String username, String fromDate, String keySearch) {
		LocalDate fDate = Libraries.convertStringToDate(fromDate);
		String sqlString = "SELECT count(d.CP_ID) "
				+ "FROM DONATION d, APP_USER u, CAMPAIGN c "
				+ "WHERE d.USER_ID = u.USER_ID and c.CP_ID = d.CP_ID and u.user_username = ? and d.DONATION_DATE >= ? and c.cp_name like ? and d.DONATION_STATUS = 1";
		Object[] params = new Object[] {username, fDate, "%"+keySearch+"%"};
		Integer res = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(res == null || res == 0)
			return 0;
		return res;
		
	}
	
	/*get count account donation*/
	public int getCountAccountDonation(String username, String fromDate) {
		LocalDate fDate = Libraries.convertStringToDate(fromDate);
		String sqlString = "SELECT count(d.CP_ID) "
				+ "FROM DONATION d, APP_USER u, CAMPAIGN c "
				+ "WHERE d.USER_ID = u.USER_ID and c.CP_ID = d.CP_ID and u.user_username = ? and d.DONATION_DATE >= ? and d.DONATION_STATUS = 1";
		Object[] params = new Object[] {username, fDate};
		Integer res = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(res == null || res == 0)
			return 0;
		return res;
		
	}

	/*get donation with key word search */
	public List<Donation> getSearchDonations(int status, String keySearch, int begin, int end) {
		//String sqlString = CampaignMapper.BASE_SQL + " where cp_status = ? and cp_name like ?";
		String sqlString = "WITH PageSearchDonations AS"
				+ "("
				+ "    SELECT c.CP_ID,c.CP_STATUS, c.CP_NAME, c.CP_AMOUNT, c.CP_GOAL, b.BANK_ID, b.BANK_ACCOUNT_NAME, b.BANK_TRADING_NAME ,"
				+ "    ROW_NUMBER() OVER (ORDER BY c.CP_ID) AS 'RowNumber' "
				+ "    FROM CAMPAIGN c, BANK b WHERE b.BANK_ID = c.BANK_ID AND c.CP_STATUS = ?	and (c.cp_name like ? or c.cp_id like ?)"
				+ ")"
				+ "SELECT * "
				+ "FROM PageSearchDonations "
				+ "WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {status, "%"+keySearch+"%", "%"+keySearch+"%", begin, end};
		
		ManageDonationMapper mDonationMapper = new ManageDonationMapper();
		List<Donation> donations = this.getJdbcTemplate().query(sqlString, params, mDonationMapper);
		return donations;
	}
	
	/*get donation with key word search */
	public List<Donation> getSearchInfoDonations(int id, String keySearch, int begin, int end) {
		//String sqlString = CampaignMapper.BASE_SQL + " where cp_status = ? and cp_name like ?";
		String sqlString = "WITH LoadInfoDonations AS("
				+ "SELECT c.CP_STATUS, d.DONATION_ID, d.CP_ID, u.USER_ID, u.USER_USERNAME, u.USER_FULLNAME, d.DONATION_AMOUNT, d.DONATION_DATE ,ROW_NUMBER() OVER (ORDER BY d.DONATION_DATE desc) AS 'RowNumber' "
				+ "FROM DONATION d, APP_USER u, CAMPAIGN c WHERE d.USER_ID = u.USER_ID and d.CP_ID = c.CP_ID and d.CP_ID = ? and (u.USER_FULLNAME like ? or CONVERT(VARCHAR(25), d.DONATION_DATE, 105) LIKE ? or u.USER_USERNAME like ?)"
				+ ")"
				+ "SELECT * FROM LoadInfoDonations WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {id, "%"+keySearch+"%", "%"+keySearch+"%", "%"+keySearch+"%", begin, end};
		
		InfoDonationMapper iDonationMapper = new InfoDonationMapper();
		List<Donation> donations = this.getJdbcTemplate().query(sqlString, params, iDonationMapper);
		return donations;
	}
	
	/*get count search info donation*/
	public int getCountSearchInfoDonation(int id, String keySearch) {
		String sqlString = "SELECT count(u.USER_ID) "
				+ "FROM DONATION d, APP_USER u "
				+ "WHERE d.USER_ID = u.USER_ID and d.CP_ID = ? and (u.USER_FULLNAME like ? or CONVERT(VARCHAR(25), d.DONATION_DATE, 105) LIKE ? or u.USER_USERNAME like ?) ";
		Object[] params = new Object[] {id, "%"+keySearch+"%", "%"+keySearch+"%", "%"+keySearch+"%"};
		Integer res = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(res == null || res == 0)
			return 0;
		return res;
		
	}
	
	/*get donation with campaign id*/
	public Donation getDonation(int id) {
		String sqlString = "SELECT c.CP_ID,c.CP_STATUS, c.CP_NAME, c.CP_AMOUNT, c.CP_GOAL, b.BANK_ID, b.BANK_ACCOUNT_NAME, b.BANK_TRADING_NAME ,ROW_NUMBER() OVER (ORDER BY c.CP_ID) AS 'RowNumber' "
				+ "FROM CAMPAIGN c, BANK b WHERE b.BANK_ID = c.BANK_ID AND c.CP_ID = ?	";
		Object[] params = new Object[] {id};
		ManageDonationMapper mDonationMapper = new ManageDonationMapper();
		Donation donation = this.getJdbcTemplate().queryForObject(sqlString, params, mDonationMapper);
		return donation;
	}
		
	/*get help donation*/
	public Donation getHelpDonation(int campaignId) {
		String sqlString = "SELECT b.BANK_ID, b.BANK_NAME, b.BANK_ACCOUNT_NAME, b.BANK_ACCOUNT_NUMBER, b.BANK_TRADING_NAME, c.CP_ID "
				+ "FROM BANK b, CAMPAIGN c "
				+ "WHERE b.BANK_ID = c.BANK_ID AND c.CP_ID = ?";
		Object[] params = new Object[] {campaignId};
		DonationHelpMapper donationHelpMapper = new DonationHelpMapper();
		Donation donation = this.getJdbcTemplate().queryForObject(sqlString, params, donationHelpMapper);
		
		return donation;
	}
	
	/* save update donation*/
	public boolean saveUpdateDonation(UpdateDonationForm form) {
		
		
		String sqlString = "insert into donation(user_id, cp_id, donation_amount, donation_date) "
				+ "values (?,?,?,?)";
		Object[] params;
		if(form.getDateTime() == "" || form.getDateTime() == null) {
			sqlString = "insert into donation(user_id, cp_id, donation_amount) "
					+ "values (?,?,?)";
			params = new Object[] {form.getUserId(), form.getCampaignId(), form.getAmount()};
			
		}else {
			LocalDate date = Libraries.convertStringToDate(form.getDateTime());
			params = new Object[] {form.getUserId(), form.getCampaignId(), form.getAmount(), date};
		}
		
		Integer rs = this.getJdbcTemplate().update(sqlString, params);
		if(rs < 0 || rs == null)
			return false;
		return true;
	}
	
	//get general donor
	public List<Donor> getGeneralDonor(String keySearch){
		String sqlString = "select user_id, user_username from app_user where user_username like ?";
		Object[] params = new Object[] {"%"+keySearch+"%"};
		DonorMapper donorMapper = new DonorMapper();
		List<Donor> donors = this.getJdbcTemplate().query(sqlString, params, donorMapper);
		return donors;
	}
	
	/*update info donation*/
	public boolean updateInfoDonation(String id, Float amount) {
		String sqlString = "update donation set DONATION_AMOUNT = ? where DONATION_ID = ?";
		Object[] params = new Object[] {amount, id};
		
		Integer rs = this.getJdbcTemplate().update(sqlString, params);
		if(rs < 0 || rs == null)
			return false;
		return true;
	}	
	
}

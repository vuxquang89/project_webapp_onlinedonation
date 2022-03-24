package com.example.demo.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.lib.Libraries;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.mapper.CampaignNewsMapper;
import com.example.demo.mapper.CampaignPageMapper;
import com.example.demo.mapper.CampaignViewNewsMapper;
import com.example.demo.model.Campaign;

@Repository
@Transactional
public class CampaignDAO extends JdbcDaoSupport {

	@Autowired
	public CampaignDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	/*call story procedures from data*/
	public void update() {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.getJdbcTemplate()).withProcedureName("cp_update");
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute();
		System.out.println(simpleJdbcCall);
	}
	
	/*get max id campaign */
	public int getMaxCampaignId() {
		String sqlString = "select max(cp_id) from campaign";
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, Integer.class);
		
		if(value == null)
			return 0;
		return value;
	}
	
	/*get count campaign with status*/
	public int getCountCampaign(int status) {
		String sqlString = "select count(cp_id) from campaign where cp_status = ?";
		Object[] params = new Object[] {status};
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(value == null)
			return 0;
		return value;
	}
	
	/*get campaigns with status between begin to end*/
	public List<Campaign> getCampaigns(int status,int begin, int end) {
		String sqlString = "WITH PageCampaigns AS"
				+ "("
				+ "    SELECT *,"
				+ "    ROW_NUMBER()  OVER (ORDER BY cp_date_created desc) AS 'RowNumber'"
				+ "    FROM CAMPAIGN WHERE cp_status = ?"
				+ ")"
				+ "SELECT * "
				+ "FROM PageCampaigns "
				+ "WHERE RowNumber > ? and RowNumber <= ?";
		//String sqlString = CampaignMapper.BASE_SQL + " limit ? where cp_status = ?";
		Object[] params = new Object[] {status, begin, end};
		
		CampaignMapper campaignMapper = new CampaignMapper();
		List<Campaign> campaigns = this.getJdbcTemplate().query(sqlString, params, campaignMapper);
		return campaigns;
	}
	
	/*get list active campaign for index page*/
	public List<Campaign> getActiveCampaigns(int begin, int end) {
		String sqlString = "WITH PageCampaigns AS("
				+ "				SELECT c.CP_ID, c.CP_NAME, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, count(d.cp_id) CP_COUNT ,ROW_NUMBER()  OVER (ORDER BY c.CP_DATE_CREATED desc) AS 'RowNumber' "
				+ "				from CAMPAIGN c left join DONATION d on c.CP_ID = d.CP_ID where c.CP_STATUS = 1 or c.CP_STATUS = 3 and c.CP_DATE_END > GETDATE() "
				+ "				GROUP BY c.CP_NAME, c.CP_ID, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED "
				+ "				) "
				+ "SELECT * FROM PageCampaigns WHERE RowNumber > ? and RowNumber <= ?";
		
		Object[] params = new Object[] {begin, end};
		CampaignPageMapper campaignMapper = new CampaignPageMapper();
		List<Campaign> campaigns = this.getJdbcTemplate().query(sqlString,params, campaignMapper);
		return campaigns;
	}
	
	/*get count active campaign */
	public int getCountActiveCampaign() {
		String sqlString = "select count(cp_id) from campaign where CP_STATUS = 1 or CP_STATUS = 3 and CP_DATE_END > GETDATE()";		
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, Integer.class);
		if(value == null)
			return 0;
		return value;
	}
	
	/*get list campaign for news */
	public List<Campaign> getNewsCampaigns(int status, int begin, int end){
		/*
		String sqlString = "WITH PageNewsCampaigns AS("
				+ "				SELECT c.CP_ID, c.CP_NAME, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, count(d.cp_id) CP_COUNT ,ROW_NUMBER()  OVER (ORDER BY c.cp_date_created desc) AS 'RowNumber' "
				+ "				FROM DONATION d, CAMPAIGN c WHERE c.CP_ID = d.CP_ID AND c.CP_STATUS >= 2 AND c.CP_DATE_END < DATEADD(month, -2, ?) "
				+ "				GROUP BY c.CP_NAME, c.CP_ID, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED "
				+ "				) "
				+ "SELECT * FROM PageNewsCampaigns WHERE RowNumber > ? and RowNumber <= ?";
		*/
		String sqlString = "WITH PageNewsCampaigns AS("
				+ "				SELECT c.CP_ID, c.CP_NAME, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, count(d.cp_id) CP_COUNT ,ROW_NUMBER()  OVER (ORDER BY c.cp_date_created desc) AS 'RowNumber' "
				+ "				FROM CAMPAIGN c left join DONATION d ON c.CP_ID = d.CP_ID WHERE c.CP_STATUS >= ? and c.CP_DATE_END <= GETDATE() "
				+ "				GROUP BY c.CP_NAME, c.CP_ID, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED "
				+ "				) "
				+ "SELECT * FROM PageNewsCampaigns WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {status, begin, end};
		CampaignNewsMapper campaignNewsMapper = new CampaignNewsMapper();
		List<Campaign> campaigns = this.getJdbcTemplate().query(sqlString, params, campaignNewsMapper);
		return campaigns;
	}
	
	/*get campaign for news with id*/
	public Campaign getViewCampaign(int id){
		
		String sqlString = "SELECT c.CP_ID, c.CP_NAME,CAST(c.CP_CONTENT AS NVARCHAR(MAX)) CP_CONTENT,CAST(c.CP_SUMMARY AS NVARCHAR(MAX)) CP_SUMMARY, "
				+ "c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, count(d.cp_id) CP_COUNT, c.BANK_ID "
				+ "from CAMPAIGN c left join DONATION d on c.CP_ID = d.CP_ID where c.CP_ID = ? "
				+ "GROUP BY c.CP_NAME,CAST(c.CP_CONTENT AS NVARCHAR(MAX)),CAST(c.CP_SUMMARY AS NVARCHAR(MAX)), "
				+ "c.CP_ID, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, c.BANK_ID";
		Object[] params = new Object[] {id};
		CampaignViewNewsMapper campaignViewNewsMapper = new CampaignViewNewsMapper();
		Campaign campaigns = this.getJdbcTemplate().queryForObject(sqlString, params, campaignViewNewsMapper);
		return campaigns;
	}
	
	/*get list active campaign with random id*/
	public Campaign getTop1RandomCampaigns(int randomId){
		
		String sqlString = "SELECT TOP 1 c.CP_ID, c.CP_NAME, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, count(d.cp_id) CP_COUNT "
				+ "	from CAMPAIGN c left join DONATION d on c.CP_ID = d.CP_ID where c.CP_STATUS = 1 AND c.CP_ID >= ? "
				+ "	GROUP BY c.CP_NAME, c.CP_ID, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED ";
		Object[] params = new Object[] {randomId};
		CampaignPageMapper campaignMapper = new CampaignPageMapper();
		Campaign campaign = this.getJdbcTemplate().queryForObject(sqlString, params, campaignMapper);
		return campaign;
	}
	
	/*get top3 list active campaign with random id*/
	public List<Campaign> getTop3RandomCampaigns(int randomId){
		
		String sqlString = "SELECT TOP 3 c.CP_ID, c.CP_NAME, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED, count(d.cp_id) CP_COUNT "
				+ "	from CAMPAIGN c left join DONATION d on c.CP_ID = d.CP_ID where c.CP_STATUS = 1 AND c.CP_ID >= ? "
				+ "	GROUP BY c.CP_NAME, c.CP_ID, c.CP_PICTURE, c.CP_STATUS, c.CP_AMOUNT, c.CP_GOAL, c.CP_DATE_END, c.CP_DATE_CREATED ";
		Object[] params = new Object[] {randomId};
		CampaignPageMapper campaignMapper = new CampaignPageMapper();
		List<Campaign> campaigns = this.getJdbcTemplate().query(sqlString, params, campaignMapper);
		return campaigns;
	}
	
	/*get count news campaign */
	public int getCountNewsCampaign() {
		String sqlString = "select count(cp_id) from campaign where cp_status >= 2 and CP_DATE_END <= GETDATE() ";		
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, Integer.class);
		if(value == null)
			return 0;
		return value;
	}
	
	/*get count campaign search*/
	public int getCountSearchCampaigns(int status, String keySearch) {
		String sqlString = "select count(cp_id) from campaign where cp_status = ? and (cp_name like ? or cp_id like ?)";
		
		Object[] params = new Object[] {status, "%"+keySearch+"%", "%"+keySearch+"%"};
		
		CampaignMapper campaignMapper = new CampaignMapper();
		Integer res = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(res == null)
			return 0;
		return res;
		
	}
	
	/*get campaign with key word search */
	public List<Campaign> getSearchCampaigns(int status, String keySearch, int begin, int end) {
		//String sqlString = CampaignMapper.BASE_SQL + " where cp_status = ? and cp_name like ?";
		String sqlString = "WITH PageCampaigns AS"
				+ "("
				+ "    SELECT *,"
				+ "    ROW_NUMBER()  OVER (ORDER BY cp_date_created desc) AS 'RowNumber'"
				+ "    FROM CAMPAIGN WHERE cp_status = ? and cp_name like ?"
				+ ")"
				+ "SELECT * "
				+ "FROM PageCampaigns "
				+ "WHERE RowNumber > ? and RowNumber <= ?";
		Object[] params = new Object[] {status, "%"+keySearch+"%", begin, end};
		
		CampaignMapper campaignMapper = new CampaignMapper();
		List<Campaign> campaigns = this.getJdbcTemplate().query(sqlString, params, campaignMapper);
		return campaigns;
	}
	
	/* get campaign with id*/
	public List<Campaign> getCampaign(int id) {
		String sqlString = CampaignMapper.BASE_SQL + " where cp_id = ?";
		Object[] params = new Object[] {id};
		
		CampaignMapper campaignMapper = new CampaignMapper();
		List<Campaign> campaigns = this.getJdbcTemplate().query(sqlString, params, campaignMapper);
		return campaigns;
	}
	
	/*check campaign empty*/
	public boolean isCampaignEmpty(int id) {
		String sqlString = "select count(cp_id) from campaign where cp_id = ?";
		Object[] params = new Object[] {id};
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		
		if(value == null || value == 0)
			return false;
		return true;
	}
	
	/*update campaign*/
	public boolean saveCampaign(Campaign campaign) {
		
		String sqlString = "";
		Object[] params;
		if(campaign.getStatus() == 0) {
			sqlString = "UPDATE CAMPAIGN "
					+ "   SET CP_NAME = ?"
					+ "      ,CP_CONTENT = ?"
					+ "      ,CP_PICTURE = ?"
					+ "      ,CP_STATUS = ?"
					+ "      ,CP_GOAL = ?"
					+ "      ,CP_DATE_START = ?"
					+ "      ,CP_DATE_END = ?"
					+ "      ,CP_SUMMARY = ?"
					+ "      ,BANK_ID = ?"
					+ " WHERE CP_ID = ?";
			params = new Object[] {campaign.getName(), campaign.getContent(), campaign.getUrlAvatar(),
					campaign.getStatus(), campaign.getGoal(), 
					Libraries.convertStringToDate(campaign.getDateStart()),
					Libraries.convertStringToDate(campaign.getDateEnd()), 
					campaign.getSummary(), campaign.getBankId(), campaign.getCampaignId()};
		}else {
			
			sqlString = "UPDATE CAMPAIGN "
					+ "   SET CP_DATE_END = ?"					
					+ " WHERE CP_ID = ?";
			params = new Object[] {
					Libraries.convertStringToDate(campaign.getDateEnd()), 
					campaign.getCampaignId()};
		}
		
		Integer result = this.getJdbcTemplate().update(sqlString, params);
		System.out.println(result);
		if(result <= 0 || result == null)
			return false;
		return true;
	}
	
	/*delete campaign*/
	public boolean deleteCampaign(int id) {
		String sqlString = "DELETE FROM CAMPAIGN WHERE CP_ID = ?";
		Object[] params = new Object[] {id};
		int result = this.getJdbcTemplate().update(sqlString, params);
		if(result <= 0)
			return false;
		return true;
	}
	
	/* insert new campaign */
	public boolean insertCampaign(Campaign campaign) {
		String sqlString = "insert into campaign(cp_id, cp_name, cp_content, cp_picture, cp_amount, cp_goal, cp_date_created, cp_date_start, cp_date_end, cp_summary, bank_id) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?)";
		
		int campaignID = this.getMaxCampaignId() + 1;		
		Object[] params = new Object[] {campaignID, campaign.getName(), campaign.getContent(), campaign.getUrlAvatar(),0, 
				campaign.getGoal(), Libraries.convertStringToDate(campaign.getDateCreated()), 
				Libraries.convertStringToDate(campaign.getDateStart()),
				Libraries.convertStringToDate(campaign.getDateEnd()), 
				campaign.getSummary(), campaign.getBankId()};
		
		int result = this.getJdbcTemplate().update(sqlString, params);
		if(result <= 0)
			return false;
		return true;
	}
}

package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.form.AccountInfoForm;
import com.example.demo.form.UserForm;
import com.example.demo.lib.EncrytedPassword;
import com.example.demo.lib.Libraries;
import com.example.demo.mapper.AccountInfoMapper;
import com.example.demo.mapper.UserMapper;

import com.example.demo.model.AppUser;

@Repository
@Transactional
public class UserDAO extends JdbcDaoSupport {

	@Autowired
	public UserDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	/*get max id user */
	public int getMaxUserId() {
		String sqlString = "select max(user_id) from app_user";
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, Integer.class);
		
		if(value == null)
			return 0;
		return value;
	}
	
	/*get count user with status*/
	public int getCountUser(int role) {
		String sqlString = "select count(user_id) from app_user where user_role > ?";
		Object[] params = new Object[] {role};
		Integer value = this.getJdbcTemplate().queryForObject(sqlString,params, Integer.class);
		if(value == null)
			return 0;
		return value;
	}
	
	/*get users with status between begin to end*/
	public List<AppUser> getUsers(int role, int begin, int end) {
		String sqlString = "WITH PageUsers AS"
				+ "("
				+ "    SELECT *,"
				+ "    ROW_NUMBER() OVER (ORDER BY user_id desc) AS 'RowNumber'"
				+ "    FROM APP_USER WHERE USER_ROLE > ?"
				+ ")"
				+ "SELECT * "
				+ "FROM PageUsers "
				+ "WHERE RowNumber > ? and RowNumber <= ?";
		//String sqlString = CampaignMapper.BASE_SQL + " limit ? where cp_status = ?";
		Object[] params = new Object[] {role, begin, end};
		
		UserMapper userMapper = new UserMapper();
		List<AppUser> users = this.getJdbcTemplate().query(sqlString, params, userMapper);
		return users;
	}
	
	/*get active user*/
	public int getActiveUser(int id) {
		String sqlString = "SELECT USER_STATUS FROM APP_USER "
				+ " WHERE USER_ID = ?";
		Object[] params = new Object[] {id};		
		Integer result = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(result == null)
			return -1;
		return result;
	}
	
	/*active user*/
	public boolean activeUser(int id) {
		int status = getActiveUser(id);
		if(status == -1)
			return false;
		status = status == 0 ? 1 : 0;
		String sqlString = "UPDATE APP_USER "
				+ "   SET USER_status = ?"
				+ " WHERE USER_ID = ?";
		Object[] params = new Object[] {status, id};		
		Integer result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
	
	/*active user*/
	public boolean activeUser(int id, int status) {
		
		String sqlString = "UPDATE APP_USER "
				+ "   SET USER_status = ?"
				+ " WHERE USER_ID = ?";
		Object[] params = new Object[] {status, id};		
		Integer result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
	
	/*get count user search*/
	public int getCountSearchUser(String keySearch) {
		String sqlString = "select count(user_id) from app_user where user_username like ? or user_phone like ? or USER_EMAIL like ?";
		
		Object[] params = new Object[] {"%"+keySearch+"%", "%"+keySearch+"%", "%"+keySearch+"%"};
		
		//UserMapper userMapper = new UserMapper();
		Integer res = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(res == null || res == 0)
			return 0;
		return res;
		
	}
	
	/*get campaign with key word search */
	public List<AppUser> getSearchUsers(String keySearch, int begin, int end) {
		
		String sqlString = "WITH PageUsers AS"
				+ "("
				+ "    SELECT *,"
				+ "    ROW_NUMBER()  OVER (ORDER BY user_id desc) AS 'RowNumber'"
				+ "    FROM APP_USER WHERE user_username like ? or user_phone like ? or USER_EMAIL like ?"
				+ ")"
				+ "SELECT * "
				+ "FROM PageUsers "
				+ "WHERE RowNumber > ? and RowNumber <= ?";
		
		Object[] params = new Object[] {"%"+keySearch+"%", "%"+keySearch+"%", "%"+keySearch+"%", begin, end};
		UserMapper userMapper = new UserMapper();
		List<AppUser> users = this.getJdbcTemplate().query(sqlString, params, userMapper);
		return users;
	}
	
	/* get user with id*/
	public List<AppUser> getUser(int id) {
		String sqlString = UserMapper.BASE_SQL + " where user_id = ?";
		Object[] params = new Object[] {id};
		
		UserMapper userMapper = new UserMapper();
		List<AppUser> users = this.getJdbcTemplate().query(sqlString, params, userMapper);
		return users;
	}
	
	/*check campaign empty*/
	public boolean isUserEmpty(int id) {
		String sqlString = "select count(user_id) from app_user where user_id = ?";
		Object[] params = new Object[] {id};
		Integer value = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		
		if(value == null || value == 0)
			return false;
		return true;
	}
	
	/*find user with username*/
	public AppUser findUserAccount(String userName) {
		String sqlString = UserMapper.BASE_SQL + " where user_username = ? and user_status = 1";
		Object[] params = new Object[] {userName};
		UserMapper userMapper = new UserMapper();
		try {
			AppUser userInfo = this.getJdbcTemplate().queryForObject(sqlString, params, userMapper);
			return userInfo;
		}catch(EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/*find user with username*/
	public AppUser findCheckAccount(String userName) {
		String sqlString = UserMapper.BASE_SQL + " where user_username = ?";
		Object[] params = new Object[] {userName};
		UserMapper userMapper = new UserMapper();
		try {
			AppUser userInfo = this.getJdbcTemplate().queryForObject(sqlString, params, userMapper);
			return userInfo;
		}catch(EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/*find user with email*/
	public AppUser findAccountEmail(String email) {
		String sqlString = UserMapper.BASE_SQL + " where user_email = ? and user_status = 1";
		Object[] params = new Object[] {email};
		UserMapper userMapper = new UserMapper();
		try {
			AppUser userInfo = this.getJdbcTemplate().queryForObject(sqlString, params, userMapper);
			return userInfo;
		}catch(EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/*find user with username for account info form*/
	public AccountInfoForm findAccount(String userName) {
		String sqlString = UserMapper.BASE_SQL + " where user_username = ? and user_status = 1";
		Object[] params = new Object[] {userName};
		AccountInfoMapper accountInfoMapper = new AccountInfoMapper();
		try {
			AccountInfoForm accountInfo = this.getJdbcTemplate().queryForObject(sqlString, params, accountInfoMapper);
			return accountInfo;
		}catch(EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/*find user with username for account info form*/
	public boolean findAccount(String userName, String pass) {
		String sqlString = "select count(user_id) from app_user where user_username = ? and user_password = ? and user_status = 1";
		Object[] params = new Object[] {userName, pass};
		
		try {
			Integer res = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
			if(res == null || res == 0)
				return false;
			return true;
		}catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	/*get role of user */
	public List<String> getRoleNames(int role){
		List<String> roles = new ArrayList<String>();
		
		switch (role) {
		case 0:
			roles.add("ROLE_ROOT");
			break;
		case 1:
			roles.add("ROLE_ADMIN");
			break;

		default:
			roles.add("ROLE_USER");
			break;
		}
		
		return roles;
	}
	
	/*update user*/
	public boolean saveUser(AppUser user) {
		
		String sqlString = "UPDATE APP_USER "
				+ "   SET USER_EMAIL = ?"
				+ "      ,USER_FULLNAME = ?"
				//+ "      ,USER_AVATAR = ?"
				+ "      ,USER_PHONE = ?"
				+ "      ,USER_ROLE = ?"
				//+ "      ,USER_status = ?"
				+ " WHERE USER_ID = ?";
		if(user.getFullName() == "" || user.getFullName() == null)
			user.setFullName("Nhà hảo tâm " + user.getId());
		Object[] params = new Object[] {user.getEmail(), user.getFullName(), user.getPhone(),
				user.getRole(), user.getId()};		
		Integer result = this.getJdbcTemplate().update(sqlString, params);
		
		if(result <= 0)
			return false;
		return true;
	}
	
	/*update pass new of user*/
	public boolean updatePass(String username, String pass) {
		
		String sqlString = "UPDATE APP_USER "
				+ "   SET USER_PASSWORD = ?"
				+ " WHERE USER_USERNAME = ?";
		Object[] params = new Object[] {pass, username};		
		Integer result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
	
	
	/*delete user*/
	public boolean deleteUser(int id) {
		String sqlString = "DELETE FROM APP_USER WHERE USER_ID = ?";
		Object[] params = new Object[] {id};
		int result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
	
	/* check user name*/
	public boolean checkUserName(String username) {
		String sqlString = "select count(user_id) from app_user where USER_USERNAME = ?";
		Object[] params = new Object[] {username};
		Integer result = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		
		if(result == 0 || result == null)
			return false;
		return true;
	}
	
	/* check email*/
	public boolean checkEmail(String email) {
		String sqlString = "select count(user_id) from app_user where USER_EMAIL = ?";
		Object[] params = new Object[] {email};
		Integer result = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(result == 0 || result == null)
			return false;
		return true;
	}
	
	/* check phone*/
	public boolean checkPhone(String phone) {
		String sqlString = "select count(user_id) from app_user where USER_PHONE = ?";
		Object[] params = new Object[] {phone};
		Integer result = this.getJdbcTemplate().queryForObject(sqlString, params, Integer.class);
		if(result == 0 || result == null)
			return false;
		return true;
	}
	
	/*update user*/
	public boolean updateInfoUser(AccountInfoForm aForm, String username) {
		
		String sqlString = "UPDATE APP_USER "
				+ "   SET USER_FULLNAME = ?, USER_PHONE = ?, USER_EMAIL = ?"
				+ " WHERE USER_USERNAME = ?";
		
		if(aForm.getFullName() == "" || aForm.getFullName() == null)
			aForm.setFullName("Nhà hảo tâm ");
		
		Object[] params = new Object[] {aForm.getFullName(), aForm.getPhone(), aForm.getEmail(), username};		
		Integer result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
	
	/* insert new user */
	public boolean insertUser(AppUser user) {
		String sqlString = "insert into app_user(user_id, user_email, user_password, user_fullname, user_username, user_phone, user_avatar) "
				+ "values (?,?,?,?,?,?,?)";
		
		int userID = this.getMaxUserId() + 1;		
		if(user.getFullName() == "" || user.getFullName() == null)
			user.setFullName("Nhà hảo tâm " + userID);
		user.setAvatar("/assets/images/avatar.jpg");
		//String encrytedPassword = this.passwordEncoder.encode(user.getPass());
		String encrytedPassword = EncrytedPassword.encrytePassword(user.getPass());
		Object[] params = new Object[] {userID, user.getEmail(), encrytedPassword, user.getFullName(), user.getUsername(), user.getPhone(), user.getAvatar()};
		
		int result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
	
	/* insert new user */
	public boolean insertUser(UserForm user) {
		String sqlString = "insert into app_user(user_id, user_email, user_password, user_fullname, user_username, user_phone, user_avatar) "
				+ "values (?,?,?,?,?,?,?)";
		
		int userID = this.getMaxUserId() + 1;		
		
		user.setAvatar("/assets/images/avatar.jpg");		
		
		String encrytedPassword = EncrytedPassword.encrytePassword(user.getPass());
		Object[] params = new Object[] {userID, user.getEmail(), encrytedPassword, user.getFullName(), user.getUsername(), user.getPhone(), user.getAvatar()};
		
		int result = this.getJdbcTemplate().update(sqlString, params);
		if(result < 0)
			return false;
		return true;
	}
}

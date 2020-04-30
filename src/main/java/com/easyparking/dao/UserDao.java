package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户管理Dao
 */
@Mapper
public interface UserDao {

	public Map<String, Object> getUserInfoByLogin(Map<String, Object> map);

	public List<Map<String, Object>> getUserInfoByPage(Map<String, Object> map);

	public int getUserInfoCount(Map<String, Object> map);

	public int insertUser(Map<String, Object> map);
	
	public int updateUser(Map<String, Object> map);

	public int deleteUser(Map<String, Object> map);
	
	public int enableUser(Map<String, Object> map);
	
	public int disableUser(Map<String, Object> map);

	public List<Map<String, Object>> getUserInfo(Map<String, Object> map);

	public int updateUserPassword(Map<String, Object> map);

	public Map<String, Object> checkUserPassword(Map<String, Object> map);

	public int updateUserForClient(Map<String, Object> map);
	
	public int updateUserAuth(Map<String, Object> map);
	
}

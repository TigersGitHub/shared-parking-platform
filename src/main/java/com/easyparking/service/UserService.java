package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户管理服务
 */
public interface UserService {

	public JSONObject getUserInfoByLogin(String userName, String password);

	public JSONArray getUserInfoByPage(Map<String, Object> param);

	public int getUserInfoCount(Map<String, Object> param);

	public int insertUser(Map<String, Object> map);
	
	public int updateUser(Map<String, Object> map);

	public int deleteUser(String id);
	
	public int enableUser(String id);
	
	public int disableUser(String id);

	public JSONObject getUserInfo(String id);
	
	public JSONArray getUserInfoByEmail(String email);

	public int updateUserPassword(String id, String newpw);

	public boolean checkUserPassword(String id, String oldpw);

	public int updateUserForClient(Map<String, Object> map);
	
	public int updateUserAuth(Map<String, Object> map);
	
}

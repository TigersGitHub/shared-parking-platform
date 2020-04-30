package com.easyparking.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyparking.dao.UserDao;
import com.easyparking.service.UserService;
import com.easyparking.util.data.CollectionUtil;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public JSONObject getUserInfoByLogin(String userName, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("password", password);
		return CollectionUtil.toJSONObject(userDao.getUserInfoByLogin(map));
	}

	@Override
	public JSONArray getUserInfoByPage(Map<String, Object> map) {
		return CollectionUtil.toJSONArray(userDao.getUserInfoByPage(map));
	}

	@Override
	public int getUserInfoCount(Map<String, Object> map) {
		return userDao.getUserInfoCount(map);
	}

	@Override
	public int insertUser(Map<String, Object> map) {
		return userDao.insertUser(map);
	}

	@Override
	public int updateUser(Map<String, Object> map) {
		return userDao.updateUser(map);
	}

	@Override
	public int deleteUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return userDao.deleteUser(map);
	}

	@Override
	public int enableUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return userDao.enableUser(map);
	}

	@Override
	public int disableUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return userDao.disableUser(map);
	}

	@Override
	public JSONObject getUserInfo(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		List<Map<String, Object>> list = userDao.getUserInfo(map);
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return CollectionUtil.toJSONObject(list.get(0));	
		}
	}

	@Override
	public JSONArray getUserInfoByEmail(String email) {
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		return CollectionUtil.toJSONArray(userDao.getUserInfo(map));
	}

	@Override
	public int updateUserPassword(String id, String newpw) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("newpw", newpw);
		return userDao.updateUserPassword(map);
	}

	@Override
	public boolean checkUserPassword(String id, String oldpw) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("oldpw", oldpw);
		Map<String, Object> map2 = userDao.checkUserPassword(map);
		return map2 != null;
	}

	@Override
	public int updateUserForClient(Map<String, Object> map) {
		return userDao.updateUserForClient(map);
	}

	@Override
	public int updateUserAuth(Map<String, Object> map) {
		return userDao.updateUserAuth(map);
	}

}

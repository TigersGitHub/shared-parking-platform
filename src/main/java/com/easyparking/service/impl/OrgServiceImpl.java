package com.easyparking.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyparking.dao.OrgDao;
import com.easyparking.service.OrgService;
import com.easyparking.util.data.CollectionUtil;

@Service("orgService")
public class OrgServiceImpl implements OrgService {
	
	@Autowired
	private OrgDao dao;

	@Override
	public JSONArray getOrgInfoSelector(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getOrgInfoSelector(param));
	}

	@Override
	public JSONArray getOrgInfoByPage(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getOrgInfoByPage(param));
	}

	@Override
	public int getOrgInfoCount(Map<String, Object> param) {
		return dao.getOrgInfoCount(param);
	}

	@Override
	public int insertOrg(Map<String, Object> map) {
		return dao.insertOrg(map);
	}

	@Override
	public int disableOrg(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.disableOrg(map);
	}

	@Override
	public int enableOrg(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.enableOrg(map);
	}

	@Override
	public int deleteOrg(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.deleteOrg(map);
	}

	@Override
	public int updateOrg(Map<String, Object> map) {
		return dao.updateOrg(map);
	}

	@Override
	public JSONObject getOrgInfo(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return CollectionUtil.toJSONObject(dao.getOrgInfo(map));
	}

}

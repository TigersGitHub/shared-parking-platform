package com.easyparking.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.easyparking.dao.LogDao;
import com.easyparking.service.LogService;
import com.easyparking.util.data.CollectionUtil;

@Service("logService")
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogDao dao;

	@Override
	public JSONArray getLoginLogByPage(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getLoginLogByPage(param));
	}

	@Override
	public int getLoginLogCount(Map<String, Object> param) {
		return dao.getLoginLogCount(param);
	}

	@Override
	public int insertLoginLog(Map<String, Object> map) {
		return dao.insertLoginLog(map);
	}

	@Override
	public JSONArray getAuditLogByPage(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getAuditLogByPage(param));
	}

	@Override
	public int getAuditLogCount(Map<String, Object> param) {
		return dao.getAuditLogCount(param);
	}

	@Override
	public int insertAuditLog(Map<String, Object> map) {
		return dao.insertAuditLog(map);
	}

}

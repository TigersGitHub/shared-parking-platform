package com.easyparking.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.easyparking.dao.AuditDao;
import com.easyparking.service.AuditService;
import com.easyparking.util.data.CollectionUtil;

@Service("auditService")
public class AuditServiceImpl implements AuditService {
	
	@Autowired
	private AuditDao dao;

	@Override
	public JSONArray getUserAuditResult(String type, String dataId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type);
		param.put("data_id", dataId);
		return CollectionUtil.toJSONArray(dao.getUserAuditResult(param));
	}

	@Override
	public int saveAuditTask(Map<String, Object> map) {
		return dao.saveAuditTask(map);
	}

	@Override
	public JSONArray getTaskByPage(Map<String, Object> map) {
		String type = (String) map.get("type");
		if ("1".equals(type)) {
			return CollectionUtil.toJSONArray(dao.getUserTaskByPage(map));			
		} else if ("2".equals(type)) {
			return CollectionUtil.toJSONArray(dao.getCarTaskByPage(map));
		} else if ("3".equals(type)) {
			return CollectionUtil.toJSONArray(dao.getPlaceTaskByPage(map));
		} else {
			return null;
		}
	}

	@Override
	public int getTaskCount(Map<String, Object> map) {
		return dao.getTaskCount(map);
	}

}

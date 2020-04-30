package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/**
 * 审批服务
 */
public interface AuditService {

	public JSONArray getUserAuditResult(String type, String dataId);

	public int saveAuditTask(Map<String, Object> map);

	public JSONArray getTaskByPage(Map<String, Object> param);

	public int getTaskCount(Map<String, Object> param);
	
	
}

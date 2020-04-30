package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/**
 * 日志管理服务
 */
public interface LogService {

	public JSONArray getLoginLogByPage(Map<String, Object> param);

	public int getLoginLogCount(Map<String, Object> param);

	public int insertLoginLog(Map<String, Object> map);

	public JSONArray getAuditLogByPage(Map<String, Object> param);

	public int getAuditLogCount(Map<String, Object> param);

	public int insertAuditLog(Map<String, Object> map);

}

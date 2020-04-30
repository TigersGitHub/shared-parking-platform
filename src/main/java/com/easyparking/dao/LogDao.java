package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 日志管理Dao
 */
@Mapper
public interface LogDao {
	
	public List<Map<String, Object>> getLoginLogByPage(Map<String, Object> param);

	public int getLoginLogCount(Map<String, Object> param);

	public int insertLoginLog(Map<String, Object> map);
	
	public List<Map<String, Object>> getAuditLogByPage(Map<String, Object> param);

	public int getAuditLogCount(Map<String, Object> param);

	public int insertAuditLog(Map<String, Object> map);

}

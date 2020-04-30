package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据审核Dao
 */
@Mapper
public interface AuditDao {

	public List<Map<String, Object>> getUserAuditResult(Map<String, Object> param);

	public int saveAuditTask(Map<String, Object> map);

	public List<Map<String, Object>> getUserTaskByPage(Map<String, Object> map);

	public List<Map<String, Object>> getCarTaskByPage(Map<String, Object> map);

	public List<Map<String, Object>> getPlaceTaskByPage(Map<String, Object> map);

	public int getTaskCount(Map<String, Object> map);
	
}

package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 首页统计信息查询Dao
 */
@Mapper
public interface StatisticsDao {

	public List<Map<String, Object>> getDailyLoginCount();

	public List<Map<String, Object>> getRoleUserCount();

	public List<Map<String, Object>> getRegionUserCount();

	public List<Map<String, Object>> getHourlyLoginCount();
	
}

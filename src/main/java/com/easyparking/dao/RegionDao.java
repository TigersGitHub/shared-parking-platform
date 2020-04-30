package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 区划查询Dao
 */
@Mapper
public interface RegionDao {

	public List<Map<String, Object>> getRegionInfo(Map<String, Object> param);
	
}

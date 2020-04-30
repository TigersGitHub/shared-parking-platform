package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 车位管理Dao
 */
@Mapper
public interface ParkingPlaceDao {

	public List<Map<String, Object>> getParkingPlaceByPage(Map<String, Object> param);

	public int getParkingPlaceCount(Map<String, Object> param);

	public int insertParkingPlace(Map<String, Object> map);

	public int disableParkingPlace(Map<String, Object> map);

	public int enableParkingPlace(Map<String, Object> map);

	public int deleteParkingPlace(Map<String, Object> map);

	public int updateParkingPlace(Map<String, Object> map);

	public List<Map<String, Object>> getParkingPlaceInfo(Map<String, Object> param);

	public int putOnParkingPlace(Map<String, Object> map);
	
	public int putOffParkingPlace(Map<String, Object> map);
	
	public Map<String, Object> getParkingPlaceImage(Map<String, Object> map);

	public int completeTrade(Map<String, Object> map);
}

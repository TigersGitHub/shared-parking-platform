package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆管理Dao
 */
@Mapper
public interface CarDao {
	
	public List<Map<String, Object>> getCarInfoByPage(Map<String, Object> param);

	public List<Map<String, Object>> getCarInfo(Map<String, Object> map);

	public int getCarInfoCount(Map<String, Object> param);

	public int insertCar(Map<String, Object> map);

	public int updateCar(Map<String, Object> map);

	public int disableCar(Map<String, Object> map);

	public int enableCar(Map<String, Object> map);

	public int deleteCar(Map<String, Object> map);
	
}

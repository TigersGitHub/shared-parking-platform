package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 停车管理Dao
 */
@Mapper
public interface ParkingLotDao {

	public List<Map<String, Object>> getParkingLotSelector(Map<String, Object> param);

	public List<Map<String, Object>> getParkingLotByPage(Map<String, Object> param);

	public int getParkingLotCount(Map<String, Object> param);

	public int insertParkingLot(Map<String, Object> map);

	public int disableParkingLot(Map<String, Object> map);

	public int enableParkingLot(Map<String, Object> map);

	public int deleteParkingLot(Map<String, Object> map);

	public int updateParkingLot(Map<String, Object> map);

	public int insertLotUser(Map<String, Object> map);

	public int deleteLotUser(Map<String, Object> map);

	public List<Map<String, Object>> getLotUsers(Map<String, Object> param);

	public int addParkingAppointment(Map<String, Object> map);

	public int writeOffParkingAppointment(Map<String, Object> map);

	public int minusParkingLotLiveNum(Map<String, Object> map);

	public List<Map<String, Object>> getAppointmentInfoByUser(Map<String, Object> map);

	public int cancelParkingAppointment(Map<String, Object> map);

	public int addUpParkingLotLiveNum(Map<String, Object> map);
}

package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/**
 * 停车场管理服务
 */
public interface ParkingLotService {

	public JSONArray getParkingLotSelector(Map<String, Object> param);

	public JSONArray getParkingLotByPage(Map<String, Object> param);

	public int getParkingLotCount(Map<String, Object> param);

	public int insertParkingLot(Map<String, Object> map);

	public int disableParkingLot(String id);

	public int enableParkingLot(String id);

	public int deleteParkingLot(String id);

	public int updateParkingLot(Map<String, Object> map);

	public int insertLotUser(Map<String, Object> map);

	public int deleteLotUser(String id);

	public JSONArray getLotUsers(Map<String, Object> param);

	public int addParkingAppointment(Map<String, Object> map);
	
	public int writeOffParkingAppointment(Map<String, Object> map);

	public int minusParkingLotLiveNum(String lotId);
	
	public int addUpParkingLotLiveNum(String lotId);

	public JSONArray getAppointmentInfoByUser(String id);

	public int cancelParkingAppointment(String id);
}

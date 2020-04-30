package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 车位管理服务
 */
public interface ParkingPlaceService {

	public JSONArray getParkingPlaceByPage(Map<String, Object> param);

	public int getParkingPlaceCount(Map<String, Object> param);

	public int insertParkingPlace(Map<String, Object> map);

	public int disableParkingPlace(String id);

	public int enableParkingPlace(String id);

	public int deleteParkingPlace(String id);

	public int updateParkingPlace(Map<String, Object> map);

	public JSONArray getParkingPlaceInfoByUser(String userId);

	public int putOnParkingPlace(String id);
	
	public int putOffParkingPlace(String id);

	public JSONObject getParkingPlace(String id);

	public String getParkingPlaceImage(String id);

	public int completeTrade(String id);
}

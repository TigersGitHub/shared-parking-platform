package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 车辆管理服务
 */
public interface CarService {

	public JSONArray getCarInfoByPage(Map<String, Object> param);

	public JSONObject getCarInfo(String id);

	public int getCarInfoCount(Map<String, Object> param);

	public int insertCar(Map<String, Object> map);

	public int updateCar(Map<String, Object> map);

	public int disableCar(String id);

	public int enableCar(String id);

	public int deleteCar(String id);

	public JSONArray getCarInfoByUser(String id);

	public JSONArray getCarSelectorByUser(String id);

}

package com.easyparking.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyparking.dao.CarDao;
import com.easyparking.service.CarService;
import com.easyparking.util.data.CollectionUtil;

@Service("carService")
public class CarServiceImpl implements CarService {
	
	@Autowired
	private CarDao dao;

	@Override
	public JSONArray getCarInfoByPage(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getCarInfoByPage(param));
	}

	@Override
	public int getCarInfoCount(Map<String, Object> param) {
		return dao.getCarInfoCount(param);
	}

	@Override
	public int insertCar(Map<String, Object> map) {
		return dao.insertCar(map);
	}

	@Override
	public int disableCar(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.disableCar(map);
	}

	@Override
	public int enableCar(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.enableCar(map);
	}

	@Override
	public int deleteCar(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.deleteCar(map);
	}

	@Override
	public int updateCar(Map<String, Object> map) {
		return dao.updateCar(map);
	}

	@Override
	public JSONObject getCarInfo(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		List<Map<String, Object>> list = dao.getCarInfo(map);
		if (list != null && list.size() > 0) {
			return CollectionUtil.toJSONObject(list.get(0));	
		} else {
			return null;
		}
	}

	@Override
	public JSONArray getCarInfoByUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("owner", id);
		return CollectionUtil.toJSONArray(dao.getCarInfo(map));
	}
	
	@Override
	public JSONArray getCarSelectorByUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("owner", id);
		map.put("verified", "1");
		return CollectionUtil.toJSONArray(dao.getCarInfo(map));
	}

}

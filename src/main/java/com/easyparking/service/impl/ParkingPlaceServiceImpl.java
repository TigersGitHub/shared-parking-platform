package com.easyparking.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyparking.dao.ParkingPlaceDao;
import com.easyparking.service.ParkingPlaceService;
import com.easyparking.util.data.CollectionUtil;

@Service("parkingPlaceService")
public class ParkingPlaceServiceImpl implements ParkingPlaceService {
	
	@Autowired
	private ParkingPlaceDao dao;

	@Override
	public JSONArray getParkingPlaceInfoByUser(String userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("owner", userId);
		return CollectionUtil.toJSONArray(dao.getParkingPlaceInfo(param));
	}
	
	@Override
	public JSONArray getParkingPlaceByPage(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getParkingPlaceByPage(param));
	}

	@Override
	public int getParkingPlaceCount(Map<String, Object> param) {
		return dao.getParkingPlaceCount(param);
	}

	@Override
	public int insertParkingPlace(Map<String, Object> map) {
		return dao.insertParkingPlace(map);
	}

	@Override
	public int disableParkingPlace(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.disableParkingPlace(map);
	}

	@Override
	public int enableParkingPlace(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.enableParkingPlace(map);
	}

	@Override
	public int deleteParkingPlace(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.deleteParkingPlace(map);
	}

	@Override
	public int updateParkingPlace(Map<String, Object> map) {
		return dao.updateParkingPlace(map);
	}

	@Override
	public int putOnParkingPlace(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.putOnParkingPlace(map);
	}

	@Override
	public int putOffParkingPlace(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.putOffParkingPlace(map);
	}

	@Override
	public JSONObject getParkingPlace(String id) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		List<Map<String, Object>> list = dao.getParkingPlaceInfo(param);
		if (list != null && list.size() > 0) {
			return CollectionUtil.toJSONObject(list.get(0));
		}
		return null;
	}

	@Override
	public String getParkingPlaceImage(String id) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		Map<String, Object> map = dao.getParkingPlaceImage(param);
		if (map != null) {
			byte[] arr = (byte[]) map.get("IMAGE");
			return new String(arr);
		}
		return null;
	}

	@Override
	public int completeTrade(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.completeTrade(map);
	}

}

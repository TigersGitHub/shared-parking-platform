package com.easyparking.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.easyparking.dao.ParkingLotDao;
import com.easyparking.service.ParkingLotService;
import com.easyparking.util.data.CollectionUtil;

@Service("parkingLotService")
public class ParkingLotServiceImpl implements ParkingLotService {
	
	@Autowired
	private ParkingLotDao dao;

	@Override
	public JSONArray getParkingLotSelector(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getParkingLotSelector(param));
	}

	@Override
	public JSONArray getParkingLotByPage(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getParkingLotByPage(param));
	}

	@Override
	public int getParkingLotCount(Map<String, Object> param) {
		return dao.getParkingLotCount(param);
	}

	@Override
	public int insertParkingLot(Map<String, Object> map) {
		return dao.insertParkingLot(map);
	}

	@Override
	public int disableParkingLot(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.disableParkingLot(map);
	}

	@Override
	public int enableParkingLot(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.enableParkingLot(map);
	}

	@Override
	public int deleteParkingLot(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.deleteParkingLot(map);
	}

	@Override
	public int updateParkingLot(Map<String, Object> map) {
		return dao.updateParkingLot(map);
	}

	@Override
	public int insertLotUser(Map<String, Object> map) {
		return dao.insertLotUser(map);
	}

	@Override
	public int deleteLotUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.deleteLotUser(map);
	}

	@Override
	public JSONArray getLotUsers(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getLotUsers(param));
	}

	@Override
	public int addParkingAppointment(Map<String, Object> map) {
		return dao.addParkingAppointment(map);
	}

	@Override
	public int writeOffParkingAppointment(Map<String, Object> map) {
		return dao.writeOffParkingAppointment(map);
	}

	@Override
	public int minusParkingLotLiveNum(String lotId) {
		Map<String, Object> map = new HashMap<>();
		map.put("lot_id", lotId);
		return dao.minusParkingLotLiveNum(map);
	}

	@Override
	public int addUpParkingLotLiveNum(String lotId) {
		Map<String, Object> map = new HashMap<>();
		map.put("lot_id", lotId);
		return dao.addUpParkingLotLiveNum(map);
	}

	@Override
	public JSONArray getAppointmentInfoByUser(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return CollectionUtil.toJSONArray(dao.getAppointmentInfoByUser(map));
	}

	@Override
	public int cancelParkingAppointment(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return dao.cancelParkingAppointment(map);
	}

}

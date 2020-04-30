package com.easyparking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.easyparking.dao.StatisticsDao;
import com.easyparking.service.StatisticsService;
import com.easyparking.util.data.CollectionUtil;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private StatisticsDao dao;
	
	@Override
	public JSONArray getDailyLoginCount() {
		return CollectionUtil.toJSONArray(dao.getDailyLoginCount());
	}

	@Override
	public JSONArray getRoleUserCount() {
		return CollectionUtil.toJSONArray(dao.getRoleUserCount());
	}

	@Override
	public JSONArray getRegionUserCount() {
		return CollectionUtil.toJSONArray(dao.getRegionUserCount());
	}

	@Override
	public JSONArray getHourlyLoginCount() {
		return CollectionUtil.toJSONArray(dao.getHourlyLoginCount());
	}

}

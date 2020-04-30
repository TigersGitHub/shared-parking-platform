package com.easyparking.service;

import com.alibaba.fastjson.JSONArray;

/**
 * 首页统计服务
 */
public interface StatisticsService {

	public JSONArray getDailyLoginCount();

	public JSONArray getRoleUserCount();

	public JSONArray getRegionUserCount();

	public JSONArray getHourlyLoginCount();
	
}

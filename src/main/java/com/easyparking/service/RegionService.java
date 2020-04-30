package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/**
 * 区划查询服务
 */
public interface RegionService {

	public JSONArray getRegionInfo(Map<String, Object> param);

}

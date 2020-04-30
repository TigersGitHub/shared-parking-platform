package com.easyparking.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.easyparking.dao.RegionDao;
import com.easyparking.service.RegionService;
import com.easyparking.util.data.CollectionUtil;

@Service("regionService")
public class RegionServiceImpl implements RegionService {
	
	@Autowired
	private RegionDao dao;

	@Override
	public JSONArray getRegionInfo(Map<String, Object> param) {
		return CollectionUtil.toJSONArray(dao.getRegionInfo(param));
	}

}

package com.easyparking.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 物业组织机构管理服务
 */
public interface OrgService {

	public JSONArray getOrgInfoSelector(Map<String, Object> param);

	public JSONArray getOrgInfoByPage(Map<String, Object> param);

	public int getOrgInfoCount(Map<String, Object> param);

	public int insertOrg(Map<String, Object> map);

	public int disableOrg(String id);

	public int enableOrg(String id);

	public int deleteOrg(String id);

	public int updateOrg(Map<String, Object> map);

	public JSONObject getOrgInfo(String id);

}

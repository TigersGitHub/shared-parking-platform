package com.easyparking.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 物业组织机构管理Dao
 */
@Mapper
public interface OrgDao {

	public List<Map<String, Object>> getOrgInfoSelector(Map<String, Object> param);

	public List<Map<String, Object>> getOrgInfoByPage(Map<String, Object> param);

	public int getOrgInfoCount(Map<String, Object> param);

	public int insertOrg(Map<String, Object> map);

	public int disableOrg(Map<String, Object> map);

	public int enableOrg(Map<String, Object> map);

	public int deleteOrg(Map<String, Object> map);

	public int updateOrg(Map<String, Object> map);

	public Map<String, Object> getOrgInfo(Map<String, Object> map);
	
}

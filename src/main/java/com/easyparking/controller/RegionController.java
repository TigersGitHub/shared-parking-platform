package com.easyparking.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.easyparking.controller.BaseController;
import com.easyparking.service.RegionService;
import com.easyparking.util.data.ValidateUtil;

/**
 * 行政区划管理控制器
 */
@Controller
public class RegionController extends BaseController {
	
	/**
	 * 行政区划服务
	 */
	@Autowired
	private RegionService regionService;

	/**
	 * 行政区划列表查询
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/region/select" })
	@ResponseBody
	public String select(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 上级区划id
		String parentId = this.getString("parentId");
		
		// 创建参数map
		Map<String, Object> param = new HashMap<String, Object>();
		if (ValidateUtil.isNotBlank(parentId)) {
			// 如果上级区划id不为空，添加到map
			param.put("parentId", parentId);
		} else {
			// 否则，添加parentId=#，查询一级区划
			param.put("parentId", "#");
		}
		
		// 执行查询
		JSONArray data = regionService.getRegionInfo(param);
		// 返回结果
		return this.getResponse(data, true, null);
	}

}

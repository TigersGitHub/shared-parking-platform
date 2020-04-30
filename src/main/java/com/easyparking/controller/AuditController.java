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
import com.easyparking.service.AuditService;
import com.easyparking.util.data.ValidateUtil;

/**
 * 信息审核控制器
 */
@Controller
public class AuditController extends BaseController {
	
	@Autowired
	AuditService auditService;

	/**
	 * 车位审批页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/audit/place" })
	public String place(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取车位审批页面视图
		return this.getView("manage/audit/place", map);
	}
	
	/**
	 * 用户审批页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/audit/user" })
	public String user(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取用户审批页面视图
		return this.getView("manage/audit/user", map);
	}

	/**
	 * 车辆信息审批页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/audit/car" })
	public String car(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取车辆审批页面视图
		return this.getView("manage/audit/car", map);
	}
	
	/**
	 * 审核列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/audit/list" })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 开始时间
		String startTime = this.getString("startTime");
		// 截止时间
		String endTime = this.getString("endTime");
		// 状态
		String status = this.getString("status");
		// 类型
		String type = this.getString("type");
		
		// 创建一个参数map
		Map<String, Object> param = new HashMap<String, Object>();
		// 计算起始行号
		int start = (pageNum - 1) * pageSize;
		// 填充参数
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		if (ValidateUtil.isNotBlank(startTime)) {
			param.put("startTime", startTime);
		}
		if (ValidateUtil.isNotBlank(endTime)) {
			param.put("endTime", endTime);
		}
		if (ValidateUtil.isNotBlank(status)) {
			param.put("status", status);
		}
		if (ValidateUtil.isNotBlank(type)) {
			param.put("type", type);
		}

		// 查询用户列表
		JSONArray data = auditService.getTaskByPage(param);
		// 查询用户总数
		int total = auditService.getTaskCount(param);
		// 返回数据
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, "");
	}

}

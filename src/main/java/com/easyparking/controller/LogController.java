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
import com.easyparking.service.LogService;
import com.easyparking.util.data.ValidateUtil;

/**
 * 日志管理控制器
 */
@Controller
public class LogController extends BaseController {
	
	/**
	 * 日志管理服务
	 */
	@Autowired
	private LogService logService;

	/**
	 * 管理端-登录日志页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/log/loginlog" })
	public String loginLog(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 没有什么处理，直接返回视图
		return this.getView("manage/log/loginlog", map);
	}
	
	/**
	 * 管理端-审批日志页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/log/auditlog" })
	public String audtiLog(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 没有什么处理，直接返回视图
		return this.getView("manage/log/auditlog", map);
	}

	/**
	 * 登录日志列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/log/loginList" })
	@ResponseBody
	public String loginList(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 开始时间
		String startTime = this.getString("startTime");
		// 结束时间
		String endTime = this.getString("endTime");
		
		// 创建参数map
		Map<String, Object> param = new HashMap<String, Object>();
		// 根据页码和每页条数计算起始行号
		int start = (pageNum - 1) * pageSize;
		// 将起始行号、每页行数放入参数map
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		// 如果开始时间不为空，将开始时间放入参数map
		if (ValidateUtil.isNotBlank(startTime)) {
			param.put("startTime", startTime);
		}
		// 如果截止时间不为空，将截止时间放入参数map
		if (ValidateUtil.isNotBlank(endTime)) {
			param.put("endTime", endTime);
		}
		
		// 查询日志信息
		JSONArray data = logService.getLoginLogByPage(param);
		// 查询日志总数
		int total = logService.getLoginLogCount(param);
		// 返回到页面
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, null);
	}

	/**
	 * 审批日志列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/log/auditList" })
	@ResponseBody
	public String auditList(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 开始时间
		String startTime = this.getString("startTime");
		// 结束时间
		String endTime = this.getString("endTime");
		
		// 创建参数map
		Map<String, Object> param = new HashMap<String, Object>();
		// 根据页码和每页条数计算起始行号
		int start = (pageNum - 1) * pageSize;
		// 将起始行号、每页行数放入参数map
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		// 如果开始时间不为空，将开始时间放入参数map
		if (ValidateUtil.isNotBlank(startTime)) {
			param.put("startTime", startTime);
		}
		// 如果截止时间不为空，将截止时间放入参数map
		if (ValidateUtil.isNotBlank(endTime)) {
			param.put("endTime", endTime);
		}

		// 查询日志信息
		JSONArray data = logService.getAuditLogByPage(param);
		// 查询日志总数
		int total = logService.getAuditLogCount(param);
		// 返回到页面
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, null);
	}
	
}

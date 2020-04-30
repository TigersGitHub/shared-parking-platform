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
import com.alibaba.fastjson.JSONObject;
import com.easyparking.controller.BaseController;
import com.easyparking.service.CarService;
import com.easyparking.service.ParkingLotService;
import com.easyparking.util.data.DatetimeUtil;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;

/**
 * 停车功能控制器
 */
@Controller
public class ParkingController extends BaseController {
	
	/**
	 * 车辆管理服务
	 */
	@Autowired
	private CarService carService;
	
	/*
	 * 停车场管理服务
	 */
	@Autowired
	private ParkingLotService parkingLotService;
	
	/**
	 * 客户端停车预约列表页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/parking/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		// 如果登录人不为空
		if (user != null) {
			// 取出登录人id
			String id = user.getString("ID");
			// 查询登录人已预约信息列表
			JSONArray records = parkingLotService.getAppointmentInfoByUser(id);
			// 如果查到已预约信息
			if (records != null && records.size() > 0) {
				// hasAppointment=1，用于控制页面列表显示
				map.put("hasAppointment", "1");
				// 已预约列表数据
				map.put("record", records.toJSONString());
			} else {
				// hasAppointment=0，用于控制页面列表隐藏
				map.put("hasAppointment", "0");
			}
		}
		// 返回页面视图
		return this.getView("client/parking/list", map);
	}
	
	/**
	 * 客户端预约停车页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/parking/parking" })
	public String parking(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		// 如果登录人不为空
		if (user != null) {
			// 取登录人id
			String id = user.getString("ID");
			// 取登录人实名认证状态 
			String authStatus = user.getString("AUTHENTICATION");
			// 返回auth到页面
			map.put("auth", authStatus);
			
			// 查询登录人名下的车辆
			JSONArray cars = carService.getCarSelectorByUser(id);
			if (cars != null && cars.size() > 0) {
				// 如果查到了,返回hasCar=1，同时返回车辆列表
				map.put("hasCar", "1");
				map.put("cars", cars.toJSONString());
			} else {
				// 没查到，返回hasCar=0，页面提示先添加车辆
				map.put("hasCar", "0");
			}
		}
		// 返回页面视图
		return this.getView("client/parking/parking", map);
	}
	
	/**
	 * 提交后重新加载已预约列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/client/parking/reload" })
	@ResponseBody
	public String reload(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		// 如不为空
		if (user != null) {
			// 去除登录人id
			String id = user.getString("ID");
			// 查询登录人预约记录
			JSONArray records = parkingLotService.getAppointmentInfoByUser(id);
			// 返回结果
			return this.getResponse(records, true);
		} else {
			// 登录人为空，表示登录状态已过期，返回错误提示
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}
	
	/**
	 * 预约
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/client/parking/add" })
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录人信息
		JSONObject user = this.getCurrentUser();
		// 如不为空
		if (user != null) {
			// 车辆id
			String carId = this.getString("carId");
			// 为空则返回提示
			if (ValidateUtil.isBlank(carId)) {
				return this.getResponse(false, "车辆ID不能为空");
			}
			// 停车场id
			String lotId = this.getString("lotId");
			// 为空则返回提示
			if (ValidateUtil.isBlank(lotId)) {
				return this.getResponse(false, "停车场ID不能为空");
			}
			// 预计到达时间
			String arriveTime = this.getString("arriveTime");
			// 为空则返回提示
			if (ValidateUtil.isBlank(arriveTime)) {
				return this.getResponse(false, "预计到达时间不能为空");
			}
			// 预计到达时间前面加上今天的日期
			arriveTime = DatetimeUtil.getNowDate() + " " + arriveTime;
			
			// 填充参数
			Map<String, Object> map = new HashMap<>();
			map.put("id", StringUtil.createUUID()); // 生成一个32位随机码作为预约记录id
			map.put("user_id", user.get("ID")); // 预约人id
			map.put("car_id", carId);
			map.put("lot_id", lotId);
			map.put("appointment_time", DatetimeUtil.getNow()); // 预约时间取当前时间
			map.put("arrive_time", arriveTime);
			
			// 尝试扣减停车场剩余车位数
			int row1 = parkingLotService.minusParkingLotLiveNum(lotId);
			if (row1 > 0) {
				// 如果扣减成功，保存预约记录
				int row = parkingLotService.addParkingAppointment(map);
				if (row > 0) {
					// 保存记录成功，返回页面
					return this.getResponse(true);
				} else {
					// 否则加回车位数
					parkingLotService.addUpParkingLotLiveNum(lotId);		
					// 提示预约失败
					return this.getResponse(false, "预约失败，请稍后再试");
				}
			} else {
				// 扣减失败，提示车位已满
				return this.getResponse(false, "车位已满，请重新查询并选择其它停车场");
			}
		} else {
			// 获取不到登录人信息，提示登录状态异常
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}
	
	/**
	 * 取消预约
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/client/parking/cancel" })
	@ResponseBody
	public String cancel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		// 如不为空
		if (user != null) {
			// 预约记录id
			String id = this.getString("id");
			// 为空则提示
			if (ValidateUtil.isBlank(id)) {
				return this.getResponse(false, "预约ID不能为空");
			}
			
			// 执行取消预约
			int row = parkingLotService.cancelParkingAppointment(id);
			if (row > 0) {
				// 返回成功信息
				return this.getResponse(true);
			} else {
				// 返回失败信息
				return this.getResponse(false, "预约失败，请稍后再试");
			}
		} else {
			// 获取不到登录人，返回登录状态异常信息
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

}

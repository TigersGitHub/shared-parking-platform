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
import com.easyparking.service.ParkingPlaceService;
import com.easyparking.service.StatisticsService;
import com.easyparking.service.UserService;

/**
 * 管理端统计页面控制器
 */
@RequestMapping(value = { "/manage/view" })
@Controller
public class ViewController extends BaseController {

	/**
	 * 用户管理服务
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 停车场管理服务
	 */
	@Autowired
	private ParkingLotService lotService;

	/**
	 * 车位管理服务
	 */
	@Autowired
	private ParkingPlaceService placeService;

	/**
	 * 车辆管理服务
	 */
	@Autowired
	private CarService carService;
	
	/**
	 * 统计分析服务
	 */
	@Autowired
	private StatisticsService statisticsService;
	
	/**
	 * 平台管理统计页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/overall" })
	public String overall(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 创建一个空的map
		Map<String, Object> param = new HashMap<>();
		// 查询总用户数
		int user = userService.getUserInfoCount(param);
		map.put("user", user);
		// 查询总停车场数
		int lot = lotService.getParkingLotCount(param);
		map.put("lot", lot);
		// 查询总车位数
		int place = placeService.getParkingPlaceCount(param);
		map.put("place", place);
		// 查询总车辆数
		int car = carService.getCarInfoCount(param);
		map.put("car", car);
		// 返回页面视图
		return this.getView("manage/view/overall", map);
	}

	/**
	 * 登录情况统计
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/getDailyLoginCount" })
	@ResponseBody
	public String getDailyLoginCount(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 查询近七天登录数
		JSONArray array = statisticsService.getDailyLoginCount();
		// 返回到页面
		return this.getResponse(array, true);
	}
	
	/**
	 * 用户角色分布统计
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/getRoleUserCount" })
	@ResponseBody
	public String getRoleUserCount(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 查询用户角色分布统计数据
		JSONArray array = statisticsService.getRoleUserCount();
		// 遍历数据
		for (int i = 0; i < array.size(); i++) {
			// 取出单条数据
			JSONObject obj = array.getJSONObject(i);
			// 把type的数字转换成角色名
			switch (obj.getString("TYPE")) {
			case "1":
				obj.put("TYPE", "社会用户");
				break;
			case "2":
				obj.put("TYPE", "平台管理");
				break;
			case "3":
				obj.put("TYPE", "物业管理");
				break;
			default:
				obj.put("TYPE", "无角色");
				break;
			}
		}
		// 返回到页面
		return this.getResponse(array, true);
	}
	
	/**
	 * 用户区划分布统计
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/getRegionUserCount" })
	@ResponseBody
	public String getRegionUserCount(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 查询用户区划分布统计数据
		JSONArray array = statisticsService.getRegionUserCount();
		// 返回到页面
		return this.getResponse(array, true);
	}
	
	/**
	 * 用户使用时段统计
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/getHourlyLoginCount" })
	@ResponseBody
	public String getHourlyLoginCount(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 按使用时段统计登录数
		JSONArray array = statisticsService.getHourlyLoginCount();
		// 遍历每条数据 
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			// 把hour字段转成int类型（去掉01，02前面的0）
			obj.put("HOUR", Integer.parseInt(obj.getString("HOUR")));
		}
		// 返回页面
		return this.getResponse(array, true);
	}

	/**
	 * 停车场管理端统计页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/lotOverall" })
	public String lotOverall(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		// 若用户不为空
		if (user != null) {
			// 获取用户id
			String id = user.getString("ID");
			// 根据用户id查询所管理的停车场
			Map<String, Object> param = new HashMap<>();
			param.put("user_id", id);
			JSONArray lots = lotService.getLotUsers(param);
			if (lots != null && lots.size() > 0) {
				// 如果有停车场权限，把停车场信息返回到页面
				map.put("lots", lots);
				// 取第一个停车场的id查询其统计数据
//				JSONObject lot = lots.getJSONObject(0);
//				String lotId = lot.getString("LOT_ID");
//				param.clear();
//				param.put("ID", lotId);
//				JSONArray lotInfo = lotService.getParkingLotByPage(param);
			}
		}
		// 返回页面视图
		return this.getView("manage/view/lotOverall", map);
	}

}

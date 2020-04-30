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
import com.easyparking.service.AuditService;
import com.easyparking.service.CarService;
import com.easyparking.util.data.DatetimeUtil;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;

/**
 * 车辆管理控制器
 */
@Controller
public class CarController extends BaseController {

	/**
	 * 注入车辆管理服务
	 */
	@Autowired
	private CarService carService;
	
	/**
	 * 注入信息审核服务
	 */
	@Autowired
	private AuditService auditService;

	/**
	 * 移动端-我的车辆页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/car/index" })
	public String myCar(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取登录用户信息
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 获取用户id
			String id = user.getString("ID");
			// 根据用户id查询用户名下的车辆
			JSONArray cars = carService.getCarInfoByUser(id);
			if (cars != null && cars.size() > 0) {
				// 如果名下有车辆，返回有车辆（hasCar=1）
				map.put("hasCar", "1");
				// 返回车辆信息的JSON数组
				map.put("cars", cars.toJSONString());
			} else {
				// 名下没有车辆，只返回一个无车辆的状态码（hasCar=0）
				map.put("hasCar", "0");
			}
		}
		return this.getView("client/car/car", map);
	}

	/**
	 * 移动端-车辆添加页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/car/addCar" })
	public String addCar(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 没有什么需要显示的数据，直接返回页面视图
		return this.getView("client/car/addCar", map);
	}

	/**
	 * 管理端-车辆管理页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 没有什么需要显示的数据，直接返回页面视图
		return this.getView("manage/car/car", map);
	}

	/**
	 * 管理端的车辆列表查询
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/list" })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 所有者（可以为空）
		String owner = this.getString("owner");
		// 车牌号（可以为空）
		String plate_num = this.getString("plate_num");

		// 创建一个请求参数map
		Map<String, Object> param = new HashMap<String, Object>();
		
		// 根据页码和每页条数计算start值（从第几行开始读取数据）
		int start = (pageNum - 1) * pageSize;
		
		// 把起始行和每页条数放进请求参数里
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		
		// 如果所有者不为空，将所有者放进请求参数
		if (ValidateUtil.isNotBlank(owner)) {
			param.put("owner", owner);
		}
		
		// 如果车牌号不为空，将车牌号放进请求参数
		if (ValidateUtil.isNotBlank(plate_num)) {
			param.put("plate_num", plate_num);
		}
		
		// 根据拼装好的参数查询车辆信息
		JSONArray data = carService.getCarInfoByPage(param);
		
		// 查询车辆总数（页面右下角显示）
		int total = carService.getCarInfoCount(param);
		
		// 将结果返回给页面
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, null);
	}
	
	/**
	 * 车辆添加操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/add" })
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 车牌号
			String plate_num = this.getString("plate_num");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(plate_num)) {
				return this.getResponse(false, "车牌号不能为空");
			}
			// 车架号
			String identification_num = this.getString("identification_num");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(identification_num)) {
				return this.getResponse(false, "车架号不能为空");
			}
			// 品牌型号
			String brand = this.getString("brand");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(brand)) {
				return this.getResponse(false, "品牌型号不能为空");
			}
			// 车型
			String type = this.getString("type");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "车型不能为空");
			}
			// 颜色
			String color = this.getString("color");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(color)) {
				return this.getResponse(false, "颜色不能为空");
			}
			// 车辆照片
			String image1 = this.getString("image1");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(image1)) {
				return this.getResponse(false, "车辆及车牌照片不能为空");
			}
			// 车架号证明材料
			String image2 = this.getString("image2");
			// 如果为空则返回错误提示
			if (ValidateUtil.isBlank(image2)) {
				return this.getResponse(false, "车架号证明照片不能为空");
			}

			// 创建一个请求参数map
			Map<String, Object> map = new HashMap<>();
			
			// 生成一个随机的车辆id
			String carId = StringUtil.createUUID();
			
			//将校验好的所有数据放进请求参数Map里
			map.put("id", carId);
			map.put("owner", user.getString("ID"));
			map.put("plate_num", plate_num);
			map.put("identification_num", identification_num);
			map.put("brand", brand);
			map.put("type", type);
			map.put("color", color);
			map.put("image1", image1.getBytes());
			map.put("image2", image2.getBytes());
			
			// 保存到数据库
			int row = carService.insertCar(map);
			
			if (row > 0) {
				// 如果插入成功，再拼装一条待审批的记录
				Map<String, Object> map1 = new HashMap<>();
				// 生成一个审批id
				map1.put("id", StringUtil.createUUID());
				// 审批类型，2是车辆
				map1.put("type", "2");
				// 车辆id，记录要审批的是哪个车辆
				map1.put("data_id", carId);
				// 提交人（当前登录人）
				map1.put("submitter", user.getString("ID"));
				// 提交时间
				map1.put("submit_time", DatetimeUtil.getNow());
				// 保存到数据库
				row = auditService.saveAuditTask(map1);
				if (row > 0) {
					// 如果成功，返回成功信息
					return this.getResponse(true);
				} else {
					// 如果保存待审批信息失败，返回失败信息
					return this.getResponse(false, "数据保存失败，请稍后再试");
				}
			} else {
				// 如果保存车辆信息失败，返回失败信息
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 如果获取不到用户，可能是页面打开时间太长过期了，提示用户重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 车辆禁用操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/disable" })
	@ResponseBody
	public String disable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 要禁用的车辆id
		String id = this.getString("id");
		// 如果车辆id为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车辆ID不能为空");
		}
		
		// 保存到数据库
		int row = carService.disableCar(id);
		
		if (row > 0) {
			// 成功则提示操作成功
			return this.getResponse(true);
		} else {
			// 否则提示禁用失败
			return this.getResponse(false, "禁用失败，请稍后再试");
		}
	}

	/**
	 * 车辆启用操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/enable" })
	@ResponseBody
	public String enable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 要启用的车辆id
		String id = this.getString("id");
		// 如果车辆id为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车辆ID不能为空");
		}

		// 保存到数据库
		int row = carService.enableCar(id);
		if (row > 0) {
			// 成功则提示操作成功
			return this.getResponse(true);
		} else {
			// 否则提示启用失败
			return this.getResponse(false, "启用失败，请稍后再试");
		}
	}

	/**
	 * 车辆删除操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/del" })
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车辆id
		String id = this.getString("id");
		// 如果车辆id为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车辆ID不能为空");
		}
		
		// 保存到数据库
		int row = carService.deleteCar(id);
		if (row > 0) {
			// 成功则提示操作成功
			return this.getResponse(true);
		} else {
			// 否则提示删除失败
			return this.getResponse(false, "删除失败，请稍后再试");
		}
	}

	/**
	 * 车辆编辑的保存操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/save" })
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 车辆id
			String id = this.getString("id");
			// 如果为空则提示
			if (ValidateUtil.isBlank(id)) {
				return this.getResponse(false, "车辆ID不能为空");
			}
			// 车牌号
			String plate_num = this.getString("plate_num");
			// 如果为空则提示
			if (ValidateUtil.isBlank(plate_num)) {
				return this.getResponse(false, "车牌号不能为空");
			}
			// 车架号
			String identification_num = this.getString("identification_num");
			// 如果为空则提示
			if (ValidateUtil.isBlank(identification_num)) {
				return this.getResponse(false, "车架号不能为空");
			}
			// 品牌型号
			String brand = this.getString("brand");
			// 如果为空则提示
			if (ValidateUtil.isBlank(brand)) {
				return this.getResponse(false, "品牌型号不能为空");
			}
			// 车型
			String type = this.getString("type");
			// 如果为空则提示
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "车型不能为空");
			}
			// 颜色
			String color = this.getString("color");
			// 如果为空则提示
			if (ValidateUtil.isBlank(color)) {
				return this.getResponse(false, "颜色不能为空");
			}
			
			// 创建一个请求参数map
			Map<String, Object> map = new HashMap<>();
			// 将数据放到请求参数里
			map.put("id", id);
			map.put("owner", user.getString("ID"));
			map.put("plate_num", plate_num);
			map.put("identification_num", identification_num);
			map.put("brand", brand);
			map.put("type", type);
			map.put("color", color);
			
			// 保存到数据库
			int row = carService.updateCar(map);
			if (row > 0) {
				// 成功则返回成功信息
				return this.getResponse(true);
			} else {
				// 失败返回保存失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 如果获取不到用户，可能是页面打开时间太长过期了，提示用户重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 点击车辆编辑的查询操作
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/car/queryOne" })
	@ResponseBody
	public String queryOne(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 车辆id
		String id = this.getString("id");
		// 如果id为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车辆ID不能为空");
		} else {
			// 查询车辆信息
			JSONObject object = carService.getCarInfo(id);
			
			// 返回车辆信息
			return this.getResponse(object, true);
		}
	}

}

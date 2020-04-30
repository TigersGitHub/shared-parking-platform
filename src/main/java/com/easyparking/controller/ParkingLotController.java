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
import com.easyparking.service.ParkingLotService;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;

/**
 * 停车场管理控制器
 */
@Controller
public class ParkingLotController extends BaseController {
	
	/**
	 * 停车场管理服务
	 */
	@Autowired
	private ParkingLotService parkingLotService;

	/**
	 * 管理端-停车场列表页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		return this.getView("manage/parkingLot/parkingLot", map);
	}

	/**
	 * 查询停车场信息列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/list" })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 停车场名称
		String name = this.getString("name");
		// 停车场类别
		String type = this.getString("type");
		
		// 拼装参数
		Map<String, Object> param = new HashMap<String, Object>();
		int start = (pageNum - 1) * pageSize;
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		// 若停车场名称不为空则添加到参数
		if (ValidateUtil.isNotBlank(name)) {
			param.put("name", name);
		}
		// 若停车场类别不为空则添加到参数
		if (ValidateUtil.isNotBlank(type)) {
			param.put("type", type);
		}
		
		// 查询列表
		JSONArray data = parkingLotService.getParkingLotByPage(param);
		// 查询停车场总数
		int total = parkingLotService.getParkingLotCount(param);
		// 返回数据
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, null);
	}
	
	/**
	 * 查询停车场下拉框
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/select" })
	@ResponseBody
	public String select(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 所在区划
		String region = this.getString("region");
		
		// 创建参数map 
		Map<String, Object> param = new HashMap<String, Object>();
		// 如所在区划不为空则添加到参数map，否则就是查全部停车场
		if (ValidateUtil.isNotBlank(region)) {
			param.put("region", region);
		}
		
		// 查询下拉框数据
		JSONArray data = parkingLotService.getParkingLotSelector(param);
		// 返回结果
		return this.getResponse(data, true, null);
	}
	
	/**
	 * 添加停车场
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/add" })
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		// 如不为空
		if (user != null) {
			// 停车场名称
			String name = this.getString("name");
			// 如果为空则提示
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "停车场名称不能为空");
			}
			// 停车场类型
			String type = this.getString("type");
			// 如果为空则提示
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "组织机构类型不能为空");
			}
			// 车位数
			Integer place_num = this.getInteger("place_num");
			// 如果为空则提示
			if (place_num == null) {
				return this.getResponse(false, "额定车位数量不能为空");
			}
			// 每小时停车费
			Double price = this.getDouble("price");
			// 如果为空则提示
			if (price == null) {
				return this.getResponse(false, "停车费（单价）不能为空");
			}
			// 是否封顶
			String capped = this.getString("capped");
			// 如果为空则提示
			if (ValidateUtil.isBlank(capped)) {
				return this.getResponse(false, "是否有封顶金额不能为空");
			}
			// 每日封顶金额
			Double capped_price = this.getDouble("capped_price");
			// 如果为空则提示
			if ("1".equals(capped) && capped_price == null) {
				return this.getResponse(false, "停车费（封顶金额）不能为空");
			}
			// 所在区划
			String region = this.getString("region");
			// 如果为空则提示
			if (ValidateUtil.isBlank(region)) {
				return this.getResponse(false, "所属行政区划不能为空");
			}
			// 详细地址
			String address = this.getString("address");
			// 如果为空则提示
			if (ValidateUtil.isBlank(address)) {
				return this.getResponse(false, "详细地址不能为空");
			}
			// 所属组织机构
			String org_id = this.getString("org_id");
			// 如果为空则提示
			if (ValidateUtil.isBlank(org_id)) {
				return this.getResponse(false, "所属管理方不能为空");
			}
			
			// 填充参数
			Map<String, Object> map = new HashMap<>();
			map.put("id", StringUtil.createUUID());	// 32位随机码，作为停车场id
			map.put("name", name);
			map.put("type", type);
			map.put("place_num", place_num);
			map.put("price", price);
			map.put("capped", capped);
			map.put("capped_price", capped_price);
			map.put("region", region);
			map.put("address", address);
			map.put("org_id", org_id);
			map.put("creator", user.getString("ID")); // 创建人就是当前登录人id
			
			// 执行新增
			int row = parkingLotService.insertParkingLot(map);
			if (row > 0) {
				// 返回执行成功
				return this.getResponse(true);
			} else {
				// 返回执行失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 登录人为空，返回登录状态异常
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}
	
	/**
	 * 禁用停车场
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/disable" })
	@ResponseBody
	public String disable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 停车场id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "停车场ID不能为空");
		}
		
		// 执行禁用
		int row = parkingLotService.disableParkingLot(id);
		
		if (row > 0) {
			// 返回执行成功
			return this.getResponse(true);
		} else {
			// 返回执行失败
			return this.getResponse(false, "禁用失败，请稍后再试");
		}
	}

	/**
	 * 启用停车场
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/enable" })
	@ResponseBody
	public String enable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 停车场id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "停车场ID不能为空");
		}
		
		// 执行启用
		int row = parkingLotService.enableParkingLot(id);
		if (row > 0) {
			// 返回启用成功
			return this.getResponse(true);
		} else {
			// 返回启用失败
			return this.getResponse(false, "启用失败，请稍后再试");
		}
	}

	/**
	 * 删除停车场
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/del" })
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 停车场id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "停车场ID不能为空");
		}
		
		// 执行删除
		int row = parkingLotService.deleteParkingLot(id);
		if (row > 0) {
			// 返回删除成功
			return this.getResponse(true);
		} else {
			// 返回删除失败
			return this.getResponse(false, "删除失败，请稍后再试");
		}
	}

	/**
	 * 编辑后保存
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/save" })
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 停车场id
			String id = this.getString("id");
			// 为空则提示
			if (ValidateUtil.isBlank(id)) {
				return this.getResponse(false, "停车场ID不能为空");
			}
			// 停车场名称
			String name = this.getString("name");
			// 为空则提示
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "停车场名称不能为空");
			}
			// 停车场类型
			String type = this.getString("type");
			// 为空则提示
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "停车场类型不能为空");
			}
			// 车位数
			Integer place_num = this.getInteger("place_num");
			// 为空则提示
			if (place_num == null) {
				return this.getResponse(false, "额定车位数量不能为空");
			}
			// 每小时停车费
			Double price = this.getDouble("price");
			// 为空则提示
			if (price == null) {
				return this.getResponse(false, "停车费（单价）不能为空");
			}
			// 是否封顶
			String capped = this.getString("capped");
			// 为空则提示
			if (ValidateUtil.isBlank(capped)) {
				return this.getResponse(false, "是否有封顶金额不能为空");
			}
			// 每日封顶金额
			Double capped_price = this.getDouble("capped_price");
			// 为空则提示
			if ("1".equals(capped) && capped_price == null) {
				return this.getResponse(false, "停车费（封顶金额）不能为空");
			}
			// 所属区划
			String region = this.getString("region");
			// 为空则提示
			if (ValidateUtil.isBlank(region)) {
				return this.getResponse(false, "所属行政区划不能为空");
			}
			// 详细地址
			String address = this.getString("address");
			// 为空则提示
			if (ValidateUtil.isBlank(address)) {
				return this.getResponse(false, "详细地址不能为空");
			}
			// 所属组织机构
			String org_id = this.getString("org_id");
			// 为空则提示
			if (ValidateUtil.isBlank(org_id)) {
				return this.getResponse(false, "所属管理方不能为空");
			}
			
			// 填充参数
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("name", name);
			map.put("type", type);
			map.put("place_num", place_num);
			map.put("price", price);
			map.put("capped", capped);
			map.put("capped_price", capped_price);
			map.put("region", region);
			map.put("address", address);
			map.put("org_id", org_id);
			map.put("updator", user.getString("ID")); // 修改人填写当前登录人id
			
			// 保存数据
			int row = parkingLotService.updateParkingLot(map);
			if (row > 0) {
				// 返回保存成功信息
				return this.getResponse(true);
			} else {
				// 返回保存失败信息
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 登录人为空，返回登录状态异常
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}
	
	/**
	 * 给物业用户分配停车场权限
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/addLotUser" })
	@ResponseBody
	public String addLotUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 停车场id
			String lot_id = this.getString("lot_id");
			// 为空则提示
			if (ValidateUtil.isBlank(lot_id)) {
				return this.getResponse(false, "停车场ID不能为空");
			}
			// 物业管理员id
			String user_id = this.getString("user_id");
			// 为空则提示
			if (ValidateUtil.isBlank(user_id)) {
				return this.getResponse(false, "用户ID不能为空");
			}
			
			// 填充参数
			Map<String, Object> map = new HashMap<>();
			map.put("id", StringUtil.createUUID());
			map.put("lot_id", lot_id);
			map.put("user_id", user_id);
			map.put("creator", user.get("ID"));
			// 执行添加
			int row = parkingLotService.insertLotUser(map);
			if (row > 0) {
				// 添加成功
				return this.getResponse(true);
			} else {
				// 添加失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 登录人为空，返回登录状态异常
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 取消物业用户停车场权限
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/delLotUser" })
	@ResponseBody
	public String delLotUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 停车场权限记录id
		String id = this.getString("id");
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "ID不能为空");
		}
		// 删除权限
		int row = parkingLotService.deleteLotUser(id);
		if (row > 0) {
			// 删除成功
			return this.getResponse(true);
		} else {
			// 删除失败
			return this.getResponse(false, "删除失败，请稍后再试");
		}
	}
	
	/**
	 * 查询停车场授权列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingLot/lotUserList" })
	@ResponseBody
	public String lotUserList(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 停车场id
		String lot_id = this.getString("lot_id");
		if (ValidateUtil.isBlank(lot_id)) {
			return this.getResponse(false, "停车场ID不能为空");
		}
		
		// 填充参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("lot_id", lot_id);
	
		// 查询停车场授权数据
		JSONArray data = parkingLotService.getLotUsers(param);
		// 返回结果
		return this.getResponse(data, true, null);
	}
	
}

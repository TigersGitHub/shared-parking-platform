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
import com.easyparking.service.OrgService;
import com.easyparking.service.ParkingPlaceService;
import com.easyparking.service.UserService;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;

/**
 * 车位管理控制器
 */
@Controller
public class ParkingPlaceController extends BaseController {
	
	/**
	 * 车位管理服务
	 */
	@Autowired
	private ParkingPlaceService parkingPlaceService;
	
	/**
	 * 用户管理服务
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 物业组织机构管理服务
	 */
	@Autowired
	private OrgService orgService;

	/**
	 * 客户端-我的车位页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/place/index" })
	public String place(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		// 若用户不为空 
		if (user != null) {
			// 取用户id
			String id = user.getString("ID");
			// 根据用户id查询名下车位信息
			JSONArray places = parkingPlaceService.getParkingPlaceInfoByUser(id);
			if (places != null && places.size() > 0) {
				// 查询到车位信息，状态hasPlace=1，并返回车位数据
				map.put("hasPlace", "1");
				map.put("places", places.toJSONString());
			} else {
				// 未查询到车位信息，hasPlace=0
				map.put("hasPlace", "0");
			}
		}
		// 返回页面视图
		return this.getView("client/place/place", map);
	}
	
	/**
	 * 客户端-车位市场页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/place/market" })
	public String market(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 创建一个map
		Map<String, Object> param = new HashMap<>();
		// 设置参数VERIFIED=1（只查审核通过的）
		param.put("verified", "1");
		// 查询车位信息
		JSONArray places = parkingPlaceService.getParkingPlaceByPage(param);
		if (places != null && places.size() > 0) {
			// 查到结果，返回hasPlace=1，及车位数据
			map.put("hasPlace", "1");
			map.put("places", places.toJSONString());
		} else {
			// 没查到结果，hasPlace=0
			map.put("hasPlace", "0");
		}
		// 返回页面视图
		return this.getView("client/place/market", map);
	}

	/**
	 * 客户端-添加车位页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/place/addPlace" })
	public String addPlace(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 直接返回页面视图
		return this.getView("client/place/add", map);
	}

	/**
	 * 客户端-车位编辑页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/place/edit" })
	public String editPlace(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 车位id
		String id = this.getString("id");
		// 查询车位信息
		JSONObject obj = parkingPlaceService.getParkingPlace(id);
		// 如果不为空，把车位信息放入视图map
		if (obj != null) {
			map.putAll(obj);
		}
		// 返回页面视图
		return this.getView("client/place/edit", map);
	}

	/**
	 * 车位管理列表页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 直接返回页面视图
		return this.getView("manage/parkingPlace/parkingPlace", map);
	}

	/**
	 * 查询车位列表数据（分页）
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/list" })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页行数
		Integer pageSize = this.getInteger("pageSize");
		// 停车场id
		String lot_id = this.getString("lot_id");
		// 所有者
		String owner = this.getString("owner");
		
		// 创建参数map
		Map<String, Object> param = new HashMap<String, Object>();
		// 计算起始行号
		int start = (pageNum - 1) * pageSize;
		
		// 填充参数
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		if (ValidateUtil.isNotBlank(lot_id)) {
			param.put("lot_id", lot_id);
		}
		if (ValidateUtil.isNotBlank(owner)) {
			param.put("owner", owner);
		}
		
		// 查询车位数据
		JSONArray data = parkingPlaceService.getParkingPlaceByPage(param);
		// 查询车位总数
		int total = parkingPlaceService.getParkingPlaceCount(param);
		// 返回数据
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, null);
	}
	
	/**
	 * 获取图片数据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/getImage" })
	@ResponseBody
	public String getImage(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		if (ValidateUtil.isNotBlank(id)) {
			// 车位id不为空，单独查询车位表的图片字段
			return parkingPlaceService.getParkingPlaceImage(id);
		} else {
			// 车位id为空，返回null
			return null;
		}
	}
	
	/**
	 * 添加车位
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/add" })
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 停车场id
			String lot_id = this.getString("lot_id");
			// 如果为空则提示
			if (ValidateUtil.isBlank(lot_id)) {
				return this.getResponse(false, "停车场ID不能为空");
			}
			// 车位编号
			String place_code = this.getString("place_code");
			// 如果为空则提示
			if (ValidateUtil.isBlank(place_code)) {
				return this.getResponse(false, "车位编号不能为空");
			}
			// 所有者类型
			String owner_type = this.getString("owner_type");
			// 如果为空则提示
			if (ValidateUtil.isBlank(owner_type)) {
				return this.getResponse(false, "所有者类型不能为空");
			}
			// 所有者id
			String owner;
			if ("1".equals(owner_type)) {
				// 类型是企业，则所有者必填
				owner = this.getString("owner");
				// 所有者为空则提示
				if (ValidateUtil.isBlank(owner)) {
					return this.getResponse(false, "所有者（企业）不能为空");
				}
			} else {
				// 类型是个人，所有者取登录用户id
				owner = user.getString("ID");
			}
			// 交易类别
			String trade_type = this.getString("trade_type");
			// 如果为空则提示
			if (ValidateUtil.isBlank(trade_type)) {
				return this.getResponse(false, "交易类别不能为空");
			}
			// 车位简介
			String trade_title = this.getString("trade_title");
			// 如果为空则提示
			if (ValidateUtil.isBlank(trade_title)) {
				return this.getResponse(false, "车位简介不能为空");
			}
			// 图片
			String image = this.getString("image");
			// 如果为空则提示
			if (ValidateUtil.isBlank(image)) {
				return this.getResponse(false, "车位图片不能为空");
			}
			
			// 创建一个参数map
			Map<String, Object> map = new HashMap<>();
			// 填充参数
			map.put("id", StringUtil.createUUID()); // 生成一个32位uuid作为车位id
			map.put("lot_id", lot_id);
			map.put("place_code", place_code);
			map.put("owner_type", owner_type);
			map.put("owner", owner);
			map.put("trade_type", trade_type);
			map.put("trade_title", trade_title);
			map.put("image", image.getBytes());
			map.put("creator", user.getString("ID")); // 创建人取登录人id
			
			// 执行插入
			int row = parkingPlaceService.insertParkingPlace(map);
			if (row > 0) {
				// 插入成功
				return this.getResponse(true);
			} else {
				// 插入失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录用户，报错提示
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 车位禁用
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/disable" })
	@ResponseBody
	public String disable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 执行禁用
		int row = parkingPlaceService.disableParkingPlace(id);
		if (row > 0) {
			// 禁用成功
			return this.getResponse(true);
		} else {
			// 禁用失败
			return this.getResponse(false, "禁用失败，请稍后再试");
		}
	}

	/**
	 * 车位启用
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/enable" })
	@ResponseBody
	public String enable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 执行启用
		int row = parkingPlaceService.enableParkingPlace(id);
		if (row > 0) {
			// 启用成功
			return this.getResponse(true);
		} else {
			// 启用失败
			return this.getResponse(false, "启用失败，请稍后再试");
		}
	}

	/**
	 * 车位删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/del" })
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 执行删除
		int row = parkingPlaceService.deleteParkingPlace(id);
		if (row > 0) {
			// 删除成功
			return this.getResponse(true);
		} else {
			// 删除失败
			return this.getResponse(false, "删除失败，请稍后再试");
		}
	}
	
	/**
	 * 车位挂牌
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/putOn" })
	@ResponseBody
	public String putOn(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 执行挂牌
		int row = parkingPlaceService.putOnParkingPlace(id);
		if (row > 0) {
			// 成功
			return this.getResponse(true);
		} else {
			// 失败
			return this.getResponse(false, "挂牌失败，请稍后再试");
		}
	}	
	
	/**
	 * 车位取消挂牌
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/putOff" })
	@ResponseBody
	public String putOff(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 执行取消挂牌
		int row = parkingPlaceService.putOffParkingPlace(id);
		if (row > 0) {
			// 取消挂牌成功
			return this.getResponse(true);
		} else {
			// 取消挂牌失败
			return this.getResponse(false, "取消挂牌失败，请稍后再试");
		}
	}
	
	/**
	 * 车位标记为交易成功
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/complete" })
	@ResponseBody
	public String complete(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 标记为交易成功
		int row = parkingPlaceService.completeTrade(id);
		if (row > 0) {
			// 操作成功
			return this.getResponse(true);
		} else {
			// 操作失败
			return this.getResponse(false, "操作失败，请稍后再试");
		}
	}
	
	/**
	 * 车位市场查询联系方式
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/contact" })
	@ResponseBody
	public String contact(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 车位id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "车位ID不能为空");
		}
		// 查询车位信息
		JSONObject obj = parkingPlaceService.getParkingPlace(id);
		// 车位信息不为空
		if (obj != null) {
			// 取所有者类型
			String ownerType = obj.getString("OWNER_TYPE");
			// 取所有者id
			String owner = obj.getString("OWNER_ID");
			if ("2".equals(ownerType)) {
				// 所有者类型是个人，根据用户id查询用户信息
				JSONObject user = userService.getUserInfo(owner);
				if (user != null) {
					// 查到了，取用户姓名电话
					String name = user.getString("NAME");
					String mobile = user.getString("MOBILE");
					// 返回数据
					JSONObject resp = new JSONObject();
					resp.put("name", name);
					resp.put("mobile", mobile);
					return this.getResponse(resp, true);
				} else {
					// 查不到，返回提示信息
					return this.getResponse(false, "未查询到联系人");
				}
			} else {
				// 所有者类型是企业，根据物业组织机构id查询企业信息
				JSONObject org = orgService.getOrgInfo(owner);
				if (org != null) {
					// 查到了，取物业公司联系人、联系电话
					String name = org.getString("LINKMAN");
					String mobile = org.getString("PHONE_NUM");
					// 返回数据
					JSONObject resp = new JSONObject();
					resp.put("name", name);
					resp.put("mobile", mobile);
					return this.getResponse(resp, true);
				} else {
					// 查不到，返回提示信息
					return this.getResponse(false, "未查询到联系人");
				}
			}
		} else {
			// 车位都查不到，返回提示信息
			return this.getResponse(false, "车位不存在");
		}
	}

	/**
	 * 车位编辑保存
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/parkingPlace/save" })
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		// 登录用户不为空
		if (user != null) {
			// 车位id
			String id = this.getString("id");
			// 为空则提示
			if (ValidateUtil.isBlank(id)) {
				return this.getResponse(false, "车位ID不能为空");
			}
			// 车位编号
			String place_code = this.getString("place_code");
			// 为空则提示
			if (ValidateUtil.isBlank(place_code)) {
				return this.getResponse(false, "车位编号不能为空");
			}
			// 所有者类型
			String owner_type = this.getString("owner_type");
			// 为空则提示
			if (ValidateUtil.isBlank(owner_type)) {
				return this.getResponse(false, "所有者类型不能为空");
			}
			// 所有者id
			String owner = this.getString("owner");
			// 类型是企业且所有者为空则提示
			if ("1".equals(owner_type) && ValidateUtil.isBlank(owner)) {
				return this.getResponse(false, "所有者（企业）不能为空");
			}
			// 交易类别
			String trade_type = this.getString("trade_type");
			// 为空则提示
			if (ValidateUtil.isBlank(trade_type)) {
				return this.getResponse(false, "交易类别不能为空");
			}
			// 车位简介
			String trade_title = this.getString("trade_title");
			// 为空则提示
			if (ValidateUtil.isBlank(trade_title)) {
				return this.getResponse(false, "车位简介不能为空");
			}
			// 图片
			String image = this.getString("image");
			// 为空则提示
			if (ValidateUtil.isBlank(image)) {
				return this.getResponse(false, "车位图片不能为空");
			}
			
			// 创建一个map
			Map<String, Object> map = new HashMap<>();
			// 填充参数
			map.put("id", id);
			map.put("place_code", place_code);
			map.put("owner_type", owner_type);
			if ("1".equals(owner_type)) {
				// type=1是企业，填物业组织机构id
				map.put("owner", owner);
			} else {
				// type=2是个人，填当前用户id
				map.put("owner", user.getString("ID"));
			}
			map.put("trade_type", trade_type);
			map.put("trade_title", trade_title);
			map.put("image", image.getBytes());
			map.put("updator", user.getString("ID")); // 修改人填当前登录用户id
			
			// 执行更新
			int row = parkingPlaceService.updateParkingPlace(map);
			if (row > 0) {
				// 更新保存成功
				return this.getResponse(true);
			} else {
				// 保存失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录用户，提示重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

}

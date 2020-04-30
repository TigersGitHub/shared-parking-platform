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
import com.easyparking.service.OrgService;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;

/**
 * 物业组织机构控制器
 */
@Controller
public class OrgController extends BaseController {
	
	/**
	 * 物业组织机构管理服务
	 */
	@Autowired
	private OrgService orgService;

	/**
	 * 管理端-物业组织机构列表页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 没有什么处理，直接返回视图
		return this.getView("manage/org/org", map);
	}

	/**
	 * 分页查询物业组织机构列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/list" })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 物业公司名称
		String name = this.getString("name");
		// 物业公司类型
		String type = this.getString("type");
		
		Map<String, Object> param = new HashMap<String, Object>();
		// 计算起始行号 
		int start = (pageNum - 1) * pageSize;
		// 拼装查询参数
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		// 如果物业公司名称不为空，添加到查询参数中
		if (ValidateUtil.isNotBlank(name)) {
			param.put("name", name);
		}
		// 如果物业公司类型不为空，添加到查询参数中
		if (ValidateUtil.isNotBlank(type)) {
			param.put("type", type);
		}
		
		// 查询物业公司列表
		JSONArray data = orgService.getOrgInfoByPage(param);
		// 查询物业公司总数
		int total = orgService.getOrgInfoCount(param);
		// 返回数据
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, null);
	}
	
	/**
	 * 物业组织机构下拉框数据查询
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/select" })
	@ResponseBody
	public String select(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 不需要准备参数，直接查出所有的物业公司
		JSONArray data = orgService.getOrgInfoSelector(param);
		// 返回数据
		return this.getResponse(data, true, null);
	}
	
	/**
	 * 组织机构新增操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/add" })
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 取当前登录人信息
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 组织机构名称
			String name = this.getString("name");
			// 若为空返回错误信息
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "组织机构名称不能为空");
			}
			// 组织机构简称
			String short_name = this.getString("short_name");
			// 若为空返回错误信息
			if (ValidateUtil.isBlank(short_name)) {
				return this.getResponse(false, "组织机构简称不能为空");
			}
			// 组织机构类型
			String type = this.getString("type");
			// 若为空返回错误信息
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "组织机构类型不能为空");
			}
			// 统一社会信用代码
			String credit_code = this.getString("credit_code");
			// 所在区划
			String region = this.getString("region");
			// 详细地址
			String address = this.getString("address");
			// 联系人
			String linkman = this.getString("linkman");
			// 联系电话
			String phone_num = this.getString("phone_num");
			
			// 拼装参数map
			Map<String, Object> map = new HashMap<>();
			map.put("id", StringUtil.createUUID()); // 生成一个32位编码作为id
			map.put("name", name);
			map.put("short_name", short_name);
			map.put("type", type);
			map.put("credit_code", credit_code);
			map.put("region", region);
			map.put("address", address);
			map.put("linkman", linkman);
			map.put("phone_num", phone_num);
			map.put("creator", user.getString("ID")); // 当前登录用户的id作为创建人
			
			// 执行新增 
			int row = orgService.insertOrg(map);
			
			// 返回执行结果
			if (row > 0) {
				return this.getResponse(true);
			} else {
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 如果当前登录人为空，提示登录状态异常
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 组织机构禁用操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/disable" })
	@ResponseBody
	public String disable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 组织机构id
		String id = this.getString("id");
		// 若为空则返回提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "组织机构ID不能为空");
		}
		
		// 执行禁用
		int row = orgService.disableOrg(id);
		// 返回执行结果
		if (row > 0) {
			return this.getResponse(true);
		} else {
			return this.getResponse(false, "禁用失败，请稍后再试");
		}
	}

	/**
	 * 组织机构启用操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/enable" })
	@ResponseBody
	public String enable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 组织机构id
		String id = this.getString("id");
		// 若为空则返回提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "组织机构ID不能为空");
		}
		
		// 执行启用
		int row = orgService.enableOrg(id);
		// 返回执行结果
		if (row > 0) {
			return this.getResponse(true);
		} else {
			return this.getResponse(false, "启用失败，请稍后再试");
		}
	}

	/**
	 * 组织机构删除操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/del" })
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 组织机构id
		String id = this.getString("id");
		// 若为空则返回提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "组织机构ID不能为空");
		}
		
		// 执行删除
		int row = orgService.deleteOrg(id);
		// 返回结果
		if (row > 0) {
			return this.getResponse(true);
		} else {
			return this.getResponse(false, "删除失败，请稍后再试");
		}
	}

	/**
	 * 组织机构编辑保存操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/save" })
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录人
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 组织机构id
			String id = this.getString("id");
			// 为空则提示
			if (ValidateUtil.isBlank(id)) {
				return this.getResponse(false, "ID不能为空");
			}
			// 组织机构名称
			String name = this.getString("name");
			// 为空则提示
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "组织机构名称不能为空");
			}
			// 组织机构简称
			String short_name = this.getString("short_name");
			// 为空则提示
			if (ValidateUtil.isBlank(short_name)) {
				return this.getResponse(false, "组织机构简称不能为空");
			}
			// 组织机构类型
			String type = this.getString("type");
			// 为空则提示
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "组织机构类型不能为空");
			}
			// 统一社会信用代码
			String credit_code = this.getString("credit_code");
			// 所属区划
			String region = this.getString("region");
			// 详细地址
			String address = this.getString("address");
			// 联系人
			String linkman = this.getString("linkman");
			// 联系电话
			String phone_num = this.getString("phone_num");
			
			// 填充参数
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("name", name);
			map.put("short_name", short_name);
			map.put("type", type);
			map.put("credit_code", credit_code);
			map.put("region", region);
			map.put("address", address);
			map.put("linkman", linkman);
			map.put("phone_num", phone_num);
			map.put("updator", user.getString("ID")); // 修改人为当前登录人id
			
			// 执行修改
			int row = orgService.updateOrg(map);
			// 返回修改结果
			if (row > 0) {
				return this.getResponse(true);
			} else {
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录人信息，说明登录已过期，提示登录状态异常
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 点击编辑时查询单个组织机构信息
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/org/queryOne" })
	@ResponseBody
	public String queryOne(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 要查询的组织机构id
		String id = this.getString("id");
		if (ValidateUtil.isBlank(id)) {
			// 为空则提示
			return this.getResponse(false, "组织机构ID不能为空");
		} else {
			// 不为空则执行查询
			JSONObject object = orgService.getOrgInfo(id);
			// 返回查询结果
			return this.getResponse(object, true);
		}
	}

}

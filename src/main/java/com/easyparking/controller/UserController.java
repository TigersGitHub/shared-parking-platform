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
import com.easyparking.service.UserService;
import com.easyparking.util.data.DatetimeUtil;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;

/**
 * 用户管理控制器
 */
@Controller
public class UserController extends BaseController {
	
	/**
	 * 用户管理服务
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 审批管理服务
	 */
	@Autowired
	private AuditService auditService;
	
	/**
	 * 移动端-用户信息修改页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/user/edit" })
	public String edit(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 不为空，放进页面视图map
			map.putAll(user);
		}
		// 返回页面视图
		return this.getView("client/user/edit", map);
	}
	
	/**
	 * 移动端-用户密码重置页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/user/reset" })
	public String reset(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 返回页面视图
		return this.getView("client/user/reset", map);
	}

	/**
	 * 移动端-用户实名认证页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/user/auth" })
	public String auth(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			String id = user.getString("ID");
			String authStatus = user.getString("AUTHENTICATION");
			if (!"0".equals(authStatus)) {
				JSONArray arr = auditService.getUserAuditResult("1", id);
				map.put("record", arr.toJSONString());
			}
			map.put("auth", authStatus);
		}
		// 返回页面视图
		return this.getView("client/user/auth", map);
	}
	
	/**
	 * 移动端-用户提交实名认证信息页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/client/user/addAuth" })
	public String addAuth(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			String auth = user.getString("AUTHENTICATION");
			String name = user.getString("NAME");
			map.put("name", name);
			if (ValidateUtil.isNotBlank(auth) 
					&& ("1".equals(auth) || "2".equals(auth))) {
				// 审批中、审批通过的跳转至实名认证列表页
				return auth(request, response, map);
			}
		}
		// 返回页面视图
		return this.getView("client/user/addAuth", map);
	}

	/**
	 * 管理端-用户管理列表页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			map.put("id", user.getString("ID"));
		}
		// 返回页面视图
		return this.getView("manage/user/user", map);
	}

	/**
	 * 用户添加
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/add" })
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 账号
			String account = this.getString("account");
			if (ValidateUtil.isBlank(account)) {
				return this.getResponse(false, "账号不能为空");
			}
			// 根据账号查询用户
			JSONObject object = userService.getUserInfo(account);
			if (object != null) {
				// 查到了，提示账号重复
				return this.getResponse(false, "账号已存在");
			}
			// 姓名
			String name = this.getString("name");
			// 为空则提示
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "姓名不能为空");
			}
			// 密码
			String password = this.getString("password");
			// 为空则提示
			if (ValidateUtil.isBlank(password)) {
				return this.getResponse(false, "密码不能为空");
			}
			// 手机号
			String mobile = this.getString("mobile");
			// 邮箱
			String email = this.getString("email");
			// 为空则提示
			if (ValidateUtil.isBlank(email)) {
				return this.getResponse(false, "邮箱不能为空");
			}
			// 所属区划
			String region = this.getString("region");
			// 详细地址
			String address = this.getString("address");
			// 用户类型
			String type = this.getString("type");
			// 为空则提示
			if (ValidateUtil.isBlank(type)) {
				return this.getResponse(false, "用户类型不能为空");
			}
			// 组织机构id
			String orgId = this.getString("org_id");
			// 用户类型为3，物业组织机构为空则提示
			if ("3".equals(type) && ValidateUtil.isBlank(orgId)) {
				return this.getResponse(false, "所属组织机构不能为空");
			}
			
			// 创建参数map
			Map<String, Object> map = new HashMap<>();
			// 填充参数
			map.put("id", StringUtil.createUUID()); // 32位随机码作用户id
			map.put("account", account);
			map.put("name", name);
			map.put("password", password);
			map.put("mobile", mobile);
			map.put("email", email);
			map.put("region", region);
			map.put("address", address);
			map.put("type", type);
			map.put("org_id", orgId);
			map.put("creator", user.getString("ID")); // 创建人取当前登录人
			
			// 执行插入
			int row = userService.insertUser(map);
			if (row > 0) {
				// 插入成功
				return this.getResponse(true);
			} else {
				// 插入失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录用户，提示重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 用户禁用
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/disable" })
	@ResponseBody
	public String disable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 用户id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "用户ID不能为空");
		}
		
		// 执行禁用
		int row = userService.disableUser(id);
		if (row > 0) {
			// 禁用成功
			return this.getResponse(true);
		} else {
			// 禁用失败
			return this.getResponse(false, "禁用失败，请稍后再试");
		}
	}

	/**
	 * 用户启用
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/enable" })
	@ResponseBody
	public String enable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 用户id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "用户ID不能为空");
		}
		// 执行启用
		int row = userService.enableUser(id);
		if (row > 0) {
			// 启用成功
			return this.getResponse(true);
		} else {
			// 启用失败
			return this.getResponse(false, "启用失败，请稍后再试");
		}
	}

	/**
	 * 用户删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/del" })
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 用户id
		String id = this.getString("id");
		// 为空则提示
		if (ValidateUtil.isBlank(id)) {
			return this.getResponse(false, "用户ID不能为空");
		}
		
		// 执行删除
		int row = userService.deleteUser(id);
		if (row > 0) {
			// 删除成功
			return this.getResponse(true);
		} else {
			// 删除失败
			return this.getResponse(false, "删除失败，请稍后再试");
		}
	}

	/**
	 * 移动端-用户编辑保存
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/client/user/save" })
	@ResponseBody
	public String clientSave(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 手机号
			String mobile = this.getString("mobile");
			// 为空或不为11位则提示
			if (ValidateUtil.isNotBlank(mobile) && mobile.length() != 11) {
				return this.getResponse(false, "手机号码格式不正确");
			}
			// 所属区划
			String region = this.getString("region");
			// 详细地址
			String address = this.getString("address");
			// 为空则提示
			if (ValidateUtil.isBlank(region) || ValidateUtil.isBlank(address)) {
				return this.getResponse(false, "通讯地址不能为空");
			}
			// 创建参数map
			Map<String, Object> map = new HashMap<>();
			// 填充参数map
			map.put("id", user.getString("ID"));
			map.put("mobile", mobile);
			map.put("region", region);
			map.put("address", address);
			map.put("updator", user.getString("ID"));
			
			// 执行用户更新
			int row = userService.updateUserForClient(map);
			if (row > 0) {
				// 更新成功
				return this.getResponse(true);
			} else {
				// 更新失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录用户，提示重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}
	
	/**
	 * 重置密码后保存
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/client/user/savePassword" })
	@ResponseBody
	public String savePassword(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 取出用户id
			String id = user.getString("ID");
			// 旧密码
			String oldpw = this.getString("oldpw");
			// 为空则提示
			if (ValidateUtil.isBlank(oldpw)) {
				return this.getResponse(false, "旧密码不能为空");
			}
			// 新密码
			String newpw = this.getString("newpw");
			// 为空则提示
			if (ValidateUtil.isBlank(newpw)) {
				return this.getResponse(false, "新密码不能为空");
			}
			// 根据id和旧密码查询用户信息
			boolean flag = userService.checkUserPassword(id, oldpw);
			if (flag) {
				// 查到了，说明旧密码正确，更新密码
				int row = userService.updateUserPassword(id, newpw);
				if (row > 0) {
					// 更新成功
					return this.getResponse(true);
				} else {
					// 更新失败
					return this.getResponse(false, "密码重置失败，请稍后再试");	
				}
			} else {
				// 没查到，旧密码错误
				return this.getResponse(false, "旧密码不正确，请重新输入");
			}
		} else {
			// 获取不到登录用户，提示重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}
		
	/**
	 * 保存实名信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/client/user/saveAuth" })
	@ResponseBody
	public String saveAuth(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 姓名
			String name = this.getString("name");
			// 为空则提示
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "姓名不能为空");
			}
			// 证件类型
			String identity_type = this.getString("identity_type");
			// 为空则提示
			if (ValidateUtil.isBlank(identity_type)) {
				return this.getResponse(false, "证件类型不能为空");
			}
			// 证件号码
			String identity_num = this.getString("identity_num");
			// 为空则提示
			if (ValidateUtil.isBlank(identity_num)) {
				return this.getResponse(false, "证件号码不能为空");
			}
			// 图片1
			String image1 = this.getString("image1");
			// 为空则提示
			if (ValidateUtil.isBlank(image1)) {
				return this.getResponse(false, "证件正面照片不能为空");
			}
			// 图片2
			String image2 = this.getString("image2");
			// 为空则提示
			if (ValidateUtil.isBlank(image2)) {
				return this.getResponse(false, "证件反面照片不能为空");
			}
			
			// 创建参数map
			Map<String, Object> map = new HashMap<>();
			// 填充参数
			map.put("id", user.getString("ID")); // 登录用户id
			map.put("name", name);
			map.put("identity_type", identity_type);
			map.put("identity_num", identity_num);
			map.put("image1", image1);
			map.put("image2", image2);
			map.put("updator", user.getString("ID")); // 更新人填登录用户id
			
			// 更新用户表
			int row = userService.updateUserAuth(map);
			if (row > 0) {
				// 更新成功后，创建另一个参数map
				Map<String, Object> map1 = new HashMap<>();
				// 填充参数
				map1.put("id", StringUtil.createUUID());
				map1.put("type", "1");
				map1.put("data_id", user.getString("ID"));
				map1.put("submitter", user.getString("ID"));
				map1.put("submit_time", DatetimeUtil.getNow());
				// 添加到审批表
				row = auditService.saveAuditTask(map1);
				if (row > 0) {
					// 添加成功
					return this.getResponse(true);
				} else {
					// 添加审批信息失败
					return this.getResponse(false, "数据保存失败，请稍后再试");	
				}
			} else {
				// 更新用户失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录用户，提示重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 用户编辑保存
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/save" })
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 用户id
			String id = this.getString("id");
			// 为空则提示
			if (ValidateUtil.isBlank(id)) {
				return this.getResponse(false, "ID不能为空");
			}
			// 姓名
			String name = this.getString("name");
			// 为空则提示
			if (ValidateUtil.isBlank(name)) {
				return this.getResponse(false, "姓名不能为空");
			}
			// 手机号
			String mobile = this.getString("mobile");
			// 邮箱
			String email = this.getString("email");
			// 为空则提示
			if (ValidateUtil.isBlank(email)) {
				return this.getResponse(false, "邮箱不能为空");
			}
			// 所在区划
			String region = this.getString("region");
			// 详细信息
			String address = this.getString("address");
			// 所属组织机构
			String orgId = this.getString("org_id");
			
			// 创建参数map
			Map<String, Object> map = new HashMap<>();
			// 填充参数
			map.put("id", id);
			map.put("name", name);
			map.put("mobile", mobile);
			map.put("email", email);
			map.put("region", region);
			map.put("address", address);
			map.put("org_id", orgId);
			map.put("updator", user.getString("ID")); // 修改人填当前登录用户id
			
			// 执行更新
			int row = userService.updateUser(map);
			if (row > 0) {
				// 更新成功
				return this.getResponse(true);
			} else {
				// 更新失败
				return this.getResponse(false, "数据保存失败，请稍后再试");
			}
		} else {
			// 获取不到登录用户，提示重新登录
			return this.getResponse(false, "登录状态异常，请退出后重新登录");
		}
	}

	/**
	 * 查询单个用户信息
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/queryOne" })
	@ResponseBody
	public String queryOne(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 用户id
		String id = this.getString("id");
		if (ValidateUtil.isBlank(id)) {
			// 为空则提示
			return this.getResponse(false, "用户ID不能为空");
		} else {
			// 查询用户信息
			JSONObject object = userService.getUserInfo(id);
			// 返回用户信息
			return this.getResponse(object, true);
		}
	}

	/**
	 * 查询用户信息列表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/list" })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 页码
		Integer pageNum = this.getInteger("pageNum");
		// 每页条数
		Integer pageSize = this.getInteger("pageSize");
		// 账号
		String account = this.getString("account");
		// 姓名
		String name = this.getString("name");
		// 手机号
		String mobile = this.getString("mobile");
		// 邮箱
		String email = this.getString("email");
		// 类型
		String type = this.getString("type");
		
		// 创建一个参数map
		Map<String, Object> param = new HashMap<String, Object>();
		// 计算起始行号
		int start = (pageNum - 1) * pageSize;
		// 填充参数
		param.put("start", start < 0 ? 0 : start);
		param.put("size", pageSize);
		if (ValidateUtil.isNotBlank(account)) {
			param.put("account", account);
		}
		if (ValidateUtil.isNotBlank(name)) {
			param.put("name", name);
		}
		if (ValidateUtil.isNotBlank(mobile)) {
			param.put("mobile", mobile);
		}
		if (ValidateUtil.isNotBlank(email)) {
			param.put("email", email);
		}
		if (ValidateUtil.isNotBlank(type)) {
			param.put("type", type);
		}
		
		// 查询用户列表
		JSONArray data = userService.getUserInfoByPage(param);
		// 查询用户总数
		int total = userService.getUserInfoCount(param);
		// 返回数据
		return this.getPaginateResponse(data, pageNum, pageSize, total, true, "");
	}

}

package com.easyparking.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.easyparking.constant.StatusConst;
import com.easyparking.controller.BaseController;
import com.easyparking.service.LogService;
import com.easyparking.service.UserService;
import com.easyparking.util.data.StringUtil;
import com.easyparking.util.data.ValidateUtil;
import com.easyparking.util.image.VerifyCodeUtil;

/**
 * 登录、注册、首页控制器
 */
@Controller
public class IndexController extends BaseController {

	/**
	 * 读取配置文件里的session名
	 */
	@Value("${app.sessionKey:}")
	private String sessionKey;

	/**
	 * 读取配置文件里的图形验证码session名
	 */
	@Value("${app.captchaSessionKey:}")
	private String captchaSessionKey;

	/**
	 * 注入用户管理服务
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 注入登录日志管理服务
	 */
	@Autowired
	private LogService loginLogService;

	/**
	 * 首页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/index", "/", "" })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 如果登录用户不为空，将用户id、姓名、类型放到返回参数里
			map.put("id", user.getString("ID"));
			map.put("username", user.getString("NAME"));
			map.put("type", user.getString("TYPE"));
			
			// 根据类型编码，将类型名称放到返回参数里，管理端页面右上角会显示
			if ("1".equals(user.getString("ID"))) {
				map.put("typename", "超级管理员");
			} else {
				switch (user.getString("TYPE")) {
				case "1":
					map.put("typename", "社会用户");
					break;
				case "2":
					map.put("typename", "平台管理");
					break;
				case "3":
					map.put("typename", "物业管理");
					break;
				}
			}
			
			if ("1".equals(user.get("TYPE"))) {
				// 如果类型是1，返回移动端首页的视图
				return this.getView("client/index", map);
			} else {
				// 否则返回管理端首页的视图
				return this.getView("manage/index", map);
			}
		} else {
			// 如果当前登录用户为空，调用登录页面方法
			return this.login(request, response, map);
		}
	}

	/**
	 * 登录页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/login" })
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 返回登录页面视图
		return this.getView("login", map);
	}

	/**
	 * 管理端重置密码页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/reset" })
	public String reset(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 返回页面视图
		return this.getView("manage/user/reset", map);
	}

	/**
	 * 注销（是个按钮，没有页面）
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/logout" })
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 把session清掉
		request.getSession().invalidate();
		
		// 返回登录页面视图
		return login(request, response, map);
	}

	/**
	 * 注册页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/reg" })
	public String reg(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 返回注册页面的视图
		return this.getView("reg", map);
	}
	
	/**
	 * 重置密码操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/resetPassword" })
	@ResponseBody
	public String resetPassword(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 旧密码
		String oldpw = this.getString("oldpw");
		if (ValidateUtil.isBlank(oldpw)) {
			return this.getResponse(false, "原密码不能为空");
		}
		// 新密码
		String newpw = this.getString("newpw");
		if (ValidateUtil.isBlank(newpw)) {
			return this.getResponse(false, "新密码不能为空");
		}
		// 获取当前登录用户
		JSONObject user = this.getCurrentUser();
		if (user != null) {
			// 根据id和旧密码查密码是否正确
			String id = user.getString("ID");
			if (userService.checkUserPassword(id, oldpw)) {
				// 如果正确就更新密码
				int row = userService.updateUserPassword(id, newpw);
				if (row > 0) {
					// 更新成功
					return this.getResponse(true);
				} else {
					// 更新失败，返回提示信息
					return this.getResponse(false, "密码修改失败，请稍后再试");
				}
			} else {
				// 不正确则返回提示信息
				return this.getResponse(false, "原密码不正确，请重新输入");
			}
		} else {
			// 用户登录状态过期提示
			return this.getResponse(false, "登录状态异常，请重新登录");
		}
	}
	
	/**
	 * 用户注册操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/regSubmit" })
	@ResponseBody
	public String regSubmit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 账号
		String account = this.getString("account");
		// 若为空则提示
		if (ValidateUtil.isBlank(account)) {
			return this.getResponse(false, "账号不能为空");
		}
		// 查询账号是否存在
		JSONObject object = userService.getUserInfo(account);
		// 若存在则提示重复
		if (object != null) {
			return this.getResponse(false, "账号已存在");
		}
		// 姓名
		String name = this.getString("name");
		// 若为空则提示
		if (ValidateUtil.isBlank(name)) {
			return this.getResponse(false, "姓名不能为空");
		}
		// 密码
		String password = this.getString("password");
		// 若为空则提示
		if (ValidateUtil.isBlank(password)) {
			return this.getResponse(false, "密码不能为空");
		}
		// 手机号
		String mobile = this.getString("mobile");
		// 邮箱
		String email = this.getString("email");
		// 若为空则提示
		if (ValidateUtil.isBlank(email)) {
			return this.getResponse(false, "邮箱不能为空");
		}
		// 所属区划
		String region = this.getString("region");
		// 若为空则提示
		if (ValidateUtil.isBlank(region)) {
			return this.getResponse(false, "所属区划不能为空");
		}
		// 详细地址
		String address = this.getString("address");
		// 若为空则提示
		if (ValidateUtil.isBlank(address)) {
			return this.getResponse(false, "详细地址不能为空");
		}
		
		// 校验通过，建一个参数map
		Map<String, Object> map = new HashMap<>();
		// 生成一个用户id
		String id = StringUtil.createUUID();
		// 把所有用户字段填充到map
		map.put("id", id);
		map.put("account", account);
		map.put("name", name);
		map.put("password", password);
		map.put("mobile", mobile);
		map.put("email", email);
		map.put("region", region);
		map.put("address", address);
		map.put("type", "1");
		map.put("creator", id);
		
		// 插入用户
		int row = userService.insertUser(map);
		if (row > 0) {
			// 插入成功返回提示
			return this.getResponse(true);
		} else {
			// 插入失败，提示注册失败
			return this.getResponse(false, "注册失败，请稍后再试");
		}
	}

	/**
	 * 获取登录验证码图片
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping(value = { "/captcha" })
	public void captcha(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 生成一个4位验证码
		String code = VerifyCodeUtil.getCode(4);
		// 放到session里
		request.getSession().setAttribute(captchaSessionKey, code);
		// 制作验证码图片并通过输出流返回到页面
		try {
			VerifyCodeUtil.getImageStream(100, 39, response.getOutputStream(), code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 登录流程
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/loginHandler" })
	@ResponseBody
	public String loginHandler(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// 获取用户名
		String userName = this.getString("u");
		// 获取密码
		String password = this.getString("p");
		// 获取验证码
		String captcha = this.getString("captcha");
		// 获取session里正确的验证码
		String rightCaptcha = (String) request.getSession().getAttribute(captchaSessionKey);
		// 作废session中的验证码
		request.getSession().removeAttribute(captchaSessionKey);

		// 校验字段是否为空
		if (ValidateUtil.isBlank(userName)) {
			return this.loginFailed("用户名不能为空，请检查");
		}
		if (ValidateUtil.isBlank(password)) {
			return this.loginFailed("密码不能为空，请检查");
		}
		if (ValidateUtil.isBlank(captcha)) {
			return this.loginFailed("验证码不能为空，请检查");
		}
		if (ValidateUtil.isBlank(rightCaptcha)) {
			return this.loginFailed("验证码已过期，请刷新页面再试");
		}
		// 校验验证码是否正确
		if (!rightCaptcha.toLowerCase().equals(captcha.toLowerCase())) {
			return this.loginFailed("验证码错误，请重新输入");
		}
		
		// 根据账号、密码去数据库查用户
		JSONObject user = userService.getUserInfoByLogin(userName, password);
		
		if (user == null) {
			// 没查到用户，返回用户名或密码错误
			return this.loginFailed("用户名或密码错误，请重新输入");
		} else {
			// 查到用户了
			if ("1".equals(user.getString("DISABLED"))) {
				// 用户状态为禁用，返回用户已禁用的提示
				return this.loginFailed("用户账号已被禁用，请联系管理员");
			} else {
				// 用户状态正常，把用户信息保存到session
				request.getSession().setAttribute(sessionKey, user.toJSONString());
			}
		}
		
		// 返回登录成功信息
		return this.loginSuccess();
	}
	
	/**
	 * 记录日志并返回登录失败信息
	 * @param message
	 * @return
	 */
	private String loginFailed(String message) {
		Map<String, Object> map = new HashMap<>();
		String userName = this.getString("u");
		// 拼装登录日志参数
		map.put("id", StringUtil.createUUID());
		map.put("login_name", userName);
		map.put("ip", this.getIPAddress());
		map.put("port", this.getRequestPort());
		map.put("device", this.getUserDevice());
		map.put("os", this.getUserOperatingSystem());
		map.put("ua", this.getUserBrowser());
		map.put("status", StatusConst.FAILURE);
		map.put("note", message);
		// 保存登录日志
		loginLogService.insertLoginLog(map);
		return this.getResponse(false, message);
	}
	
	/**
	 * 记录日志并返回登录成功
	 * @return
	 */
	private String loginSuccess() {
		Map<String, Object> map = new HashMap<>();
		String userName = this.getString("u");
		// 拼装登录日志参数
		map.put("id", StringUtil.createUUID());
		map.put("login_name", userName);
		map.put("ip", this.getIPAddress());
		map.put("port", this.getRequestPort());
		map.put("device", this.getUserDevice());
		map.put("os", this.getUserOperatingSystem());
		map.put("ua", this.getUserBrowser());
		map.put("status", StatusConst.SUCCESS);
		// 保存登录日志
		loginLogService.insertLoginLog(map);
		return this.getResponse(true);
	}
	
}

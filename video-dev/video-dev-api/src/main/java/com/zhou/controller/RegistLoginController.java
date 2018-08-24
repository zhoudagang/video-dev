package com.zhou.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.pojo.Users;
import com.zhou.service.IUserService;
import com.zhou.utils.MD5Utils;
import com.zhou.utils.ZhouJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class RegistLoginController {

	@Autowired
	private IUserService userService;

	@ApiOperation(value="用户注册", notes="用户注册的接口")
	@RequestMapping("/regist")
	public ZhouJSONResult regist(@RequestBody Users users) throws Exception {
		// 判断用户名或密码是否为空
		if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
			return ZhouJSONResult.errorMsg("用户名和密码为空!");
		}
		// 用户名是否存在
		if (!userService.queryUsernameIsExist(users.getUsername())) {
			users.setNickname(users.getUsername());
			users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
			users.setFansCounts(0);
			users.setReceiveLikeCounts(0);
			users.setFollowCounts(0);
			userService.addUser(users);
		}else {
			return ZhouJSONResult.errorMsg("换个名字试一下子!");
		}
		return ZhouJSONResult.ok();
	}
	
	@ApiOperation(value="用户登陆", notes="用户登陆的接口")
	@RequestMapping("/login")
	public ZhouJSONResult login(@RequestBody Users users) throws Exception {
		String username = users.getUsername();
		String password = users.getPassword();
		// 判断用户名或密码是否为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return ZhouJSONResult.errorMsg("用户名和密码为空!");
		}
		// 用户名是否存在
		Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
		if (null == userResult) {
			return ZhouJSONResult.errorMsg("用户名或密码不正确！");
		}else {
			return ZhouJSONResult.ok(userResult);
		}
	}
	@RequestMapping("/")
	public String login() throws Exception {
			return "<h1>巴啦啦小魔仙全身变！</h1>";
	}
	
	
	

}

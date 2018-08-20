package com.zhou.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.pojo.Users;
import com.zhou.utils.ZhouJSONResult;

@RestController
public class RegistLoginController {

	@RequestMapping("/regist")
	public ZhouJSONResult regist(@RequestBody Users users) {
		
		
		
		return ZhouJSONResult.ok();
	}
	
	
}

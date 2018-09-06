package com.zhou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.service.IBgmService;
import com.zhou.utils.ZhouJSONResult;

@RestController
@RequestMapping("/bgm")
public class BgmController {

	@Autowired
	private IBgmService iBgmService;

	@RequestMapping("/qureryBgmList")
	public ZhouJSONResult qureryBmgList() {
		return ZhouJSONResult.ok(iBgmService.queryBgmList());
	}

}

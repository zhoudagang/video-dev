package com.zhou.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zhou.Application;
import com.zhou.pojo.Users;
import com.zhou.service.IUserService;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestRegist {
	@Autowired
	private IUserService userService;
	@Test
	public void TestRegist() {
		Users user = new Users();
		user.setId("aaa");
		user.setUsername("a");
		user.setPassword("aaa");
		user.setNickname("aaa");
		userService.addUser(user);
	}
	
}

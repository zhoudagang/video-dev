package com.zhou.service;

import com.zhou.pojo.Users;

/**
 * @author zhougang
 *
 */
public interface IUserService {

	
	
	/**
	 * 判断用户名是否存在
	 * @param userNme
	 * @return
	 */
	boolean queryUsernameIsExist(String userName);
	
	
	/**添加用户
	 * @param user
	 */
	void addUser(Users user);
	
	/**
	 * 判断用户是否存在
	 * @param user
	 * @return
	 */
	Users queryUserForLogin(String username,String password);
	
	
}

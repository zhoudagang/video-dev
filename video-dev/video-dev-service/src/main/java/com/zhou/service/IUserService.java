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
	
	/**
	 * 更改用户信息
	 * @param user
	 * @return
	 */
	void updateUserInfo(Users user);
	
	/**
	 * 根据用户id查询用户信息
	 * @param user
	 * @return
	 */
	Users queryUserById(String userid);
}

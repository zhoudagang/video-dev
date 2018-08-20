package com.zhou.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhou.mapper.UsersMapper;
import com.zhou.pojo.Users;
import com.zhou.service.IUserService;

public class UserServiceImpl implements IUserService {

	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private Sid sid;

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String userName) {
		Users user = new Users();
		user.setUsername(userName);
		Users result = usersMapper.selectOne(user);
		return result == null ? false : true;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addUser(Users user) {
		user.setId(sid.nextShort());
		usersMapper.insert(user);
	}

}

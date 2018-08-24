package com.zhou.service.impl;

import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhou.mapper.UsersMapper;
import com.zhou.pojo.Users;
import com.zhou.service.IUserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
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
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String password) {
		Example userExample = new Example(Users.class);
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andEqualTo("username",username);
		createCriteria.andEqualTo("password",password);
		Users user = usersMapper.selectOneByExample(userExample);
		return user;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateUserInfo(Users user) {
		Example userExample = new Example(Users.class);
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andEqualTo("id",user.getId());
		usersMapper.updateByExampleSelective(user, userExample);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Users queryUserById(String userid) {
		Example userExample = new Example(Users.class);
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andEqualTo("id",userid);
		return usersMapper.selectOneByExample(userExample);
	}

}

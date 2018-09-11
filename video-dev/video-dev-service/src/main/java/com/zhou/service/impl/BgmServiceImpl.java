package com.zhou.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhou.mapper.BgmMapper;
import com.zhou.pojo.Bgm;
import com.zhou.service.IBgmService;

@Service
public class BgmServiceImpl implements IBgmService {

	@Autowired
	private BgmMapper bgmMapper;

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public List<Bgm> queryBgmList() {
		List<Bgm> selectAll = bgmMapper.selectAll();
		return selectAll;
	}

	@Override
	public Bgm queryBgmById(String bgmId) {
		return bgmMapper.selectByPrimaryKey(bgmId);
	}

}

package com.zhou.mapper;

import java.util.List;


import com.zhou.pojo.Videos;
import com.zhou.pojo.vo.VideosVO;
import com.zhou.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	/**
	 * @Description: 条件查询所有视频列表
	 */
	public List<VideosVO> queryAllVideos();

}
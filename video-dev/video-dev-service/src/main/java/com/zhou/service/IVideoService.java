package com.zhou.service;


import com.zhou.pojo.Videos;
import com.zhou.utils.PagedResult;

/**
 * @author zhougang
 *
 */
public interface IVideoService {

	/**
	 * @Description: 保存视频
	 */
	public String saveVideo(Videos video);
	
	/**
	 * @Description: 修改视频的封面
	 */
	public void updateVideo(String videoId, String coverPath);
	
	/**
	 * @Description: 分页查询视频列表
	 */
	public PagedResult getAllVideos(Integer page, Integer pageSize);
}

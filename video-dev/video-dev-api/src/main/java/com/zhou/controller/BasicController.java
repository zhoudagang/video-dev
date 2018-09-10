package com.zhou.controller;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class BasicController {

	
	// 文件保存的命名空间
	public static final String FILE_SPACE = "C:/zhou_videos_dev";
	
	// ffmpeg所在目录
	public static final String FFMPEG_EXE = "C:\\ffmpeg\\bin\\ffmpeg.exe";
	
	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 5;
	
}

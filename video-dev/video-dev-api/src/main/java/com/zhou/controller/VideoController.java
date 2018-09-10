package com.zhou.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhou.enums.VideoStatusEnum;
import com.zhou.pojo.Bgm;
import com.zhou.pojo.Videos;
import com.zhou.service.IBgmService;
import com.zhou.service.IVideoService;
import com.zhou.utils.FetchVideoCover;
import com.zhou.utils.MergeVideoMp3;
import com.zhou.utils.PagedResult;
import com.zhou.utils.ZhouJSONResult;

@RestController
@RequestMapping("/video")
public class VideoController extends BasicController {

	@Autowired
	private IBgmService bgmService;

	@Autowired
	private IVideoService videoService;

	@RequestMapping(value = "/upload", headers = "content-type=multipart/form-data")
	public ZhouJSONResult upload(String userId, String bgmId, double videoSeconds, int videoWidth, int videoHeight,
			String desc, MultipartFile file) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return ZhouJSONResult.errorMsg("用户id不能为空！");
		}
		// 保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";
		String coverPathDB = "/" + userId + "/video";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		// 文件上传的最终保存路径
		String finalVideoPath = "";
		try {
			if (file != null) {
				String fileName = file.getOriginalFilename();
				// abc.mp4
				String arrayFilenameItem[] = fileName.split("\\.");
				String fileNamePrefix = "";
				for (int i = 0; i < arrayFilenameItem.length - 1; i++) {
					fileNamePrefix += arrayFilenameItem[i];
				}
				// fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
				// String fileNamePrefix = fileName.split("\\.")[0];

				if (StringUtils.isNotBlank(fileName)) {

					finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";

					File outFile = new File(finalVideoPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);

				} else {
					return ZhouJSONResult.errorMsg("获取视频失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		// 判断bgmId是否为空，如果不为空，
		// 那就查询bgm的信息，并且合并视频，生产新的视频
		if (StringUtils.isNotBlank(bgmId)) {
			Bgm bgm = bgmService.queryBgmById(bgmId);
			String mp3InputPath = FILE_SPACE + bgm.getPath();

			MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
			String videoInputPath = finalVideoPath;

			String videoOutputName = UUID.randomUUID().toString() + ".mp4";
			uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
			finalVideoPath = FILE_SPACE + uploadPathDB;
			tool.convertor(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
		}
		System.out.println("uploadPathDB=" + uploadPathDB);
		System.out.println("finalVideoPath=" + finalVideoPath);
		// 对视频进行截图
		FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
		videoInfo.getCover(finalVideoPath, FILE_SPACE + coverPathDB);
		// 保存视频信息到数据库
		Videos video = new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float) videoSeconds);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoPath(uploadPathDB);
		video.setCoverPath(coverPathDB);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		video.setCreateTime(new Date());

		String videoId = videoService.saveVideo(video);

		return ZhouJSONResult.ok(videoId);
	}

	@PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
	public ZhouJSONResult uploadCover(String userId, String videoId, MultipartFile file) throws Exception {

		if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
			return ZhouJSONResult.errorMsg("视频主键id和用户id不能为空...");
		}
		// 保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		// 文件上传的最终保存路径
		String finalCoverPath = "";
		try {
			if (file != null) {

				String fileName = file.getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {

					finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finalCoverPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}

			} else {
				return ZhouJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ZhouJSONResult.errorMsg("上传出错...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		videoService.updateVideo(videoId, uploadPathDB);
		return ZhouJSONResult.ok();
	}

	/**
	 * 
	 * @Description: 分页和搜索查询视频列表 isSaveRecord：1 - 需要保存 0 - 不需要保存 ，或者为空的时候
	 */
	@PostMapping(value = "/showAll")
	public ZhouJSONResult showAll(Integer page, Integer pageSize) throws Exception {
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		PagedResult result = videoService.getAllVideos(page, pageSize);
		return ZhouJSONResult.ok(result);
	}
}

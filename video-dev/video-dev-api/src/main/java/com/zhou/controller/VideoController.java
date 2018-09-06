package com.zhou.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhou.utils.ZhouJSONResult;

@RestController
@RequestMapping("/video")
public class VideoController {

	@RequestMapping(value = "/upload", headers = "content-type=multipart/form-data")
	public ZhouJSONResult upload(String userId, String bgmId, double videoSeconds, String videoWidth,
			String videoHeight, String desc,  MultipartFile file) throws IOException {

		if (StringUtils.isBlank(userId)) {
			return ZhouJSONResult.errorMsg("用户id不能为空！");
		}
		// 文件保存的位置
		String fileSpace = "C://video_dev_face";
		// 保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (file != null) {
				String fileName = file.getOriginalFilename();
				if (null != fileName && fileName.length() > 0) {
					// 最终的上传路径
					String finaVideoPath = fileSpace + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finaVideoPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父目录
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}

			} else {
				return ZhouJSONResult.errorMsg("获取视频失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		return ZhouJSONResult.ok();
	}

}

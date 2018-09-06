package com.zhou.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhou.pojo.Users;
import com.zhou.pojo.WxUsers;
import com.zhou.service.IUserService;
import com.zhou.utils.HttpClientUtil;
import com.zhou.utils.WxUtils;
import com.zhou.utils.ZhouJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.iharder.Base64;

@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@ApiOperation(value="用户注册", notes="用户注册的接口")
	@RequestMapping("/uploadFace")
	@ApiImplicitParam(name="username",value="用户名",required=true,dataType="String",paramType="query")
	public ZhouJSONResult uploadFace(String userid,@RequestParam("file") MultipartFile [] files) throws Exception {
		
		if (StringUtils.isBlank(userid)) {
			return ZhouJSONResult.errorMsg("用户id不能为空！");
		}
		// 文件保存的位置
		String fileSpace = "C://video_dev_face";
		// 保存到数据库中的相对路径
		String uploadPathDB = "/" + userid + "/face";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (files != null && files.length > 0) {

				String fileName = files[0].getOriginalFilename();
				if (null != fileName && fileName.length() > 0) {
					// 最终的上传路径
					String finaFacePath = fileSpace + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finaFacePath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父目录
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}

			}else {
				return ZhouJSONResult.errorMsg("获取头像失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		Users user = new Users();
		user.setId(userid);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);

		return ZhouJSONResult.ok(uploadPathDB);
	}
	

	@ApiOperation(value="根据用户ID查询用户信息", notes="根据用户ID查询用户信息的接口")
	@RequestMapping("/queryUserInfoById")
	@ApiImplicitParam(name="username",value="用户名",required=true,dataType="String",paramType="query")
	public ZhouJSONResult queryUserInfoById(String userid) throws Exception {
		Users user = userService.queryUserById(userid);
		return ZhouJSONResult.ok(user);
	}
	
	//----------------------------------------------------------------
	
	@ApiOperation(value="新增用户信息", notes="新增用户信息的接口")
	@RequestMapping("/insertWxUser")
	@ApiImplicitParam(name="user",value="WxUsers",required=true,dataType="String",paramType="query")
	public ZhouJSONResult insertWxUser(WxUsers user) throws Exception {
		try {
			WxUsers wxuser = userService.queryUserByOpendId(user.getOpenid());
			if (wxuser == null) {
				userService.insertWxUser(user);
				return ZhouJSONResult.ok(user);
			}
			return ZhouJSONResult.errorMsg("用户已经存在");
		} catch (Exception e) {
			e.printStackTrace();
			return ZhouJSONResult.errorMsg("新增用户失败");
		}
	}
	
	@RequestMapping("/queryUserInfoByOpenId")
	public ZhouJSONResult queryUserInfoByOpenId(String openid) throws Exception {
		WxUsers user = userService.queryUserByOpendId(openid);
		return ZhouJSONResult.ok(user);
	}
	
	@RequestMapping("/register")
	public ZhouJSONResult register(String code, String encryptedData, String iv) throws Exception {
		System.err.println("code"+code+"====encryptedData"+encryptedData+"====iv"+iv);
		
		JSONObject SessionKeyOpenId = WxUtils.getSessionKeyOrOpenId( code );
	    System.out.println("post请求获取的SessionAndopenId="+SessionKeyOpenId);
	    String openid = SessionKeyOpenId.getString("openid" );
	    String sessionKey = SessionKeyOpenId.getString( "session_key" );
	    System.out.println("openid="+openid+",session_key="+sessionKey);

	    JSONObject userInfo = WxUtils.getUserInfo( encryptedData, sessionKey, iv );
	    System.out.println("根据解密算法获取的userInfo="+userInfo);
		return ZhouJSONResult.ok();
	}

	

}

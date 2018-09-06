package com.zhou.utils;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.iharder.Base64;

public class WxUtils {
	public static JSONObject getSessionKeyOrOpenId(String code) {
		// 微信端登录code
		String wxCode = code;
		String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
		StringBuffer sb = new StringBuffer();
		sb.append("appid=wxd994f9ee4728adc2");
		sb.append("&secret=9b07175849b1a8689556e1d2b5f8e94b");
		sb.append("&js_code=" + wxCode);
		sb.append("&grant_type=authorization_code");

		// 发送post请求读取调用微信接口获取openid用户唯一标识
		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(HttpClientUtil.sendPost(requestUrl, sb.toString()));
		return jsonObject;
	}

	public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) throws IOException {
		// 被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.decode(sessionKey);
		// 偏移量
		byte[] ivByte = Base64.decode(iv);
		try {
			// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			// 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				return JSON.parseObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

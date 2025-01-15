package com.gl.myparadise.common.tools.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.KeyRepresentation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class GoogleAuthUtils {

	/**
	 * 生成认证
	 * 
	 * @return
	 */
	public static GoogleAuthenticatorKey createCredentials() {
		GoogleAuthenticatorConfigBuilder gacb = new GoogleAuthenticatorConfigBuilder()
				.setKeyRepresentation(KeyRepresentation.BASE32);
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(gacb.build());
		GoogleAuthenticatorKey credentials = googleAuthenticator.createCredentials();
		return credentials;
	}

	/**
	 * 获取谷歌二维码地址
	 * 
	 * @param platformName
	 * @param username
	 * @param credentials
	 * @return
	 */
	public static String getOtpAuthURL(String platformName, String username, GoogleAuthenticatorKey credentials) {
		String TOTP_URI_FORMAT = "https://api.qrserver.com/v1/create-qr-code/?data=%s&size=200x200";
		String result = "";
		try {
			result = String.format(
					TOTP_URI_FORMAT,
					URLEncoder.encode((
							GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(platformName, username, credentials)),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding is not supported by URLEncoder.", e);
		}
		return result;
	}

	/**
	 * 验证方法
	 * 
	 * @param googleKey
	 * @param inputKey
	 * @return
	 */
	public static boolean verify(String googleKey, int inputKey) {
		GoogleAuthenticatorConfigBuilder gacb = new GoogleAuthenticatorConfigBuilder()
				.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30)).setWindowSize(5).setCodeDigits(6);
		GoogleAuthenticator ga = new GoogleAuthenticator(gacb.build());
		return ga.authorize(googleKey, inputKey);
	}

}
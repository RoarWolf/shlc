package com.hedong.hedongwx.entity.wx;

public class AccessToken {

	public String token;//token
	public int expiresIn;//截止时间
	public long expirescutoff;//获取token
	public String refreshToken;//刷新的token
	
	public static String openToken;//开放平台token
	public static String openExpiresIn;//开放平台截止时间
	public static String openExpiresOut;//开放平台截止时间
	public static String openRefreshToken;//开放平台刷新的token
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public long getExpirescutoff() {
		return expirescutoff;
	}
	public void setExpirescutoff(long expirescutoff) {
		this.expirescutoff = expirescutoff;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public static String getOpenToken() {
		return openToken;
	}
	public static void setOpenToken(String openToken) {
		AccessToken.openToken = openToken;
	}
	public static String getOpenExpiresIn() {
		return openExpiresIn;
	}
	public static void setOpenExpiresIn(String openExpiresIn) {
		AccessToken.openExpiresIn = openExpiresIn;
	}
	public static String getOpenExpiresOut() {
		return openExpiresOut;
	}
	public static void setOpenExpiresOut(String openExpiresOut) {
		AccessToken.openExpiresOut = openExpiresOut;
	}
	public static String getOpenRefreshToken() {
		return openRefreshToken;
	}
	public static void setOpenRefreshToken(String openRefreshToken) {
		AccessToken.openRefreshToken = openRefreshToken;
	}
	
	
	
}

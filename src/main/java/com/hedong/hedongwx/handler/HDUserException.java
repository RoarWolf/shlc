package com.hedong.hedongwx.handler;

public class HDUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errMsg;
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public HDUserException(String errMsg) {
		this.errMsg = errMsg;
	}
}

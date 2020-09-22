package com.hedong.hedongwx.entity;

public class ParseMsg {

	private String param1;
	private String param2;

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	@Override
	public String toString() {
		return "总共空闲端口为：" + param1 + ", 起始端口为：" + param2;
	}

}

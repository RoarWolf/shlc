package com.hedong.hedongwx.entity;

public class ServerReceiveInfo {

	private Integer id;
	private String ipaddr;
	private String message;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

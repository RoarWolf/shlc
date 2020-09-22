package com.hedong.hedongwx.entity.wx.model;

public class ImageMessage extends BaseMessage {

	private String PicUrl;
	private Image image;
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	
}

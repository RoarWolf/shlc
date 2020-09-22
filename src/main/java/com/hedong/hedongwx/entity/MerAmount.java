package com.hedong.hedongwx.entity;

/**
 * 商户收益总计表
 * @author Administrator
 *
 */
public class MerAmount {

	/** 表自增id*/
	private Integer id;
	/** 商户id*/
	private Integer merid;
	/** 总在线收益*/
    private Double totalOnlineEarn;
    /** 今日在线收益*/
    private Double nowOnlineEarn;
    /** 总投币收益*/
    private Integer totalCoinsEarn;
    /** 今日投币收益*/
    private Integer nowCoinsEarn;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMerid() {
		return merid;
	}
	public void setMerid(Integer merid) {
		this.merid = merid;
	}
	public Double getTotalOnlineEarn() {
		return totalOnlineEarn;
	}
	public void setTotalOnlineEarn(Double totalOnlineEarn) {
		this.totalOnlineEarn = totalOnlineEarn;
	}
	public Double getNowOnlineEarn() {
		return nowOnlineEarn;
	}
	public void setNowOnlineEarn(Double nowOnlineEarn) {
		this.nowOnlineEarn = nowOnlineEarn;
	}
	public Integer getTotalCoinsEarn() {
		return totalCoinsEarn;
	}
	public void setTotalCoinsEarn(Integer totalCoinsEarn) {
		this.totalCoinsEarn = totalCoinsEarn;
	}
	public Integer getNowCoinsEarn() {
		return nowCoinsEarn;
	}
	public void setNowCoinsEarn(Integer nowCoinsEarn) {
		this.nowCoinsEarn = nowCoinsEarn;
	}
    
}

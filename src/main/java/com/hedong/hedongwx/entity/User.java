package com.hedong.hedongwx.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5153353426578731643L;
	/** 用户id */
	private Integer id;
	/** 用户名 */
	private String username;
	/** 用户名 */
	private String imageUrl;
	/** 用户真实姓名 */
	private String realname;
	/** 用户微信唯一标识 */
	private String openid;
	/** 用户微信关联唯一标识 */
	private String unionid;
	/** 用户微信小程序唯一标识 */
	private String openidApplet;
	/** 用户密码 */
	private String password;
	/** 用户手机号码 */
	private String phoneNum;
	/** 服务手机号 */
	private String servephone;
	/** 用户银行卡表id*/
	private String bankcardId;
	/** 用户等级 */
	private Integer level; //0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理、5：微信子商户 、6：子账号
	/** 用户收益 */
	private Double earnings;
	
	private Integer  feerate;//费率
	/** 用户钱包余额 */
	private Double balance;
	/** 用户钱包余额 */
	private Double sendmoney;
	/** 用户注册时间 */
	private Date createTime;
	/** 用户修改时间 */
	private Date updateTime;
	/** 用户所属商户 */
	private Integer merid;
	/** 用户所属小区 */
	private Integer aid;
	/** 支付提示信息 */
	private Integer payhint;
	
	/** 商家应该缴费*/
	private Double payMonet;
	
	/** 代理商的id*/
	private Integer agentId;
	//----------------------
	/** 微信子商户的appid*/
	private String subAppId;
	/** 子商户的商户号*/
	private String subMchId;
	/** 子商户app应用秘钥*/
	private String appSecret;
	/** 秘钥*/
	private String keyWord;
	/** 特约商户*/
	private String teYue;
	/** 默认为0,1:公众号，2:小程序*/
	private Integer type;
	/** 小程序appid*/
	private String smallAppId;
	/** 小程序的秘钥*/
	private String smallAppSecret;
	/** 标记用户是否为微信特约商户*/
	private Integer subMer;

	public Integer getSubMer() {
		return subMer;
	}

	public void setSubMer(Integer subMer) {
		this.subMer = subMer;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getTeYue() {
		return teYue;
	}

	public void setTeYue(String teYue) {
		this.teYue = teYue;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSmallAppId() {
		return smallAppId;
	}

	public void setSmallAppId(String smallAppId) {
		this.smallAppId = smallAppId;
	}

	public String getSmallAppSecret() {
		return smallAppSecret;
	}

	public void setSmallAppSecret(String smallAppSecret) {
		this.smallAppSecret = smallAppSecret;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Double getPayMonet() {
		return payMonet;
	}

	public void setPayMonet(Double payMonet) {
		this.payMonet = payMonet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenidApplet() {
		return openidApplet;
	}

	public void setOpenidApplet(String openidApplet) {
		this.openidApplet = openidApplet;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getServephone() {
		return servephone;
	}

	public void setServephone(String servephone) {
		this.servephone = servephone;
	}

	public String getBankcardId() {
		return bankcardId;
	}

	public void setBankcardId(String bankcardId) {
		this.bankcardId = bankcardId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Double getEarnings() {
		return earnings;
	}

	public void setEarnings(Double earnings) {
		this.earnings = earnings;
	}

	public Integer getFeerate() {
		return feerate;
	}

	public void setFeerate(Integer feerate) {
		this.feerate = feerate;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getSendmoney() {
		return sendmoney;
	}

	public void setSendmoney(Double sendmoney) {
		this.sendmoney = sendmoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getMerid() {
		return merid;
	}

	public void setMerid(Integer merid) {
		this.merid = merid;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getPayhint() {
		return payhint;
	}

	public void setPayhint(Integer payhint) {
		this.payhint = payhint;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", realname=" + realname + ", openid=" + openid
				+ ", unionid=" + unionid + ", openidApplet=" + openidApplet + ", password=" + password + ", phoneNum="
				+ phoneNum + ", servephone=" + servephone + ", bankcardId=" + bankcardId + ", level=" + level
				+ ", earnings=" + earnings + ", feerate=" + feerate + ", balance=" + balance + ", sendmoney="
				+ sendmoney + ", createTime=" + createTime + ", updateTime=" + updateTime + ", merid=" + merid
				+ ", aid=" + aid + ", payhint=" + payhint + ", payMonet=" + payMonet + ", agentId=" + agentId
				+ ", subAppId=" + subAppId + ", subMchId=" + subMchId + ", appSecret=" + appSecret + ", keyWord="
				+ keyWord + ", teYue=" + teYue + ", type=" + type + ", smallAppId=" + smallAppId + ", smallAppSecret="
				+ smallAppSecret + ", subMer=" + subMer + "]";
	}
	
	
}

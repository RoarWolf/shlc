package com.hedong.hedongwx.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
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
	
	/** 是否是桩主*/
	private boolean ismeruser;

}

package com.hedong.hedongwx.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ChargingTemplate implements Serializable {
	/**
	 *主键id
	 **/
	private Integer id;
	/**
	 *创建时间
	 **/
	private Date createTime;
	/**
	 *更新时间
	 **/
	private Date updateTime;
	/**
	 *父类id
	 **/
	private Integer parentId;
	/**
	 *计费版本
	 **/
	private String billver;
	/**
	 *停车费
	 **/
	private BigDecimal parkingfee;
	/**
	 *计费时间段总数
	 **/
	private Integer timenum;
	/**
	 *充电费用
	 **/
	private BigDecimal chargefee;
	/**
	 *小时
	 **/
	private Integer hour;
	/**
	 *服务费
	 **/
	private BigDecimal serverfee;
	/**
	 *类型1-尖,2-峰,3-平,4-谷
	 **/
	private String type;
	/**
	 *分钟
	 **/
	private Integer minute;

	/**
	 *电流类型1直流,2交流
	 **/
	private String currentType;

	/**
	 *是否是默认版本1是2否
	 **/
	private String isDefault;
	/**
	 *子模板信息
	 **/
	private List<ChargingTemplate> childTemplate;
	
	
	

}

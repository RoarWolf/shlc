package com.hedong.hedongwx.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Meruser {
	
	//表自增id
    private Integer id;

    //用户id
    private Integer uid;

    //手机号
    private String phonenum;
    
    //总收益
    private Double totalEarn;
    
    //当前收益（余额）
    private Double nowEarn;

    //是否可用
    private Byte enabled;
    
    //省id
    private Integer province;
    
    //省名
    private String provincename;
    
    //市
    private Integer city;
    
    //市名
    private String cityname;
    
    //县
    private Integer country;
    
    //县名
    private String countryname;

    //创建时间
    private Date createTime;
    
    private Idcard idcardinfo;
    
    //昵称
    private String username;
    
    //姓名
    private String realname;
    
    private Integer startindex;
    
    //省市区合名
    private String areaname;
    
    //详细地址
    private String address;

}
package com.hedong.hedongwx.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Idcard {
	
	//表自增id
    private Integer id;

    //用户id
    private Integer uid;

    //身份证号码
    private String idCardnum;

    //身份证正面照地址
    private String cardimgFront;

    //身份证背面照地址
    private String cardimgBack;

    //创建时间
    private Date createTime;
}
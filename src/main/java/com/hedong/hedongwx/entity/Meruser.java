package com.hedong.hedongwx.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Meruser {
	
	//表自增id
    private Integer id;

    //用户id
    private Integer uid;

    //手机号
    private String phonenum;

    //是否可用
    private Byte enable;

    //创建时间
    private Date createTime;
    
    private Idcard idcardinfo;

}
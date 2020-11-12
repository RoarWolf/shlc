package com.hedong.hedongwx.entity;

import lombok.Data;

/**
*@author：RoarWolf
*@Date：2020年11月10日
*地区表
*/

@Data
public class District {
	
	//表自增id
    private String id;

    //名称
    private String name;

    //父id
    private String parentId;

    //简称
    private String shortName;

    //等级类型（0国家1省2市3区）
    private String levelType;

    //城市编码
    private String cityCode;

    //邮政编码
    private String zipCode;

    //合并名称
    private String mergerName;

    //城市经度
    private String lng;

    //城市纬度
    private String lat;

    //城市拼音
    private String pinyin;

    //地区名称
    private String areaName;

    private String isHot;

    private String isStore;

    private String createTime;

    private String updateTime;

    private String isDeleted;

}

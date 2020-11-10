package com.hedong.hedongwx.entity;

import lombok.Data;

/**
*@author：RoarWolf
*@Date：2020年11月10日
*地区表
*/

@Data
public class District {

	private Integer id;

    private Integer pid;

    private String districtName;

    private Integer type;

    private Integer hierarchy;

    private String districtSqe;
}

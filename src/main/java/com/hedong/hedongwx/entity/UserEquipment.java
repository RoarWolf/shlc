package com.hedong.hedongwx.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserEquipment {

	private Integer userId;

    private String equipmentCode;
    
    private Integer manid;
    
    private Double divideinto;
    
    private Double dividePercent;
    
    private String username;

}

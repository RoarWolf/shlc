package com.hedong.hedongwx.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author  origin
 * 创建时间：   2019年5月25日 上午9:56:02  
 */

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Operaterecord {
	
	/** 操作表自增id */
	private Integer id;
	
	/** 操作内容（名字） */
	private String name;
	
	/** 操作人id 默认为0为系统执行  */
	private Integer opeid;
	
	/** 操作对象id */
	private Integer objid;
	
	/** 类型 0预约中、1成功、2失败*/
	private Integer type;
	
	/** 来源 1、申请桩主、2、佣金充值 */
	private Integer source;
	
	/** 备注 */
	private String remark;
	
	/** 操作时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	
	/** 操作人 */
	private String opename;
	
	/** 操作对象 */
	private String objname;

}

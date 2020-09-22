# 和动数据库更改记录

## 2018-11-26~~~2018-12-02
	- 表 hd_chargerecord
		- update resultinfo 	varchar -> int
		- update balance -> refund_money
	- 表 hd_incoins  名inCoins -> incoins
    
    Rzc 
    - 表 hd_statistics
        - incoinsorder   int   投币记录数量
        - incoinsmoney   double  投币金额计数

## 2018-12-03~~~2018-12-09
	- 表 hd_mer_detail 此表为添加
	- 表 hd_gen_detail 此表为添加
	- 表hd_user
		- 添加字段 merid 类型int

## 2018~12-24~~~2018~12-30
	- 表hd_equipment
		- 添加字段aid 小区id
		
## 2018~12-24~~~2018~12-30
	- 表hd_card
		- 添加字段remark 卡备注
		
## 2019~2-13~~~2019~2-17
	- 添加表hd_codestatistics
	- 表hd_equipment
		- 添加字段remark设备名称
		- 添加字段totalmoney设备名称

## 2019年3月20日 上午11:54:36
	- 表hd_area
	 	-添加字段 divideinto 合伙人分成比 （合伙人/总收益） double 类型，默认两位小数   默认值0.00 
	- 表hd_traderecord
	 	-添加字段mermoney 商户收益金额     	 double 类型，默认两位小数   默认值0.00 
	 	-添加字段manmoney 合伙人收益金额	  double 类型，默认两位小数   默认值0.00 
	 	
## 2019年4月8日~2019年4月14日
	- 表hd_user
	 	-添加字段 aid 小区id
	 	
## 2019年5月25日
	- 添加表hd_codesystemparam
	- 表moneyrecord 添加字段tomoney double类型
	- 表tempparent 修改common int类型
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
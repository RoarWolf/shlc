package com.hedong.hedongwx.entity;

import java.util.Date;

public class CodeSystemParam {

	/** 设备编号 */
	private String code;
	/** 设置投币充电时间(单位为分钟240) */
	private Integer coinMin;
	/** 设置刷卡充电时间 (单位为分钟240) */
	private Integer cardMin;
	/** 设置单次投币最大用电量(单位为度,1KWH) */
	private Double coinElec;
	/** 设置单次刷卡最大用电量(单位为度,1KWH) */
	private Double cardElec;
	/** 设置刷卡扣费金额(单位为1元) */
	private Double cst;
	/** 设置第一档最大充电功率（最大功率以机器支持为准200） */
	private Integer powerMax1;
	/** 设置第二档最大充电功率（最大功率以机器支持为准400） */
	private Integer powerMax2;
	/** 设置第三档最大充电功率（最大功率以机器支持为准600） */
	private Integer powerMax3;
	/** 设置第四档最大充电功率（最大功率以机器支持为准800） */
	private Integer powerMax4;
	/** 设置第二档充电时间百分比 75*/
	private Integer powerTim2;
	/** 设置第三档充电时间百分比 50*/
	private Integer powerTim3;
	/** 设置第四档充电时间百分比25 */
	private Integer powerTim4;
	/** 是否支持余额回收1（1为支持 0为不支持) */
	private Integer spRecMon;
	/** 是否支持断电自停1（1为支持 0为不支持) */
	private Integer spFullEmpty;
	/** 设置充电器最大浮充功率30 （功率为瓦，当充电器功率低于这个值的话，可视为充电器已充满) */
	private Integer fullPowerMin;
	/** 设置浮充时间120 （充电器充满变绿灯之后的，继续浮充时间，单位为分钟） */
	private Integer fullChargeTime;
	/** 是否初始显示电量 0（此功能是否支持和设备相关） */
	private Integer elecTimeFirst;
	/** 电量价格 */
	private Integer elecPri;
	/** 温度阈值 */
	private Integer hotDoorsill;
	/** 烟感阈值 */
	private Integer smokeDoorsill;
	/** 总功率值 */
	private Integer powerTotal;
	/** 最后修改时间（读取或修改） */
	private Date updateTime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCoinMin() {
		return coinMin;
	}

	public void setCoinMin(Integer coinMin) {
		this.coinMin = coinMin;
	}

	public Integer getCardMin() {
		return cardMin;
	}

	public void setCardMin(Integer cardMin) {
		this.cardMin = cardMin;
	}

	public Double getCoinElec() {
		return coinElec;
	}

	public void setCoinElec(Double coinElec) {
		this.coinElec = coinElec;
	}

	public Double getCardElec() {
		return cardElec;
	}

	public void setCardElec(Double cardElec) {
		this.cardElec = cardElec;
	}

	public Double getCst() {
		return cst;
	}

	public void setCst(Double cst) {
		this.cst = cst;
	}

	public Integer getPowerMax1() {
		return powerMax1;
	}

	public void setPowerMax1(Integer powerMax1) {
		this.powerMax1 = powerMax1;
	}

	public Integer getPowerMax2() {
		return powerMax2;
	}

	public void setPowerMax2(Integer powerMax2) {
		this.powerMax2 = powerMax2;
	}

	public Integer getPowerMax3() {
		return powerMax3;
	}

	public void setPowerMax3(Integer powerMax3) {
		this.powerMax3 = powerMax3;
	}

	public Integer getPowerMax4() {
		return powerMax4;
	}

	public void setPowerMax4(Integer powerMax4) {
		this.powerMax4 = powerMax4;
	}

	public Integer getPowerTim2() {
		return powerTim2;
	}

	public void setPowerTim2(Integer powerTim2) {
		this.powerTim2 = powerTim2;
	}

	public Integer getPowerTim3() {
		return powerTim3;
	}

	public void setPowerTim3(Integer powerTim3) {
		this.powerTim3 = powerTim3;
	}

	public Integer getPowerTim4() {
		return powerTim4;
	}

	public void setPowerTim4(Integer powerTim4) {
		this.powerTim4 = powerTim4;
	}

	public Integer getSpRecMon() {
		return spRecMon;
	}

	public void setSpRecMon(Integer spRecMon) {
		this.spRecMon = spRecMon;
	}

	public Integer getSpFullEmpty() {
		return spFullEmpty;
	}

	public void setSpFullEmpty(Integer spFullEmpty) {
		this.spFullEmpty = spFullEmpty;
	}

	public Integer getFullPowerMin() {
		return fullPowerMin;
	}

	public void setFullPowerMin(Integer fullPowerMin) {
		this.fullPowerMin = fullPowerMin;
	}

	public Integer getFullChargeTime() {
		return fullChargeTime;
	}

	public void setFullChargeTime(Integer fullChargeTime) {
		this.fullChargeTime = fullChargeTime;
	}

	public Integer getElecTimeFirst() {
		return elecTimeFirst;
	}

	public void setElecTimeFirst(Integer elecTimeFirst) {
		this.elecTimeFirst = elecTimeFirst;
	}

	public Integer getElecPri() {
		return elecPri;
	}

	public void setElecPri(Integer elecPri) {
		this.elecPri = elecPri;
	}

	public Integer getHotDoorsill() {
		return hotDoorsill;
	}

	public void setHotDoorsill(Integer hotDoorsill) {
		this.hotDoorsill = hotDoorsill;
	}

	public Integer getSmokeDoorsill() {
		return smokeDoorsill;
	}

	public void setSmokeDoorsill(Integer smokeDoorsill) {
		this.smokeDoorsill = smokeDoorsill;
	}

	public Integer getPowerTotal() {
		return powerTotal;
	}

	public void setPowerTotal(Integer powerTotal) {
		this.powerTotal = powerTotal;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "CodeSystemParam [code=" + code + ", coinMin=" + coinMin + ", cardMin=" + cardMin + ", coinElec="
				+ coinElec + ", cardElec=" + cardElec + ", cst=" + cst + ", powerMax1=" + powerMax1 + ", powerMax2="
				+ powerMax2 + ", powerMax3=" + powerMax3 + ", powerMax4=" + powerMax4 + ", powerTim2=" + powerTim2
				+ ", powerTim3=" + powerTim3 + ", powerTim4=" + powerTim4 + ", spRecMon=" + spRecMon + ", spFullEmpty="
				+ spFullEmpty + ", fullPowerMin=" + fullPowerMin + ", fullChargeTime=" + fullChargeTime
				+ ", elecTimeFirst=" + elecTimeFirst + ", elecPri=" + elecPri + ", hotDoorsill=" + hotDoorsill
				+ ", smokeDoorsill=" + smokeDoorsill + ", powerTotal=" + powerTotal + ", updateTime=" + updateTime
				+ "]";
	}
	
	
//	@Override
//	public String toString() {
//		return "CodeSystemParam [code=" + code + ", coinMin=" + coinMin + ", cardMin=" + cardMin + ", coinElec="
//				+ coinElec + ", cardElec=" + cardElec + ", cst=" + cst + ", powerMax1=" + powerMax1 + ", powerMax2="
//				+ powerMax2 + ", powerMax3=" + powerMax3 + ", powerMax4=" + powerMax4 + ", powerTim2=" + powerTim2
//				+ ", powerTim3=" + powerTim3 + ", powerTim4=" + powerTim4 + ", spRecMon=" + spRecMon + ", spFullEmpty="
//				+ spFullEmpty + ", fullPowerMin=" + fullPowerMin + ", fullChargeTime=" + fullChargeTime
//				+ ", elecTimeFirst=" + elecTimeFirst + ", updateTime=" + updateTime + "]";
//	}
	

}

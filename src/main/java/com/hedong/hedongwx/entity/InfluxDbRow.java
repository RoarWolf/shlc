package com.hedong.hedongwx.entity;

import java.util.Map;

/**
 * 时序数据库
 * @author zz
 *
 */
public class InfluxDbRow {
	private String measurement;
	private Map<String,String> tags;
	private Map<String,Object> fields;
	private Long timeSecond;
	
	
	
	public InfluxDbRow() {
		super();
	}
	public InfluxDbRow(String measurement, Map<String, String> tags, Map<String, Object> fields, Long timeSecond) {
		this.measurement = measurement;
		this.tags = tags;
		this.fields = fields;
		this.timeSecond = timeSecond;
	}
	public String getMeasurement() {
		return measurement;
	}
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}
	public Map<String, String> getTags() {
		return tags;
	}
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
	public Map<String, Object> getFields() {
		return fields;
	}
	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
	public Long getTimeSecond() {
		return timeSecond;
	}
	public void setTimeSecond(Long timeSecond) {
		this.timeSecond = timeSecond;
	}
	@Override
	public String toString() {
		return "InfluxDbRow [measurement=" + measurement + ", tags=" + tags + ", fields=" + fields + ", timeSecond="
				+ timeSecond + "]";
	}
}

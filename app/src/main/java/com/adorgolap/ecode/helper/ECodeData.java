package com.adorgolap.ecode.helper;

public class ECodeData {
	private String code;
	private String name;
	private String description;
	private String halalStatus;

	public ECodeData(String code, String name, String description, String halalStatus) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.halalStatus = halalStatus;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHalalStatus() {
		return halalStatus;
	}
	public void setHalalStatus(String halalStatus) {
		this.halalStatus = halalStatus;
	}
	
}

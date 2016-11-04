package com.adorgolap.ecode.helper;

public class ECodeData {
	public String code;
	public String name;
	public String description;
	public String halalStatus;

	public ECodeData(String code, String name, String description, String halalStatus) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.halalStatus = halalStatus;
	}
	public ECodeData(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}

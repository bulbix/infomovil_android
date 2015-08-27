package com.infomovil.infomovil.model;


public class CallingCodes {

	private String countryCode;
	private String countryName;
	private String phoneCode;
	private int idBase;
	
	public CallingCodes(String code, String name, String phone) {
		countryCode = code;
		countryName = name;
		phoneCode = phone;
	}
	public CallingCodes() {
		countryCode = "";
		countryName = "";
		phoneCode = "";
		idBase = 0;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	public int getIdBase() {
		return idBase;
	}
	public void setIdBase(int idBase) {
		this.idBase = idBase;
	}
}

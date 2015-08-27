package com.infomovil.infomovil.model;

public class ContactoModel {
	
	private String longLabelNaptr, regExp, servicesNaptr, subCategory, expAux, codCountry;
	private int visible, idContacto;
	
	public String getLongLabelNaptr() {
		return longLabelNaptr;
	}
	public void setLongLabelNaptr(String longLabelNaptr) {
		this.longLabelNaptr = longLabelNaptr;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public String getServicesNaptr() {
		return servicesNaptr;
	}
	public void setServicesNaptr(String servicesNaptr) {
		this.servicesNaptr = servicesNaptr;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public int getIdContacto() {
		return idContacto;
	}
	public void setIdContacto(int idContacto) {
		this.idContacto = idContacto;
	}
	public String getExpAux() {
		return expAux;
	}
	public void setExpAux(String expAux) {
		this.expAux = expAux;
	}
	public String getCodCountry() {
		return codCountry;
	}
	public void setCodCountry(String codCountry) {
		this.codCountry = codCountry;
	}
}

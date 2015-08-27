package com.infomovil.infomovil.model;

public class KeywordDataModel {
	
	private int idKeyword;
	private String keywordField, keywordValue, idAuxKeyword;
	
	public int getIdKeyword() {
		return idKeyword;
	}
	public void setIdKeyword(int idKeyword) {
		this.idKeyword = idKeyword;
	}
	public String getKeywordField() {
		return keywordField;
	}
	public void setKeywordField(String keywordField) {
		this.keywordField = keywordField;
	}
	public String getKeywordValue() {
		return keywordValue;
	}
	public void setKeywordValue(String keywordValue) {
		this.keywordValue = keywordValue;
	}
	public String getIdAuxKeyword() {
		return idAuxKeyword;
	}
	public void setIdAuxKeyword(String idAuxKeyword) {
		this.idAuxKeyword = idAuxKeyword;
	}

}
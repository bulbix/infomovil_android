package com.infomovil.infomovil.model;

import com.google.gson.Gson;

public class CompraModel {
	
	private String plan;
	private String montoBruto;
	private Integer comision;
	//private Double montoReal;
	private String pagoId;
	private String statusPago;
	private String codigoCobro;
	private String referencia;
	private String tipoCompra;
	private String montoOrigen;
	
	
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	public String getMontoBruto() {
		return montoBruto;
	}
	public void setMontoBruto(String montoBruto) {
		this.montoBruto = montoBruto;
	}
	public Integer getComision() {
		return comision;
	}
	public void setComision(Integer comision) {
		this.comision = comision;
	}
//	public Double getMontoReal() {
//		return montoReal;
//	}
//	public void setMontoReal(Double montoReal) {
//		this.montoReal = montoReal;
//	}
	public String getPagoId() {
		return pagoId;
	}
	public void setPagoId(String pagoId) {
		this.pagoId = pagoId;
	}
	public String getStatusPago() {
		return statusPago;
	}
	public void setStatusPago(String statusPago) {
		this.statusPago = statusPago;
	}
	public String getCodigoCobro() {
		return codigoCobro;
	}
	public void setCodigoCobro(String codigoCobro) {
		this.codigoCobro = codigoCobro;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getTipoCompra() {
		return tipoCompra;
	}
	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}
	
	public String getMontoOrigen() {
		return montoOrigen;
	}
	public void setMontoOrigen(String montoOrigen) {
		this.montoOrigen = montoOrigen;
	}
	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	static public CompraModel create(String serializedData) {
		try{
			Gson gson = new Gson();
			return gson.fromJson(serializedData, CompraModel.class);
		}
		catch(Exception e){
			
		}
		
		return null;
	}


}

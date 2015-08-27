package com.infomovil.infomovil.model;

public class HorariosModel {
	
	private String dia, inicio, cierre;

	public HorariosModel(String _dia, String _inicio, String _cierre) {
		dia = _dia;
		inicio = _inicio;
		cierre = _cierre;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getCierre() {
		return cierre;
	}

	public void setCierre(String cierre) {
		this.cierre = cierre;
	}

}

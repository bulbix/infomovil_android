package com.infomovil.infomovil.model;

public class PromocionesTipo {
	
	private String titulo, tituloAux;
	private boolean status;
	
	public PromocionesTipo(String titulo, boolean status) {
		this.titulo = titulo;
		this.status = status;
	}
	
	public PromocionesTipo(String titulo, boolean status, String tituloAux) {
		this.titulo = titulo;
		this.status = status;
		this.tituloAux = tituloAux;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getTituloAux() {
		return tituloAux;
	}
	public void setTituloAux(String tituloAux) {
		this.tituloAux = tituloAux;
	}
}

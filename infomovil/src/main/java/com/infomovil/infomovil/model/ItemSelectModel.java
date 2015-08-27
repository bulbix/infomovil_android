package com.infomovil.infomovil.model;

import java.io.Serializable;

import com.google.gson.Gson;

public class ItemSelectModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String autor;
	private String titulo;
	private String linkImagen;
	private String linkImagenFull;
	
	private String linkVideo;
//	NSArray *link;
//	NSArray *thumbnail;
//	UIImage *imagenPrevia;
	private String descripcion;

	protected String linkSolo;

	protected String categoria;

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getLinkSolo() {
		return linkSolo;
	}

	public void setLinkSolo(String linkSolo) {
		this.linkSolo = linkSolo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getLinkImagen() {
		return linkImagen;
	}

	public void setLinkImagen(String linkImagen) {
		this.linkImagen = linkImagen;
	}

	public String getLinkVideo() {
		return linkVideo;
	}

	public void setLinkVideo(String linkVideo) {
		this.linkVideo = linkVideo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLinkImagenFull() {
		return linkImagenFull;
	}

	public void setLinkImagenFull(String linkImagenFull) {
		this.linkImagenFull = linkImagenFull;
	}

	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	static public ItemSelectModel create(String serializedData) {
		try{
			Gson gson = new Gson();
			return gson.fromJson(serializedData, ItemSelectModel.class);
		}
		catch(Exception e){
			
		}
		
		return null;
	}
	
	
}

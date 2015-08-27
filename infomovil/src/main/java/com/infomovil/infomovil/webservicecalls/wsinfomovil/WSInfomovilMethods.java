package com.infomovil.infomovil.webservicecalls.wsinfomovil;

public enum WSInfomovilMethods
{
	GET_EXIST_DOMAIN ("getExistDomain",0),
	GET_EXIST_USER ("getExistUser",0),
	INSERT_USER_DOMAIN ("insertUserDomain1",2),
	CREATE_DOMAIN ("createDomain",3),
	UPDATE_DOMAIN ("updateDomain",4),
	UPDATE_KEY_WORD_DATA ("updateKeyWordData",5),
	UPDATE_RECORD_NAPTR ("updateRecordNaptr",6),
	DELETE_IMAGE ("deleteImage",7),
	DELETE_KEY_WORD_DATA ("deleteKeyWordData",8),
	DELETE_RECORD_NAPTR ("deleteRecordNaptr",9),
	DELETE_VIDEO ("deleteVideo",10),
	DELETE_LOC_RECORD ("deleteLocRecord",11),
	DELETE_OFFER_RECORD ("deleteOfferRecord",12),
	INSERT_VIDEO ("insertVideo",13),
	INSERT_IMAGE ("insertImage",14),
	UPDATE_IMAGE ("updateImage",15),
	UPDATE_INSERT_LOC_RECORD ("updateInserLocRecord",16),
	INSERT_RECORD_NAPTR ("insertRecordNaptr",17),
	UPDATEINSERT_OFFER_RECORD ("updateinsertOfferRecord",18),
	INSERT_KEY_WORD_DATA ("insertKeyWordData",19),
	INSERT_KEYWORD_DATA_ADDRESS ("insertKeywordDataAddress",20),
	//GET_NUM_VISITAS ("getNumVisitas",21),
	GET_ESTADISTICAS ("getEstadisticas",21),
	GET_FECHA_EXPIRACION ("getFechaExpiracion",22),
	INSERT_UPDATE_INFO_USER ("insertUpdateInfoUser",23),
	CAMBIO_PASSWORD ("cambioPassword",24),
	CANCELAR_CUENTA ("cancelarCuenta",25),
	GET_ITEMS_GRATIS_DOMAIN ("getItemsGratisDomain",26),
	COMPRA_DOMINIO ("compraDominio",27),
	GET_DOMAIN ("getDomain",28),
	GET_SESSION ("sessionActiva", 29),
	CLOSE_SESSION ("cerrarSession", 30),
	GET_STATUS_DOMAIN ("statusDominio", 31),
	UPDATE_TEXT_RECORD ("updateTextRecord", 32),
	UPDATE_COLOR ("updateColor", 33),
	UPDATE_DES_CORTA ("updateDesCorta", 34),
	INSERT_USER_CAMP ("insertUserCam", 35),
	GET_EXIST_CAMPANIABYMAIL("getExistCampaniaByMail",36),
	CAMBIA_NOMBRE_RECURSO("cambiaNombreRecurso",37),
	COMPRA_INTENTO ("compraDominio",38),
	UPDATE_TEMPLATE("updateTemplate",39),
	GET_DOMAIN_LOGIN ("getDomainLogin",40),
	GET_IMAGENES("getImagenes",41),
	GET_HASH_MOVILIZA_SITIO("generaHashMovilizaSitio",42),
	GET_HASH_CAMBIO_PASSWORD("generaHashCambioPassword",43),
	GET_CATALOGO_DOMINIOS("catalogoDominios",44);
	
	
	private int value;
	private String metodo;
	
	private WSInfomovilMethods(String metodo,int value)
	{
		this.metodo	= metodo;
		this.value	= value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
}

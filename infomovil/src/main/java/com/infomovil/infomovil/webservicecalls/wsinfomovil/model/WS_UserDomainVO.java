/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

/**
 * @author Ignaki Dominguez
 *
 */
public class WS_UserDomainVO implements KvmSerializable
{
	private static final int K_EMAIL			 = 0;
	private static final int K_PHONE			 = 1;
	private static final int K_PASSWORD			 = 2;
	private static final int K_DOMAINNAME		 = 3;
	private static final int K_STATUS			 = 4;
	private static final int K_SISTEMA			 = 5;
	private static final int K_TYPODISPOSITIVO	 = 6;
	private static final int K_NOTIFICACION		 = 7;
	
	private static final int K_ACTION			 = 8;
	private static final int K_PAIS				 = 9;
	private static final int K_CANAL			 = 10;
	private static final int K_SUCURSAL			 = 11;
	private static final int K_FOLIO			 = 12;
	
	private static final int K_NAME 			 = 13;
	private static final int K_ADDRES1			 = 14;
	private static final int K_ADDRES2			 = 15;
	private static final int K_NPAIS			 = 16;
	private static final int K_CODIGOCAMP		 = 17;
	private static final int K_DOMAINTYPE		 = 18;	
	private static final int K_IDDOMINIO		 = 19;
	private static final int K_EMAILTEL			 = 20;
	private static final int K_IDCATDOMINIO		 = 21;
	
	
	private String email;
	private String phone;
	private String password;
	private String domainName;
	private int status;
	private String sistema;
	private String typoDispositivo;
	private String notificacion;
	//nuevas propiedades
	private int tipoAction; //5
	private int pais; 
	private int canal;
	private int sucursal;
	private int folio;
	
	private String nombreUsuario;
	private String direccion1;
	private String direccion2;
	private int nPais;
	private String accion="";
	private String codigoCamp="";
	private String domainType;	
	private int idDominio;
	private String emailTel;
	private String idCatDominio;
	
	public WS_UserDomainVO()
	{
		this.email = "";
		this.phone = "";
		this.password = "";
		this.domainName = "";
		this.sistema = "";
		this.typoDispositivo = "";
		this.notificacion = "";

		this.nombreUsuario = "";
		this.direccion1 = "";
		this.direccion2 = "";
		this.domainType = "";		
		this.idDominio = 0;
		this.emailTel = "";
		this.idCatDominio = "";
	}
	
	public WS_UserDomainVO(WS_UserDomainVO userDomainVO)
	{
		this.email			 = userDomainVO.getEmail();
		this.phone			 = userDomainVO.getPhone();
		this.password		 = userDomainVO.getPassword();
		this.domainName		 = userDomainVO.getDomainName();
		this.status			 = userDomainVO.getStatus();
		this.sistema		 = userDomainVO.getSistema();
		this.typoDispositivo = userDomainVO.getTypoDispositivo();
		this.notificacion	 = userDomainVO.getNotificacion();
		
		this.tipoAction		 = userDomainVO.getTipoAction();
		this.pais			 = userDomainVO.getPais();
		this.canal			 = userDomainVO.getCanal();
		this.sucursal		 = userDomainVO.getSucursal();
		this.folio 			 = userDomainVO.getFolio();
		
		this.nombreUsuario   = userDomainVO.getNombreUsuario();
		this.direccion1		 = userDomainVO.getDireccion1();
		this.direccion2      = userDomainVO.getDireccion2();
		this.nPais			 = userDomainVO.getnPais();
		this.domainType = userDomainVO.getDomainType();		
		this.idDominio = userDomainVO.getIdDominio();
		this.emailTel = userDomainVO.getEmailTel();
		this.idCatDominio = userDomainVO.getIdCatDominio();
		
	}
	
	@Override
	public String toString()
	{
		int max = getPropertyCount();
		String str = "{";
		PropertyInfo info = new PropertyInfo();
		
		for ( int i = 0; i<max; i++ )
		{
			getPropertyInfo(i, null, info);
			str = str + "["+info.name+"="+getPropertyValue(i)+"]";
		}
		str = str +"}";
		
		return str;
	}
	//**********************************************//
	// 			Metodos de KvmSerializable			//
	//**********************************************//
	@Override
	public Object getProperty(int arg0)
	{
 		try
		{
			DatosUsuario datosUsuario	= DatosUsuario.getInstance();
			String token				= datosUsuario.getToken()==null ? "": datosUsuario.getToken();
//			String token ="";
			switch (arg0)
			{
				case K_EMAIL:
					return DES.encryptDES(email, "");
				case K_PHONE:
					return ( token == null ) ? phone : DES.encryptDES(phone, token);
				case K_PASSWORD:
					return ( token == null ) ? password : DES.encryptDES(password, token);
				case K_DOMAINNAME:
					return ( token == null ) ? domainName : DES.encryptDES(domainName, token);
				case K_STATUS:
					return ( token == null ) ? status : DES.encryptDES(Integer.toString(status), token);
				case K_SISTEMA:
					return ( token == null ) ? sistema : DES.encryptDES(sistema, token);
				case K_TYPODISPOSITIVO:
					return ( token == null ) ? typoDispositivo : DES.encryptDES(typoDispositivo, token);
				case K_NOTIFICACION:
					return ( token == null ) ? notificacion : DES.encryptDES(notificacion, token);
				case K_ACTION:
					return ( token == null ) ? tipoAction : DES.encryptDES(Integer.toString(tipoAction), token);
				case K_PAIS:
					return ( token == null ) ? pais : DES.encryptDES(Integer.toString(pais), token);
				case K_CANAL:
					return ( token == null ) ? canal : DES.encryptDES(Integer.toString(canal), token);
				case K_SUCURSAL:
					return ( token == null ) ? canal : DES.encryptDES(Integer.toString(sucursal), token);
				case K_FOLIO:
					return ( token == null ) ? folio : DES.encryptDES(Integer.toString(folio), token);
				case K_NAME:
					return ( token == null ) ? nombreUsuario : DES.encryptDES(nombreUsuario, token);
				case K_ADDRES1:
					return ( token == null ) ? direccion1 : DES.encryptDES(direccion1, token);
				case K_ADDRES2:
					return ( token == null ) ? direccion2 : DES.encryptDES(direccion2, token);
				case K_NPAIS:
					return ( token == null ) ? nPais : DES.encryptDES(Integer.toString(nPais), token);
				case K_CODIGOCAMP:
					return ( token == null ) ? codigoCamp : DES.encryptDES(codigoCamp, token);
				case K_DOMAINTYPE:
					return ( token == null ) ? domainType : DES.encryptDES(domainType, token);				
				case K_IDDOMINIO:
					return ( token == null ) ? idDominio : DES.encryptDES(Integer.toString(idDominio), token);
				case K_EMAILTEL:
					return ( token == null ) ? phone : DES.encryptDES(emailTel, token);
				case K_IDCATDOMINIO:
					return ( token == null ) ? idCatDominio : DES.encryptDES(idCatDominio, token);				
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 22;
	}
	
	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
	{
		switch (arg0)
		{
			case K_EMAIL: info.type = PropertyInfo.STRING_CLASS; info.name = "email"; break;
			case K_PHONE: info.type = PropertyInfo.STRING_CLASS; info.name = "phone"; break;
			case K_PASSWORD: info.type = PropertyInfo.STRING_CLASS; info.name = "password"; break;
			case K_DOMAINNAME: info.type = PropertyInfo.STRING_CLASS; info.name = "domainName"; break;
			case K_STATUS: info.type = PropertyInfo.INTEGER_CLASS; info.name = "status"; break;
			case K_SISTEMA: info.type = PropertyInfo.STRING_CLASS; info.name = "sistema"; break;
			case K_TYPODISPOSITIVO: info.type = PropertyInfo.STRING_CLASS; info.name = "typoDispositivo"; break;
			case K_NOTIFICACION: info.type = PropertyInfo.STRING_CLASS; info.name = "notificacion"; break;
			case K_ACTION: info.type = PropertyInfo.INTEGER_CLASS; info.name = "tipoAction"; break;
			case K_PAIS: info.type = PropertyInfo.INTEGER_CLASS; info.name = "pais"; break;
			case K_CANAL: info.type = PropertyInfo.INTEGER_CLASS; info.name = "canal"; break;
			case K_SUCURSAL: info.type = PropertyInfo.INTEGER_CLASS; info.name = "sucursal"; break;
			case K_FOLIO: info.type = PropertyInfo.INTEGER_CLASS; info.name = "folio"; break;
			case K_NAME: info.type = PropertyInfo.STRING_CLASS; info.name = "nombre"; break;
			case K_ADDRES1: info.type = PropertyInfo.STRING_CLASS; info.name = "direccion1"; break;
			case K_ADDRES2: info.type = PropertyInfo.STRING_CLASS; info.name = "direccion2"; break;
			case K_NPAIS: info.type = PropertyInfo.INTEGER_CLASS; info.name = "nPais"; break;
			case K_CODIGOCAMP: info.type = PropertyInfo.STRING_CLASS; info.name = "codigoCamp"; break;
			case K_DOMAINTYPE: info.type = PropertyInfo.STRING_CLASS; info.name = "domainType"; break;						
			case K_IDDOMINIO:  info.type = PropertyInfo.STRING_CLASS; info.name = "idDominio"; break;
			case K_EMAILTEL: info.type = PropertyInfo.STRING_CLASS; info.name = "emailTel"; break;
			case K_IDCATDOMINIO: info.type = PropertyInfo.STRING_CLASS; info.name = "idCatDominio"; break;
			default:break;
		}
	}

	@Override
	public void setProperty(int arg0, Object value)
	{
		switch (arg0)
		{
			case K_EMAIL: email = value.toString();break;
			case K_PHONE: phone = value.toString();break;
			case K_PASSWORD: password = value.toString();break;
			case K_DOMAINNAME: domainName = value.toString();break;
			case K_STATUS: status = Integer.parseInt(value.toString());break;
			case K_SISTEMA: sistema = value.toString();break;
			case K_TYPODISPOSITIVO: typoDispositivo = value.toString();break;
			case K_NOTIFICACION: notificacion = value.toString();break;
			case K_ACTION: tipoAction = Integer.parseInt(value.toString());break;
			case K_PAIS: tipoAction = Integer.parseInt(value.toString());break;
			case K_CANAL: canal = Integer.parseInt(value.toString());break;
			case K_SUCURSAL: sucursal = Integer.parseInt(value.toString());break;
			case K_FOLIO: folio = Integer.parseInt(value.toString());break;
			case K_NAME: nombreUsuario = value.toString(); break;
			case K_ADDRES1: direccion1 = value.toString(); break;
			case K_ADDRES2: direccion2 = value.toString(); break;
			case K_NPAIS: nPais = Integer.parseInt(value.toString()); break;
			case K_CODIGOCAMP: codigoCamp = value.toString();break;
			case K_DOMAINTYPE: domainType = value.toString();break;				   			
			case K_IDDOMINIO:  idDominio = Integer.parseInt(value.toString()); break; 
			case K_EMAILTEL: emailTel = value.toString();break;
			case K_IDCATDOMINIO: idCatDominio = value.toString();break;
			default:break;
		}

	}
	//**********************************************//
	// 				Getters y Setters				//
	//**********************************************//
	public Object getPropertyValue(int arg0) {
		switch (arg0)
		{
			case K_EMAIL: return email;
			case K_PHONE: return phone;
			case K_PASSWORD: return password;
			case K_DOMAINNAME: return domainName;
			case K_STATUS: return status;
			case K_SISTEMA: return sistema;
			case K_TYPODISPOSITIVO: return typoDispositivo;
			case K_NOTIFICACION: return notificacion;
			case K_ACTION: return tipoAction;
			case K_PAIS: return pais;
			case K_CANAL: return canal;
			case K_SUCURSAL: return sucursal;
			case K_FOLIO: return folio;
			case K_NAME: return nombreUsuario;
			case K_ADDRES1: return direccion1;
			case K_ADDRES2: return direccion2;
			case K_NPAIS: return nPais;
			case K_CODIGOCAMP: return codigoCamp;
			case K_DOMAINTYPE: return domainType;				   			
			case K_IDDOMINIO:  return idDominio;
			case K_EMAILTEL: return emailTel;
			case K_IDCATDOMINIO: return idCatDominio;
		}
		return null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getTypoDispositivo() {
		return typoDispositivo;
	}

	public void setTypoDispositivo(String typoDispositivo) {
		this.typoDispositivo = typoDispositivo;
	}

	public String getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}
	
	public int getTipoAction() {
		return tipoAction;
	}

	public void setTipoAction(int tipoAction) {
		this.tipoAction = tipoAction;
	}

	public int getPais() {
		return pais;
	}

	public void setPais(int pais) {
		this.pais = pais;
	}

	public int getCanal() {
		return canal;
	}

	public void setCanal(int canal) {
		this.canal = canal;
	}

	public int getSucursal() {
		return sucursal;
	}

	public void setSucursal(int sucursal) {
		this.sucursal = sucursal;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getDireccion1() {
		return direccion1;
	}

	public void setDireccion1(String direccion1) {
		this.direccion1 = direccion1;
	}

	public String getDireccion2() {
		return direccion2;
	}

	public void setDireccion2(String direccion2) {
		this.direccion2 = direccion2;
	}

	public int getnPais() {
		return nPais;
	}

	public void setnPais(int nPais) {
		this.nPais = nPais;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	public String getCodigoCamp() {
		return codigoCamp;
	}

	public void setCodigoCamp(String codigoCamp) {
		this.codigoCamp = codigoCamp;
	}

	public String getDomainType() {
		return domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}	

	public int getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(int idDominio) {
		this.idDominio = idDominio;
	}

	public String getEmailTel() {
		return emailTel;
	}

	public void setEmailTel(String emailTel) {
		this.emailTel = emailTel;
	}

	public String getIdCatDominio() {
		return idCatDominio;
	}

	public void setIdCatDominio(String idCatDominio) {
		this.idCatDominio = idCatDominio;
	}
	
}

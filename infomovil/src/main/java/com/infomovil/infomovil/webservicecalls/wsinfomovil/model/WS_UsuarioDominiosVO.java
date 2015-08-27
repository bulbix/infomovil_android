package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;


public class WS_UsuarioDominiosVO implements KvmSerializable {
	
	private static final int K_IDDOMAIN = 0;
	private static final int K_IDCTRLDOMAIN = 1;
	private static final int K_DOMAINCTRLNAME = 2;
	private static final int K_DOMAINTYPE = 3;
	private static final int K_STATUSCTRLDOMINIO = 4;
	private static final int K_STATUSVISIBLE = 5;
	private static final int K_FECHACTRLINI = 6;
	private static final int K_FECHACTRLFIN = 7;
	private static final int K_VIGENTE = 8;
	private static final int K_URLSITIO = 9;
	
	
	
	private String idDomain;
	private String idCtrlDomain;
	private String domainCtrlName;
	private String domainType;
	private String statusCtrlDominio;
	private String statusVisible;
	private String fechaCtrlIni;
	private String fechaCtrlFin;
	private String vigente;
	private String urlSitio;
	
	
	public WS_UsuarioDominiosVO() {
	}
	
	public WS_UsuarioDominiosVO(String domainType){
		this.domainType = domainType;
	}
	
	
	@Override
	public Object getProperty(int arg0) {
		try
		{
			DatosUsuario datosUsuario	= DatosUsuario.getInstance();
			String token				= datosUsuario.getToken();
	        switch(arg0)
	        {
		        case K_IDDOMAIN:
		        	return ( token == null ) ? idDomain : DES.encryptDES(idDomain, token);
		        case K_IDCTRLDOMAIN:
		        	return ( token == null ) ? idCtrlDomain : DES.encryptDES(idCtrlDomain, token);
		        case K_DOMAINCTRLNAME:
		        	return ( token == null ) ? domainCtrlName : DES.encryptDES(domainCtrlName, token);
		        case K_DOMAINTYPE:
		        	return ( token == null ) ? domainType : DES.encryptDES(domainType, token);
		        case K_STATUSCTRLDOMINIO:
		        	return ( token == null ) ? statusCtrlDominio : DES.encryptDES(statusCtrlDominio, token);
		        case K_STATUSVISIBLE:
		        	return ( token == null ) ? statusVisible : DES.encryptDES(statusVisible, token);
		        case K_FECHACTRLINI:
		        	return ( token == null ) ? fechaCtrlIni : DES.encryptDES(fechaCtrlIni, token);
		        case K_FECHACTRLFIN:
		        	return ( token == null ) ? fechaCtrlFin : DES.encryptDES(fechaCtrlFin, token);
		        case K_VIGENTE:
		        	return ( token == null ) ? vigente : DES.encryptDES(vigente, token);
		        case K_URLSITIO:
		        	return ( token == null ) ? urlSitio : DES.encryptDES(urlSitio, token);
		        	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 10;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info) {
		 switch(index)
	        {
	        case K_IDDOMAIN: info.type = PropertyInfo.STRING_CLASS; info.name = "idDomain"; break;
	        case K_IDCTRLDOMAIN: info.type = PropertyInfo.STRING_CLASS; info.name = "idCtrlDomain"; break;
	        case K_DOMAINCTRLNAME: info.type = PropertyInfo.STRING_CLASS; info.name = "domainCtrlName"; break;
	        case K_DOMAINTYPE: info.type = PropertyInfo.STRING_CLASS; info.name = "domainType"; break;
	        case K_STATUSCTRLDOMINIO: info.type = PropertyInfo.STRING_CLASS; info.name = "statusCtrlDominio"; break;
	        case K_STATUSVISIBLE: info.type = PropertyInfo.STRING_CLASS; info.name = "statusVisible"; break;
	        case K_FECHACTRLINI: info.type = PropertyInfo.STRING_CLASS; info.name = "fechaCtrlIni"; break;
	        case K_FECHACTRLFIN: info.type = PropertyInfo.STRING_CLASS; info.name = "fechaCtrlFin"; break;
	        case K_VIGENTE: info.type = PropertyInfo.STRING_CLASS; info.name = "vigente"; break;
	        case K_URLSITIO: info.type = PropertyInfo.STRING_CLASS; info.name = "urlSitio"; break;
	        }
		
	}
	@Override
	public void setProperty(int index, Object value) {
		 switch(index)
	        {
		        case K_IDDOMAIN: idDomain = value.toString(); break;
		        case K_IDCTRLDOMAIN: idCtrlDomain= value.toString(); break;
		        case K_DOMAINCTRLNAME: domainCtrlName= value.toString(); break;
		        case K_DOMAINTYPE: domainType= value.toString(); break;
		        case K_STATUSCTRLDOMINIO: statusCtrlDominio= value.toString(); break;
		        case K_STATUSVISIBLE: statusVisible= value.toString(); break;
		        case K_FECHACTRLINI: fechaCtrlIni= value.toString(); break;
		        case K_FECHACTRLFIN: fechaCtrlFin= value.toString(); break;
		        case K_VIGENTE: vigente= value.toString(); break;
		        case K_URLSITIO: urlSitio = value.toString(); break;
				default:break;
	        }
		
	}
	public String getIdDomain() {
		return idDomain;
	}
	public void setIdDomain(String idDomain) {
		this.idDomain = idDomain;
	}
	public String getIdCtrlDomain() {
		return idCtrlDomain;
	}
	public void setIdCtrlDomain(String idCtrlDomain) {
		this.idCtrlDomain = idCtrlDomain;
	}
	public String getDomainCtrlName() {
		return domainCtrlName;
	}
	public void setDomainCtrlName(String domainCtrlName) {
		this.domainCtrlName = domainCtrlName;
	}
	public String getDomainType() {
		return domainType;
	}
	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}
	public String getStatusCtrlDominio() {
		return statusCtrlDominio;
	}
	public void setStatusCtrlDominio(String statusCtrlDominio) {
		this.statusCtrlDominio = statusCtrlDominio;
	}
	public String getStatusVisible() {
		return statusVisible;
	}
	public void setStatusVisible(String statusVisible) {
		this.statusVisible = statusVisible;
	}
	public String getFechaCtrlIni() {
		return fechaCtrlIni;
	}
	public void setFechaCtrlIni(String fechaCtrlIni) {
		this.fechaCtrlIni = fechaCtrlIni;
	}
	public String getFechaCtrlFin() {
		return fechaCtrlFin;
	}
	public void setFechaCtrlFin(String fechaCtrlFin) {
		this.fechaCtrlFin = fechaCtrlFin;
	}
	
	public String getVigente() {
		return vigente;
	}
	public void setVigente(String vigente) {
		this.vigente = vigente;
	}
	public String getUrlSitio() {
		return urlSitio;
	}

	public void setUrlSitio(String urlSitio) {
		this.urlSitio = urlSitio;
	}

	@Override
	public String toString() {
		
		if(domainType.equalsIgnoreCase("recurso")){
			return "Publicado";
		}
		else {
			if(StringUtils.isEmpty(fechaCtrlIni) || StringUtils.isEmpty(fechaCtrlFin)){
				return "No Publicado";
			}
			else{
				return "Publicado";
			}
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((domainType == null) ? 0 : domainType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WS_UsuarioDominiosVO other = (WS_UsuarioDominiosVO) obj;
		if (domainType == null) {
			if (other.domainType != null)
				return false;
		} else if (!domainType.equals(other.domainType))
			return false;
		return true;
	}
	
	
	

	
	

}

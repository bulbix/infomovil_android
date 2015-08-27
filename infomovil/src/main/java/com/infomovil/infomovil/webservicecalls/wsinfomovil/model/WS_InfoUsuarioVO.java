package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_InfoUsuarioVO implements KvmSerializable
{
	private static final int K_CALLENUM = 0;
	private static final int K_CANAL = 1;
	private static final int K_CIUDAD = 2;
	private static final int K_CORREO = 3;
	private static final int K_CP = 4;
	private static final int K_ESTADO = 5;
	private static final int K_NAMEEMPRESA = 6;
	private static final int K_NOMBRE = 7;
	private static final int K_PAIS = 8;
	private static final int K_POBLACION = 9;
	private static final int K_TEL = 10;
	private static final int K_USERNAME = 11;

	private String calleNum;
	private int canal;
	private String ciudad;
	private String correo;
	private String cp;
	private String estado;
	private String nameEmpresa;
	private String nombre;
	private int pais;
	private String poblacion;
	private String tel;
	private String userName;
	
	public WS_InfoUsuarioVO() {
		
	}
	
	public WS_InfoUsuarioVO(WS_InfoUsuarioVO infoUsuarioVO) 
	{
		this.calleNum	 = (infoUsuarioVO.getCalleNum() != null ) ? new String(infoUsuarioVO.getCalleNum()) : "";
		this.canal		 = infoUsuarioVO.getCanal();
		this.ciudad		 = (infoUsuarioVO.getCiudad() != null ) ? new String(infoUsuarioVO.getCiudad()) : "";
		this.correo		 = (infoUsuarioVO.getCorreo() != null ) ? new String(infoUsuarioVO.getCorreo()) : "";
		this.cp			 = (infoUsuarioVO.getCp() != null ) ? new String(infoUsuarioVO.getCp()) : "";
		this.estado		 = (infoUsuarioVO.getEstado() != null ) ? new String(infoUsuarioVO.getEstado()) : "";
		this.nameEmpresa = (infoUsuarioVO.getNameEmpresa() != null ) ? new String(infoUsuarioVO.getNameEmpresa()) : "";
		this.nombre		 = (infoUsuarioVO.getNombre() != null ) ? new String(infoUsuarioVO.getNombre()) : "";
		this.pais		 = infoUsuarioVO.getPais();
		this.poblacion	 = (infoUsuarioVO.getPoblacion() != null ) ? new String(infoUsuarioVO.getPoblacion()) : "";
		this.tel		 = (infoUsuarioVO.getTel() != null ) ? new String(infoUsuarioVO.getTel()) : "";
		this.userName	 = (infoUsuarioVO.getUserName() != null ) ? new String(infoUsuarioVO.getUserName()) : "";
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
			String token				= datosUsuario.getToken();
	        switch(arg0)
	        {
		        case K_CALLENUM:
		        	return ( token == null ) ? calleNum : DES.encryptDES(calleNum, token);
		        case K_CANAL:
		        	return ( token == null ) ? canal : DES.encryptDES(Integer.toString(canal), token);
		        case K_CIUDAD:
		        	return ( token == null ) ? ciudad : DES.encryptDES(ciudad, token);
		        case K_CORREO:
		        	return ( token == null ) ? correo : DES.encryptDES(correo, token);
		        case K_CP:
		        	return ( token == null ) ? cp : DES.encryptDES(cp, token);
		        case K_ESTADO:
		        	return ( token == null ) ? estado : DES.encryptDES(estado, token);
		        case K_NAMEEMPRESA:
		        	return ( token == null ) ? nameEmpresa : DES.encryptDES(nameEmpresa, token);
		        case K_NOMBRE:
		        	return ( token == null ) ? nombre : DES.encryptDES(nombre, token);
		        case K_PAIS:
		        	return ( token == null ) ? pais : DES.encryptDES(Integer.toString(pais), token);
		        case K_POBLACION:
		        	return ( token == null ) ? poblacion : DES.encryptDES(poblacion, token);
		        case K_TEL:
		        	return ( token == null ) ? tel : DES.encryptDES(tel, token);
		        case K_USERNAME:
		        	return ( token == null ) ? userName : DES.encryptDES(userName, token);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 0;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_CALLENUM: info.type = PropertyInfo.STRING_CLASS; info.name = "calleNum"; break;
	        case K_CANAL: info.type = PropertyInfo.INTEGER_CLASS; info.name = "canal"; break;
	        case K_CIUDAD: info.type = PropertyInfo.STRING_CLASS; info.name = "ciudad"; break;
	        case K_CORREO: info.type = PropertyInfo.STRING_CLASS; info.name = "correo"; break;
	        case K_CP: info.type = PropertyInfo.STRING_CLASS; info.name = "cp"; break;
	        case K_ESTADO: info.type = PropertyInfo.STRING_CLASS; info.name = "estado"; break;
	        case K_NAMEEMPRESA: info.type = PropertyInfo.STRING_CLASS; info.name = "nameEmpresa"; break;
	        case K_NOMBRE: info.type = PropertyInfo.STRING_CLASS; info.name = "nombre"; break;
	        case K_PAIS: info.type = PropertyInfo.INTEGER_CLASS; info.name = "pais"; break;
	        case K_POBLACION: info.type = PropertyInfo.STRING_CLASS; info.name = "poblacion"; break;
	        case K_TEL: info.type = PropertyInfo.STRING_CLASS; info.name = "tel"; break;
	        case K_USERNAME: info.type = PropertyInfo.STRING_CLASS; info.name = "userName"; break;
			default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_CALLENUM: calleNum = value.toString();break;
	        case K_CANAL: canal = Integer.parseInt(value.toString());break;
	        case K_CIUDAD: ciudad = value.toString();break;
	        case K_CORREO: correo = value.toString();break;
	        case K_CP: cp = value.toString();break;
	        case K_ESTADO: estado = value.toString();break;
	        case K_NAMEEMPRESA: nameEmpresa = value.toString();break;
	        case K_NOMBRE: nombre = value.toString();break;
	        case K_PAIS: pais = Integer.parseInt(value.toString());break;
	        case K_POBLACION: poblacion = value.toString();break;
	        case K_TEL: tel = value.toString();break;
	        case K_USERNAME: userName = value.toString();break;
			default:break;
        }
	}
	
	//**********************************************//
	// 				Getters y Setters				//
	//**********************************************//
	public Object getPropertyValue(int arg0)
	{
        switch(arg0)
        {
	        case K_CALLENUM: return calleNum;
	        case K_CANAL: return canal;
	        case K_CIUDAD: return ciudad;
	        case K_CORREO: return correo;
	        case K_CP: return cp;
	        case K_ESTADO: return estado;
	        case K_NAMEEMPRESA: return nameEmpresa;
	        case K_NOMBRE: return nombre;
	        case K_PAIS: return pais;
	        case K_POBLACION: return poblacion;
	        case K_TEL: return tel;
	        case K_USERNAME: return userName;
        }
        
        return null;
	}
	
	public String getCalleNum() {
		return calleNum;
	}
	public void setCalleNum(String calleNum) {
		this.calleNum = calleNum;
	}
	public int getCanal() {
		return canal;
	}
	public void setCanal(int canal) {
		this.canal = canal;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNameEmpresa() {
		return nameEmpresa;
	}
	public void setNameEmpresa(String nameEmpresa) {
		this.nameEmpresa = nameEmpresa;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPais() {
		return pais;
	}
	public void setPais(int pais) {
		this.pais = pais;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}

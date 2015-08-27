package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.google.gson.Gson;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_StatusDomainVO implements KvmSerializable
{
	private static final int K_DESCRIPCIONITEM = 0;
	private static final int K_ESTATUSITEM = 1;
	private static final int K_FECHAEXPIRACION = 2;

	private String descripcionItem;
	private int estatusItem;
	private int fechaExpiracion;
	
	public WS_StatusDomainVO (String descripcionItem, int estatusItem)
	{
		this.descripcionItem = descripcionItem;
		this.estatusItem = estatusItem;
	}
	
	public WS_StatusDomainVO (String descripcionItem)
	{
		this.descripcionItem = descripcionItem;
		
	}
	
	public WS_StatusDomainVO() {
		
	}
	
	public WS_StatusDomainVO(WS_StatusDomainVO statusDomainVO)
	{
		this.descripcionItem = new String(statusDomainVO.getDescripcionItem());
		this.estatusItem	 = statusDomainVO.getEstatusItem();
		this.fechaExpiracion = statusDomainVO.getFechaExpiracion();
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
		        case K_DESCRIPCIONITEM:
		        	return ( token == null ) ? descripcionItem : DES.encryptDES(descripcionItem, token);
		        case K_ESTATUSITEM:
		        	return ( token == null ) ? estatusItem : DES.encryptDES(Integer.toString(estatusItem), token);
		        case K_FECHAEXPIRACION:
		        	return ( token == null ) ? fechaExpiracion : DES.encryptDES(Integer.toString(fechaExpiracion), token);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 3;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_DESCRIPCIONITEM: info.type = PropertyInfo.STRING_CLASS; info.name = "descripcionItem"; break;
	        case K_ESTATUSITEM: info.type = PropertyInfo.INTEGER_CLASS; info.name = "estatusItem"; break;
	        case K_FECHAEXPIRACION: info.type = PropertyInfo.INTEGER_CLASS; info.name = "fechaExpiracion"; break;
			default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_DESCRIPCIONITEM: descripcionItem = value.toString();break;
	        case K_ESTATUSITEM: estatusItem = Integer.parseInt(value.toString());break;
	        case K_FECHAEXPIRACION: fechaExpiracion = Integer.parseInt(value.toString());break;
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
	        case K_DESCRIPCIONITEM: return descripcionItem;
	        case K_ESTATUSITEM: return estatusItem;
	        case K_FECHAEXPIRACION: return fechaExpiracion;
        }
        
        return null;
	}
	
	public String getDescripcionItem() {
		return descripcionItem;
	}
	public void setDescripcionItem(String descripcionItem) {
		this.descripcionItem = descripcionItem;
	}
	public int getEstatusItem() {
		return estatusItem;
	}
	public void setEstatusItem(int estatusItem) {
		this.estatusItem = estatusItem;
	}
	public int getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(int fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcionItem == null) ? 0 : descripcionItem.hashCode());
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
		WS_StatusDomainVO other = (WS_StatusDomainVO) obj;
		if (descripcionItem == null) {
			if (other.descripcionItem != null)
				return false;
		} else if (!descripcionItem.equals(other.descripcionItem))
			return false;
		return true;
	}
	
	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	static public WS_StatusDomainVO create(String serializedData) {
		try{
			Gson gson = new Gson();
			return gson.fromJson(serializedData, WS_StatusDomainVO.class);
		}
		catch(Exception e){
			
		}
		
		return null;
	}
	
	

}

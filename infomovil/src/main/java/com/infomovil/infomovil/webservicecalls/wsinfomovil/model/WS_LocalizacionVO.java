package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_LocalizacionVO implements KvmSerializable
{
	private static final int K_IDLOCALIZACION = 0;
	private static final int K_LATITUDELOC = 1;
	private static final int K_LONGITUDELOC = 2;

	private int idLocalizacion;
	private double latitudeLoc;
	private double longitudeLoc;
	
	public WS_LocalizacionVO ()
	{
		
	}
	
	public WS_LocalizacionVO(WS_LocalizacionVO localizacion)
	{
		this.idLocalizacion	= localizacion.getIdLocalizacion();
		this.latitudeLoc	= localizacion.getLatitudeLoc();
		this.longitudeLoc	= localizacion.getLongitudeLoc();
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
		        case K_IDLOCALIZACION:
		        	return ( token == null ) ? idLocalizacion : DES.encryptDES(Integer.toString(idLocalizacion), token);
		        case K_LATITUDELOC:
		        	return ( token == null ) ? latitudeLoc : DES.encryptDES(Double.toString(latitudeLoc), token);
		        case K_LONGITUDELOC:
		        	return ( token == null ) ? longitudeLoc : DES.encryptDES(Double.toString(longitudeLoc), token);
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
	        case K_IDLOCALIZACION: info.type = PropertyInfo.INTEGER_CLASS; info.name = "idLocalizacion"; break;
	        case K_LATITUDELOC: info.type = Double.class; info.name = "latitudeLoc"; break;
	        case K_LONGITUDELOC: info.type = Double.class; info.name = "longitudeLoc"; break;
			default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_IDLOCALIZACION: idLocalizacion = Integer.parseInt(value.toString());break;
	        case K_LATITUDELOC: latitudeLoc = Double.parseDouble(value.toString());break;
	        case K_LONGITUDELOC: longitudeLoc = Double.parseDouble(value.toString());break;
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
	        case K_IDLOCALIZACION: return idLocalizacion;
	        case K_LATITUDELOC: return latitudeLoc;
	        case K_LONGITUDELOC: return longitudeLoc;
        }
        
        return null;
	}
	
	public int getIdLocalizacion() {
		return idLocalizacion;
	}
	public void setIdLocalizacion(int idLocalizacion) {
		this.idLocalizacion = idLocalizacion;
	}
	public double getLatitudeLoc() {
		return latitudeLoc;
	}
	public void setLatitudeLoc(double latitudeLoc) {
		this.latitudeLoc = latitudeLoc;
	}
	public double getLongitudeLoc() {
		return longitudeLoc;
	}
	public void setLongitudeLoc(double longitudeLoc) {
		this.longitudeLoc = longitudeLoc;
	}
}

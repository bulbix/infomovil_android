package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_VisitaVO implements KvmSerializable
{
	private static final int K_DESCRIPCIONVISITAS	 = 0;
	private static final int K_FECHA				 = 1;
	private static final int K_NUMEROFECHA			 = 2;
	private static final int K_VISITAS				 = 3;

	private String descripcionVisitas;
	private String fecha;
	private int numeroFecha;
	private long visitas;
	
	public WS_VisitaVO()
	{
		
	}
	
	public WS_VisitaVO (WS_VisitaVO visitaVO)
	{
		this.descripcionVisitas	= new String(visitaVO.getDescripcionVisitas());
		this.fecha				= new String(visitaVO.getFecha());
		this.numeroFecha		= visitaVO.getNumeroFecha();
		this.visitas			= visitaVO.getVisitas();
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
		        case K_DESCRIPCIONVISITAS:
		        	return ( token == null ) ? descripcionVisitas : DES.encryptDES(descripcionVisitas, token);
		        case K_FECHA:
		        	return ( token == null ) ? fecha : DES.encryptDES(fecha, token);
		        case K_NUMEROFECHA:
		        	return ( token == null ) ? numeroFecha : DES.encryptDES(Integer.toString(numeroFecha), token);
		        case K_VISITAS:
		        	return ( token == null ) ? visitas : DES.encryptDES(Long.toString(visitas), token);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 4;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_DESCRIPCIONVISITAS: info.type = PropertyInfo.STRING_CLASS; info.name = "descripcionVisitas"; break;
	        case K_FECHA: info.type = PropertyInfo.STRING_CLASS; info.name = "fecha"; break;
	        case K_NUMEROFECHA: info.type = PropertyInfo.INTEGER_CLASS; info.name = "numeroFecha"; break;
	        case K_VISITAS: info.type = PropertyInfo.LONG_CLASS; info.name = "visitas"; break;
	        default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_DESCRIPCIONVISITAS: descripcionVisitas = value.toString();break;
	        case K_FECHA: fecha = value.toString();break;
	        case K_NUMEROFECHA: numeroFecha = Integer.parseInt(value.toString());break;
	        case K_VISITAS: visitas = Long.parseLong(value.toString());break;
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
	        case K_DESCRIPCIONVISITAS: return descripcionVisitas;
	        case K_FECHA: return fecha;
	        case K_NUMEROFECHA: return numeroFecha;
	        case K_VISITAS: return visitas;
        }
        
        return null;
	}
	
	public String getDescripcionVisitas() {
		return descripcionVisitas;
	}
	public void setDescripcionVisitas(String descripcionVisitas) {
		this.descripcionVisitas = descripcionVisitas;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getNumeroFecha() {
		return numeroFecha;
	}
	public void setNumeroFecha(int numeroFecha) {
		this.numeroFecha = numeroFecha;
	}
	public long getVisitas() {
		return visitas;
	}
	public void setVisitas(long visitas) {
		this.visitas = visitas;
	}

}

package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class WS_RespuestaVO implements KvmSerializable
{
	private static final int K_RESULTADO = 0;
	private static final int K_TOKEN = 1;
	private static final int K_STATUS = 2;
	
	private String resultado;
	private String token;
	private WsInfomovilProcessStatus status;
	
	public WS_RespuestaVO()
	{
		status = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
	}
	
	public WS_RespuestaVO(WS_RespuestaVO resp)
	{
		this.resultado	= new String(resp.getResultado());
		this.token		= new String(resp.getToken());
		this.status		= resp.getStatus();
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
			String StrToken				= datosUsuario.getToken()==null?"":datosUsuario.getToken();
			
	        switch(arg0)
	        {
		        case K_RESULTADO:
		        	return  ( StrToken == null ) ? resultado : DES.encryptDES(resultado, StrToken);
		        case K_TOKEN:
		        	return  ( StrToken == null ) ? token : DES.encryptDES(token, StrToken);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 2;
	}
	
	
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
	    switch(index)
	    {
		    case K_RESULTADO:
		    	info.type = PropertyInfo.STRING_CLASS;
		    	info.name = "resultado";
		    	break;
		    case K_TOKEN:
		    	info.type = PropertyInfo.STRING_CLASS;
		    	info.name = "token";
		    	break;
	        default:break;
	    }
	}
	
	@Override
	public void setProperty(int index, Object value)
	{
	    switch(index)
	    {
		    case K_RESULTADO: resultado = value.toString();break;
		    case K_TOKEN: token = value.toString();break;
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
	    case K_RESULTADO: return resultado;
	    case K_TOKEN: return token;
	    case K_STATUS: return status;
	    }
	    
	    return null;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public WsInfomovilProcessStatus getStatus() {
		return status;
	}

	public void setStatus(WsInfomovilProcessStatus status) {
		this.status = status;
	}

}

package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

import android.util.Base64;

public class WS_ImagenVO implements KvmSerializable
{
	private static final int K_TYPEIMAGE		 = 0;
	private static final int K_DESCRIPCIONIMAGEN = 1;
	private static final int K_IDIMAGEN			 = 2;
	private static final int K_IMAGENCLOBGALERIA = 3;
	private static final int K_URL = 4;

	private String typeImage;
	private String descripcionImagen;
	private int idImagen;
	private String imagenClobGaleria;
	private String imagenPath; // TODO a partir del path crear el clob
	private String url;
	
	
	public WS_ImagenVO()
	{
		this.typeImage = "IMAGEN";
		this.descripcionImagen = " ";
	}
	
	public WS_ImagenVO(WS_ImagenVO imagenVO)
	{
		this.typeImage			= (imagenVO.getTypeImage() != null ) ? new String(imagenVO.getTypeImage()) : "";
		this.descripcionImagen	= (imagenVO.getDescripcionImagen() != null ) ? new String(imagenVO.getDescripcionImagen()) : "";
		this.idImagen			= imagenVO.getIdImagen();
		this.imagenClobGaleria	= (imagenVO.getImagenClobGaleria() != null ) ? new String(imagenVO.getImagenClobGaleria()) : "";
		this.url	= (imagenVO.getUrl() != null ) ? new String(imagenVO.getUrl()) : "";
		this.imagenPath			= (imagenVO.getImagenPath() != null ) ? new String(imagenVO.getImagenPath()) : "";
	}
	
	@Override
	public String toString()
	{
		int max = getPropertyCount();
		String str = "{";
		PropertyInfo info = new PropertyInfo();
		
		for ( int i = 0; i<max; i++ )
		{
			if ( i == K_IMAGENCLOBGALERIA )
				continue;
			
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
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			String token			  = datosUsuario.getToken();
	        switch(arg0)
	        {
		        case K_TYPEIMAGE: 
		        	return ( token == null ) ? typeImage : DES.encryptDES(typeImage, token);
		        case K_DESCRIPCIONIMAGEN:
		        	return ( token == null ) ? descripcionImagen : DES.encryptDES(descripcionImagen, token);
		        case K_IDIMAGEN:
		        	return ( token == null ) ? idImagen : DES.encryptDES(Integer.toString(idImagen), token);
		        case K_IMAGENCLOBGALERIA:
		        	// TODO si no hay informacion, crear a traves del path
		        	if ( imagenClobGaleria != null )
		        		return imagenClobGaleria;
		        	
		        	if ( imagenPath == null )
		        		return null;
		        	
		        	String imagenBase64 = null;
		        	//generar base64 de imagen
	        		try
	        		{
	        			File f = new File(imagenPath);
	        			imagenBase64 = Base64.encodeToString(read(f),Base64.DEFAULT);
	      	  		}
	        		catch (FileNotFoundException e) 
	    		    {
	    	 	       e.printStackTrace();
	    	    	}
	        		imagenClobGaleria = imagenBase64;
	        		return imagenBase64;
		        case K_URL:
		        	return ( token == null ) ? url : DES.encryptDES(url, token);
	        		
	        		
	        }
		} catch (Exception e) {
			System.out.println(""+e.getLocalizedMessage());
		}
        
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 5;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_TYPEIMAGE: info.type = PropertyInfo.STRING_CLASS; info.name = "typeImage"; break;
	        case K_DESCRIPCIONIMAGEN: info.type = PropertyInfo.STRING_CLASS; info.name = "descripcionImagen"; break;
	        case K_IDIMAGEN: info.type = PropertyInfo.INTEGER_CLASS; info.name = "idImagen"; break;
	        case K_IMAGENCLOBGALERIA: info.type = PropertyInfo.STRING_CLASS; info.name = "imagenClobGaleria"; break;
	        case K_URL: info.type = PropertyInfo.STRING_CLASS; info.name = "url"; break;
			default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_TYPEIMAGE: typeImage = value.toString();break;
	        case K_DESCRIPCIONIMAGEN: descripcionImagen = value.toString();break;
	        case K_IDIMAGEN: idImagen = Integer.parseInt(value.toString());break;
	        case K_IMAGENCLOBGALERIA: imagenClobGaleria = value.toString();break;
	        case K_URL: url = value.toString();break;
			default:break;
        }
	}
	
	//***********************************************//
	public byte[] read(File file) throws IOException
	{
	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ( (read = ios.read(buffer)) != -1 ) {
	            ous.write(buffer, 0, read);
	        }
	    } finally { 
	        try {
	             if ( ous != null ) 
	                 ous.close();
	        } catch ( IOException e) {
	        }

	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        }
	    }
	    return ous.toByteArray();
	}
	
	//**********************************************//
	// 				Getters y Setters				//
	//**********************************************//
	public Object getPropertyValue(int arg0)
	{
        switch(arg0)
        {
	        case K_TYPEIMAGE: 
	        	return typeImage;
	        case K_DESCRIPCIONIMAGEN:
	        	return descripcionImagen;
	        case K_IDIMAGEN:
	        	return idImagen;
	        case K_IMAGENCLOBGALERIA:
	        	return imagenClobGaleria;
	        case K_URL: 
	        	return url;
        }        
        return null;
	}
	
	public String getTypeImage() {
		return typeImage;
	}
	public void setTypeImage(String typeImage) {
		this.typeImage = typeImage;
	}
	public String getDescripcionImagen() {
		return descripcionImagen;
	}
	public void setDescripcionImagen(String descripcionImagen) {
		this.descripcionImagen = descripcionImagen;
	}
	public int getIdImagen() {
		return idImagen;
	}
	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}
	public String getImagenClobGaleria() {
		return imagenClobGaleria;
	}
	public void setImagenClobGaleria(String imagenClobGaleria) {
		this.imagenClobGaleria = imagenClobGaleria;
	}
	public String getImagenPath() {
		return imagenPath;
	}
	public void setImagenPath(String imagenPath) {
		this.imagenPath = imagenPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idImagen;
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
		WS_ImagenVO other = (WS_ImagenVO) obj;
		if (idImagen != other.idImagen)
			return false;
		return true;
	}
}

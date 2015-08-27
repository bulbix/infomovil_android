package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_DomainVO implements KvmSerializable
{
	private static final int  K_BACKGROUNDIMAGE	= 0;
	private static final int  K_COLOUR			= 1;
	private static final int  K_DISPLAYSTRING	= 2;
	private static final int  K_TEXTRECORD		= 3;
	private static final int  K_DOMAINNAME		= 4;
	private static final int  K_TEXTDOMAIN		= 5;
	private static final int  K_TEMPLATE		= 6;
	
	private String backGroundImage;
	private String colour; 			// fondo
	private String template;
	private String displayString;	// descripcion
	private String textRecord;		// nombre de la empresa
	private String domainName;		// nombre del dominio
	private String textDomain;		// se manda vacio ( Revisa con geulis )
		
	public WS_DomainVO()
	{
		backGroundImage = " ";
		colour = " ";
		template = "";
		displayString = " ";
		textRecord = " ";
		domainName = " ";
		textDomain = " ";
	}
	
	public WS_DomainVO(WS_DomainVO domain)
	{
		this.backGroundImage = (domain.getBackGroundImage() != null ) ? new String(domain.getBackGroundImage()) : "";
		this.colour			 = (domain.getColour() != null ) ? new String(domain.getColour()) : "";
		this.template		 = (domain.getTemplate() != null ) ? new String(domain.getTemplate()) : "";
		this.displayString	 = (domain.getDisplayString() != null ) ? new String(domain.getDisplayString()) : "";
		this.textRecord		 = (domain.getTextRecord() != null ) ? new String(domain.getTextRecord()) : "";
		this.domainName		 = (domain.getDomainName() != null ) ? new String(domain.getDomainName()) : "";
		this.textDomain		 = (domain.getTextDomain() != null ) ? new String(domain.getTextDomain()) : "";

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
			str = str + "["+info.name+":"+getPropertyValue(i)+"]";
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
		        case K_BACKGROUNDIMAGE:
		        	return ( token == null ) ? backGroundImage : DES.encryptDES(backGroundImage, token);
		        case K_COLOUR:
		        	return ( token == null ) ? colour : DES.encryptDES(colour, token);
		        case K_DISPLAYSTRING:
		        	return ( token == null ) ? displayString : DES.encryptDES(displayString, token);
			    case K_TEXTRECORD:
			    	return ( token == null ) ? textRecord : DES.encryptDES(textRecord, token);
			    case K_DOMAINNAME:
			    	return ( token == null ) ? domainName : DES.encryptDES(domainName, token);
			    case K_TEXTDOMAIN:
			    	return ( token == null ) ? textDomain : DES.encryptDES(textDomain, token);
			    case K_TEMPLATE:
			    	return ( token == null ) ? template : DES.encryptDES(template, token);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}

	@Override
	public int getPropertyCount() {
		return 7;
	}

	
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_BACKGROUNDIMAGE:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "backGroundImage";
	            break;
	        case K_COLOUR:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "colour";
	            break;
	        case K_DISPLAYSTRING:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "displayString";
	            break;
	        case K_TEXTRECORD:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "textRecord";
	            break;
	        case K_DOMAINNAME:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "domainName";
	            break;
	        case K_TEXTDOMAIN:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "textDomain";
	            break;
	        case K_TEMPLATE:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "template";
	            break;
	        default:break;
        }
	}

	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_BACKGROUNDIMAGE:
	            backGroundImage = value.toString();
	            break;
	        case K_COLOUR:
	            colour = value.toString();
	            break;
	        case K_DISPLAYSTRING:
	            displayString = value.toString();
	            break;
	        case K_TEXTRECORD:
	            textRecord = value.toString();
	            break;
	        case K_DOMAINNAME:
	            domainName = value.toString();
	            break;
	        case K_TEXTDOMAIN:
	        	textDomain = value.toString();
	            break;
	        case K_TEMPLATE:
	        	template = value.toString();
	            break;
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
	        case K_BACKGROUNDIMAGE: return backGroundImage;
	        case K_COLOUR: return colour;
	        case K_DISPLAYSTRING: return displayString;
		    case K_TEXTRECORD: return textRecord;
		    case K_DOMAINNAME: return domainName;
		    case K_TEXTDOMAIN: return textDomain;
		    case K_TEMPLATE: return template;
        }
        
        return null;
	}
	
	public String getBackGroundImage() {
		return backGroundImage;
	}
	public void setBackGroundImage(String backGroundImage) {
		this.backGroundImage = backGroundImage;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public String getDisplayString() {
		return displayString!=null?displayString.trim():null;
	}
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
	public String getTextRecord() {
		return textRecord!=null?textRecord.trim():null;
	}
	public void setTextRecord(String textRecord) {
		this.textRecord = textRecord;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getTextDomain() {
		return textDomain;
	}
	public void setTextDomain(String textDomain) {
		this.textDomain = textDomain;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}

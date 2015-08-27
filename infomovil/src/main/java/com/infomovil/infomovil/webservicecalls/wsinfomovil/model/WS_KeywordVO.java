package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_KeywordVO implements KvmSerializable
{
	private static final int K_IDKEYWORD	 = 0;
	private static final int K_KEYWORDFIELD	 = 1;
	private static final int K_KEYWORDPOS	 = 2;
	private static final int K_KEYWORDVALUE	 = 3;

	private int idKeyword;
	private String keywordField;
	private int keywordPos;
	private String keywordValue;
	
	public WS_KeywordVO ()
	{
		
	}
	
	public WS_KeywordVO (String keywordField, String keywordValue){
		this.keywordField = keywordField;
		this.keywordValue = keywordValue;
	}
	
	public WS_KeywordVO (WS_KeywordVO keywordVO)
	{
		this.idKeyword		= keywordVO.getIdKeyword();
		this.keywordField	= (keywordVO.getKeywordField() != null) ? new String(keywordVO.getKeywordField()) : "";
		this.keywordPos		= keywordVO.getKeywordPos();
		this.keywordValue	= (keywordVO.getKeywordValue() != null) ? new String(keywordVO.getKeywordValue()) : "";
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
			switch (arg0)
			{
				case K_IDKEYWORD:
					return ( token == null ) ? idKeyword : DES.encryptDES(Integer.toString(idKeyword), token);
				case K_KEYWORDFIELD:
					return ( token == null ) ? keywordField : DES.encryptDES(keywordField, token);
				case K_KEYWORDPOS:
					return ( token == null ) ? keywordPos : DES.encryptDES(Integer.toString(keywordPos), token);
				case K_KEYWORDVALUE:
					return ( token == null ) ? keywordValue : DES.encryptDES(keywordValue, token);
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
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
	{
		switch (arg0)
		{
			case K_IDKEYWORD: info.type = PropertyInfo.INTEGER_CLASS; info.name = "idKeyword"; break;
			case K_KEYWORDFIELD: info.type = PropertyInfo.STRING_CLASS; info.name = "keywordField"; break;
			case K_KEYWORDPOS: info.type = PropertyInfo.INTEGER_CLASS; info.name = "keywordPos"; break;
			case K_KEYWORDVALUE: info.type = PropertyInfo.STRING_CLASS; info.name = "keywordValue"; break;
			default:break;
		}
		
	}
	@Override
	public void setProperty(int arg0, Object value)
	{
		switch (arg0)
		{
			case K_IDKEYWORD: idKeyword = Integer.parseInt(value.toString());break;
			case K_KEYWORDFIELD: keywordField = value.toString();break;
			case K_KEYWORDPOS: keywordPos = Integer.parseInt(value.toString());break;
			case K_KEYWORDVALUE: keywordValue = value.toString();break;
			default:break;
		}
		
	}
	
	//**********************************************//
	// 				Getters y Setters				//
	//**********************************************//
	public Object getPropertyValue(int arg0)
	{
		switch (arg0)
		{
			case K_IDKEYWORD: return idKeyword;
			case K_KEYWORDFIELD: return keywordField;
			case K_KEYWORDPOS: return keywordPos;
			case K_KEYWORDVALUE: return keywordValue;
		}
		return null;
	}
	
	public int getIdKeyword() {
		return idKeyword;
	}
	public void setIdKeyword(int idKeyword) {
		this.idKeyword = idKeyword;
	}
	public String getKeywordField() {
		return keywordField;
	}
	public void setKeywordField(String keywordField) {
		this.keywordField = keywordField;
	}
	public int getKeywordPos() {
		return keywordPos;
	}
	public void setKeywordPos(int keywordPos) {
		this.keywordPos = keywordPos;
	}
	public String getKeywordValue() {
		return keywordValue!= null?keywordValue.trim():null;
	}
	public void setKeywordValue(String keywordValue) {
		this.keywordValue = keywordValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((keywordField == null) ? 0 : keywordField.hashCode());
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
		WS_KeywordVO other = (WS_KeywordVO) obj;
		if (keywordField == null) {
			if (other.keywordField != null)
				return false;
		} else if (!keywordField.equals(other.keywordField))
			return false;
		return true;
	}
}

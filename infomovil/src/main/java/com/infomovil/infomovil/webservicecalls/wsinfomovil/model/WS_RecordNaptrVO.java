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
public class WS_RecordNaptrVO implements KvmSerializable
{
	private static final int K_CATEGORYNAPTR	 = 0;
	private static final int K_CLAVECONTACTO	 = 1;
	private static final int K_LONGLABELNAPTR	 = 2;
	private static final int K_PREFERENCE		 = 3;
	private static final int K_REGEXP			 = 4;
	private static final int K_SERVICESNAPTR	 = 5;
	private static final int K_SUBCATEGORY		 = 6;
	private static final int K_VISIBLE			 = 7;

	private String categoryNaptr;
	private int claveContacto;
	private String longLabelNaptr;
	private int preference;
	private String regExp;
	private String servicesNaptr;
	private String subCategory;
	private int visible;
	private int idTipoContacto;
	private String codCountry, expAux;
	private String noContacto, pais;

	public WS_RecordNaptrVO ()
	{
		subCategory = " ";
	}
	
	public WS_RecordNaptrVO (WS_RecordNaptrVO naptrVO)
	{
		this.categoryNaptr	= (naptrVO.getCategoryNaptr() != null ) ? new String(naptrVO.getCategoryNaptr()) : "";
		this.claveContacto	= naptrVO.getClaveContacto();
		this.longLabelNaptr	= (naptrVO.getLongLabelNaptr() != null ) ? new String(naptrVO.getLongLabelNaptr()) : "";
		this.preference		= naptrVO.getPreference();
		this.regExp			= (naptrVO.getRegExp() != null ) ? new String(naptrVO.getRegExp()) : "";
		this.servicesNaptr	= (naptrVO.getServicesNaptr() != null ) ? new String(naptrVO.getServicesNaptr()) : "";
		this.subCategory	= (naptrVO.getSubCategory() != null ) ? new String(naptrVO.getSubCategory()) : "";
		this.visible		= naptrVO.getVisible();
		this.idTipoContacto	= naptrVO.getIdTipoContacto();
		this.codCountry		= (naptrVO.getCodCountry() != null ) ? new String(naptrVO.getCodCountry()) : "";
		this.expAux			= (naptrVO.getExpAux() != null ) ? new String(naptrVO.getExpAux()) : "";	
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
		        case K_CATEGORYNAPTR:
		        	return ( token == null ) ? categoryNaptr : DES.encryptDES(categoryNaptr, token);
		        case K_CLAVECONTACTO:
		        	return ( token == null ) ? claveContacto : DES.encryptDES(Integer.toString(claveContacto), token);
		        case K_LONGLABELNAPTR:
		        	return ( token == null ) ? longLabelNaptr : DES.encryptDES(longLabelNaptr, token);
		        case K_PREFERENCE:
		        	return ( token == null ) ? preference : DES.encryptDES(Integer.toString(preference), token);
		        case K_REGEXP:
		        	return ( token == null ) ? regExp : DES.encryptDES(regExp, token);
		        case K_SERVICESNAPTR:
		        	return ( token == null ) ? servicesNaptr : DES.encryptDES(servicesNaptr, token);
		        case K_SUBCATEGORY:
		        	return ( token == null ) ? ( subCategory != null ) ? subCategory : " " : DES.encryptDES(subCategory, token);
		        case K_VISIBLE:
		        	return ( token == null ) ? visible : DES.encryptDES(Integer.toString(visible), token);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 8;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_CATEGORYNAPTR: info.type = PropertyInfo.STRING_CLASS; info.name = "categoryNaptr"; break;
	        case K_CLAVECONTACTO: info.type = PropertyInfo.INTEGER_CLASS; info.name = "claveContacto"; break;
	        case K_LONGLABELNAPTR: info.type = PropertyInfo.STRING_CLASS; info.name = "longLabelNaptr"; break;
	        case K_PREFERENCE: info.type = PropertyInfo.INTEGER_CLASS; info.name = "preference"; break;
	        case K_REGEXP: info.type = PropertyInfo.STRING_CLASS; info.name = "regExp"; break;
	        case K_SERVICESNAPTR: info.type = PropertyInfo.STRING_CLASS; info.name = "servicesNaptr"; break;
	        case K_SUBCATEGORY: info.type = PropertyInfo.STRING_CLASS; info.name = "subCategory"; break;
	        case K_VISIBLE: info.type = PropertyInfo.INTEGER_CLASS; info.name = "visible"; break;
			default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_CATEGORYNAPTR: categoryNaptr = value.toString();break;
	        case K_CLAVECONTACTO: claveContacto = Integer.parseInt(value.toString());break;
	        case K_LONGLABELNAPTR: longLabelNaptr = value.toString();break;
	        case K_PREFERENCE: preference = Integer.parseInt(value.toString());break;
	        case K_REGEXP: regExp = value.toString();break;
	        case K_SERVICESNAPTR: servicesNaptr = value.toString();break;
	        case K_SUBCATEGORY: subCategory = value.toString();break;
	        case K_VISIBLE: visible = Integer.parseInt(value.toString());break;
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
	        case K_CATEGORYNAPTR: return categoryNaptr;
	        case K_CLAVECONTACTO: return claveContacto;
	        case K_LONGLABELNAPTR: return longLabelNaptr;
	        case K_PREFERENCE: return preference;
	        case K_REGEXP: return regExp;
	        case K_SERVICESNAPTR: return servicesNaptr;
	        case K_SUBCATEGORY: return subCategory;
	        case K_VISIBLE: return visible;
        }
        
        return null;
	}
	
	public String getCategoryNaptr() {
		return categoryNaptr;
	}
	public void setCategoryNaptr(String categoryNaptr) {
		this.categoryNaptr = categoryNaptr;
	}
	public int getClaveContacto() {
		return claveContacto;
	}
	public void setClaveContacto(int claveContacto) {
		this.claveContacto = claveContacto;
	}
	public String getLongLabelNaptr() {
		return longLabelNaptr;
	}
	public void setLongLabelNaptr(String longLabelNaptr) {
		this.longLabelNaptr = longLabelNaptr;
	}
	public int getPreference() {
		return preference;
	}
	public void setPreference(int preference) {
		this.preference = preference;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public String getServicesNaptr() {
		return servicesNaptr;
	}
	public void setServicesNaptr(String servicesNaptr) {
		this.servicesNaptr = servicesNaptr;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public String getExpAux() {
		return expAux;
	}
	public void setExpAux(String expAux) {
		this.expAux = expAux;
	}
	public String getCodCountry() {
		return codCountry;
	}
	public void setCodCountry(String codCountry) {
		this.codCountry = codCountry;
	}
	public int getIdTipoContacto() {
		return idTipoContacto;
	}
	public void setIdTipoContacto(int idTipoContacto) {
		this.idTipoContacto = idTipoContacto;
	}

	public String getNoContacto() {
		return noContacto;
	}

	public void setNoContacto(String noTelefonico) {
		this.noContacto = noTelefonico;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
}

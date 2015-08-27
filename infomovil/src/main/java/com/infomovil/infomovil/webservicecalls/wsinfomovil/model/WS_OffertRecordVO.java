package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;

public class WS_OffertRecordVO implements KvmSerializable
{
	private static final int K_TITLEOFFER		 = 0;
	private static final int K_DESCOFFER		 = 1;
	private static final int K_TERMSOFFER		 = 2;
	private static final int K_IMAGECLOBOFFER	 = 3;
	private static final int K_LINKOFFER		 = 4;
	private static final int K_ENDDATEOFFER		 = 5;
	private static final int K_PROMOCODEOFFER	 = 6;
	private static final int K_DISCOUNTOFFER	 = 7;
	private static final int K_REDEEMOFFER		 = 8;
	private static final int K_IDOFFER			 = 9;
	private static final int K_URLIMAGE			 = 10;

	private String titleOffer;
	private String descOffer;
	private String termsOffer;
	private String imageClobOffer;
	private String linkOffer;
	private String endDateOffer;
	private String promoCodeOffer;
	private int discountOffer;
	private String redeemOffer;
	private int idOffer;
	private String pathImageOffer;
	private String urlImage;
	
	public WS_OffertRecordVO ()
	{
		titleOffer = " ";
		descOffer = " ";
		termsOffer = " ";
		imageClobOffer = " ";;
		linkOffer = " ";;
		endDateOffer = "01/01/1970";;
		promoCodeOffer = " ";;
		redeemOffer = " ";;
	}
	
	public WS_OffertRecordVO (WS_OffertRecordVO offertRecordVO)
	{
		this.titleOffer		= new String(offertRecordVO.getTitleOffer());
		this.descOffer		= new String(offertRecordVO.getDescOffer());
		this.termsOffer		= new String(offertRecordVO.getTermsOffer());
		this.imageClobOffer	= new String(offertRecordVO.getImageClobOffer());
		this.linkOffer		= new String(offertRecordVO.getLinkOffer());
		this.endDateOffer	= new String(offertRecordVO.getEndDateOffer());
		this.promoCodeOffer	= new String(offertRecordVO.getPromoCodeOffer());
		this.discountOffer	= offertRecordVO.getDiscountOffer();
		this.redeemOffer	= new String(offertRecordVO.getRedeemOffer());
		this.idOffer		= offertRecordVO.getIdOffer();
		this.urlImage		= offertRecordVO.getUrlImage();
	}
	
	@Override
	public String toString()
	{
		int max = getPropertyCount();
		String str = "{";
		PropertyInfo info = new PropertyInfo();
		
		for ( int i = 0; i<max; i++ )
		{
			if ( i == K_IMAGECLOBOFFER )
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
			DatosUsuario datosUsuario	= DatosUsuario.getInstance();
			String token				= datosUsuario.getToken();
	        switch(arg0)
	        {
		        case K_TITLEOFFER:
		        	return ( token == null ) ? titleOffer : DES.encryptDES(titleOffer, token);
		        case K_DESCOFFER:
		        	return ( token == null ) ? descOffer : DES.encryptDES(descOffer, token);
		        case K_TERMSOFFER:
		        	return ( token == null ) ? termsOffer : DES.encryptDES(termsOffer, token);
		        case K_IMAGECLOBOFFER:
		        	return (imageClobOffer == null || imageClobOffer.length() < 10) ? null:imageClobOffer;
		        case K_LINKOFFER:
		        	return ( token == null ) ? linkOffer : DES.encryptDES(linkOffer, token);
		        case K_ENDDATEOFFER:
		        	return ( token == null ) ? endDateOffer : DES.encryptDES(endDateOffer, token);
		        case K_PROMOCODEOFFER:
		        	return ( token == null ) ? promoCodeOffer : DES.encryptDES(promoCodeOffer, token);
		        case K_DISCOUNTOFFER:
		        	return ( token == null ) ? discountOffer : DES.encryptDES(Integer.toString(discountOffer), token);
		        case K_REDEEMOFFER:
		        	return ( token == null ) ? redeemOffer : DES.encryptDES(redeemOffer, token);
		        case K_IDOFFER:
		        	return ( token == null ) ? idOffer : DES.encryptDES(Integer.toString(idOffer), token);
		        case K_URLIMAGE:
		        	return ( token == null ) ? urlImage : DES.encryptDES(urlImage, token);
	        }
		} catch (Exception e) {
			//e.printStackTrace();
		}
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 11;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_TITLEOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "titleOffer"; break;
	        case K_DESCOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "descOffer"; break;
	        case K_TERMSOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "termsOffer"; break;
	        case K_IMAGECLOBOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "imageClobOffer"; break;
	        case K_LINKOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "linkOffer"; break;
	        case K_ENDDATEOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "endDateOffer"; break;
	        case K_PROMOCODEOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "promoCodeOffer"; break;
	        case K_DISCOUNTOFFER: info.type = PropertyInfo.INTEGER_CLASS; info.name = "discountOffer"; break;
	        case K_REDEEMOFFER: info.type = PropertyInfo.STRING_CLASS; info.name = "redeemOffer"; break;
	        case K_IDOFFER: info.type = PropertyInfo.INTEGER_CLASS; info.name = "idOffer"; break;
	        case K_URLIMAGE: info.type = PropertyInfo.STRING_CLASS; info.name = "urlImage"; break;
			default:break;
        }
	}
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_TITLEOFFER: titleOffer = value.toString();break;
	        case K_DESCOFFER: descOffer = value.toString();break;
	        case K_TERMSOFFER: termsOffer = value.toString();break;
	        case K_IMAGECLOBOFFER: imageClobOffer = value.toString();break;
	        case K_LINKOFFER: linkOffer = value.toString();break;
	        case K_ENDDATEOFFER: endDateOffer = value.toString();break;
	        case K_PROMOCODEOFFER: promoCodeOffer = value.toString();break;
	        case K_DISCOUNTOFFER: discountOffer = Integer.parseInt(value.toString());break;
	        case K_REDEEMOFFER: redeemOffer = value.toString();break;
	        case K_IDOFFER: idOffer = Integer.parseInt(value.toString());break;
	        case K_URLIMAGE: urlImage = value.toString();break;
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
	        case K_TITLEOFFER: return titleOffer;
	        case K_DESCOFFER: return descOffer;
	        case K_TERMSOFFER: return termsOffer;
	        case K_IMAGECLOBOFFER: return imageClobOffer;
	        case K_LINKOFFER: return linkOffer;
	        case K_ENDDATEOFFER: return endDateOffer;
	        case K_PROMOCODEOFFER: return promoCodeOffer;
	        case K_DISCOUNTOFFER: return discountOffer;
	        case K_REDEEMOFFER: return redeemOffer;
	        case K_IDOFFER: return idOffer;
	        case K_URLIMAGE: return urlImage;
        }
        
        return null;
	}
	
	public String getTitleOffer() {
		return titleOffer!=null?titleOffer.trim():"";
	}
	public void setTitleOffer(String titleOffer) {
		this.titleOffer = titleOffer;
	}
	public String getDescOffer() {
		return descOffer!=null?descOffer.trim():"";
	}
	public void setDescOffer(String descOffer) {
		this.descOffer = descOffer;
	}
	public String getTermsOffer() {
		return termsOffer!=null?termsOffer.trim():"";
	}
	public void setTermsOffer(String termsOffer) {
		this.termsOffer = termsOffer;
	}
	public String getImageClobOffer() {
		return imageClobOffer;
	}
	public void setImageClobOffer(String imageClobOffer) {
		this.imageClobOffer = imageClobOffer;
	}
	public String getLinkOffer() {
		return linkOffer;
	}
	public void setLinkOffer(String linkOffer) {
		this.linkOffer = linkOffer;
	}
	public String getEndDateOffer() {
		return endDateOffer!=null?endDateOffer.trim():"";
	}
	public void setEndDateOffer(String endDateOffer) {
		this.endDateOffer = endDateOffer;
	}
	public String getPromoCodeOffer() {
		return promoCodeOffer;
	}
	public void setPromoCodeOffer(String promoCodeOffer) {
		this.promoCodeOffer = promoCodeOffer;
	}
	public int getDiscountOffer() {
		return discountOffer;
	}
	public void setDiscountOffer(int discountOffer) {
		this.discountOffer = discountOffer;
	}
	public String getRedeemOffer() {
		return redeemOffer;
	}
	public void setRedeemOffer(String redeemOffer) {
		this.redeemOffer = redeemOffer;
	}
	public int getIdOffer() {
		return idOffer;
	}
	public void setIdOffer(int idOffer) {
		this.idOffer = idOffer;
	}

	public String getPathImageOffer() {
		return pathImageOffer;
	}

	public void setPathImageOffer(String pathImageOffer) {
		this.pathImageOffer = pathImageOffer;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
}

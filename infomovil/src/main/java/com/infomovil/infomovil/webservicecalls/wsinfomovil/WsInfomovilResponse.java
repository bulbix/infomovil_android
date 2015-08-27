package com.infomovil.infomovil.webservicecalls.wsinfomovil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.ksoap2.serialization.NullSoapObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.util.Log;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ReadFilesUtils;
import com.infomovil.infomovil.common.utils.StringUtils;
import com.infomovil.infomovil.model.Catalogo;
import com.infomovil.infomovil.model.CompraModel;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.MarshalDatosUsuario;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.util.DES;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DeleteItem;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DominioVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_InfoUsuarioVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_LocalizacionVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_OffertRecordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RespuestaVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_VisitaVO;

/**
 * @author Ignaki Dominguez
 *
 */
public class WsInfomovilResponse
{
	private String[] clavesDireccion = {"a1","a2","a3","c","pc","sp","tc"};
	private String[] clavesPerfil = {"pos","sas","oh","pm","tpa","mnu","np"};
	
	public WsInfomovilProcessStatus processGetExistDomainResponse(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		return resp.getStatus();
//		return WsInfomovilProcessStatus.SIN_EXITO;
	}
	
	public WsInfomovilProcessStatus processGetExistUserResponse(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processGetExistCampaniaByMailResponse(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processActiveSessionResponse(SoapObject resultsRequestSOAP) {
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processCloseSessionResponse(SoapObject resultsRequestSOAP) {
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
//		return resp.getStatus();
		return WsInfomovilProcessStatus.EXITO;
	}
	
	public WsInfomovilProcessStatus processGetStatusDominio(SoapObject resultsRequestSOAP) {
		WsInfomovilProcessStatus result;
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
//		WS_DominioVO dominioVO = datosUsuario.get;
//		WsInfomovilProcessStatus resp = procesaDominioVO((SoapObject)resultsRequestSOAP.getProperty("DominioVO"), dominioVO);
		SoapObject dominioVO = (SoapObject)resultsRequestSOAP.getProperty("DominioVO");
		try {
			String token = DES.decryptDES(dominioVO.getPropertySafelyAsString("token"),"");
//			String fechaIniTel = getSafeElement(dominioVO.getPropertyAsString("fTelNamesIni"), token);
//			String fechaFinTel = getSafeElement(dominioVO.getPropertySafelyAsString("fTelNamesFin"),token);
			String statusDomain = getSafeElement(dominioVO.getPropertySafelyAsString("statusDominio"),token);
			
			Vector<WS_UsuarioDominiosVO> lstUsuarioDominios = null;
			
			int max =  dominioVO.getPropertyCount();
			PropertyInfo proInfo = new PropertyInfo();
			for ( int i = 0; i<max; i++ ) {
				dominioVO.getPropertyInfo(i, proInfo);
				if ( proInfo.getName().equalsIgnoreCase("listUsuarioDominiosVO") ) {
					if ( lstUsuarioDominios == null ){
						lstUsuarioDominios = new Vector<WS_UsuarioDominiosVO>(15,3);
					}
					lstUsuarioDominios.add(procesaUsuarioDominiosVO((SoapObject)proInfo.getValue(),token));
				}
			}
			
//			datosUsuario.setFechaInicioTel(fechaIniTel);
//			datosUsuario.setFechaFinTel(fechaFinTel);
			datosUsuario.setStatusDominio(statusDomain);
			datosUsuario.setUsuarioDominios(lstUsuarioDominios);
			
			MarshalDatosUsuario.setDominioFechas();
			
			result = WsInfomovilProcessStatus.EXITO;
		}
		catch (Exception e) {
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		
		return result;
	}
	
	/**
	 * @param resultsRequestSOAP
	 * @return
	 * @throws WsInfomovilException
	 */
	public WsInfomovilProcessStatus processInsertUserDomainResponse(SoapObject resultsRequestSOAP, 
			WS_UserDomainVO usrDomainVO, WSInfomovilMethods metodo, Activity activity)
	{
		WsInfomovilProcessStatus result = null;
		long intResultado = -1;
		try
		{
			SoapObject respuestaVO = (SoapObject)resultsRequestSOAP.getProperty("RespuestaVO");
			String token = DES.decryptDES(respuestaVO.getPropertySafelyAsString("token"),"");
			if ( token == null || token.isEmpty() )
				throw new WsInfomovilException("Error de token");			
			
			String strResultado = getSafeElement(respuestaVO.getPropertySafelyAsString("resultado"),token);
			
			String fechaIni = getSafeElement(respuestaVO.getPropertySafelyAsString("fechaIni"),token);
			String fechaFin = getSafeElement(respuestaVO.getPropertySafelyAsString("fechaFin"),token);
			
//			String fechaIniTel = getSafeElement(respuestaVO.getPropertySafelyAsString("fTelNamesIni"),token);
//			String fechaFinTel = getSafeElement(respuestaVO.getPropertySafelyAsString("fTelNamesFin"),token);
			String codeCamp = getSafeElement(respuestaVO.getPropertySafelyAsString("codeCamp"),token);
			
			
			if ( strResultado != null && !strResultado.isEmpty() ){
				intResultado = Long.parseLong(strResultado);

			}
			
			if(usrDomainVO.getAccion().equalsIgnoreCase("publicar")){
				if ( intResultado > 0 )
					result = WsInfomovilProcessStatus.EXITO;				
			}
			else if(usrDomainVO.getAccion().equalsIgnoreCase("registrar")){
				InfomovilApp.tipoInfomovil="recurso"; //Cuando es registro recurso por default
				
				if ( intResultado > 0 )
					result = WsInfomovilProcessStatus.EXITO;
			}
			else if(usrDomainVO.getAccion().equalsIgnoreCase("redimir")){
				
				String codeError = getSafeElement(respuestaVO.getPropertySafelyAsString("codeError"),token);			
				
				if(Integer.parseInt(codeError) == 0)
					result = WsInfomovilProcessStatus.EXITO;				
				else
					result = WsInfomovilProcessStatus.byValue(Integer.valueOf(codeError));
				
				if(metodo == WSInfomovilMethods.INSERT_USER_CAMP && result != WsInfomovilProcessStatus.EXITO){
					return result; 
				}
				
			}
			
			Log.d("infoLog", "el Resultado es " + intResultado);			
			
			String statusDominio = getSafeElement(respuestaVO.getPropertySafelyAsString("statusDominio"),token);
			
			Vector<WS_StatusDomainVO> lstStatusDomain	= null;
			
			int max =  respuestaVO.getPropertyCount();			
			PropertyInfo proInfo = new PropertyInfo();
			
			for ( int i = 0; i<max; i++ ) {
				respuestaVO.getPropertyInfo(i, proInfo);
				if ( proInfo.getName().equalsIgnoreCase("listStatusDomainVO") ) {
					if ( lstStatusDomain == null ) {
						lstStatusDomain = new Vector<WS_StatusDomainVO>(15,3);						
					}
					lstStatusDomain.add(procesaStatusDomainVO((SoapObject)proInfo.getValue(),token));
				}				
			}
			
			Vector<WS_UsuarioDominiosVO> lstUsuarioDominios = null;
			
			max =  respuestaVO.getPropertyCount();
			proInfo = new PropertyInfo();
			for ( int i = 0; i<max; i++ ) {
				respuestaVO.getPropertyInfo(i, proInfo);
				if ( proInfo.getName().equalsIgnoreCase("listUsuarioDominiosVO") ) {
					if ( lstUsuarioDominios == null ){
						lstUsuarioDominios = new Vector<WS_UsuarioDominiosVO>(15,3);
					}
					lstUsuarioDominios.add(procesaUsuarioDominiosVO((SoapObject)proInfo.getValue(),token));
				}
			}
			
			
			if ( intResultado > 0 ) {
				DatosUsuario datosUsr = DatosUsuario.getInstance();
				datosUsr.setDomainid(intResultado);
				datosUsr.setNombreUsuario(usrDomainVO.getEmail());
				datosUsr.setPassword(usrDomainVO.getPassword());
			
//				if(metodo == WSInfomovilMethods.INSERT_USER_DOMAIN){				
				datosUsr.setFechaInicio(fechaIni);
				datosUsr.setFechaFin(fechaFin);
				
//				datosUsr.setFechaInicioTel(fechaIniTel);
//				datosUsr.setFechaFinTel(fechaFinTel);
				//}				
			
				datosUsr.setToken(token);
				datosUsr.setStatusDominio(statusDominio);

				if(usrDomainVO.getAccion().equalsIgnoreCase("registrar")){
					datosUsr.setCodeCamp(codeCamp);
					
					//Guardamos credenciales
					SharedPreferences prefs = activity.getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
					Editor edit = prefs.edit();
					edit.putString("email", datosUsr.getNombreUsuario());
					edit.putString("password", datosUsr.getPassword());
					edit.commit();
					
				}				
				
				if ( lstStatusDomain != null )
					datosUsr.setItemsDominio(ordenarItems(lstStatusDomain));
				
				if( lstUsuarioDominios != null){
					datosUsr.setUsuarioDominios(lstUsuarioDominios);
					MarshalDatosUsuario.setDominioFechas();
				}
				
			} 
			else if (intResultado == -3 || intResultado == -5) {
				DatosUsuario datosUsr = DatosUsuario.getInstance();
				datosUsr.setDomainid(intResultado);
				datosUsr.setNombreUsuario(usrDomainVO.getEmail());
				datosUsr.setPassword(usrDomainVO.getPassword());
				datosUsr.setToken(token);
				datosUsr.setStatusDominio("Pendiente");
				result = WsInfomovilProcessStatus.ERROR_PUBLICAR;
			}
			else if (intResultado == -4) {
				result = WsInfomovilProcessStatus.ERROR_DOM_EXISTE;
			}			
			else {
				result = WsInfomovilProcessStatus.ERROR_INSERTA_USUARIO;
			}
		} catch (Exception e) {
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		return result;
	}
	
	
	
	
	public WsInfomovilProcessStatus processCreateDomainResponse(SoapObject resultsRequestSOAP, WS_DominioVO dominioVO)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			dominioVO.setToken(resp.getToken());
			MarshalDatosUsuario.populateDatosUsuario(dominioVO);
		}
		
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processUpdateDomainResponse(SoapObject resultsRequestSOAP, WS_DomainVO domainVO)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		WsInfomovilProcessStatus result = resp.getStatus();
		
		if ( result == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			datosUsr.setDomainData(domainVO);
		}
		
		return result;
	}
	
	public WsInfomovilProcessStatus processUpdateKeyWordDataResponse(SoapObject resultsRequestSOAP, Vector<WS_KeywordVO> arrKeyword)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{		
			ArrayList<String>arrClavesDir	 = new ArrayList<String>(Arrays.asList(clavesDireccion));
			ArrayList<String>arrClavesPerfil = new ArrayList<String>(Arrays.asList(clavesPerfil));
			
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			Vector<WS_KeywordVO> listDirec = dataUsr.getArregloDireccion();
			Vector<WS_KeywordVO> listPerfi = dataUsr.getArregloPerfil();
			Vector<WS_KeywordVO> listInfAd = dataUsr.getArregloInformacionAdicional();
			
			for (WS_KeywordVO ws_KeywordVO : arrKeyword)
			{
				if ( arrClavesDir.indexOf(ws_KeywordVO.getKeywordField()) >= 0 )
					actualizaElementoKeywordEnVecto(listDirec,ws_KeywordVO);
				else if ( arrClavesPerfil.indexOf(ws_KeywordVO.getKeywordField()) >= 0 )
					actualizaElementoKeywordEnVecto(listPerfi,ws_KeywordVO);
				else
					actualizaElementoKeywordEnVecto(listInfAd,ws_KeywordVO);
			}
		}
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processUpdateRecordNaptrResponse(SoapObject resultsRequestSOAP)
	{
		Log.d("infoLog", resultsRequestSOAP.toString());
		if ( resultsRequestSOAP.getPropertySafely("RespuestaVO") instanceof NullSoapObject )
			return WsInfomovilProcessStatus.ERROR_TOKEN; 
		
		WsInfomovilProcessStatus result;
		try
		{
			WS_DominioVO dominioDataVO = new WS_DominioVO();
			
			result = procesaDominioVO((SoapObject)resultsRequestSOAP.getPropertySafely("RespuestaVO"),dominioDataVO);
			if ( result == WsInfomovilProcessStatus.EXITO )
			{
				DatosUsuario dataUsr				= DatosUsuario.getInstance();
				Vector<WS_RecordNaptrVO> naptrVOs	= dominioDataVO.getListRecordNaptrVo(); 
				if ( naptrVOs != null && naptrVOs.size() > 0 ) {
					dataUsr.setListaContactos(naptrVOs);
					Log.d("infoLog", "El tamaño es "+naptrVOs.size());  
				}
			}
		} catch (Exception e) {
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		return result;
	}
	
	public WsInfomovilProcessStatus processDeleteImageResponse(SoapObject resultsRequestSOAP, WS_DeleteItem deleteItem)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			Vector<WS_ImagenVO> listImagen = dataUsr.getListaImagenes();
			
			int i = -1;
			for (WS_ImagenVO imagenVO : listImagen)
			{
				if ( imagenVO.getIdImagen() == deleteItem.getIndexItem() )
				{
					i = listImagen.indexOf(imagenVO);
					break;
				}
					
			}
			if ( i >= 0 )
				listImagen.remove(i);
			else
				dataUsr.setImagenLogo(null);
		}
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processDeleteKeyWordDataResponse(SoapObject resultsRequestSOAP, WS_DeleteItem deleteItem)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			Vector<WS_KeywordVO> keywordVOs = dataUsr.getArregloDireccion();
			if ( borraKeyWordData(keywordVOs, deleteItem) >= 0 )
				return resp.getStatus();
			
			keywordVOs = dataUsr.getArregloPerfil();
			int index;
			if ( ( index = borraKeyWordData(keywordVOs, deleteItem) ) >= 0 )
			{
				WS_KeywordVO keywordVO = new WS_KeywordVO();
				keywordVO.setKeywordField(clavesPerfil[index]);
				keywordVOs.insertElementAt(keywordVO, index);
				return resp.getStatus();
			}
			
			keywordVOs = dataUsr.getArregloInformacionAdicional();
			if ( borraKeyWordData(keywordVOs, deleteItem) >= 0 )
				return resp.getStatus();
		}
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processDeleteRecordNaptrResponse(SoapObject resultsRequestSOAP, WS_DeleteItem deleteItem)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			Vector<WS_RecordNaptrVO> listContac = dataUsr.getListaContactos();
			
			int i = -1;
			for (WS_RecordNaptrVO naptrVO : listContac)
			{
				if ( naptrVO.getClaveContacto() == deleteItem.getIndexItem() )
				{
					i = listContac.indexOf(naptrVO);
					break;
				}
					
			}
			if ( i >= 0 )
				listContac.remove(i);
		}
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processDeleteVideoResponse(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setVideoSeleccionado(null);
		}
		
		return resp.getStatus();
	}
	public WsInfomovilProcessStatus processDeleteLocRecordResponse(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setCoordenadasUbicacion(null);
		}
		
		return resp.getStatus();
	}
	public WsInfomovilProcessStatus processDeleteOfferRecordResponse(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setPromocionDominio(null);
		}
		
		return resp.getStatus();
	}
	public WsInfomovilProcessStatus processInsertVideoResponse(SoapObject resultsRequestSOAP, ItemSelectModel modelVideo)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setVideoSeleccionado(modelVideo);
		}	
		return resp.getStatus();
	}
	public WsInfomovilProcessStatus processInsertImageResponse(SoapObject resultsRequestSOAP, WS_ImagenVO imagen) 
	{
		WsInfomovilProcessStatus result;
		try
		{
			long intResultado = procesaRespuestaVONumerico((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));
			if ( intResultado > 0 )
			{
				imagen.setIdImagen((int)intResultado);
				
				DatosUsuario datosUsr			= DatosUsuario.getInstance();
				if ( imagen.getTypeImage().equalsIgnoreCase("Imagen") )
				{
					Vector<WS_ImagenVO> lstImagen	= datosUsr.getListaImagenes();
					
					if ( lstImagen == null )
					{
						lstImagen = new Vector<WS_ImagenVO>(1);
						datosUsr.setListaImagenes(lstImagen);
					}
					lstImagen.add(imagen);
				} else
					datosUsr.setImagenLogo(imagen);
				
				result = WsInfomovilProcessStatus.EXITO;
			}
			else if (intResultado == 0) {
				result = WsInfomovilProcessStatus.ERROR_TOKEN;
			}
			else if (intResultado == -100) {
				result = WsInfomovilProcessStatus.ERROR_SESION;
			}
			else
				result = WsInfomovilProcessStatus.ERROR_INSERTA_IMAGEN;
		} catch (Exception e) {
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		return result;
	}
	
	public WsInfomovilProcessStatus processUpdateImageResponse(SoapObject resultsRequestSOAP)
	{
		if ( resultsRequestSOAP.getPropertySafely("ImagenVO") instanceof NullSoapObject )
			return WsInfomovilProcessStatus.ERROR_TOKEN;
		
		WsInfomovilProcessStatus result;
		 
		try
		{
			WS_DominioVO dominioDataVO = new WS_DominioVO();
			
			result = procesaDominioVO((SoapObject)resultsRequestSOAP.getPropertySafely("ImagenVO"),dominioDataVO);
			if ( result == WsInfomovilProcessStatus.EXITO )
			{
				Vector<WS_ImagenVO> listImagen	= dominioDataVO.getListImagenVO();
				
				if ( listImagen == null || listImagen.size() <= 0 )
					return WsInfomovilProcessStatus.ERROR_ACTUALIZA;
				
				DatosUsuario dataUsr			= DatosUsuario.getInstance();
				Vector<WS_ImagenVO> imagenVOs	= new Vector<WS_ImagenVO>();
				for (WS_ImagenVO ws_ImagenVO : listImagen)
				{
					if ( ws_ImagenVO.getTypeImage().equalsIgnoreCase("Imagen") )
						imagenVOs.add(ws_ImagenVO);
					else
						dataUsr.setImagenLogo(ws_ImagenVO);
				}
				
				if ( listImagen.size() > 0 )
					dataUsr.setListaImagenes(listImagen);
			}
		}catch (Exception e){
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		return result;
	}
	
	public WsInfomovilProcessStatus processUpdateInserLocRecordResponse(SoapObject resultsRequestSOAP, WS_LocalizacionVO localizaVO)
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setCoordenadasUbicacion(localizaVO);
		}
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processInsertRecordNaptrResponse(SoapObject resultsRequestSOAP, Vector<WS_RecordNaptrVO> naptrVOs) throws WsInfomovilException
	{
		long result = procesaRespuestaVONumerico((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));
		WsInfomovilProcessStatus resp = WsInfomovilProcessStatus.ERROR_INSERTA_REGISTRO;
		if ( result > 0 )
		{
			naptrVOs.firstElement().setClaveContacto((int)result);
			
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			Vector<WS_RecordNaptrVO>vectorNaptrVOs = dataUsr.getListaContactos();
			if ( vectorNaptrVOs == null )
				vectorNaptrVOs = new Vector<WS_RecordNaptrVO>();
			
			vectorNaptrVOs.addAll(naptrVOs);
			//TO-DO checar contactos
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			datosUsr.setListaContactos(vectorNaptrVOs);
			resp = WsInfomovilProcessStatus.EXITO;
		}
		else if (result == 0) {
			resp = WsInfomovilProcessStatus.ERROR_TOKEN;
		}
		else if (result == -100) {
			resp = WsInfomovilProcessStatus.ERROR_SESION;
		}
		return resp;
	}
	
	public WsInfomovilProcessStatus processUpdateInsertOfferRecordResponse(SoapObject resultsRequestSOAP, WS_OffertRecordVO offerRecord) throws WsInfomovilException
	{		
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));  
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			offerRecord.setIdOffer(Integer.parseInt(resp.getResultado()));
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setPromocionDominio(offerRecord);
			dataUsr.setToken(resp.getToken());
		}
		return resp.getStatus();
	}
	
	
	public WsInfomovilProcessStatus processInsertKeyWordDataResponse(SoapObject resultsRequestSOAP, Vector<WS_KeywordVO> keywordVOs)
	{
		/*WsInfomovilProcessStatus result;
		try
		{
			long resultado = procesaRespuestaVONumerico((SoapObject) resultsRequestSOAP.getProperty("RespuestaVO"));
			if ( resultado > 0 )
			{
				// TODO cuando es uno regresa el id del elemento
				keywordVOs.firstElement().setIdKeyword((int)resultado);
				insertaKeywordData(keywordVOs);
				result = WsInfomovilProcessStatus.EXITO;
				
			} else
				result = WsInfomovilProcessStatus.ERROR_INSERTA_REGISTRO;
			
		} catch (Exception e) {
			e.printStackTrace();
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		
		return result;*/
		
		return processInsertKeywordDataAddressResponse(resultsRequestSOAP, keywordVOs);
	}
	
	public WsInfomovilProcessStatus processInsertKeywordDataAddressResponse(SoapObject resultsRequestSOAP, Vector<WS_KeywordVO> arrKeyword)
	{
		Object object = resultsRequestSOAP.getProperty("DominioVO");
		if (object instanceof NullSoapObject || ((SoapObject) object).getPropertySafely("listKeywordVO") instanceof NullSoapObject ) {
			return WsInfomovilProcessStatus.ERROR_TOKEN;
		}
		WS_DominioVO dominioDataVO = new WS_DominioVO();
		WsInfomovilProcessStatus result = procesaDominioVO((SoapObject)object, dominioDataVO);
		try
		{
			if ( result == WsInfomovilProcessStatus.EXITO )
				insertaKeywordData(dominioDataVO.getListKeywordVO());
			
		} catch (Exception e) {
			e.printStackTrace();
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		
		return result;
	}

	public WsInfomovilProcessStatus processGetNumVisitasResponse(SoapObject resultsRequestSOAP, Vector<WS_VisitaVO> visitaVOs) throws WsInfomovilException
	{
		if ( resultsRequestSOAP.getPropertyCount() <= 0 )
			return WsInfomovilProcessStatus.ERROR_TOKEN;
		
		Vector<WS_VisitaVO> lstItemDom	= new Vector<WS_VisitaVO>(resultsRequestSOAP.getPropertyCount());
		
		for (int i= 0; i<resultsRequestSOAP.getPropertyCount(); i++){			
		
			Object[] result = 
			procesaVisitaVO((SoapObject)resultsRequestSOAP.getProperty(i));
			
			WsInfomovilProcessStatus status = (WsInfomovilProcessStatus)result[0];
			
			if( status == WsInfomovilProcessStatus.ERROR_TOKEN){
				return status;
			}
			else{
				WS_VisitaVO visitaTemp = (WS_VisitaVO)result[1];
				lstItemDom.add(visitaTemp);
			}
		}
		
		visitaVOs.removeAllElements();
		visitaVOs.addAll(lstItemDom);
		
		return WsInfomovilProcessStatus.EXITO;
	}
	
	public WsInfomovilProcessStatus processGetFechaExpiracionResponse(SoapObject resultsRequestSOAP) throws WsInfomovilException
	{
		WsInfomovilProcessStatus result = WsInfomovilProcessStatus.EXITO;
		long iResult = procesaRespuestaVONumerico((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO"));
		if ( iResult > 0 ) {
			
			result = WsInfomovilProcessStatus.EXITO;
		}
		else if (iResult == 0) {
			result = WsInfomovilProcessStatus.ERROR_TOKEN;
		}
		else if (iResult == -100) {
			result = WsInfomovilProcessStatus.ERROR_SESION;
		}
		else {
			result = WsInfomovilProcessStatus.ERROR_GET_FECHA_EXPIRA;
		}
		return result; 
	}
	
	public WsInfomovilProcessStatus processInsertUpdateInfoUserResponse(SoapObject resultsRequestSOAP, WS_InfoUsuarioVO infoUsr) throws WsInfomovilException
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setInfoUsr(infoUsr);
		}
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processCambioPasswordResponse(SoapObject resultsRequestSOAP) throws WsInfomovilException 
	{
		// TODO revisar cuando se meta la respuesta usuario no existe
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processCancelarCuentaResponse(SoapObject resultsRequestSOAP) throws WsInfomovilException
	{
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		
		if ( resp.getStatus() == WsInfomovilProcessStatus.EXITO )
		{
			// TODO revisar que se debe hacer al cancelar la cuenta
		}
		
		return resp.getStatus();
	}
	
	public WsInfomovilProcessStatus processGetItemsGratisDomainResponse(SoapObject resultsRequestSOAP) throws WsInfomovilException
	{
		if ( resultsRequestSOAP.getPropertyCount() <= 0 )
			return WsInfomovilProcessStatus.SIN_ELEMENTOS;
		
		Vector<WS_StatusDomainVO> lstItemDom	= new Vector<WS_StatusDomainVO>(resultsRequestSOAP.getPropertyCount());
		try
		{
			for (int i= 0; i<resultsRequestSOAP.getPropertyCount(); i++)
				lstItemDom.add(procesaStatusDomainVO((SoapObject)resultsRequestSOAP.getProperty(i)));
			
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			dataUsr.setItemsDominioGratuito(lstItemDom);
		} catch (Exception e) {
			return WsInfomovilProcessStatus.ERROR_GENERICO;
		}
		
		return WsInfomovilProcessStatus.EXITO;
	}
	
	public WsInfomovilProcessStatus processCompraDominioResponse(SoapObject resultsRequestSOAP) 
	{
		// TODO revisar como regresa la informacion
		if ( resultsRequestSOAP.getPropertyCount() <= 0 )
			return WsInfomovilProcessStatus.ERROR_COMPRA_DOMINIO;
		
		WsInfomovilProcessStatus result;
		
		try
		{
			SoapObject dominio	= (SoapObject)resultsRequestSOAP.getPropertySafely("DominioVO");
//			String status 		= dominio.getProperty("statusDominio").toString();
//			String key = dominio.getProperty("status").toString();
//			
//			if ( !status.equalsIgnoreCase("Pago") || status.trim().isEmpty() )
//				return WsInfomovilProcessStatus.ERROR_COMPRA_DOMINIO;
			
			
			WS_DominioVO dominioDataVO = new WS_DominioVO();
			
			result = procesaDominioVO(dominio,dominioDataVO);
			if ( result == WsInfomovilProcessStatus.EXITO )
			{
				DatosUsuario datosUsuario = DatosUsuario.getInstance();				
				datosUsuario.setFechaInicio(dominioDataVO.getFechaIni());
				datosUsuario.setFechaFin(dominioDataVO.getFechaFin());
//				datosUsuario.setFechaInicioTel(dominioDataVO.getFechaIniTel());
//				datosUsuario.setFechaFinTel(dominioDataVO.getFechaFinTel());
				datosUsuario.setStatusDominio(dominioDataVO.getStatusDominio());
				datosUsuario.setItemsDominio(dominioDataVO.getListStatusDomainVO());
				datosUsuario.setDescripcionDominio(dominioDataVO.getDescripcionDominio());
				datosUsuario.setUsuarioDominios(dominioDataVO.getListUsuarioDominiosVO());
				MarshalDatosUsuario.setDominioFechas();
				
				CompraModel compra = datosUsuario.getCompra();
				compra.setReferencia(dominioDataVO.getReferencia());
				compra.setPagoId(dominioDataVO.getResultado());				
				datosUsuario.setCompra(compra);				
				
			}
			
			
		} catch (Exception e){
			result = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		
		return result;
	}
	
	public WsInfomovilProcessStatus processGetDomainResponse(SoapObject resultsRequestSOAP, Activity activity) throws WsInfomovilException 
	{
		if ( resultsRequestSOAP.getPropertySafely("Domain") instanceof NullSoapObject )
			return WsInfomovilProcessStatus.ERROR_LOGIN;
		
		WsInfomovilProcessStatus result;
		WS_DominioVO dominioDataVO = new WS_DominioVO();
		
		result = procesaDominioVO((SoapObject)resultsRequestSOAP.getPropertySafely("Domain"),dominioDataVO);
		if ( result == WsInfomovilProcessStatus.EXITO ){
			
			MarshalDatosUsuario.populateDatosUsuario(dominioDataVO);
			
			//Guardamos credenciales
			SharedPreferences prefs = activity.getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
			DatosUsuario datosUsuario = DatosUsuario.getInstance();				
			Editor edit = prefs.edit();
			edit.putString("email", datosUsuario.getNombreUsuario());
			edit.putString("password", datosUsuario.getPassword());
			edit.commit();
		}
			
		
		
		return result;
	}
	
	
	//**************************************************************************************************************//
	// 									Metodos de parse de elementos genericos										//
	//**************************************************************************************************************//
	private String getSafeElement(String strOrigen)
	{
		if ( strOrigen != null && !strOrigen.isEmpty() && !strOrigen.trim().equalsIgnoreCase("anyType{}") )
			return strOrigen.trim();
		else
			return "";
	}
	
	private String getSafeElement(String strOrigen, String token)
	{
		if ( strOrigen != null && !strOrigen.isEmpty() && !strOrigen.trim().equalsIgnoreCase("anyType{}") )
		{
			try
			{
				String resp = DES.decryptDES(strOrigen,token);
				return resp;
			}catch (Exception e) {
				// simplemente regresa vacio
			}
			
		}
		return "";
	}
	
	public boolean isInteger( String input )
	{
	   try
	   {
	      Integer.parseInt( input );
	      return true;
	   }
	   catch( Exception e)
	   {
	      return false;
	   }
	}
	
	private WS_RespuestaVO procesaRespuestaVO(SoapObject resultsRequestSOAP)
	{
		WS_RespuestaVO respuestaVO = new WS_RespuestaVO();
		try
		{
			String token = DES.decryptDES(resultsRequestSOAP.getPropertySafelyAsString("token"),"");
			if ( token == null || token.isEmpty() )
			{
				respuestaVO.setStatus(WsInfomovilProcessStatus.ERROR_TOKEN);
				return respuestaVO;
			}
			respuestaVO.setToken(token);
			
			String resultado = getSafeElement(resultsRequestSOAP.getPropertySafelyAsString("resultado"),token);
			if ( resultado != null && !resultado.equalsIgnoreCase("0") 
				&& (resultado.equalsIgnoreCase("Existe") || resultado.equalsIgnoreCase("Exito") || isInteger(resultado) || resultado.startsWith("{codigo")))
			{
				respuestaVO.setResultado(resultado);
				respuestaVO.setStatus(WsInfomovilProcessStatus.EXITO);
			}
			else if (resultado != null && resultado.equalsIgnoreCase("Error de token")) {
				respuestaVO.setStatus(WsInfomovilProcessStatus.ERROR_TOKEN);
			}
			else if (resultado != null && resultado.equalsIgnoreCase("SessionTO")) {
				respuestaVO.setStatus(WsInfomovilProcessStatus.ERROR_SESION);
			}
			else
				respuestaVO.setStatus(WsInfomovilProcessStatus.SIN_EXITO);
			
		} catch (Exception e) {
			respuestaVO.setStatus(WsInfomovilProcessStatus.ERROR_DESCONOCIDO);
		}
		
		DatosUsuario.getInstance().setRespuestaVOActual(respuestaVO);
		
		return respuestaVO;
	}
	
//	private String procesaRespuestaVOGetToken(SoapObject resultsRequestSOAP)
//	{
//		String token = null;
//		try
//		{
//			token = DES.decryptDES(resultsRequestSOAP.getPropertySafelyAsString("token"),"");
//			if ( token == null || token.isEmpty() )
//				return null;
//			
//			String resultado = getSafeElement(resultsRequestSOAP.getPropertySafelyAsString("resultado"),token);
//			if ( resultado != null && (resultado.equalsIgnoreCase("Existe") || resultado.equalsIgnoreCase("Exito")) )
//				token = null;
//			
//		} catch (Exception e) {
//			token = null;
//		}
//		return token;
//	}
//	
	private long procesaRespuestaVONumerico(SoapObject resultsRequestSOAP) throws WsInfomovilException
	{
		long resultado = 0;
		try
		{
			String token = DES.decryptDES(resultsRequestSOAP.getPropertySafelyAsString("token"),"");
			if ( token == null || token.isEmpty() )
				throw new WsInfomovilException("Error de token");
			
			String strResultado = getSafeElement(resultsRequestSOAP.getPropertySafelyAsString("resultado"),token);
			if ( strResultado != null && !strResultado.isEmpty() ) {
				if (strResultado.equalsIgnoreCase("Error de token")) {
					resultado = 0;
				}
				else if (strResultado.equalsIgnoreCase("SessionTO")) {
					resultado = -100;
				}
				else {
					resultado = Long.parseLong(strResultado);
				}
			}
			
		} catch (WsInfomovilException e){
			throw e;
		} catch (NumberFormatException e) {
			resultado = -1;
		} catch (Exception e) {
			throw new WsInfomovilException("Error desconocido");
		}
		return resultado;
	}
	 
//	private WsInfomovilProcessStatus procesaRespStrResult(SoapObject resultsRequestSOAP)
//	{
//		try
//		{
//			String resultado = resultsRequestSOAP.getPropertySafelyAsString("resultado");
//			if ( resultado == null || !resultado.equalsIgnoreCase("Exito") )
//				return WsInfomovilProcessStatus.SIN_EXITO;
//			
//			return WsInfomovilProcessStatus.EXITO;
//		} catch (Exception e) {
//			return WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
//		}
//	}
	 
	private WsInfomovilProcessStatus procesaDominioVO(SoapObject dominioVO,WS_DominioVO dominioDataVO)
	{
		WsInfomovilProcessStatus resulado = WsInfomovilProcessStatus.EXITO;
		String token = null;
		String temp  = null;
		 
		try
		{
			token = dominioVO.getPropertySafelyAsString("token");
			token = DES.decryptDES(dominioVO.getPropertySafelyAsString("token"),"");
			if ( token == null || token.isEmpty() )
				return WsInfomovilProcessStatus.ERROR_TOKEN;
			
			temp = DES.decryptDES(dominioVO.getPropertySafelyAsString("idDomain"),token);
			if ( !temp.isEmpty() )
			{
				int idDomain = Integer.parseInt(temp);
				
				if ( idDomain <= 0 ){
					temp = DES.decryptDES(dominioVO.getPropertySafelyAsString("statusDominio"),token);
					DatosUsuario.getInstance().setStatusDominio(temp);										
					return WsInfomovilProcessStatus.ERROR_LOGIN;
				}
				
				dominioDataVO.setIdDomain(idDomain);
			}
//			else {
//				return WsInfomovilProcessStatus.ERROR_TOKEN;
//			}
		} catch (NumberFormatException e) {
			return WsInfomovilProcessStatus.ERROR_LOGIN;
		} catch (Exception e) {
			e.printStackTrace();
			return WsInfomovilProcessStatus.ERROR_DESENCRIPTA;
		}
		 
		Vector<WS_RecordNaptrVO> lstReNaptr				= null;
		Vector<WS_KeywordVO> lstKeyword					= null;
		Vector<WS_DomainVO> lstDomain					= null;
		Vector<WS_LocalizacionVO> lstLocaliza			= null;
		Vector<WS_OffertRecordVO> lstOffer				= null;
		Vector<WS_ImagenVO> lstImagen					= null;
		Vector<WS_InfoUsuarioVO> lstInfoUsr				= null;
		Vector<WS_StatusDomainVO> lstStatusDomain		= null;
		Vector<WS_StatusDomainVO> lstStatusDomainGratis	= null;
		Vector<WS_UsuarioDominiosVO> lstUsuarioDominios = null;
		
		try
		{
			int max =  dominioVO.getPropertyCount();
			PropertyInfo proInfo = new PropertyInfo();
			for ( int i = 0; i<max; i++ )
			{
				dominioVO.getPropertyInfo(i, proInfo);
				if ( proInfo.getName().equalsIgnoreCase("idDomain") ) {
					// ya se extrajo arriba
				}else if ( proInfo.getName().equalsIgnoreCase("video") ) {
					dominioDataVO.setVideo(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("token") ) {
					dominioDataVO.setToken(token);
				} else if ( proInfo.getName().equalsIgnoreCase("statusDominio") ) {
					dominioDataVO.setStatusDominio(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("descripcionDominio") ) {
					dominioDataVO.setDescripcionDominio(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("fTelNamesIni") ) {
					dominioDataVO.setFechaIniTel(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("fTelNamesFin") ) {
					dominioDataVO.setFechaFinTel(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("fechaIni") ) {
					dominioDataVO.setFechaIni(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("fechaFin") ) {
					dominioDataVO.setFechaFin(getSafeElement(proInfo.getValue().toString(),token));				
				} else if ( proInfo.getName().equalsIgnoreCase("codeCamp") ) {
					dominioDataVO.setCodeCamp(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("canal") ) {
					dominioDataVO.setCanal(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("campania") ) {
					dominioDataVO.setCampania(getSafeElement(proInfo.getValue().toString(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("tipoUsuario") ) {
					dominioDataVO.setTipoUsuario(getSafeElement(proInfo.getValue().toString(),token));
				}else if ( proInfo.getName().equalsIgnoreCase("referencia") ) {
					dominioDataVO.setReferencia(getSafeElement(proInfo.getValue().toString(),token));	
				} else if ( proInfo.getName().equalsIgnoreCase("listRecordNaptrVo") ) {
					if ( lstReNaptr == null )
					{
						lstReNaptr = new Vector<WS_RecordNaptrVO>(2,2);
						dominioDataVO.setListRecordNaptrVo(lstReNaptr);
					}
					lstReNaptr.add(procesaRecordNptrVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listKeywordVO") ) {
					if ( lstKeyword == null )
					{
						lstKeyword = new Vector<WS_KeywordVO>(10,5);
						dominioDataVO.setListKeywordVO(lstKeyword);
					}
					lstKeyword.add(procesaKeywordVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listDominioVO") ) {
					if ( lstDomain == null )
					{
						lstDomain = new Vector<WS_DomainVO>(1,1);
						dominioDataVO.setListDominioVO(lstDomain);
					}
					lstDomain.add(procesaDomainVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listLatitud") ) {
					if ( lstLocaliza == null )
					{
						lstLocaliza = new Vector<WS_LocalizacionVO>(1,1);
						dominioDataVO.setListLatitud(lstLocaliza);
					}
					lstLocaliza.add(procesaLocalizacionVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listOffertRecordVO") ) {
					if ( lstOffer == null )
					{
						lstOffer = new Vector<WS_OffertRecordVO>(1,1);
						dominioDataVO.setListOffertRecordVO(lstOffer);
					}
					lstOffer.add(procesaOffertRecordVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listImagenVO") ) {
					if ( lstImagen == null )
					{
						lstImagen = new Vector<WS_ImagenVO>(2,2);
						dominioDataVO.setListImagenVO(lstImagen);
					}
					lstImagen.add(procesaImagenVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listInfoUsuarioVO") ) {
					if ( lstInfoUsr == null )
					{
						lstInfoUsr = new Vector<WS_InfoUsuarioVO>(15,3);
						dominioDataVO.setListInfoUsuarioVO(lstInfoUsr);
					}
					lstInfoUsr.add(procesaInfoUsuarioVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listStatusDomainVO") ) {
					if ( lstStatusDomain == null )
					{
						lstStatusDomain = new Vector<WS_StatusDomainVO>(15,3);
						dominioDataVO.setListStatusDomainVO(lstStatusDomain);
					}
					lstStatusDomain.add(procesaStatusDomainVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listStatusDomainGratisVO") ) {
					if ( lstStatusDomainGratis == null )
					{
						lstStatusDomainGratis = new Vector<WS_StatusDomainVO>(15,3);
						dominioDataVO.setListStatusDomainGratisVO(lstStatusDomainGratis);
					}
					lstStatusDomainGratis.add(procesaStatusDomainVO((SoapObject)proInfo.getValue(),token));
				} else if ( proInfo.getName().equalsIgnoreCase("listUsuarioDominiosVO") ) {
					if ( lstUsuarioDominios == null )
					{
						lstUsuarioDominios = new Vector<WS_UsuarioDominiosVO>(15,3);
						dominioDataVO.setListUsuarioDominiosVO(lstUsuarioDominios);
					}
					lstUsuarioDominios.add(procesaUsuarioDominiosVO((SoapObject)proInfo.getValue(),token));
				}
				else if (proInfo.getName().equalsIgnoreCase("resultado")) {
					String auxStr = getSafeElement(proInfo.getValue().toString(),token);
					dominioDataVO.setResultado(auxStr);
					
					if (auxStr.equalsIgnoreCase("SessionTO")) {
						resulado = WsInfomovilProcessStatus.ERROR_SESION;
					}
				}
			}
			
			if ( lstStatusDomain != null )
				dominioDataVO.setListStatusDomainVO(ordenarItems(lstStatusDomain)); 
			
			if (lstReNaptr != null && lstReNaptr.size() > 0) {
				lstReNaptr = ReadFilesUtils.acomodaContactos(lstReNaptr);
				
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			resulado = WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		catch (Exception e) {
			Log.d("infoLog", "el error es "+e);
			resulado = WsInfomovilProcessStatus.ERROR_TOKEN;
		}
		
		return resulado;
	}
	
	private Vector<WS_StatusDomainVO> ordenarItems(Vector<WS_StatusDomainVO> listDomain)
	{
		Log.d("infoLog", "Entrando a ordenar elementos");
		Vector<WS_StatusDomainVO> arrayItems = listDomain;
		Vector<WS_StatusDomainVO> arregloAux = new Vector<WS_StatusDomainVO>();
		Resources res = InfomovilApp.getApp().getResources();
		String arregloTitulos[] = {res.getString(R.string.txtNombreoEmpresa), res.getString(R.string.txtLogo), res.getString(R.string.negocioProfesion), res.getString(R.string.txtDescripcionCortaTitulo), res.getString(R.string.txtContacto), res.getString(R.string.txtMapaTitutlo), res.getString(R.string.txtTituloVideo), res.getString(R.string.txtPromociones), res.getString(R.string.txtGaleriaImagenesTitulo), res.getString(R.string.txtPerfilTitulo), res.getString(R.string.txtTituloDireccion), res.getString(R.string.txtTituloInfoAdicional)};
		String arregloTitulosEs[] = {res.getString(R.string.txtNombreoEmpresa2), res.getString(R.string.txtLogo2), res.getString(R.string.negocioProfesion2), res.getString(R.string.txtDescripcionCortaTitulo2), res.getString(R.string.txtContacto2), res.getString(R.string.txtMapaTitutlo2), res.getString(R.string.txtTituloVideo2), res.getString(R.string.txtPromociones2), res.getString(R.string.txtGaleriaImagenesTitulo2), res.getString(R.string.txtPerfilTitulo2), res.getString(R.string.txtTituloDireccion2), res.getString(R.string.txtInfoAdicional2)};
		
		for(int i = 0; i < arregloTitulosEs.length; i++)
		{
//			String stringAux = StringUtils.eliminarAcentos(arregloTitulos[i]);
			String stringAux = StringUtils.stripAccents(arregloTitulosEs[i]);
			if(i == 2) {
				WS_StatusDomainVO item = new WS_StatusDomainVO();
				item.setDescripcionItem(arregloTitulos[i]);
				item.setEstatusItem(0);
				arregloAux.add(item);
			}
			else {
			
			for(int j = 0; j < arrayItems.size(); j++)
			{
				WS_StatusDomainVO item = arrayItems.get(j);
				if( stringAux.equalsIgnoreCase(item.getDescripcionItem()) )
				{
					item.setDescripcionItem(arregloTitulos[i]);
					arregloAux.add(item);
					break;
				}
			}
			}
			
		}
		return arregloAux;
	}
	
	private WS_InfoUsuarioVO procesaInfoUsuarioVO(SoapObject infoUsuario, String token)
	{
		try
		{
			WS_InfoUsuarioVO infoUsr = new WS_InfoUsuarioVO();
			String temp = null; 
			
			infoUsr.setCalleNum(getSafeElement(infoUsuario.getPropertySafelyAsString("calleNum"),token));
			
			temp = getSafeElement(infoUsuario.getPropertySafelyAsString("canal"),token);
			infoUsr.setCanal(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			infoUsr.setCiudad(getSafeElement(infoUsuario.getPropertySafelyAsString("ciudad"),token));
			infoUsr.setCorreo(getSafeElement(infoUsuario.getPropertySafelyAsString("correo"),token));
			infoUsr.setCp(getSafeElement(infoUsuario.getPropertySafelyAsString("cp"),token));
			infoUsr.setEstado(getSafeElement(infoUsuario.getPropertySafelyAsString("estado"),token));
			infoUsr.setNameEmpresa(getSafeElement(infoUsuario.getPropertySafelyAsString("nameEmpresa"),token));
			infoUsr.setNombre(getSafeElement(infoUsuario.getPropertySafelyAsString("nombre"),token));
			
			temp = getSafeElement(infoUsuario.getPropertySafelyAsString("pais"),token);
			infoUsr.setPais(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			infoUsr.setPoblacion(getSafeElement(infoUsuario.getPropertySafelyAsString("poblacion"),token));
			infoUsr.setTel(getSafeElement(infoUsuario.getPropertySafelyAsString("tel"),token));
			infoUsr.setUserName(getSafeElement(infoUsuario.getPropertySafelyAsString("userName"),token));
			
			return infoUsr;
		} catch (Exception e) {
			return null;
		}
	}

	private WS_ImagenVO procesaImagenVO(SoapObject imagenData, String token)
	{
		try
		{
			WS_ImagenVO imagenVO = new WS_ImagenVO();
			String temp = null; 
			
			imagenVO.setTypeImage(getSafeElement(imagenData.getPropertySafelyAsString("typeImage"),token));
			imagenVO.setDescripcionImagen(getSafeElement(imagenData.getPropertySafelyAsString("descripcionImagen"),token));
			
			temp = getSafeElement(imagenData.getPropertySafelyAsString("idImagen"),token);
			imagenVO.setIdImagen(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			imagenVO.setImagenClobGaleria(StringUtils.quitarSaltos(imagenData.getPropertySafelyAsString("imagenClobGaleria")));
			
			imagenVO.setUrl(getSafeElement(imagenData.getPropertySafelyAsString("url"),token));
			
			Log.d("infoLog", "terminando de procesar la imagen");
			return imagenVO;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("infoLog", "el error es "+e);
			return null;
		}
	}
	
	private WS_ImagenVO procesaImagenVO(SoapObject imagenData)
	{
		try
		{
			WS_ImagenVO imagenVO = new WS_ImagenVO();
			String temp = null; 
			
			imagenVO.setTypeImage(imagenData.getPropertySafelyAsString("typeImage"));
			imagenVO.setDescripcionImagen(imagenData.getPropertySafelyAsString("descripcionImagen"));
			
			temp = imagenData.getPropertySafelyAsString("idImagen");
			imagenVO.setIdImagen(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			imagenVO.setImagenClobGaleria(StringUtils.quitarSaltos(imagenData.getPropertySafelyAsString("imagenClobGaleria")));
			
			imagenVO.setUrl(imagenData.getPropertySafelyAsString("url"));
			
			Log.d("infoLog", "terminando de procesar la imagen");
			return imagenVO;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("infoLog", "el error es "+e);
			return null;
		}
	}

	private WS_OffertRecordVO procesaOffertRecordVO(SoapObject offerData, String token)
	{
		try
		{
			WS_OffertRecordVO offerVO = new WS_OffertRecordVO();
			String temp = null;
			
			offerVO.setTitleOffer(getSafeElement(offerData.getPropertySafelyAsString("titleOffer"),token));
			offerVO.setDescOffer(getSafeElement(offerData.getPropertySafelyAsString("descOffer"),token));
			offerVO.setTermsOffer(getSafeElement(offerData.getPropertySafelyAsString("termsOffer"),token));
			offerVO.setImageClobOffer(getSafeElement(offerData.getPropertySafelyAsString("imageClobOffer")));
			offerVO.setLinkOffer(getSafeElement(offerData.getPropertySafelyAsString("linkOffer"),token));
			offerVO.setEndDateOffer(getSafeElement(offerData.getPropertySafelyAsString("endDateOffer"),token));
			offerVO.setPromoCodeOffer(getSafeElement(offerData.getPropertySafelyAsString("promoCodeOffer"),token));
			
			temp = getSafeElement(offerData.getPropertySafelyAsString("discountOffer"), token);
			offerVO.setDiscountOffer(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			offerVO.setRedeemOffer(getSafeElement(offerData.getPropertySafelyAsString("redeemOffer"),token));
			
			temp = getSafeElement(offerData.getPropertySafelyAsString("idOffer"), token);
			offerVO.setIdOffer(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			offerVO.setUrlImage(getSafeElement(offerData.getPropertySafelyAsString("urlImage"),token));
			
			return offerVO;
		} catch (Exception e) {
			return null;
		}
	}

	private WS_LocalizacionVO procesaLocalizacionVO(SoapObject localizaData, String token) {
		try
		{
			WS_LocalizacionVO localizaVO = new WS_LocalizacionVO();
			String temp = null;
			
			temp = getSafeElement(localizaData.getPropertySafelyAsString("idLocalizacion"),token);
			localizaVO.setIdLocalizacion(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			
			temp = getSafeElement(localizaData.getPropertySafelyAsString("latitudeLoc"),token);
			localizaVO.setLatitudeLoc(Double.parseDouble( temp.isEmpty() ? "0" : temp ));
			
			temp = getSafeElement(localizaData.getPropertySafelyAsString("longitudeLoc"),token);
			localizaVO.setLongitudeLoc(Double.parseDouble( temp.isEmpty() ? "0" : temp ));
			
			return localizaVO;
		} catch (Exception e) {
			return null;
		}
	}

	private WS_RecordNaptrVO procesaRecordNptrVO(SoapObject recordNAPTRData, String token)
	{
		try
		{
			WS_RecordNaptrVO recordNAPTR = new WS_RecordNaptrVO();
			String temp = null;
			
			recordNAPTR.setCategoryNaptr(getSafeElement(recordNAPTRData.getPropertySafelyAsString("categoryNaptr"),token));
			
			temp = getSafeElement(recordNAPTRData.getPropertySafelyAsString("claveContacto"), token);
			recordNAPTR.setClaveContacto(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			recordNAPTR.setLongLabelNaptr(getSafeElement(recordNAPTRData.getPropertySafelyAsString("longLabelNaptr"),token));
			
			temp = getSafeElement(recordNAPTRData.getPropertySafelyAsString("preference"), token);
			recordNAPTR.setPreference(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			recordNAPTR.setRegExp(getSafeElement(recordNAPTRData.getPropertySafelyAsString("regExp"),token));
			recordNAPTR.setServicesNaptr(getSafeElement(recordNAPTRData.getPropertySafelyAsString("servicesNaptr"),token));
			recordNAPTR.setSubCategory(getSafeElement(recordNAPTRData.getPropertySafelyAsString("subCategory"),token));
			
			temp = getSafeElement(recordNAPTRData.getPropertySafelyAsString("visible"), token);
			recordNAPTR.setVisible(Integer.parseInt( temp.isEmpty() ? "0" : temp ));	
			
			return recordNAPTR;
		} catch (Exception e) {
			return null;
		}
	}
	
	private WS_DomainVO procesaDomainVO(SoapObject domainVO, String token)
	{
		try
		{
			WS_DomainVO domain = new WS_DomainVO();
			
			domain.setBackGroundImage(getSafeElement(domainVO.getPropertySafelyAsString("backGroundImage"),token));
			domain.setColour(getSafeElement(domainVO.getPropertySafelyAsString("colour"),token));
			domain.setDisplayString(getSafeElement(domainVO.getPropertySafelyAsString("displayString"),token));
			String txtAux = getSafeElement(domainVO.getPropertySafelyAsString("textRecord"),token);
			domain.setTextRecord(txtAux.equals("Titulo")?"":txtAux); 
			domain.setTemplate(getSafeElement(domainVO.getPropertySafelyAsString("template"),token));
			domain.setDomainName(getSafeElement(domainVO.getPropertySafelyAsString("domainName"),token));
			domain.setTextDomain(getSafeElement(domainVO.getPropertySafelyAsString("textDomain"),token));
			
			return domain;
		} catch (Exception e) {
			return null;
		}
	}
	
	private WS_KeywordVO procesaKeywordVO(SoapObject keywordVO, String token)
	{
		try
		{
			WS_KeywordVO keyword = new WS_KeywordVO();
			String temp = null;
			
			temp = getSafeElement(keywordVO.getPropertySafelyAsString("idKeyword"),token);
			keyword.setIdKeyword(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			keyword.setKeywordField(getSafeElement(keywordVO.getPropertySafelyAsString("keywordField"),token));
			
			temp = getSafeElement(keywordVO.getPropertySafelyAsString("keywordPos"),token);
			keyword.setKeywordPos(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			keyword.setKeywordValue(getSafeElement(keywordVO.getPropertySafelyAsString("keywordValue"),token));
			
			return keyword;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	private WS_UsuarioDominiosVO procesaUsuarioDominiosVO(SoapObject usuarioDominios, String token)
	{
		try
		{
			WS_UsuarioDominiosVO itemDom	= new WS_UsuarioDominiosVO();
					
			itemDom.setIdDomain(getSafeElement(usuarioDominios.getPropertySafelyAsString("idDomain"),token));
			itemDom.setIdCtrlDomain(getSafeElement( usuarioDominios.getPropertySafelyAsString("idCtrlDomain"),token));
			itemDom.setDomainCtrlName(getSafeElement( usuarioDominios.getPropertySafelyAsString("domainCtrlName"),token));
			itemDom.setDomainType(getSafeElement( usuarioDominios.getPropertySafelyAsString("domainType"),token));
			itemDom.setStatusCtrlDominio(getSafeElement( usuarioDominios.getPropertySafelyAsString("statusCtrlDominio"),token));
			itemDom.setStatusVisible(getSafeElement( usuarioDominios.getPropertySafelyAsString("statusVisible"),token));
			itemDom.setFechaCtrlIni(getSafeElement( usuarioDominios.getPropertySafelyAsString("fechaCtrlIni"),token));
			itemDom.setFechaCtrlFin(getSafeElement( usuarioDominios.getPropertySafelyAsString("fechaCtrlFin"),token));
			itemDom.setVigente(getSafeElement( usuarioDominios.getPropertySafelyAsString("vigente"),token));
			itemDom.setUrlSitio(getSafeElement( usuarioDominios.getPropertySafelyAsString("urlSitio"),token));
			return itemDom;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	private WS_StatusDomainVO procesaStatusDomainVO(SoapObject statusDomain)
	{
		try
		{
			WS_StatusDomainVO itemDom	= new WS_StatusDomainVO();
					
			itemDom.setDescripcionItem(getSafeElement(statusDomain.getPropertySafelyAsString("descripcionItem")));
			itemDom.setEstatusItem(Integer.parseInt( statusDomain.getPropertySafelyAsString("status") ));
//			itemDom.setFechaExpiracion(Integer.parseInt(statusDomain.getPropertySafelyAsString("fechaExpiracion")));
			
			return itemDom;
		} catch (Exception e) {
			return null;
		}
	}
	
	private WS_StatusDomainVO procesaStatusDomainVO(SoapObject statusDomain, String token)
	{
		try
		{
			WS_StatusDomainVO itemDom	= new WS_StatusDomainVO();
			String temp = null;
					
			itemDom.setDescripcionItem(getSafeElement(statusDomain.getPropertySafelyAsString("descripcionItem"),token));
			
			temp = getSafeElement(statusDomain.getPropertySafelyAsString("status"),token);
			itemDom.setEstatusItem(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			
//			temp = getSafeElement(statusDomain.getPropertySafelyAsString("fechaExpiracion"),token);
//			itemDom.setFechaExpiracion(Integer.parseInt( temp.isEmpty() ? "0" : temp ));
			
			return itemDom;
		} catch (Exception e) {
			return null;
		}
	}
	
	private Object[] procesaVisitaVO(SoapObject visita)
	{		
		Object[] result = {WsInfomovilProcessStatus.EXITO, new WS_VisitaVO()};	
		
		try
		{
			WS_VisitaVO visitaTemp = new WS_VisitaVO();
			String token = getSafeElement(visita.getPropertySafelyAsString("token"),"");
			
			if ( token == null || token.isEmpty() ){
				result[0] = WsInfomovilProcessStatus.ERROR_TOKEN;
			}
							
			String temp = null;
			
			visitaTemp.setDescripcionVisitas(getSafeElement(visita.getPropertySafelyAsString("descripcionVisitas"),token));
			visitaTemp.setFecha(getSafeElement(visita.getPropertySafelyAsString("fecha"),token));
			
			temp = getSafeElement(visita.getPropertySafelyAsString("numeroFecha"),token);
			visitaTemp.setNumeroFecha(Integer.parseInt(temp.isEmpty() ? "0" : temp ));
			
			temp = getSafeElement(visita.getPropertySafelyAsString("resultado"),token);
			if (temp.equalsIgnoreCase("SessionTO")) {
				result[0]= WsInfomovilProcessStatus.ERROR_SESION;
			}
			
			
			temp = getSafeElement(visita.getPropertySafelyAsString("visitas"),token);
			visitaTemp.setVisitas(Long.parseLong(temp.isEmpty() ? "0" : temp ));
			
			result[1] = visitaTemp;
			
			return result;
		} catch (Exception e) {
			result[0]= WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
			return result;
		}
	}
	
	private void insertaKeywordData(Vector<WS_KeywordVO> arrKeyword)
	{
		ArrayList<String>arrClavesDir	 = new ArrayList<String>(Arrays.asList(clavesDireccion));
		ArrayList<String>arrClavesPerfil = new ArrayList<String>(Arrays.asList(clavesPerfil));
		
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		Vector<WS_KeywordVO> listDirec = dataUsr.getArregloDireccion();
		Vector<WS_KeywordVO> listPerfi = dataUsr.getArregloPerfil();
		Vector<WS_KeywordVO> listInfAd = dataUsr.getArregloInformacionAdicional();
		
		if (listDirec == null)
		{
			listDirec = new Vector<WS_KeywordVO>(7);
			dataUsr.setArregloDireccion(listDirec);
		}
		
		if (listPerfi == null)
		{
			listPerfi = new Vector<WS_KeywordVO>(7);
			dataUsr.setArregloPerfil(listPerfi);
		}
		
		if (listInfAd == null)
		{
			listInfAd = new Vector<WS_KeywordVO>(1);
			dataUsr.setArregloInformacionAdicional(listInfAd);
		}
		
		int index = -1;
		int indexAux = arrKeyword.size();
		for (int i = 0; i < indexAux; i++)
		{
			WS_KeywordVO ws_KeywordVO = arrKeyword.get(i);
			
			if (   ( index =arrClavesDir.indexOf(ws_KeywordVO.getKeywordField())) >= 0 ) {
				listDirec.set(index,ws_KeywordVO);
			} 
			else if ( ( index = arrClavesPerfil.indexOf(ws_KeywordVO.getKeywordField()) ) >= 0 )
			{
				listPerfi.remove(index);
				listPerfi.insertElementAt(ws_KeywordVO,index);
			} else {				
				index = listInfAd.indexOf(ws_KeywordVO);
				if(index == -1){
					listInfAd.add(ws_KeywordVO);
				}
				else{
					listInfAd.set(index,ws_KeywordVO);
				}
			}
		}
		
	}
	
	private void actualizaElementoKeywordEnVecto(Vector<WS_KeywordVO> keywordVOs, WS_KeywordVO ws_KeywordVO)
	{
		try
		{
			for (WS_KeywordVO temp_KeywordVO : keywordVOs)
			{
				if ( temp_KeywordVO.getKeywordField().equals(ws_KeywordVO.getKeywordField()) )
				{
					temp_KeywordVO.setIdKeyword(ws_KeywordVO.getIdKeyword());
					temp_KeywordVO.setKeywordPos(ws_KeywordVO.getKeywordPos());
					temp_KeywordVO.setKeywordValue(ws_KeywordVO.getKeywordValue());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int borraKeyWordData(Vector<WS_KeywordVO> keywordVOs, WS_DeleteItem deleteItem)
	{
		int i = -1;
		for (WS_KeywordVO keywordVO : keywordVOs)
		{
			if ( keywordVO.getIdKeyword() == deleteItem.getIndexItem() )
			{
				i = keywordVOs.indexOf(keywordVO);
				break;
			}
				
		}
		if ( i >= 0 )
			keywordVOs.remove(i);
		
		return i;
	}

	public WsInfomovilProcessStatus processCambiaNombreRecursoResponse(
			SoapObject resultsRequestSOAP) {
		
		WS_RespuestaVO resp = procesaRespuestaVO((SoapObject)resultsRequestSOAP.getProperty("RespuestaVO")); 
		return resp.getStatus();
	}

	public WsInfomovilProcessStatus processGetImagenesResponse(
			SoapObject resultsRequestSOAP) {
		
		Vector<WS_ImagenVO> lstImagen = new Vector<WS_ImagenVO>();

		int max =  resultsRequestSOAP.getPropertyCount();
		PropertyInfo proInfo = new PropertyInfo();
		for ( int i = 0; i<max; i++ ) {
			resultsRequestSOAP.getPropertyInfo(i, proInfo);

			if ( proInfo.getName().equalsIgnoreCase("ImagenesVO") ) {
				lstImagen.add(procesaImagenVO((SoapObject)proInfo.getValue()));
			}
		}
		
		MarshalDatosUsuario.fusionaImagenes(lstImagen);
		
		return WsInfomovilProcessStatus.EXITO;
	}

	public WsInfomovilProcessStatus processGetHashMovilizaSitio(SoapObject resultsRequestSOAP) {
		try
		{
			SoapObject soapObject = (SoapObject)resultsRequestSOAP.getProperty("RespuestaVO");
			String hashMovilizaSitio = soapObject.getPropertySafelyAsString("resultado");
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			datosUsuario.setHashMovilizaSitio(hashMovilizaSitio);
			return WsInfomovilProcessStatus.EXITO;
		}
		catch(Exception e){
			return WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
		
	}

	/****
	 * Genera 
	 * @param resultsRequestSOAP
	 * @param activity
	 * @return
	 */
	public WsInfomovilProcessStatus processGetHashCambioPassword(SoapObject resultsRequestSOAP, Activity activity, String email) {
		try
		{
			SoapObject soapObject = (SoapObject)resultsRequestSOAP.getProperty("RespuestaVO");
			int codeError = Integer.parseInt(soapObject.getPropertySafelyAsString("codeError"));
			
			if(codeError == 0){
//				String hashCambioPassword = soapObject.getPropertySafelyAsString("resultado");
//				Appboy.getInstance(activity).changeUser(email).
//					setCustomUserAttribute("hashCambioPassword", hashCambioPassword);
				return WsInfomovilProcessStatus.EXITO;
			}
			else{
				return WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
			}
		}
		catch(Exception e){
			return WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
	}

	/**
	 * 
	 * @param resultsRequestSOAP
	 * @return
	 */
	public WsInfomovilProcessStatus processGetCatalogoDominios(SoapObject resultsRequestSOAP) {
		try
		{
			
			List<Catalogo> lstCatalogoDominios = new ArrayList<Catalogo>();
			
			PropertyInfo proInfo = new PropertyInfo();
			for ( int i = 0; i<resultsRequestSOAP.getPropertyCount(); i++ ) {
				resultsRequestSOAP.getPropertyInfo(i, proInfo);
				if ( proInfo.getName().equalsIgnoreCase("CatalogoDominios") ) {
					SoapObject item = (SoapObject)proInfo.getValue();
					lstCatalogoDominios.add(new Catalogo(Integer.parseInt(item.getPropertyAsString("id")), 
							item.getPropertyAsString("descripcion")));
				}
			}
			
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			datosUsuario.setCatalogoDominios(lstCatalogoDominios);
			return WsInfomovilProcessStatus.EXITO;
			
		}
		catch(Exception e){
			return WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
		}
	}
	
		
}
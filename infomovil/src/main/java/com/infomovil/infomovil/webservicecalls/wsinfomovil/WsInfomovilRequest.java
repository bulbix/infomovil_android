/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil;

import java.util.Vector;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.infomovil.infomovil.model.CompraModel;
import com.infomovil.infomovil.model.DatosUsuario;
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
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;

/**
 * @author Ignaki Dominguez
 *
 */
public class WsInfomovilRequest
{
	public void setupGetExistDomainRequest(SoapObject request) throws WsInfomovilException
	{	
		try
		{
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			WS_UserDomainVO userDomainVO = datosUsuario.getUserDomainData();
			request.addProperty("domainName", DES.encryptDES(userDomainVO.getDomainName(), ""));
			request.addProperty("domainType", DES.encryptDES(datosUsuario.getDomainType(), ""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error en la encriptacion");
		}
	}
	
	public void setupGetExistUserRequest(SoapObject request, String userName) throws WsInfomovilException
	{
		if ( userName == null )
			throw new WsInfomovilException("No se tiene el nombre del usuario a buscar");
		
		try
		{
			request.addProperty("userName", DES.encryptDES(userName, ""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error en la encriptacion");
		}
	}
	
	public void setupGetExistCampaniaByMailRequest(SoapObject request) throws WsInfomovilException
	{
		try
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			request.addProperty("email", DES.encryptDES(dataUsr.getNombreUsuario(), ""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error en la encriptacion");
		}
	}
	
	public void setupInsertUserDomainRequest(SoapObject request, WS_UserDomainVO usrDomainVO) throws WsInfomovilException //////*****************
	{
		if ( usrDomainVO == null )
			throw new WsInfomovilException("No se cuenta con la informacion del dominio");
			
		request.addProperty("UserDomainVO", usrDomainVO);
	}
	
	public void setupCreateDomainRequest(SoapObject request, WS_DominioVO dominioDataVO) throws WsInfomovilException
	{
		if ( dominioDataVO == null )
			throw new WsInfomovilException("No se tienen datos del dominio para su creacion");
		
		
		try
		{
			SoapObject arg0 = new SoapObject("", "");
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			String token = datosUsuario.getToken();
			
			arg0.addProperty("idDomain", DES.encryptDES(Long.toString(dominioDataVO.getIdDomain()),token));
			arg0.addProperty("video", DES.encryptDES(dominioDataVO.getVideo(),token));
			arg0.addProperty("token", DES.encryptDES(datosUsuario.getNombreUsuario(),""));
			arg0.addProperty("statusDominio", DES.encryptDES(dominioDataVO.getStatusDominio(),token));
			arg0.addProperty("fechaIni", DES.encryptDES(dominioDataVO.getFechaIni(),token));
			arg0.addProperty("fechaFin", DES.encryptDES(dominioDataVO.getFechaFin(),token));
			
			Vector<WS_RecordNaptrVO>recordNaptrVOs = dominioDataVO.getListRecordNaptrVo();
			if ( recordNaptrVOs != null )
			{
				for (WS_RecordNaptrVO naptrVO : recordNaptrVOs)
					arg0.addProperty("listRecordNaptrVo",naptrVO);			
			}
			
			Vector<WS_KeywordVO>keywordVOs = dominioDataVO.getListKeywordVO();
			if ( keywordVOs != null )
			{
				for (WS_KeywordVO keywordVO : keywordVOs)
					arg0.addProperty("listKeywordVO",keywordVO);			
			}
			
			Vector<WS_DomainVO>domainVOs = dominioDataVO.getListDominioVO();
			if ( domainVOs != null )
			{
				for (WS_DomainVO domainVO : domainVOs)
					arg0.addProperty("listDominioVO",domainVO);			
			}
			
			Vector<WS_LocalizacionVO>localizacionVOs = dominioDataVO.getListLatitud();
			if ( localizacionVOs != null )
			{
				for (WS_LocalizacionVO localizacionVO : localizacionVOs)
					arg0.addProperty("listLatitud",localizacionVO);			
			}
	
			Vector<WS_OffertRecordVO>offertRecordVOs = dominioDataVO.getListOffertRecordVO();
			if ( offertRecordVOs != null )
			{
				for (WS_OffertRecordVO offertRecordVO : offertRecordVOs)
					arg0.addProperty("listOffertRecordVO",offertRecordVO);			
			}
	
			Vector<WS_ImagenVO>imagenVOs = dominioDataVO.getListImagenVO();
			if ( imagenVOs != null )
			{
				for (WS_ImagenVO imagenVO : imagenVOs)
					arg0.addProperty("listImagenVO",imagenVO);			
			}
	
			Vector<WS_InfoUsuarioVO>infoUsuarioVOs = dominioDataVO.getListInfoUsuarioVO();
			if ( infoUsuarioVOs != null )
			{
				for (WS_InfoUsuarioVO infoUsuarioVO : infoUsuarioVOs)
					arg0.addProperty("listInfoUsuarioVO",infoUsuarioVO);			
			}
	
	//		Vector<WS_StatusDomainVO>statusDomainVOs = dominioDataVO.getListStatusDomainVO();
	//		if ( statusDomainVOs != null )
	//		{
	//			for (WS_StatusDomainVO statusDomainVO : statusDomainVOs)
	//				arg0.addProperty("listStatusDomainVO",statusDomainVO);			
	//		}
			
			request.addProperty("arg0", arg0);
			request.addProperty("token", DES.encryptDES(datosUsuario.getNombreUsuario(),""));
			request.addProperty("password", DES.encryptDES(datosUsuario.getPassword(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupUpdateDomainRequest(SoapObject request, WS_DomainVO domainVO) throws WsInfomovilException
	{
		if ( domainVO == null )
			throw new WsInfomovilException("No se cuenta con la informacion del dominio a actualizar");
		try
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			
			request.addProperty("DomainVO", domainVO);
			request.addProperty("idDomain", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token", DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupUpdateKeyWordDataRequest(SoapObject request, Vector<WS_KeywordVO> arrKeyword) throws WsInfomovilException
	{
		if ( arrKeyword == null || arrKeyword.size() < 0 )
			throw new WsInfomovilException("No se tienen elementos a actualizar");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			for (WS_KeywordVO ws_KeywordVO : arrKeyword)
			{
				request.addProperty("KeywordVO",ws_KeywordVO);	
			}
			
			request.addProperty("domainId", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token", DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupUpdateRecordNaptrRequest(SoapObject request, Vector<WS_RecordNaptrVO> arrRecordNaptr) throws WsInfomovilException
	{
		if ( arrRecordNaptr == null || arrRecordNaptr.size() < 0 )
			throw new WsInfomovilException("No se tienen elementos a actualizar");
		
		try
		{
			DatosUsuario datosUsr		= DatosUsuario.getInstance();
			
			for (WS_RecordNaptrVO ws_RecordNaptrVO : arrRecordNaptr)
			{
				request.addProperty("RecordNaptrVO",ws_RecordNaptrVO);
				Log.d("infoLog", "los contactos son " +ws_RecordNaptrVO.toString());
			}
			
			request.addProperty("domain_id", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token", DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			 throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupDeleteImageRequest(SoapObject request, WS_DeleteItem deleteItem) throws WsInfomovilException
	{
		if ( deleteItem == null )
			throw new WsInfomovilException("No se cuenta con elemento para borrar");
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance(); 
			String token			= datosUsr.getToken();
			
			request.addProperty("domainId", DES.encryptDES(Long.toString(deleteItem.getIdDomain()),token));
			request.addProperty("imageId", DES.encryptDES(Integer.toString(deleteItem.getIndexItem()),token));
			request.addProperty("token", DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupDeleteKeyWordDataRequest(SoapObject request, WS_DeleteItem deleteItem) throws WsInfomovilException
	{
		if ( deleteItem == null )
			throw new WsInfomovilException("No se cuenta con elemento para borrar");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			request.addProperty("idDomain", DES.encryptDES(Long.toString(deleteItem.getIdDomain()),datosUsr.getToken()));
			request.addProperty("token", DES.encryptDES(datosUsr.getNombreUsuario(),""));
			request.addProperty("keywordId", DES.encryptDES(Integer.toString(deleteItem.getIndexItem()),datosUsr.getToken()));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupDeleteRecordNaptrRequest(SoapObject request, WS_DeleteItem deleteItem) throws WsInfomovilException
	{
		if ( deleteItem == null )
			throw new WsInfomovilException("No se cuenta con elemento para borrar");
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			request.addProperty("idDomain", DES.encryptDES(Long.toString(deleteItem.getIdDomain()),datosUsr.getToken()));
			request.addProperty("naptrId", DES.encryptDES(Integer.toString(deleteItem.getIndexItem()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupDeleteVideoRequest(SoapObject request) throws WsInfomovilException
	{
		try
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			
			request.addProperty("idDomain", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupDeleteLocRecordRequest(SoapObject request) throws WsInfomovilException
	{
		try
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			request.addProperty("domainId", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupDeleteOfferRecordRequest(SoapObject request, WS_DeleteItem deleteItem) throws WsInfomovilException
	{
		if ( deleteItem == null )
			throw new WsInfomovilException("No se cuenta con elemento para borrar");
		
		try
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			
			request.addProperty("DomainId", DES.encryptDES(Long.toString(deleteItem.getIdDomain()),datosUsr.getToken()));
			request.addProperty("offerdId", DES.encryptDES(Long.toString(deleteItem.getIndexItem()),datosUsr.getToken()));
			request.addProperty("token", DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupInsertVideoRequest(SoapObject request, ItemSelectModel modeloVideo) throws WsInfomovilException
	{
		if ( modeloVideo == null || modeloVideo.getLinkVideo() == null || modeloVideo.getLinkVideo().trim().equals("") )
			throw new WsInfomovilException("No se cuenta con la url del video");
		try
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			
			request.addProperty("idDomain", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("url", DES.encryptDES(modeloVideo.getLinkVideo(),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupInsertImageRequest(SoapObject request, WS_ImagenVO imagen, Activity activity) throws WsInfomovilException
	{
		if ( imagen == null )
			throw new WsInfomovilException("No se cuenta con la imagen a insertar");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			request.addProperty("domainId", DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
			request.addProperty("arg1",imagen);
			request.addProperty("sistema",DES.encryptDES("ANDROID",datosUsr.getToken()));
			PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			request.addProperty("versionSistema",DES.encryptDES(pInfo.versionName,datosUsr.getToken()));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupUpdateImageRequest(SoapObject request, Vector<WS_ImagenVO> lstImagen, Activity activity) throws WsInfomovilException
	{
		if ( lstImagen == null || lstImagen.size() < 0 )
			throw new WsInfomovilException("No se cuenta con la lista de imagenes a actualizar");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			PropertyInfo info		= null;
			
			request.addProperty("domainId",DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
			request.addProperty("sistema",DES.encryptDES("ANDROID",datosUsr.getToken()));
			PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			request.addProperty("versionSistema",DES.encryptDES(pInfo.versionName,datosUsr.getToken()));
			for (WS_ImagenVO ws_ImagenVO : lstImagen)
			{
				info = new PropertyInfo();
				info.setName("arg1");
				info.setValue(ws_ImagenVO);
				request.addProperty(info);
			}
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupUpdateInserLocRecordRequest(SoapObject request, WS_LocalizacionVO localizaVO) throws WsInfomovilException
	{
		if ( localizaVO == null )
			throw new WsInfomovilException("No se cuenta con los datos de localizacion ");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			request.addProperty("localizacionVO",localizaVO);
			request.addProperty("domainId",DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupInsertRecordNaptrRequest(SoapObject request, Vector<WS_RecordNaptrVO> arrRecordNaptr) throws WsInfomovilException
	{
		if ( arrRecordNaptr == null || arrRecordNaptr.size() < 0 )
			throw new WsInfomovilException("No se tienen elementos a actualizar");
		
		try
		{
			DatosUsuario datosUsr		= DatosUsuario.getInstance();
			
			request.addProperty("domain_id",DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
			
			for (WS_RecordNaptrVO ws_RecordNaptrVO : arrRecordNaptr)
				request.addProperty("RecordNaptrVO",ws_RecordNaptrVO);
			
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupUpdateinsertOfferRecordRequest(SoapObject request, WS_OffertRecordVO offerRecord) throws WsInfomovilException
	{
		if ( offerRecord == null )
			throw new WsInfomovilException("No se cuenta con el objeto de oferta");
		
		try
		{
			DatosUsuario datosUsr = DatosUsuario.getInstance();
			if ( offerRecord.getEndDateOffer() == null || offerRecord.getEndDateOffer().isEmpty() )
				offerRecord.setEndDateOffer("01/01/1970");

			request.addProperty("domainid",DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(), ""));
			request.addProperty("OffertRecordVO",offerRecord);
			
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupInsertKeyWordDataRequest(SoapObject request, Vector<WS_KeywordVO> keywordVOs) throws WsInfomovilException
	{
		if ( keywordVOs == null || keywordVOs.size() < 0 )
			throw new WsInfomovilException("No se cuenta con los registros a insertar");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			request.addProperty("domainId",DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
			for ( WS_KeywordVO ws_KeywordVO : keywordVOs)
				request.addProperty("KeywordVO",ws_KeywordVO);
			
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupInsertKeywordDataAddressRequest(SoapObject request, Vector<WS_KeywordVO> keywordVOs) throws WsInfomovilException
	{ 
		if ( keywordVOs == null || keywordVOs.size() < 0 )
			throw new WsInfomovilException("No se cuenta con los registros a insertar");
		
		try
		{
			DatosUsuario datosUsr	= DatosUsuario.getInstance();
			
			for ( WS_KeywordVO ws_KeywordVO : keywordVOs)
				request.addProperty("ListKeyword",ws_KeywordVO);			
	
			request.addProperty("idDomain",DES.encryptDES(Long.toString(datosUsr.getDomainid()),datosUsr.getToken()));
			request.addProperty("token",DES.encryptDES(datosUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
		
	public void setupGetNumVisitasRequest(SoapObject request, String tipoConsulta) throws WsInfomovilException
	{
		if ( tipoConsulta == null )
			throw new WsInfomovilException("No se cuenta con el tipo de consulta para obtener las vistas");
		
		try
		{
			
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			Log.d("Nombre dominio", dataUsr.getDomainData().getDomainName());
			Log.d("Token", dataUsr.getToken());
			Log.d("Nombre Usuario", dataUsr.getNombreUsuario());
			
			request.addProperty("Domain", DES.encryptDES(dataUsr.getDomainData().getDomainName(), dataUsr.getToken()));
			request.addProperty("TipoConsulta",tipoConsulta);
			request.addProperty("token",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupGetFechaExpiracionRequest(SoapObject request) throws WsInfomovilException
	{
		try
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			
			request.addProperty("Domain", dataUsr.getDomainData().getDomainName());
			request.addProperty("token",DES.encryptDES(dataUsr.getToken(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupInsertUpdateInfoUserRequest(SoapObject request, WS_InfoUsuarioVO infoUsr) throws WsInfomovilException
	{
		if ( infoUsr == null )
			throw new WsInfomovilException("No se cuenta con informacion para actualizar informacion del usuario");
		
		try
		{
			DatosUsuario dataUsr	= DatosUsuario.getInstance();
			
			request.addProperty("ListInfoUser",infoUsr);
			request.addProperty("token",DES.encryptDES(dataUsr.getToken(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupCambioPasswordRequest(SoapObject request, String strUsuario) throws WsInfomovilException
	{
		if ( strUsuario == null )
			throw new WsInfomovilException("No se tiene el correo a solicitar");
		
		try
		{
			request.addProperty("Usuario", DES.encryptDES(strUsuario,""));
			request.addProperty("token", DES.encryptDES(strUsuario,""));
			
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupCancelarCuentaRequest(SoapObject request, WS_UserDomainVO userDomain) throws WsInfomovilException
	{
		if ( userDomain == null || userDomain.getNotificacion().isEmpty())
			throw new WsInfomovilException("No se ha capturado el motivo de la cancelacion");
		
		try
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			request.addProperty("domain", DES.encryptDES(userDomain.getDomainName(), dataUsr.getToken()) );
			request.addProperty("Usuario",DES.encryptDES(dataUsr.getNombreUsuario(), dataUsr.getToken()));
			request.addProperty("descripcion",DES.encryptDES(userDomain.getNotificacion(), dataUsr.getToken()));
			request.addProperty("password", DES.encryptDES(userDomain.getPassword(), dataUsr.getToken()));
			request.addProperty("token",DES.encryptDES(dataUsr.getNombreUsuario(),""));
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupGetItemsGratisDomainRequest(SoapObject request)
	{
		// no se mandan datos
	}
	
	public void setupCompraDominioRequest(SoapObject request, String plan) throws WsInfomovilException
	{
		try
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			
			CompraModel compra = dataUsr.getCompra();
			
			request.addProperty("usuario",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			
			if(dataUsr.getStatusDominio().equalsIgnoreCase("Tramite")){//Compra antes de publicar
				String newDomainName = dataUsr.getNombreUsuario();
				dataUsr.getDomainData().setDomainName(newDomainName);
			}
			
			request.addProperty("dominio",DES.encryptDES(dataUsr.getDomainData().getDomainName(),dataUsr.getToken()));
			request.addProperty("plan",DES.encryptDES(compra.getPlan(),dataUsr.getToken()));
			request.addProperty("medioPago",DES.encryptDES("PLAY STORE",dataUsr.getToken()));
			request.addProperty("titulo",DES.encryptDES(compra.getMontoOrigen(),dataUsr.getToken()));
			
			request.addProperty("montoB",DES.encryptDES(compra.getMontoBruto(),dataUsr.getToken()));
			request.addProperty("comision",DES.encryptDES(compra.getComision()+"",dataUsr.getToken()));
			//request.addProperty("montoReal",DES.encryptDES(compra.getMontoReal()+"",""));
			
			request.addProperty("PagoId",DES.encryptDES(compra.getPagoId(),dataUsr.getToken()));
			request.addProperty("statusPago",DES.encryptDES(compra.getStatusPago(),dataUsr.getToken()));
			request.addProperty("codigoCobro",DES.encryptDES(compra.getCodigoCobro(),dataUsr.getToken()));
			request.addProperty("referencia",DES.encryptDES(compra.getReferencia(),dataUsr.getToken()));
			request.addProperty("tipoCompra",DES.encryptDES(compra.getTipoCompra(),dataUsr.getToken()));
			
		} catch (Exception e) {
			throw new WsInfomovilException("Error al armar la peticion");
		}
	}
	
	public void setupCloseSession(SoapObject request) throws WsInfomovilException {
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		try
		{
			request.addProperty("email",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			request.addProperty("token",DES.encryptDES(dataUsr.getToken(),""));
			
		} catch(Exception e) {
			throw new WsInfomovilException("Error al crear la peticion getDomain");
		}
	}
	
	public void setupGetStatusDomain (SoapObject request) throws WsInfomovilException {
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		try
		{
			request.addProperty("domainId",DES.encryptDES(Long.toString(dataUsr.getDomainid()), dataUsr.getToken()));
			request.addProperty("email",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			
		} catch(Exception e) {
			throw new WsInfomovilException("Error al crear la peticion getDomain");
		}
	}
	
	public void setupGetSessionRequest(SoapObject request) throws WsInfomovilException {
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		try
		{
			request.addProperty("email",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			
		} catch(Exception e) {
			throw new WsInfomovilException("Error al crear la peticion getDomain");
		}
	}
	
	public void setupGetDomainRequest(SoapObject request, Activity activity) throws WsInfomovilException
	{
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		try
		{
			request.addProperty("email",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			request.addProperty("password",DES.encryptDES(dataUsr.getPassword(),""));
			request.addProperty("redSocial",DES.encryptDES(dataUsr.getRedSocial(),""));
			request.addProperty("sistema",DES.encryptDES("ANDROID",""));
			PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			request.addProperty("versionSistema",DES.encryptDES(pInfo.versionName,""));
			request.addProperty("suscrito",DES.encryptDES(dataUsr.getSuscritoCompra(),""));
			request.addProperty("tipoPlan",DES.encryptDES(dataUsr.getTipoPlanCompra(),""));
			request.addProperty("medioPago",DES.encryptDES("PLAY STORE",""));
			
			
//			if ( dataUsr.getUserDomainData() != null )
//			{
//				request.addProperty("tipoDispositivo",DES.encryptDES(dataUsr.getUserDomainData().getTypoDispositivo(),""));
//				request.addProperty("sistema",DES.encryptDES(dataUsr.getUserDomainData().getSistema(),""));
//				request.addProperty("notificacion",DES.encryptDES(dataUsr.getUserDomainData().getNotificacion(),""));
//			}
		} catch(Exception e) {
			throw new WsInfomovilException("Error al crear la peticion getDomain");
		}
	}

	public void setupCambiaNombreRecurso(SoapObject request,  String newRecurso) throws WsInfomovilException {
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		try
		{
			request.addProperty("domain",DES.encryptDES(newRecurso,dataUsr.getToken()));
			request.addProperty("domainId",DES.encryptDES(dataUsr.getUsuarioDominioVOActual().getIdDomain(),dataUsr.getToken()));
			request.addProperty("recursoId",DES.encryptDES(dataUsr.getUsuarioDominioVOActual().getIdCtrlDomain(),dataUsr.getToken()));
			request.addProperty("email",DES.encryptDES(dataUsr.getNombreUsuario(),""));
			
		} catch(Exception e) {
			throw new WsInfomovilException("Error al crear la peticion setupCambiaNombreRecurso");
		}
		
	}

	public void setupGetImagenes(SoapObject request, Activity activity) throws WsInfomovilException {
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		try
		{
			request.addProperty("email",dataUsr.getNombreUsuario());
			request.addProperty("domainId", dataUsr.getDomainid());
			request.addProperty("sistema",dataUsr.getRedSocial());
			request.addProperty("sistema","ANDROID");
			PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			request.addProperty("versionSistema",pInfo.versionName);
		} catch(Exception e) {
			throw new WsInfomovilException("Error al crear la peticion getImagenes");
		}
		
	}

	public void setupGetHashMovilizaSitio(SoapObject request, Activity activity) {
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		request.addProperty("email", dataUsr.getNombreUsuario());
	}
	
	public void setupGetHashCambioPassword(SoapObject request, String email) {
		request.addProperty("email", email);
	}

	public void setupGetCatalogoDominios(SoapObject request) {
		// TODO Auto-generated method stub
		
	}
}

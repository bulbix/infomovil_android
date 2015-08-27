/**
 * 
 */
package com.infomovil.infomovil.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.joda.time.LocalDate;

import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DominioVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_InfoUsuarioVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_LocalizacionVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_OffertRecordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;

/**
 * @author Ignaki Dominguez
 *
 */
public class MarshalDatosUsuario
{
	private static String[] clavesDireccion = {"a1","a2","a3","c","pc","sp","tc"};
	private static String[] clavesPerfil = {"pos","sas","oh","pm","tpa","mnu","np"};
	
	public static WS_DominioVO marshalFromDatosUsuarios()
	{
		WS_DominioVO domain = null;
		try
		{
			DatosUsuario dataUsr = DatosUsuario.getInstance();
			
			domain = new WS_DominioVO();
			
			domain.setIdDomain(dataUsr.getDomainid());
			
			if ( dataUsr.getVideoSeleccionado() != null )
				domain.setVideo(dataUsr.getVideoSeleccionado().getLinkVideo());
			
			domain.setToken(dataUsr.getToken());
			domain.setStatusDominio(dataUsr.getStatusDominio());
			domain.setFechaIni(dataUsr.getFechaInicio());
			domain.setFechaFin(dataUsr.getFechaFin());
			domain.setListRecordNaptrVo(dataUsr.getListaContactos());
			
			Vector<WS_KeywordVO> keywordVOs = new Vector<WS_KeywordVO>(20,5);
			if ( dataUsr.getArregloDireccion() != null )
				keywordVOs.addAll(dataUsr.getArregloDireccion());
			if ( dataUsr.getArregloPerfil() != null )
				keywordVOs.addAll(dataUsr.getArregloPerfil());
			if ( dataUsr.getArregloInformacionAdicional() != null )
				keywordVOs.addAll(dataUsr.getArregloInformacionAdicional());
			if ( keywordVOs.size() > 0  )
				domain.setListKeywordVO(keywordVOs);
			
			Vector<WS_DomainVO> domainVOs = new Vector<WS_DomainVO>(1);
			domainVOs.add(dataUsr.getDomainData());
			domain.setListDominioVO(domainVOs);
			
			if ( dataUsr.getCoordenadasUbicacion() != null  )
			{
				Vector<WS_LocalizacionVO> localizacionVOs = new Vector<WS_LocalizacionVO>(1,1);
				localizacionVOs.add(dataUsr.getCoordenadasUbicacion());
				domain.setListLatitud(localizacionVOs);
			}
			
			if ( dataUsr.getPromocionDominio() != null )
			{
				Vector<WS_OffertRecordVO> offertRecordVOs = new Vector<WS_OffertRecordVO>(1,1);
				offertRecordVOs.add(dataUsr.getPromocionDominio());
				domain.setListOffertRecordVO(offertRecordVOs);
			}
			
			
			if ( dataUsr.getListaImagenes() != null && dataUsr.getListaImagenes().size() > 0 )
			{
				Vector<WS_ImagenVO> imagenVOs = new Vector<WS_ImagenVO>(dataUsr.getListaImagenes());
				domain.setListImagenVO(imagenVOs);
			}
			
			if ( dataUsr.getImagenLogo() != null )
			{
				if ( domain.getListImagenVO() == null )
				{
					domain.setListImagenVO(new Vector<WS_ImagenVO>(1));
				}
				domain.getListImagenVO().add(dataUsr.getImagenLogo());
			}
			
			if ( dataUsr.getInfoUsr() != null )
			{
				Vector<WS_InfoUsuarioVO> infoUsuarioVOs = new Vector<WS_InfoUsuarioVO>(1,1);
				infoUsuarioVOs.add(dataUsr.getInfoUsr());
				domain.setListInfoUsuarioVO(infoUsuarioVOs);
			}
			
			if ( dataUsr.getItemsDominio() != null && dataUsr.getItemsDominio().size() > 0 )
			{
				Vector<WS_StatusDomainVO> statusDomainVOs = new Vector<WS_StatusDomainVO>(dataUsr.getItemsDominio());
				domain.setListStatusDomainVO(statusDomainVOs);
			}
			
		} catch (Exception e) {
			domain = null;
		}
		
		return domain;
	}
	
	public static void populateDatosUsuario(WS_DominioVO domain)
	{
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		
		try
		{
			dataUsr.setDomainid(domain.getIdDomain());
			
			if ( dataUsr.getVideoSeleccionado() == null )
				dataUsr.setVideoSeleccionado(new ItemSelectModel());
			
			dataUsr.getVideoSeleccionado().setLinkVideo(domain.getVideo());
			dataUsr.setToken(domain.getToken());
			dataUsr.setStatusDominio(domain.getStatusDominio());
			dataUsr.setDescripcionDominio(domain.getDescripcionDominio());
			dataUsr.setCodeCamp(domain.getCodeCamp());
			dataUsr.setFechaInicio(domain.getFechaIni());
			dataUsr.setFechaFin(domain.getFechaFin());
//			dataUsr.setFechaInicioTel(domain.getFechaIniTel());
//			dataUsr.setFechaFinTel(domain.getFechaFinTel());
			
			dataUsr.setCanal(domain.getCanal());
			dataUsr.setCampania(domain.getCampania());
			dataUsr.setTipoUsuario(domain.getTipoUsuario());
			
			if(CuentaUtils.isUsuarioCanal()){
				InfomovilApp.tipoInfomovil = "tel";
			}
			else{
				InfomovilApp.tipoInfomovil = "recurso";
			}
			
			
			dataUsr.setListaContactos(domain.getListRecordNaptrVo());
			
			// Se crea el arreglo vacio de 7 elementos para el perfil
			Vector<WS_KeywordVO> listaPerfil = new Vector<WS_KeywordVO>(7);
			WS_KeywordVO temp_KeywordVO;
			for (int i = 0; i < 7 ; i++)
			{
				temp_KeywordVO = new WS_KeywordVO();
				temp_KeywordVO.setKeywordField(clavesPerfil[i]);
				listaPerfil.add(temp_KeywordVO);
			}
			dataUsr.setArregloPerfil(listaPerfil);
			if ( domain.getListKeywordVO() != null )
				clasificaKeyWordRecords(domain.getListKeywordVO());
			
			if ( domain.getListDominioVO() != null )
				dataUsr.setDomainData(domain.getListDominioVO().firstElement());
			
			if ( domain.getListLatitud() != null && domain.getListLatitud().size() > 0 )
				dataUsr.setCoordenadasUbicacion(domain.getListLatitud().firstElement());
			
			if ( domain.getListOffertRecordVO() != null && domain.getListOffertRecordVO().size() > 0 )
				dataUsr.setPromocionDominio(domain.getListOffertRecordVO().firstElement());
			
			if ( domain.getListImagenVO() != null )
				clasificaImagenes(domain.getListImagenVO());
			
			if ( domain.getListInfoUsuarioVO() != null && domain.getListInfoUsuarioVO().size() > 0 )
				dataUsr.setInfoUsr(domain.getListInfoUsuarioVO().firstElement());
			
			if ( domain.getListStatusDomainVO() != null )
				dataUsr.setItemsDominio(domain.getListStatusDomainVO());
			
			if ( domain.getListStatusDomainGratisVO() != null )
				dataUsr.setItemsDominioGratuito(domain.getListStatusDomainGratisVO());
			
			if ( domain.getListUsuarioDominiosVO() != null )
				dataUsr.setUsuarioDominios(domain.getListUsuarioDominiosVO());
			
			setDominioFechas();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * Asigna nombre de dominio y fechas de dominio
	 * @param dataUsr
	 */
	public static void setDominioFechas() {
		
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		if(dataUsr.getUsuarioDominios() != null){
			for(WS_UsuarioDominiosVO usuarioDominios: dataUsr.getUsuarioDominios()){
				
				if(usuarioDominios.getDomainType().equalsIgnoreCase("tel")){//Le damos prioridad punto tel

					String newDomainName = usuarioDominios.getDomainCtrlName();
					dataUsr.getDomainData().setDomainName(newDomainName);
					dataUsr.setFechaInicioTel(usuarioDominios.getFechaCtrlIni());
					dataUsr.setFechaFinTel(usuarioDominios.getFechaCtrlFin());
					
					InfomovilApp.tipoInfomovil="tel";
					InfomovilApp.urlInfomovil=usuarioDominios.getUrlSitio();
					
					if(usuarioDominios.getVigente().equalsIgnoreCase("SI")){
						break;
					}
					
				}
				else if(usuarioDominios.getDomainType().equalsIgnoreCase("recurso")){
					
					String newDomainName = usuarioDominios.getDomainCtrlName();
					dataUsr.getDomainData().setDomainName(newDomainName);
					dataUsr.setFechaInicioTel("");
					dataUsr.setFechaFinTel("");
					
					InfomovilApp.tipoInfomovil="recurso";
					InfomovilApp.urlInfomovil=usuarioDominios.getUrlSitio();
					
					break;
				}
				
			}
		}
	}
	
	public static void clasificaImagenes(Vector<WS_ImagenVO> imagenVOs)
	{
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		for (WS_ImagenVO wsImagenVO : imagenVOs) 
		{
			if ( wsImagenVO.getTypeImage().equalsIgnoreCase("Logo") )
			{
				datosUsuario.setImagenLogo(wsImagenVO);
				break;
			}
		}
		
		if ( datosUsuario.getImagenLogo() != null )
		{
			imagenVOs.remove(datosUsuario.getImagenLogo());
		}
		datosUsuario.setListaImagenes(imagenVOs);
	}
	
	public static void fusionaImagenes(Vector<WS_ImagenVO> imagenVOs)
	{
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		Vector<WS_ImagenVO> imagenesList = datosUsuario.getListaImagenes();
		
		for (WS_ImagenVO wsImagenVO : imagenVOs) 
		{
			//Recuperamos cache
			if(imagenesList.indexOf(wsImagenVO) != -1){
				wsImagenVO.setImagenPath(imagenesList.get(imagenesList.indexOf(wsImagenVO)).getImagenPath());
			}
			
			if ( wsImagenVO.getTypeImage().equalsIgnoreCase("Logo") ){
				datosUsuario.setImagenLogo(wsImagenVO);
			}
		}
		
		if ( datosUsuario.getImagenLogo() != null ){
			imagenVOs.remove(datosUsuario.getImagenLogo());
		}
		
		datosUsuario.setListaImagenes(imagenVOs);
	}
	
	private static void clasificaKeyWordRecords(Vector<WS_KeywordVO> keywordVOs)
	{
		DatosUsuario dataUsr = DatosUsuario.getInstance();
		
		ArrayList<String>arrClavesDir	 = new ArrayList<String>(Arrays.asList(clavesDireccion));
		ArrayList<String>arrClavesPerfil = new ArrayList<String>(Arrays.asList(clavesPerfil));
		
		int index;
		Vector<WS_KeywordVO>listaDireccion	= new Vector<WS_KeywordVO>(5,3);
		Vector<WS_KeywordVO>listaPerfil		= dataUsr.getArregloPerfil();
		Vector<WS_KeywordVO>listaInfoAd		= new Vector<WS_KeywordVO>(1,1);
		
		for (WS_KeywordVO ws_KeywordVO : keywordVOs)
		{
			if (  arrClavesDir.indexOf(ws_KeywordVO.getKeywordField()) >= 0 )
				listaDireccion.add(ws_KeywordVO);
			else if (  ( index = arrClavesPerfil.indexOf(ws_KeywordVO.getKeywordField()) ) >= 0 ) {
				listaPerfil.remove(index);
				listaPerfil.insertElementAt(ws_KeywordVO, index);
			} else
				listaInfoAd.add(ws_KeywordVO);
		}
		
		dataUsr.setArregloDireccion(listaDireccion);
		dataUsr.setArregloInformacionAdicional(listaInfoAd);
	}

}

package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.util.DES;
import static org.apache.commons.lang3.StringUtils.*;

public class WS_DominioVO implements KvmSerializable
{
	private static final int K_IDDOMAIN = 0;
	private static final int K_VIDEO = 1;
	private static final int K_TOKEN = 2;
	private static final int K_STATUSDOMINIO = 3;
	private static final int K_FECHAINI = 4;
	private static final int K_FECHAFIN = 5;
	private static final int K_LISTRECORDNAPTRVO = 6;
	private static final int K_LISTKEYWORDVO = 7;
	private static final int K_LISTDOMINIOVO = 8;
	private static final int K_LISTLATITUD = 9;
	private static final int K_LISTOFFERTRECORDVO = 10;
	private static final int K_LISTIMAGENVO = 11;
	private static final int K_LISTINFOUSUARIOVO = 12;
	private static final int K_LISTSTATUSDOMAINVO = 13;
	private static final int K_CODECAMP = 14;
	private static final int K_FECHAINITEL = 15;
	private static final int K_FECHAFINTEL = 16;
	private static final int K_LISTUSUARIODOMINIOSVO = 17;
	private static final int K_DESCRIPCIONDOMINIO = 18;
	private static final int K_LISTSTATUSDOMAINGRATISVO = 19;
	
	private static final int K_CANAL = 20;
	private static final int K_CAMPANIA = 21;
	private static final int K_TIPOUSUARIO = 22;
	

	private long idDomain;
	private String video;
	private String token;
	private String statusDominio;
	private String fechaIni;
	private String fechaFin;
	private String fechaIniTel;
	private String fechaFinTel;	
	private String descripcionDominio;
	
	private String codeCamp;
	private Vector<WS_RecordNaptrVO> listRecordNaptrVo;
	private Vector<WS_KeywordVO> listKeywordVO;
	private Vector<WS_DomainVO> listDominioVO;
	private Vector<WS_LocalizacionVO> listLatitud;
	private Vector<WS_OffertRecordVO> listOffertRecordVO;
	private Vector<WS_ImagenVO> listImagenVO;
	private Vector<WS_InfoUsuarioVO> listInfoUsuarioVO;
	private Vector<WS_StatusDomainVO> listStatusDomainVO;
	private Vector<WS_StatusDomainVO> listStatusDomainGratisVO;
	private Vector<WS_UsuarioDominiosVO> listUsuarioDominiosVO;
	
	private String canal;
	private String campania;
	private String tipoUsuario;
	
	
	//Para la compra
	private String referencia;
	private String resultado;
	
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
			String strToken				= datosUsuario.getToken();
	        switch(arg0)
	        {
		        case K_IDDOMAIN:
		        	return isEmpty(strToken) ? idDomain : DES.encryptDES(Long.toString(idDomain), strToken);
		        case K_VIDEO:
		        	return isEmpty(strToken) ? video : DES.encryptDES(video, strToken);
		        case K_TOKEN:
		        	return isEmpty(strToken) ? strToken : DES.encryptDES(token, strToken);
		        case K_STATUSDOMINIO:
		        	return isEmpty(strToken) ? statusDominio : DES.encryptDES(statusDominio, strToken);
		        case K_DESCRIPCIONDOMINIO:
		        	return isEmpty(strToken) ? descripcionDominio : DES.encryptDES(descripcionDominio, strToken);
		        case K_FECHAINI:
		        	return isEmpty(strToken) ? fechaIni : DES.encryptDES(fechaIni, strToken);
		        case K_FECHAFIN:
		        	return isEmpty(strToken) ? fechaFin : DES.encryptDES(fechaFin, strToken);
		        case K_FECHAINITEL:
		        	return isEmpty(strToken) ? fechaIni : DES.encryptDES(fechaIniTel, strToken);
		        case K_FECHAFINTEL:
		        	return isEmpty(strToken) ? fechaFin : DES.encryptDES(fechaFinTel, strToken);
		        case K_CODECAMP:
		        	return isEmpty(strToken) ? codeCamp : DES.encryptDES(codeCamp, strToken);
		        case K_LISTRECORDNAPTRVO:
		        	return listRecordNaptrVo;
		        case K_LISTKEYWORDVO:
		        	return listKeywordVO;
		        case K_LISTDOMINIOVO:
		        	return listDominioVO;
		        case K_LISTLATITUD:
		        	return listLatitud;
		        case K_LISTOFFERTRECORDVO:
		        	return listOffertRecordVO;
		        case K_LISTIMAGENVO:
		        	return listImagenVO;
		        case K_LISTINFOUSUARIOVO:
		        	return listInfoUsuarioVO;
		        case K_LISTSTATUSDOMAINVO:
		        	return listStatusDomainVO;
		        case K_LISTSTATUSDOMAINGRATISVO:
		        	return listStatusDomainGratisVO;
		        case K_LISTUSUARIODOMINIOSVO:
		        	return listUsuarioDominiosVO;
		        	
		        case K_CANAL:
		        	return isEmpty(strToken) ? codeCamp : DES.encryptDES(canal, strToken);
		        case K_CAMPANIA:
		        	return isEmpty(strToken) ? codeCamp : DES.encryptDES(campania, strToken);
		        case K_TIPOUSUARIO:
		        	return isEmpty(strToken) ? codeCamp : DES.encryptDES(tipoUsuario, strToken);
		        	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 23;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes")Hashtable arg1, PropertyInfo info)
	{
        switch(index)
        {
	        case K_IDDOMAIN: info.type = PropertyInfo.LONG_CLASS; info.name = "idDomain"; break;
	        case K_VIDEO: info.type = PropertyInfo.STRING_CLASS; info.name = "video"; break;
	        case K_TOKEN: info.type = PropertyInfo.STRING_CLASS; info.name = "token"; break;
	        case K_STATUSDOMINIO: info.type = PropertyInfo.STRING_CLASS; info.name = "statusDominio"; break;
	        case K_DESCRIPCIONDOMINIO: info.type = PropertyInfo.STRING_CLASS; info.name = "descripcionDominio"; break;
	        case K_FECHAINI: info.type = PropertyInfo.STRING_CLASS; info.name = "fechaIni"; break;
	        case K_FECHAFIN: info.type = PropertyInfo.STRING_CLASS; info.name = "fechaFin"; break;
	        case K_FECHAINITEL: info.type = PropertyInfo.STRING_CLASS; info.name = "fTelNamesIni"; break;
	        case K_FECHAFINTEL: info.type = PropertyInfo.STRING_CLASS; info.name = "fTelNamesFin"; break;
	        
	        case K_CODECAMP: info.type = PropertyInfo.STRING_CLASS; info.name = "codeCamp"; break;
	        case K_LISTRECORDNAPTRVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listRecordNaptrVo"; break;
	        case K_LISTKEYWORDVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listKeywordVO"; break;
	        case K_LISTDOMINIOVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listDominioVO"; break;
	        case K_LISTLATITUD: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listLatitud"; break;
	        case K_LISTOFFERTRECORDVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listOffertRecordVO"; break;
	        case K_LISTIMAGENVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listImagenVO"; break;
	        case K_LISTINFOUSUARIOVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listInfoUsuarioVO"; break;
	        case K_LISTSTATUSDOMAINVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listStatusDomainVO"; break;
	        case K_LISTSTATUSDOMAINGRATISVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listStatusDomainGratisVO"; break;
	        case K_LISTUSUARIODOMINIOSVO: info.type = PropertyInfo.VECTOR_CLASS; info.name = "listUsuarioDominiosVO"; break;
	        
	        case K_CANAL: info.type = PropertyInfo.STRING_CLASS; info.name = "canal"; break;
	        case K_CAMPANIA: info.type = PropertyInfo.STRING_CLASS; info.name = "campania"; break;
	        case K_TIPOUSUARIO: info.type = PropertyInfo.STRING_CLASS; info.name = "tipoUsuario"; break;
	        
	        
			default:break;
        }
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setProperty(int index, Object value)
	{
        switch(index)
        {
	        case K_IDDOMAIN: idDomain = Long.parseLong(value.toString());break;
	        case K_VIDEO: video = value.toString();break;
	        case K_TOKEN: token = value.toString();break;
	        case K_STATUSDOMINIO: statusDominio = value.toString();break;
	        case K_DESCRIPCIONDOMINIO: descripcionDominio = value.toString();break;
	        case K_FECHAINI: fechaIni = value.toString();break;
	        case K_FECHAFIN: fechaFin = value.toString();break;
	        case K_FECHAINITEL: fechaIniTel = value.toString();break;
	        case K_FECHAFINTEL: fechaFinTel = value.toString();break;
	        case K_CODECAMP: codeCamp = value.toString();break;
	        case K_LISTRECORDNAPTRVO: listRecordNaptrVo = (Vector<WS_RecordNaptrVO>)value;break;
	        case K_LISTKEYWORDVO: listKeywordVO = (Vector<WS_KeywordVO>)value;break;
	        case K_LISTDOMINIOVO: listDominioVO = (Vector<WS_DomainVO>)value;break;
	        case K_LISTLATITUD: listLatitud = (Vector<WS_LocalizacionVO>)value;break;
	        case K_LISTOFFERTRECORDVO: listOffertRecordVO = (Vector<WS_OffertRecordVO>)value;break;
	        case K_LISTIMAGENVO: listImagenVO = (Vector<WS_ImagenVO>)value;break;
	        case K_LISTINFOUSUARIOVO: listInfoUsuarioVO = (Vector<WS_InfoUsuarioVO>)value;break;
	        case K_LISTSTATUSDOMAINVO: listStatusDomainVO = (Vector<WS_StatusDomainVO>)value;break;
	        case K_LISTSTATUSDOMAINGRATISVO: listStatusDomainGratisVO = (Vector<WS_StatusDomainVO>)value;break;
	        case K_LISTUSUARIODOMINIOSVO: listUsuarioDominiosVO = (Vector<WS_UsuarioDominiosVO>)value;break;
	        
	        case K_CANAL: canal = value.toString();break;
	        case K_CAMPANIA: campania = value.toString();break;
	        case K_TIPOUSUARIO: tipoUsuario = value.toString();break;
	        
	        
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
	        case K_IDDOMAIN: return idDomain;
	        case K_VIDEO: return video;
	        case K_TOKEN: return token;
	        case K_STATUSDOMINIO: return statusDominio;
	        case K_DESCRIPCIONDOMINIO: return descripcionDominio;
	        case K_FECHAINI: return fechaIni;
	        case K_FECHAFIN: return fechaFin;
	        case K_FECHAINITEL: return fechaIniTel;
	        case K_FECHAFINTEL: return fechaFinTel;
	        case K_CODECAMP: return codeCamp;
	        case K_LISTRECORDNAPTRVO: return listRecordNaptrVo;
	        case K_LISTKEYWORDVO: return listKeywordVO;
	        case K_LISTDOMINIOVO: return listDominioVO;
	        case K_LISTLATITUD: return listLatitud;
	        case K_LISTOFFERTRECORDVO: return listOffertRecordVO;
	        case K_LISTIMAGENVO: return listImagenVO;
	        case K_LISTINFOUSUARIOVO: return listInfoUsuarioVO;
	        case K_LISTSTATUSDOMAINVO: return listStatusDomainVO;
	        case K_LISTSTATUSDOMAINGRATISVO: return listStatusDomainGratisVO;
	        case K_LISTUSUARIODOMINIOSVO: return listUsuarioDominiosVO;
	        
	        case K_CANAL: return canal;
	        case K_CAMPANIA: return campania;
	        case K_TIPOUSUARIO: return tipoUsuario;
	        
        }
        
        return null;
	}
	
	public long getIdDomain() {
		return idDomain;
	}
	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getStatusDominio() {
		return statusDominio;
	}
	public void setStatusDominio(String statusDominio) {
		this.statusDominio = statusDominio;
	}
	public String getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Vector<WS_RecordNaptrVO> getListRecordNaptrVo() {
		return listRecordNaptrVo;
	}
	public void setListRecordNaptrVo(Vector<WS_RecordNaptrVO> listRecordNaptrVo) {
		this.listRecordNaptrVo = listRecordNaptrVo;
	}
	public Vector<WS_KeywordVO> getListKeywordVO() {
		return listKeywordVO;
	}
	public void setListKeywordVO(Vector<WS_KeywordVO> listKeywordVO) {
		this.listKeywordVO = listKeywordVO;
	}
	public Vector<WS_DomainVO> getListDominioVO() {
		return listDominioVO;
	}
	public void setListDominioVO(Vector<WS_DomainVO> listDominioVO) {
		this.listDominioVO = listDominioVO;
	}
	public Vector<WS_LocalizacionVO> getListLatitud() {
		return listLatitud;
	}
	public void setListLatitud(Vector<WS_LocalizacionVO> listLatitud) {
		this.listLatitud = listLatitud;
	}
	public Vector<WS_OffertRecordVO> getListOffertRecordVO() {
		return listOffertRecordVO;
	}
	public void setListOffertRecordVO(Vector<WS_OffertRecordVO> listOffertRecordVO) {
		this.listOffertRecordVO = listOffertRecordVO;
	}
	public Vector<WS_ImagenVO> getListImagenVO() {
		return listImagenVO;
	}
	public void setListImagenVO(Vector<WS_ImagenVO> listImagenVO) {
		this.listImagenVO = listImagenVO;
	}
	public Vector<WS_InfoUsuarioVO> getListInfoUsuarioVO() {
		return listInfoUsuarioVO;
	}
	public void setListInfoUsuarioVO(Vector<WS_InfoUsuarioVO> listInfoUsuarioVO) {
		this.listInfoUsuarioVO = listInfoUsuarioVO;
	}
	public Vector<WS_StatusDomainVO> getListStatusDomainVO() {
		return listStatusDomainVO;
	}
	public void setListStatusDomainVO(Vector<WS_StatusDomainVO> listStatusDomainVO) {
		this.listStatusDomainVO = listStatusDomainVO;
	}
	
	public String getCodeCamp() {
		return codeCamp;
	}
	public void setCodeCamp(String codeCamp) {
		this.codeCamp = codeCamp;
	}
	public String getFechaIniTel() {
		return fechaIniTel;
	}
	public void setFechaIniTel(String fechaIniTel) {
		this.fechaIniTel = fechaIniTel;
	}
	public String getFechaFinTel() {
		return fechaFinTel;
	}
	public void setFechaFinTel(String fechaFinTel) {
		this.fechaFinTel = fechaFinTel;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public Vector<WS_UsuarioDominiosVO> getListUsuarioDominiosVO() {
		return listUsuarioDominiosVO;
	}
	public void setListUsuarioDominiosVO(
			Vector<WS_UsuarioDominiosVO> listUsuarioDominiosVO) {
		this.listUsuarioDominiosVO = listUsuarioDominiosVO;
	}
	public String getDescripcionDominio() {
		return descripcionDominio;
	}
	public void setDescripcionDominio(String descripcionDominio) {
		this.descripcionDominio = descripcionDominio;
	}
	public Vector<WS_StatusDomainVO> getListStatusDomainGratisVO() {
		return listStatusDomainGratisVO;
	}
	public void setListStatusDomainGratisVO(
			Vector<WS_StatusDomainVO> listStatusDomainGratisVO) {
		this.listStatusDomainGratisVO = listStatusDomainGratisVO;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	public String getCampania() {
		return campania;
	}
	public void setCampania(String campania) {
		this.campania = campania;
	}
	public String getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
}

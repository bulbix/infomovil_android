/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil;

import java.io.IOException;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.principal.LoginActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.MarshalDouble;
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
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_VisitaVO;

/**
 * @author Ignaki Dominguez
 *
 */
public class WsInfomovilCall extends AsyncTask<WSInfomovilMethods, Integer, WsInfomovilProcessStatus>
{ 
	private static final String NAMESPACE	= "http://ws.webservice.infomovil.org/";
	private static final String URL			= "http://qa.mobileinfo.io//WsInfomovil/wsInfomovildomain";
//	private static final String URL			= "http://infomovil.com/WsInfomovil/wsInfomovildomain";
//	private static final String URL			= "http://172.17.3.192:8080/WsInfomovil/wsInfomovildomain";
//	private static final String URL			= "http://172.16.51.30:8080/WsInfomovil/wsInfomovildomain";
	
	private static final String SOAP_ACTION	= "";
	 
	private WsInfomovilDelgate delegate;
	private WS_DominioVO dominioVO;
	
	private WS_DeleteItem deleteItem;
	private WS_OffertRecordVO offerRecord;
	private WS_LocalizacionVO ubicacion;
	private WS_DomainVO domain;
	private WS_UserDomainVO usrDomain;  
	private WS_InfoUsuarioVO infoUsr;
	private ItemSelectModel modeloVideo;
	private String strConsulta;
	private Vector<WS_ImagenVO> lstImagen;
	private Vector<WS_RecordNaptrVO> lstContactos; 
	private Vector<WS_KeywordVO> lstKeyword;
	private Vector<WS_VisitaVO> lstVisita;
	
	private Exception exception;
	private WSInfomovilMethods metodo;
	private String actividad;
	private Activity activity;
	
	public WsInfomovilCall(WsInfomovilDelgate delegate, Activity activity)
	{
		this.delegate = delegate;
		this.activity = activity;
	}
	
    // Do the long-running work in here
    @Override
	protected WsInfomovilProcessStatus doInBackground(WSInfomovilMethods... method)
    {	
      	if(!InfomovilApp.isConnected(activity)){
      		return  WsInfomovilProcessStatus.ERROR_CONEXION;
    	}
    	
      	WsInfomovilProcessStatus resultado	= WsInfomovilProcessStatus.ERROR_GENERICO;
    	
    	SoapObject request					= null;
    	SoapSerializationEnvelope envelope	= null;
    	HttpTransportSE transporte			= null;
    	this.metodo							= method[0];
    	
    	try
    	{
    		request					= setupMethodRequest();
    		envelope				= new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		envelope.encodingStyle	= SoapEnvelope.XSD;
    		MarshalDouble md		= new MarshalDouble();
    		
    		
    		Log.d("infoLog","request : "+request);
    		
			//Se envuelve la peticion soap
			md.register(envelope);
			envelope.setOutputSoapObject(request);
			
			
			
			transporte = new HttpTransportSE(URL, 60000);//60000
			transporte.call(SOAP_ACTION, envelope);
			
			 
			
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			    SoapObject soapObject = (SoapObject) envelope.bodyIn;
			    resultado = processResponse(soapObject);
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
			    SoapFault soapFault = (SoapFault) envelope.bodyIn;
			    throw new Exception(soapFault.getMessage());
			}
    		
			
	    	
		} catch (IOException e) {
			Log.e("Conexion Infomovil", "ERROR_IO", e);
			exception = e;
			resultado = WsInfomovilProcessStatus.ERROR_IO;
		} catch (XmlPullParserException e) {
			Log.e("Conexion Infomovil", "ERROR_PARSER_XML", e);
			exception = e;
			resultado = WsInfomovilProcessStatus.ERROR_PARSER_XML;
		} catch (WsInfomovilException e) {
			Log.e("Conexion Infomovil", "ERROR_WSINFOMOVIL", e);
			exception = e;
			resultado = WsInfomovilProcessStatus.ERROR_WSINFOMOVIL;
		} catch (Exception e) {
			Log.e("Conexion Infomovil", "ERROR_GENERICO", e);
			exception = e;
			resultado = WsInfomovilProcessStatus.ERROR_GENERICO;
		}
    	
        return resultado;
    }

    // This is called each time you call publishProgress()
    @Override
	protected void onProgressUpdate(Integer... progress) {
    }
    
    
    @Override
	protected void onPreExecute (){    
    	
    }
    
    /****
     * Manda Mensaje diciendo que hay mas de una sesion
     */
    private void showMensaje(int idMensaje){ 	
    	
    	Activity activity = null;
    	
    	if(delegate instanceof Activity){
			activity = (Activity)delegate;
		}
		else if(delegate instanceof Fragment){
			Fragment fragment = (Fragment)delegate;
			activity = fragment.getActivity();
		}    	
    	
    	if(activity!= null && !activity.isFinishing()){
    		final AlertView dialog = new AlertView(activity, AlertViewType.AlertViewTypeInfo,		
    		activity.getResources().getString(idMensaje));    	
    		dialog.setDelegado(new SalirCuenta(activity, dialog));
    		dialog.show();    		
    	}
    }

    
    // This is called when doInBackground() is finished
    @Override
	protected void onPostExecute(WsInfomovilProcessStatus resultado)
    {
    	if ( delegate == null )
    		return;
    	
    	long milisegundo = 0; // TODO calcular el tiempo que tarda    	

    	if (resultado == WsInfomovilProcessStatus.ERROR_TOKEN || resultado == WsInfomovilProcessStatus.ERROR_SESION) {
    		
    		if(delegate instanceof LoginActivity ){
    			showMensaje(R.string.txtNoExisteUsuario);
    		}
    		else{
    			showMensaje(R.string.msgCaducaSesion);
    		}    			
    		
    	}
    	else if ( resultado.getValue() >= 0 && metodo != WSInfomovilMethods.GET_ESTADISTICAS ){
    		
    		//Revisa si el sitio esta editado
    		Appboy.getInstance(activity).getCurrentUser().setCustomUserAttribute("sitioEditado", MenuPasosActivity.fueEditado(activity.getResources(),true));
    		
        	delegate.respuestaCompletada(metodo, milisegundo, resultado);
        	
        } else if ( resultado.getValue() >= 0  ) {        	
        	delegate.respuestaObj(metodo, resultado, lstVisita);
        } else{
        	
        	if(resultado == WsInfomovilProcessStatus.ERROR_CONEXION 
        			&& metodo != WSInfomovilMethods.CLOSE_SESSION && metodo != WSInfomovilMethods.COMPRA_DOMINIO ){
        		AlertView mensajeError = new AlertView(activity, AlertViewType.AlertViewTypeInfo, 
        	    		activity.getResources().getString(R.string.txtNoConexion));
        				mensajeError.setDelegado(new AlertViewCloseDialog(mensajeError));
        				mensajeError.show();
        	}
        	
        	
        	delegate.respuestaErronea(resultado, exception);
        }      	       
    	
    }  
    

    
    private SoapObject setupMethodRequest() throws WsInfomovilException
    {
    	SoapObject request				= new SoapObject(NAMESPACE, metodo.getMetodo());
    	WsInfomovilRequest wsRequest	= new WsInfomovilRequest();
    	
    	switch (metodo)
    	{
    		case GET_EXIST_DOMAIN:
    			wsRequest.setupGetExistDomainRequest(request);
    			break;
			case GET_EXIST_USER:
				wsRequest.setupGetExistUserRequest(request,strConsulta);
				break;
			case INSERT_USER_DOMAIN:
			case INSERT_USER_CAMP:
				wsRequest.setupInsertUserDomainRequest(request, usrDomain);
				break;
			case CREATE_DOMAIN:
				wsRequest.setupCreateDomainRequest(request, dominioVO);
				break;
				
			case UPDATE_DOMAIN:
			case UPDATE_TEXT_RECORD:
			case UPDATE_COLOR:
			case UPDATE_DES_CORTA:
			case UPDATE_TEMPLATE:
				wsRequest.setupUpdateDomainRequest(request, domain);
				break;			
				
			case UPDATE_KEY_WORD_DATA:
				wsRequest.setupUpdateKeyWordDataRequest(request, lstKeyword);
				break;
			case UPDATE_RECORD_NAPTR:
				wsRequest.setupUpdateRecordNaptrRequest(request, lstContactos);
				break;
			case DELETE_IMAGE:
				wsRequest.setupDeleteImageRequest(request, deleteItem);
				break;
			case DELETE_KEY_WORD_DATA:
				wsRequest.setupDeleteKeyWordDataRequest(request, deleteItem);
				break;
			case DELETE_RECORD_NAPTR:
				wsRequest.setupDeleteRecordNaptrRequest(request, deleteItem);
				break;
			case DELETE_VIDEO:
				wsRequest.setupDeleteVideoRequest(request);
				break;
			case DELETE_LOC_RECORD:
				wsRequest.setupDeleteLocRecordRequest(request);
				break;
			case DELETE_OFFER_RECORD:
				wsRequest.setupDeleteOfferRecordRequest(request, deleteItem);
				break;
			case INSERT_VIDEO:
				wsRequest.setupInsertVideoRequest(request, modeloVideo);
				break;
			case INSERT_IMAGE:
				wsRequest.setupInsertImageRequest(request, lstImagen.firstElement(), activity);
				break;
			case UPDATE_IMAGE:
				wsRequest.setupUpdateImageRequest(request, lstImagen, activity);
				break;
			case UPDATE_INSERT_LOC_RECORD:
				wsRequest.setupUpdateInserLocRecordRequest(request, ubicacion);
				break;
			case INSERT_RECORD_NAPTR:
				wsRequest.setupInsertRecordNaptrRequest(request, lstContactos);
				break;
			case UPDATEINSERT_OFFER_RECORD:
				wsRequest.setupUpdateinsertOfferRecordRequest(request, offerRecord);
				break;
			case INSERT_KEY_WORD_DATA:
				wsRequest.setupInsertKeyWordDataRequest(request, lstKeyword);
				break;
			case INSERT_KEYWORD_DATA_ADDRESS:
				wsRequest.setupInsertKeywordDataAddressRequest(request, lstKeyword);
				break;
			case GET_ESTADISTICAS:
				wsRequest.setupGetNumVisitasRequest(request,strConsulta);
				break;
			case GET_FECHA_EXPIRACION:
				wsRequest.setupGetFechaExpiracionRequest(request);
				break;
			case INSERT_UPDATE_INFO_USER:
				wsRequest.setupInsertUpdateInfoUserRequest(request, infoUsr);
				break;
			case CAMBIO_PASSWORD:
				wsRequest.setupCambioPasswordRequest(request,strConsulta);
				break;
			case CANCELAR_CUENTA:
				wsRequest.setupCancelarCuentaRequest(request, usrDomain);
				break;
			case GET_ITEMS_GRATIS_DOMAIN:
				wsRequest.setupGetItemsGratisDomainRequest(request);
				break;
			case COMPRA_DOMINIO:
			case COMPRA_INTENTO:
				wsRequest.setupCompraDominioRequest(request,strConsulta);
				break;
			case GET_DOMAIN:
			case GET_DOMAIN_LOGIN:
				wsRequest.setupGetDomainRequest(request, activity); 
				break;
			case GET_SESSION:
				wsRequest.setupGetSessionRequest(request);
				break;
			case CLOSE_SESSION:
				wsRequest.setupCloseSession(request);
				break;
			case GET_STATUS_DOMAIN:
				wsRequest.setupGetStatusDomain(request);
				break;
			case GET_EXIST_CAMPANIABYMAIL:
				wsRequest.setupGetExistCampaniaByMailRequest(request);
    			break;
			case CAMBIA_NOMBRE_RECURSO:
				wsRequest.setupCambiaNombreRecurso(request, strConsulta);
				break;
			case GET_IMAGENES:
				wsRequest.setupGetImagenes(request,activity);
				break;
			case GET_HASH_MOVILIZA_SITIO:
				wsRequest.setupGetHashMovilizaSitio(request,activity);
				break;
			case GET_HASH_CAMBIO_PASSWORD:
				wsRequest.setupGetHashCambioPassword(request,strConsulta);
				break;
			case GET_CATALOGO_DOMINIOS:
				wsRequest.setupGetCatalogoDominios(request);
				
				
				
			default:break;
		}
    	
    	return request;
    }
    
    private WsInfomovilProcessStatus processResponse(SoapObject resultsRequestSOAP) throws WsInfomovilException
    {
    	WsInfomovilResponse wsResponse	= new WsInfomovilResponse(); 
    	WsInfomovilProcessStatus status	= WsInfomovilProcessStatus.ERROR_DESCONOCIDO;
    	 
    	switch (metodo)
    	{
    		case GET_EXIST_DOMAIN:
    			status = wsResponse.processGetExistDomainResponse(resultsRequestSOAP);
    			break;
			case GET_EXIST_USER:
				status = wsResponse.processGetExistUserResponse(resultsRequestSOAP);
				break;
			case INSERT_USER_DOMAIN:
			case INSERT_USER_CAMP:
				status = wsResponse.processInsertUserDomainResponse(resultsRequestSOAP,usrDomain,metodo, activity);
				break;
				
			case CREATE_DOMAIN:
				status = wsResponse.processCreateDomainResponse(resultsRequestSOAP,dominioVO);
				break;
			case UPDATE_DOMAIN:			
			case UPDATE_TEXT_RECORD:
			case UPDATE_COLOR:
			case UPDATE_DES_CORTA:
			case UPDATE_TEMPLATE:
				
				Appboy.getInstance(activity).getCurrentUser().setCustomUserAttribute("estilo", 
						!StringUtils.isEmpty(DatosUsuario.getInstance().getDomainData().getTemplate()));
				
				status = wsResponse.processUpdateDomainResponse(resultsRequestSOAP, domain);
				break;
			case UPDATE_KEY_WORD_DATA:
				status = wsResponse.processUpdateKeyWordDataResponse(resultsRequestSOAP, lstKeyword);
				break;
			case UPDATE_RECORD_NAPTR:
				status = wsResponse.processUpdateRecordNaptrResponse(resultsRequestSOAP);
				break;
			case DELETE_IMAGE:
				status = wsResponse.processDeleteImageResponse(resultsRequestSOAP,deleteItem);
				break;
			case DELETE_KEY_WORD_DATA:
				status = wsResponse.processDeleteKeyWordDataResponse(resultsRequestSOAP,deleteItem);
				break;
			case DELETE_RECORD_NAPTR:
				status = wsResponse.processDeleteRecordNaptrResponse(resultsRequestSOAP,deleteItem);
				break;
			case DELETE_VIDEO:
				status = wsResponse.processDeleteVideoResponse(resultsRequestSOAP);
				break;
			case DELETE_LOC_RECORD:
				status = wsResponse.processDeleteLocRecordResponse(resultsRequestSOAP);
				break;
			case DELETE_OFFER_RECORD:
				status = wsResponse.processDeleteOfferRecordResponse(resultsRequestSOAP);
				break;
			case INSERT_VIDEO:
				status = wsResponse.processInsertVideoResponse(resultsRequestSOAP,modeloVideo);
				break;
			case INSERT_IMAGE:
				status = wsResponse.processInsertImageResponse(resultsRequestSOAP, lstImagen.firstElement());
				break;
			case UPDATE_IMAGE:
				status = wsResponse.processUpdateImageResponse(resultsRequestSOAP);
				break;
			case UPDATE_INSERT_LOC_RECORD:
				status = wsResponse.processUpdateInserLocRecordResponse(resultsRequestSOAP, ubicacion);
				break;
			case INSERT_RECORD_NAPTR:
				status = wsResponse.processInsertRecordNaptrResponse(resultsRequestSOAP, lstContactos);
				break;
			case UPDATEINSERT_OFFER_RECORD:
				status = wsResponse.processUpdateInsertOfferRecordResponse(resultsRequestSOAP, offerRecord);
				break;
			case INSERT_KEY_WORD_DATA:
				status = wsResponse.processInsertKeyWordDataResponse(resultsRequestSOAP,lstKeyword);
				break;
			case INSERT_KEYWORD_DATA_ADDRESS:
				status = wsResponse.processInsertKeywordDataAddressResponse(resultsRequestSOAP,lstKeyword);
				break;
			case GET_ESTADISTICAS:
				lstVisita = new Vector<WS_VisitaVO>(7,3);
				status = wsResponse.processGetNumVisitasResponse(resultsRequestSOAP, lstVisita);
				break;
			case GET_FECHA_EXPIRACION:
				status = wsResponse.processGetFechaExpiracionResponse(resultsRequestSOAP);
				break;
			case INSERT_UPDATE_INFO_USER:
				status = wsResponse.processInsertUpdateInfoUserResponse(resultsRequestSOAP,infoUsr);
				break;
			case CAMBIO_PASSWORD:
				status = wsResponse.processCambioPasswordResponse(resultsRequestSOAP);
				break;
			case CANCELAR_CUENTA:
				status = wsResponse.processCancelarCuentaResponse(resultsRequestSOAP);
				break;
			case GET_ITEMS_GRATIS_DOMAIN:
				status = wsResponse.processGetItemsGratisDomainResponse(resultsRequestSOAP);
				break;
			case COMPRA_DOMINIO:
			case COMPRA_INTENTO:
				status = wsResponse.processCompraDominioResponse(resultsRequestSOAP);
				break;
			case GET_DOMAIN:
			case GET_DOMAIN_LOGIN:
				status = wsResponse.processGetDomainResponse(resultsRequestSOAP,activity);
		        break;
			case GET_SESSION:
				status = wsResponse.processActiveSessionResponse(resultsRequestSOAP);
				break;
			case CLOSE_SESSION:
				status = wsResponse.processCloseSessionResponse(resultsRequestSOAP);
				break;
			case GET_STATUS_DOMAIN:
				status = wsResponse.processGetStatusDominio(resultsRequestSOAP);
				break;			
			case GET_EXIST_CAMPANIABYMAIL:
				status = wsResponse.processGetExistCampaniaByMailResponse(resultsRequestSOAP);
    			break;
			case CAMBIA_NOMBRE_RECURSO:
				status = wsResponse.processCambiaNombreRecursoResponse(resultsRequestSOAP);
				break;
			case GET_IMAGENES:
				status = wsResponse.processGetImagenesResponse(resultsRequestSOAP);
				break;
			case GET_HASH_MOVILIZA_SITIO:
				status = wsResponse.processGetHashMovilizaSitio(resultsRequestSOAP);
				break;
			case GET_HASH_CAMBIO_PASSWORD:
				status = wsResponse.processGetHashCambioPassword(resultsRequestSOAP,activity,strConsulta);
				break;
			case GET_CATALOGO_DOMINIOS:
				status = wsResponse.processGetCatalogoDominios(resultsRequestSOAP);
				
				
		}
    	
    	return status;
    }

	public void setDelegate(WsInfomovilDelgate delegate) {
		this.delegate = delegate;
	}
	public void setDeleteItem(WS_DeleteItem deleteItem) {
		this.deleteItem = deleteItem;
	}
	public void setOfferRecord(WS_OffertRecordVO offerRecord) {
		this.offerRecord = offerRecord;
	}
	public void setUbicacion(WS_LocalizacionVO ubicacion) {
		this.ubicacion = ubicacion;
	}
	public void setDomain(WS_DomainVO domain) {
		this.domain = domain;
	}
	public void setUsrDomain(WS_UserDomainVO usrDomain) {
		this.usrDomain = usrDomain;
	}
	public void setInfoUsr(WS_InfoUsuarioVO infoUsr) {
		this.infoUsr = infoUsr;
	}
	public void setModeloVideo(ItemSelectModel modeloVideo) {
		this.modeloVideo = modeloVideo;
	}
	public void setLstImagen(Vector<WS_ImagenVO> lstImagen) {
		this.lstImagen = lstImagen;
	}
	public void setLstContactos(Vector<WS_RecordNaptrVO> lstContactos) {
		this.lstContactos = lstContactos;
	}
	public void setLstKeyword(Vector<WS_KeywordVO> lstKeyword) {
		this.lstKeyword = lstKeyword;
	}
	public void setLstVisita(Vector<WS_VisitaVO> lstVisita) {
		this.lstVisita = lstVisita;
	}
	public void setDominioVO(WS_DominioVO dominioVO) {
		this.dominioVO = dominioVO;
	}
	public void setStrConsulta(String strConsulta) {
		this.strConsulta = strConsulta;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
	
}

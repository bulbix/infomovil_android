package com.infomovil.infomovil.server;

import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.content.res.Resources;
import android.util.Log;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.StringUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.server.common.XMLManager;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;

public class ItemsDominioParse {
	
	SoapObject response;
	Vector<WS_StatusDomainVO> arrayItems;
	WS_StatusDomainVO itemActual;
	
	public void consultaItemsDominio() {
//		Log.d("infoLog", "Empezando la consulta del servicio");
		SoapObject request = new SoapObject(InfomovilConstants.NAMESPACE, InfomovilConstants.METHOD_ITEMS_DOMINIO);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		
		response = XMLManager.getMethodResult(envelope, InfomovilConstants.SOAP_ACTION);
		parseaItemsDominio();
	}
	
	public void parseaItemsDominio() {
		if(response != null) {
			arrayItems = new Vector<WS_StatusDomainVO>();
			ScanSoapObject(response);
			ordenarItems();
		}
		else {
//			Log.d("infoLog", "La respuesta fue nula");
		}
	}
	
	private void ScanSoapObject(SoapObject result) 
	{
	    for(int i=0;i<result.getPropertyCount();i++)
	    {
	        if(result.getProperty(i) instanceof SoapObject)
	        {               
	             ScanSoapObject((SoapObject)result.getProperty(i));
	        }
	        else
	        {
	            //get the current property name:
	            PropertyInfo pi = new PropertyInfo();
	            result.getPropertyInfo(i,pi);
	            String name = pi.getName();
	            if(name.equals("descripcionItem")) {
//	            	guardaObjectos(i, result);
	            	itemActual = new WS_StatusDomainVO();
	            	itemActual.setDescripcionItem(result.getProperty(i).toString());
	            	
	            }
	            else if(name.equals("status")) {
	            	itemActual.setEstatusItem(Integer.parseInt(result.getProperty(i).toString()));
	            	arrayItems.add(itemActual);
	            } 
//	            Log.d("infoLog", "el nombre es "+result.getProperty(i));
	        }
	    }
	} 
	
	private void ordenarItems() {
		Log.d("infoLog", "Entrando a ordenar elementos al inicio");
		Vector<WS_StatusDomainVO> arregloAux = new Vector<WS_StatusDomainVO>();
		Resources res = InfomovilApp.getApp().getResources();
		String arregloTitulos[] = {res.getString(R.string.txtNombreoEmpresa), res.getString(R.string.txtLogo), res.getString(R.string.txtDescripcionCortaTitulo), res.getString(R.string.txtContacto), res.getString(R.string.txtMapaTitutlo), res.getString(R.string.txtTituloVideo), res.getString(R.string.txtPromociones), res.getString(R.string.txtGaleriaImagenesTitulo), res.getString(R.string.txtPerfilTitulo), res.getString(R.string.txtTituloDireccion), res.getString(R.string.txtTituloInfoAdicional)};
		String arregloTitulosEs[] = {res.getString(R.string.txtNombreoEmpresa2), res.getString(R.string.txtLogo2), res.getString(R.string.txtDescripcionCortaTitulo2), res.getString(R.string.txtContacto2), res.getString(R.string.txtMapaTitutlo2), res.getString(R.string.txtTituloVideo2), res.getString(R.string.txtPromociones2), res.getString(R.string.txtGaleriaImagenesTitulo2), res.getString(R.string.txtPerfilTitulo2), res.getString(R.string.txtTituloDireccion2), res.getString(R.string.txtInfoAdicional2)};
		for(int i = 0; i < arregloTitulos.length; i++) {
			String stringAux = StringUtils.stripAccents(arregloTitulosEs[i]);
			for(int j = 0; j < arrayItems.size(); j++){
				WS_StatusDomainVO item = arrayItems.get(j);
				if((stringAux.toUpperCase()).equals(item.getDescripcionItem().toUpperCase())) {
					item.setDescripcionItem(arregloTitulos[i]);
					arregloAux.add(item);
					break;
				}
			}
		}
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		datosUsuario.setItemsDominio(arregloAux);
	}
}

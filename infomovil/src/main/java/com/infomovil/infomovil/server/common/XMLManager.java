package com.infomovil.infomovil.server.common;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.infomovil.infomovil.constants.InfomovilConstants;

public class XMLManager {
	
	public static SoapObject getMethodResult (SoapSerializationEnvelope envelope, String soapAction) {
		Log.d("infoLog", "LLegando al XML Manager");
		String url = InfomovilConstants.RUTA_WS+"/"+InfomovilConstants.NOMBRE_SERVICIO+"/wsInfomovildomain";
		SoapObject result = null;
		try {
			Log.d("infoLog", "Creando objectos de consulta");
			HttpTransportSE androidHTTP = new HttpTransportSE(url);
			androidHTTP.call(soapAction, envelope);
			result = (SoapObject)envelope.bodyIn;
			Log.d("infoLog", "terminando la consulta");
		}
		catch (Exception e) {
			Log.d("infoLog", "Ocurrio un error "+e);
			e.printStackTrace();
		}
		return result;
	}
}

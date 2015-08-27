package com.infomovil.infomovil.gui.common;

import com.infomovil.infomovil.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.Session;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.principal.MainActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

/****
 * Nos da la funcionalidad de cerrar la sesion
 * @author B220897
 *
 */
public class SalirCuenta implements AlertViewInterface {

	Activity activity;		
	AlertView alerta, alertaMensaje;
	
	
	public SalirCuenta(Activity activity, AlertView alertaMensaje){
		this.activity = activity;
		this.alertaMensaje = alertaMensaje;
	}
	
	@Override
	public void accionSi() {
		alertaMensaje.dismiss();
		cerrandoSesion();
	}

	@Override
	public void accionNo() {
		alertaMensaje.dismiss();
	}

	@Override
	public void accionAceptar() {
		alertaMensaje.dismiss();
		cerrandoSesion();
	}
	
	private void cerrandoSesion(){
		salirCuenta();
	}

	
	private void salirCuenta(){
		if(alerta != null )
			alerta.dismiss();
		
		DatosUsuario.getInstance().borrarDatos();
		
		callFacebookLogout(activity);

		activity.startActivity(new Intent(activity.getBaseContext(), MainActivity.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
		activity.finish();		
	}
	
	/**
	 * Logout From Facebook 
	 */
	public static void callFacebookLogout(Context context) {
	    Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(context);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved

	    }
	}
	
}

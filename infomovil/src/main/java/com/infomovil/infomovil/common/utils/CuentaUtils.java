package com.infomovil.infomovil.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;

public class CuentaUtils {	
	
	static DatosUsuario datosUsuario;
	
	static {
		datosUsuario = DatosUsuario.getInstance();
	}
	
	public static boolean isCuentaPro(){
		return checkStatus(new String[]{"Pago","Mes PRO", "Anual PRO", "Tramite PRO"});		
	}
	
	public static boolean isDowngrade(){
		
		String descripcionDominio = datosUsuario.getDescripcionDominio();
		
		if(isCuentaPro() && !StringUtils.isEmpty(descripcionDominio) && 
				descripcionDominio.equalsIgnoreCase("Downgrade")){
			return true;
		}
		
		return false;	
		
	}
	
	/***
	 * Valida si el dominio ya ha sido comprado
	 * @return
	 */
	public static boolean dominioTelComprado(){
		WS_UsuarioDominiosVO usuarioDominioFound = null;
		int index = 0;
		
		if(datosUsuario.getUsuarioDominios()!= null && 
				(index = datosUsuario.getUsuarioDominios().indexOf(new WS_UsuarioDominiosVO("tel"))) != -1){
			usuarioDominioFound = datosUsuario.getUsuarioDominios().get(index);
		}
		
		if(usuarioDominioFound != null && usuarioDominioFound.toString().equalsIgnoreCase("No Publicado")){
			return true;
		}
		
		return false;
	}
	
	
	public static boolean isUsuarioCanal(){
		return datosUsuario.getTipoUsuario().equalsIgnoreCase("canal");
	}
	
	public static boolean isPublicado(){
		
		if(StringUtils.isEmpty(datosUsuario.getDomainData().getDomainName().trim()) ||
				datosUsuario.getDomainData().getDomainName().indexOf("@") != -1)
			return false;
		else
			return true;
	}
	
	public static boolean checkStatus(String[] status){
		String statusDominio = datosUsuario.getStatusDominio();

		if(Arrays.asList(status).contains(statusDominio)){
			return true;
		}
		
		return false;	
	}
	
	
	public static void mensajeDowngrade(Activity activity){
		final AlertView alertModif = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
				activity.getResources().getString(R.string.txtInfoDowngrade));
		alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
		alertModif.show();		
	}
	
	public static WS_StatusDomainVO getStatusDomain(String descripcion, List<WS_StatusDomainVO> lista){
		for(WS_StatusDomainVO item: lista){
			if(StringUtils.stripAccents(item.getDescripcionItem()).equalsIgnoreCase(StringUtils.stripAccents(descripcion)))
				return item;
		}
		
		return null;
	}
	
	
	public static class RedirectCuenta implements AlertViewInterface  {
		
		Activity activity;
		AlertView alerta;
		
		public RedirectCuenta(Activity activity, int idMensaje){
			this.activity = activity;
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeQuestion, 
			activity.getResources().getString(idMensaje));
			alerta.setDelegado(this);
			alerta.show();				
		}

		@Override
		public void accionSi() {
			if(alerta != null)
				alerta.dismiss();
			
			Intent intent = new Intent(activity, CuentaActivity.class);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
		}

		@Override
		public void accionNo() {
			if(alerta != null)
				alerta.dismiss();
			
		}

		@Override
		public void accionAceptar() {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	
	
	/****
	 * Valida si los datos de la cuenta son correctos
	 * @param activity
	 * @return
	 */
	public static boolean validosDatosUsuario(Activity activity){
		
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		
		if (!(activity instanceof NoSesion)  && datosUsuario.getDomainid() <= 0) {
			Log.d("EmailUsuario", datosUsuario.getNombreUsuario());			
			final AlertView dialog = new AlertView(activity, AlertViewType.AlertViewTypeInfo,		
					activity.getResources().getString(R.string.txtCerrarBackground));    	
		    dialog.setDelegado(new SalirCuenta(activity, dialog));
		    dialog.show();    
		    return false;
		}
		else{
			return true;
		}
	}
	
	public static void redireccionarPublicar(Activity activity, boolean soloUnEditado){
		if (MenuPasosActivity.fueEditado(activity.getResources(),soloUnEditado)) {
			Intent i = new Intent(activity, NombrarActivity.class);
			i.putExtra("tipoDominio", InfomovilApp.tipoInfomovil);
			activity.startActivity(i);
			activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		else {
			AlertView dialog = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
					activity.getResources().getString(R.string.editaPagina));
			dialog.setDelegado(new AlertViewCloseDialog(dialog));
			dialog.show();
		}
	}
	
	/***
	 * Lanza dialogo que permitara la publicacion
	 * @param msg
	 * @param activity
	 */
	public static void dialogoPublicar(int msg, final Activity activity){
		new AlertDialog.Builder(activity)
	    .setMessage(msg)
	    .setCancelable(false) 
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	CuentaUtils.redireccionarPublicar(activity,true);
	        }
	     })
	     .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}
	
	
	public static String getUserCountry(Context context) {
	    try {
	        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	        final String simCountry = tm.getSimCountryIso();
	        if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
	            return simCountry.toLowerCase(Locale.US);
	        }
	        else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
	            String networkCountry = tm.getNetworkCountryIso();
	            if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
	                return networkCountry.toLowerCase(Locale.US);
	            }
	        }
	    }
	    catch (Exception e) { }
	    return null;
	}
	
	

}

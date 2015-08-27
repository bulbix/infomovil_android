package com.infomovil.infomovil.gui.common;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.appboy.Appboy;
import com.appboy.enums.Gender;
import com.appboy.models.outgoing.FacebookUser;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class LoginRedSocial implements WsInfomovilDelgate {

	
	private Activity activity;
	private AlertView alerta;
	private String accion;
	
	 public LoginRedSocial(AlertView alerta, Activity activity, String email, String redSocial, String accion) {
		this.alerta = alerta;
		this.activity = activity;
		this.accion = accion;
		
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		datosUsuario.setNombreUsuario(email);
		datosUsuario.setPassword("");
		datosUsuario.setRedSocial(redSocial);
		
		String billingItem = new CompraInfomovil(activity).getItemCompradoPendiente();
		
		if(billingItem.isEmpty()){
			datosUsuario.setSuscritoCompra("false");
			datosUsuario.setTipoPlanCompra("");
		}
		else{
			datosUsuario.setSuscritoCompra("true");
			datosUsuario.setTipoPlanCompra(billingItem);
		}
	}
	 
	public void errorLogin(int idMensaje){
		final AlertView alertModif = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
				activity.getResources().getString(idMensaje));
		alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
		alertModif.show();		
		SalirCuenta.callFacebookLogout(activity);
	}
	
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		if(alerta != null)
			alerta.dismiss();
		
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			
			try{
				Appboy.getInstance(activity).changeUser(datosUsuario.getNombreUsuario());
				Appboy.getInstance(activity).getCurrentUser().setEmail(datosUsuario.getNombreUsuario());
				
				if(CuentaUtils.isUsuarioCanal()){
					Appboy.getInstance(activity).getCurrentUser().setCustomUserAttribute("canal", datosUsuario.getCanal());
					Appboy.getInstance(activity).getCurrentUser().setCustomUserAttribute("campania", datosUsuario.getCampania());
				}
				
				try{
					GraphUser graphUser = datosUsuario.getFacebookInformation();
					Log.d("Genero Facebook " , graphUser.getProperty("gender").toString());
					
					FacebookUser facebookUser = new FacebookUser(graphUser.getId(), graphUser.getFirstName(), 
							graphUser.getLastName(), datosUsuario.getNombreUsuario(),
							"", "", Gender.valueOf(graphUser.getProperty("gender").toString().toUpperCase()), 1, null, graphUser.getBirthday());
					Appboy.getInstance(activity).getCurrentUser().setFacebookData(facebookUser);
				} 
				catch(Exception e ){
					Log.d("Error track", "Appboy", e);
				}
				
			}
			catch(Exception e){
				Log.d("Error track", "Appboy", e);
			}
			
			Intent intent = new Intent(activity, MenuPasosActivity.class);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
		}
		
		else {
			
			datosUsuario.setExisteLogin(false);

			int idMensaje = -1;

			if(datosUsuario.getStatusDominio().equals("Cancelada")){
				idMensaje = R.string.txtCuentaCancelada;					
			}
			else if(datosUsuario.getStatusDominio().equals("incorrectPassword") 
					|| datosUsuario.getStatusDominio().equals("notFoundUser")){					
				idMensaje = R.string.txtErrorGenericoEmail;
			}
			else{
				idMensaje = R.string.txtErrorLogin;
			}
			
			
			errorLogin(idMensaje);
		}
		
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
		if(alerta != null)
			alerta.dismiss();
		
		errorLogin(R.string.txtErrorLogin);
	}
	
	
	public static Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				//session.requestNewPublishPermissions(new Session.NewPermissionsRequest(MainActivity.this, "email"));
				
				Log.d("InfomovilFacebook", "Facebook session opened");
			} else if (state.isClosed()) {
				Log.d("InfomovilFacebook", "Facebook session closed");
			}
		}
	};
	
	
	/****
	 * Para realizar la autenticacion 
	 * @author BAZ
	 *
	 */
	public static class UserInfoChangedCallbackImpl implements UserInfoChangedCallback {
		
		private Activity activity;
		private AlertView alerta;
		private String redSocial;
		private String accion;
		
		public UserInfoChangedCallbackImpl(Activity activity, AlertView alerta, String redSocial, String accion) {
			this.activity = activity;
			this.alerta = alerta;
			this.redSocial = redSocial;
			this.accion = accion;
		}
		
		private void redirect(Object email, String accion){
			
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity, 
			activity.getResources().getString(R.string.msgValidandoUsuario));
			alerta.show();
			
			WsInfomovilCall wsCall = new 
			WsInfomovilCall(new LoginRedSocial(alerta, activity, 
			email.toString(), redSocial, accion),activity);
			wsCall.execute(WSInfomovilMethods.GET_DOMAIN_LOGIN);
		}
		
		private class ExisteUsuario implements  WsInfomovilDelgate {
			
			Object email;
			
			public ExisteUsuario(Object email) {
				this.email = email;
			}
			
			@Override
			public void respuestaObj(WSInfomovilMethods metodo,
					WsInfomovilProcessStatus status, Object obj) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
				if(alerta != null)
					alerta.dismiss();
				
				SalirCuenta.callFacebookLogout(activity);
			}
			
			@Override
			public void respuestaCompletada(WSInfomovilMethods metodo,
					long milisegundo, WsInfomovilProcessStatus status) {
				
				if(alerta != null)
					alerta.dismiss();
				
				if(status == WsInfomovilProcessStatus.SIN_EXITO) {
					redirect(email,"registro");
				}
				else{
					redirect(email,"login");
				}
				
				
			}
		}
		
		@Override
		public void onUserInfoFetched(GraphUser user) {
			if (user != null) {
				Log.d("InfomovilFacebook", "Hello, " + user.getProperty("email"));
				
				final Object email = user.getProperty("email");
				
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity, 
						activity.getResources().getString(R.string.txtCargandoDefault));
				alerta.show();
				
				DatosUsuario datosUsuario = DatosUsuario.getInstance();
				datosUsuario.setFacebookInformation(user);
				
				Object strConsulta = email !=  null ? email : user.getId()+"@";
				
				WsInfomovilCall wsCall = new WsInfomovilCall(new ExisteUsuario(strConsulta),activity);
				wsCall.setStrConsulta(strConsulta.toString());
				wsCall.execute(WSInfomovilMethods.GET_EXIST_USER);
				
			} else {
				Log.d("InfomovilFacebook", "You are not logged");
			}
		}
	}
	
	
	
	
	

}

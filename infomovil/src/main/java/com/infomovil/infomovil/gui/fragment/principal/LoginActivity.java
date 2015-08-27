package com.infomovil.infomovil.gui.fragment.principal;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.appboy.Appboy;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.LoginRedSocial;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class LoginActivity extends InfomovilActivity implements NoSesion {
	
	private EditTextWithFont txtUsuario, txtPassword;	
	boolean buscandoSesion;
	private LoginButton loginBtn;
	private UiLifecycleHelper uiHelper;
	private CheckBox chkRecordarme;
		
	public void recordarmeClick(View v){
		CheckBox checkBox = (CheckBox)v;
		boolean recordarme = checkBox.isChecked();
        SharedPreferences prefs = context.
        getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putBoolean("recordarme", recordarme);
		edit.commit();
	}
	
	private boolean getRecordarme(){
		SharedPreferences prefs = context.
		getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
		return prefs.getBoolean("recordarme", false);
	}
		
	private void iniciarSesion(String email, String password){
		alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, 
				getResources().getString(R.string.msgValidandoUsuario));
		alerta.setDelegado(this);
		alerta.show();
		datosUsuario = DatosUsuario.getInstance();
		datosUsuario.setNombreUsuario(email);
		datosUsuario.setPassword(password);
		buscandoSesion = true;
		
		SharedPreferences prefs = context.
				getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		
		if(getRecordarme()){
			edit.putString("email", email);
			edit.putString("password", password);
		}
		else{			
			edit.putString("email", "");
			edit.putString("password", "");
		}
		
		edit.commit();

		if (InfomovilApp.isConnected(this)) {
			llamadaLogin();
		}
		else {
			if(alerta != null)
				alerta.dismiss();

			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.txtNoConexion));
			alerta.setDelegado(this);
			alerta.show();
		}

	}
			
	public void iniciarSesion(View v) throws Exception {
		if (txtUsuario.length() == 0 || txtPassword.length() == 0) {
			AlertView alertError = new AlertView(this, AlertViewType.AlertViewTypeInfo,
			getResources().getString(R.string.txtValidacionDatos));
			alertError.setDelegado(this);
			alertError.show();
			return;
		}
		
		iniciarSesion(txtUsuario.getText().toString(), 
		txtPassword.getText().toString());
	}
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		Log.d("infoLog", "El resultado es " + status);
		datosUsuario = DatosUsuario.getInstance();
		
		if(alerta != null)
			alerta.dismiss();
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			try{
				
				Appboy.getInstance(this).changeUser(datosUsuario.getNombreUsuario());
				Appboy.getInstance(this).getCurrentUser().setEmail(datosUsuario.getNombreUsuario());
				Appboy.getInstance(this).getCurrentUser().setCustomUserAttribute("pais", CuentaUtils.getUserCountry(this));
				
				if(CuentaUtils.isUsuarioCanal()){
					Appboy.getInstance(this).getCurrentUser().setCustomUserAttribute("canal", datosUsuario.getCanal());
					Appboy.getInstance(this).getCurrentUser().setCustomUserAttribute("campania", datosUsuario.getCampania());
				}
			}
			catch(Exception e){}

			Intent intent = new Intent(this, MenuPasosActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
		}
		else {
			
			datosUsuario.setExisteLogin(false);

			int idMensaje = -1;

			if(datosUsuario.getStatusDominio().equals("Cancelada")){
				idMensaje = R.string.txtCuentaCancelada;					
			}
//			else if(datosUsuario.getStatusDominio().equals("incorrectPassword") 
//					|| datosUsuario.getStatusDominio().equals("notFoundUser")){					
//				idMensaje = R.string.txtErrorGenericoEmail;
//			}
			else{
				idMensaje = R.string.txtErrorGenericoEmail;
			}

			final AlertView alertModif = new AlertView(this, AlertViewType.AlertViewTypeInfo,
					getResources().getString(idMensaje));
			alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
			alertModif.show();	
								
		}
	}
	
	public void recuperarPassword(View v) {
		Intent intent = new Intent(this, RecuperarPasswordActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.up_in, R.anim.up_out);
	}
	
	@Override
	public void accionSi() {
		super.accionSi();
		
		if(alerta != null)
			alerta.dismiss();
		
		alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, 
		getResources().getString(R.string.msgValidandoUsuario));
		alerta.setDelegado(this);
		alerta.show();
		llamadaLogin();
	}
	
	@Override
	public void accionNo() {
		super.accionNo();
		if(alerta != null)
			alerta.dismiss();
	}
	
	@Override
	public void accionAceptar() {
		super.accionAceptar();
		if(alerta != null)
			alerta.dismiss();
	}
	
	private void llamadaLogin() {
		buscandoSesion = false;
//		datosUsuario = DatosUsuario.getInstance();
//		datosUsuario.setNombreUsuario(txtUsuario.getText().toString());
//		String passEnc = txtPassword.getText().toString();
//		datosUsuario.setPassword(passEnc);
		datosUsuario = 	DatosUsuario.getInstance(); 
		datosUsuario.setRedSocial("");
		
		String billingItem = new CompraInfomovil(this).getItemCompradoPendiente();
		
		if(billingItem.isEmpty()){
			datosUsuario.setSuscritoCompra("false");
			datosUsuario.setTipoPlanCompra("");
		}
		else{
			datosUsuario.setSuscritoCompra("true");
			datosUsuario.setTipoPlanCompra(billingItem);
		}
		
		
		WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
		wsCall.execute(WSInfomovilMethods.GET_DOMAIN_LOGIN);
	}

	@Override
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBotonEliminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initCreate() {		
		cargarLayout(R.layout.login_activity);
		cambiarBackground();
		acomodarVistaInicio();
		buscandoSesion = true;
		
		txtUsuario = (EditTextWithFont) findViewById(R.id.txtLoginEmail);
		txtPassword = (EditTextWithFont) findViewById(R.id.txtPassLogin);
		validasDatosUsuario = false;
		
		chkRecordarme = (CheckBox)findViewById(R.id.chkRecordarme);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, LoginRedSocial.statusCallback);
		uiHelper.onCreate(savedInstanceState);
		
		SalirCuenta.callFacebookLogout(this);
		
		loginBtn = (LoginButton) findViewById(R.id.authButton);
		loginBtn.setReadPermissions(Arrays.asList("email","user_photos"));
		loginBtn.setUserInfoChangedCallback(
		new LoginRedSocial.UserInfoChangedCallbackImpl(this, alerta, "Facebook","login"));
		
	}

	@Override
	public void initResume() {
		ocultarMenuInferior();
		uiHelper.onResume();
		
		CompraInfomovil.configurar(this);
		
		SharedPreferences prefs = context.
				getSharedPreferences("Cuenta", Context.MODE_PRIVATE);

		chkRecordarme.setChecked(prefs.getBoolean("recordarme", false));
		String email = prefs.getString("email", "");
		String password = prefs.getString("password", "");

		txtUsuario.setText(email); 
		txtPassword.setText(password);
	}

	@Override
	public void guardarInformacion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}

}

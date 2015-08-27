package com.infomovil.infomovil.gui.fragment.principal;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.LoginRedSocial;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.fragment.principal.RecuperarPasswordActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.gui.principal.RegistroUsuarioActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.server.common.JSONManager;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class MainActivity extends InfomovilActivity implements NoSesion {
	
	private EditTextWithFont txtUsuario, txtPassword;	
	boolean buscandoSesion;
	private LoginButton loginBtn;
	private UiLifecycleHelper uiHelper;
	
	public void registrarClick(View v){
		Intent intent = new Intent(this, RegistroUsuarioActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
		
	public void iniciarSesion(String email, String password, String redSocial){
		
		alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, 
				getResources().getString(R.string.msgValidandoUsuario));
		alerta.setDelegado(this);
		alerta.show();
		
		
		datosUsuario = DatosUsuario.getInstance();
		datosUsuario.setNombreUsuario(email);
		datosUsuario.setPassword(password);
		datosUsuario.setRedSocial(redSocial);
		
		buscandoSesion = true;
		

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
		txtPassword.getText().toString(),"");
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
		datosUsuario = 	DatosUsuario.getInstance(); 
		//datosUsuario.setRedSocial("");
		
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
		cargarLayout(R.layout.activity_login);
		cambiarBackground();
		acomodarVistaInicio();
		buscandoSesion = true;
		
		txtUsuario = (EditTextWithFont) findViewById(R.id.txtLoginEmail);
		txtPassword = (EditTextWithFont) findViewById(R.id.txtPassLogin);
		validasDatosUsuario = false;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, LoginRedSocial.statusCallback);
		uiHelper.onCreate(savedInstanceState);
		
		SalirCuenta.callFacebookLogout(this);
		
		loginBtn = (LoginButton) findViewById(R.id.authButton);
		loginBtn.setReadPermissions(Arrays.asList("email","user_photos"));
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loginBtn.setUserInfoChangedCallback(
						new LoginRedSocial.UserInfoChangedCallbackImpl(MainActivity.this, alerta, "Facebook","login"));
			}
		});
		
		
	}

	@Override
	public void initResume() {
		
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod")){
			//new CheckVersion().execute();
		}
		
		CompraInfomovil.configurar(this);
		ocultarMenuInferior();
		
		if(datosUsuario.getDomainid() == 0){
			SharedPreferences prefs = context.
			getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
			
			String email  = prefs.getString("email", "");
			String password = prefs.getString("password", "");
			
			if(!StringUtils.isEmpty(email)){
				if(!StringUtils.isEmpty(password)){
					txtUsuario.setText(email); 
					txtPassword.setText(password);
					iniciarSesion(email, password, "");
				}
				else{
					iniciarSesion(email, password, "Facebook");
				}
				
			}
		}
		
		uiHelper.onResume();
		
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
	
	
	class CheckVersion extends AsyncTask<Void, Void, String> {

		//AlertView progressDialog;

		@Override
		protected void onPreExecute() {
			//			progressDialog = new AlertView(MainActivity.this,
			//					AlertViewType.AlertViewTypeActivity, 
			//					getResources().getString(R.string.msgValidandoVersion));
			//					progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String version = "";

			try {
				JSONObject jObject = JSONManager
						.getJSONfromURL("https://androidquery.appspot.com/api/market?app=com.infomovil.infomovil");
				version = jObject.getString("version");
				Log.d("Version tienda", version);

			} catch (Throwable e) {
				version = "";
			}



			return version;

		}

		@Override
		protected void onPostExecute(String result) {
			//progressDialog.dismiss();

			if(!result.isEmpty()){
				try {
					String[] tokensStore = result.split("\\.");
					int majorStore = Integer.parseInt(tokensStore[0]);
					Log.d("Check Major Tienda", majorStore+"");
					int minorStore = Integer.parseInt(tokensStore[1]);
					Log.d("Check Minor Tienda", minorStore+"");
					int revisionStore = Integer.parseInt(tokensStore[2]);
					Log.d("Check Revision Tienda", revisionStore+"");

					PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
					String[] tokensCurrent = pInfo.versionName.split("\\.");
					int majorCurrent = Integer.parseInt(tokensCurrent[0]);
					Log.d("Check Major Current", majorCurrent+"");
					int minorCurrent = Integer.parseInt(tokensCurrent[1]);
					Log.d("Check Minor Current", minorCurrent+"");
					int revisionCurrent = Integer.parseInt(tokensCurrent[2]);
					Log.d("Check Revision Current", revisionCurrent+"");


					final String appName = "com.infomovil.infomovil";

					if(majorCurrent < majorStore){
						redirect(appName);
					}
					else if(majorCurrent == majorStore) {
						if(minorCurrent < minorStore){
							redirect(appName);
						}
						else if(minorCurrent == minorStore){
							if(revisionCurrent < revisionStore){
								redirect(appName);
							}
						}
					}

				} catch (Throwable e) {

				}
			}
		}

		private void redirect(final String appName){
			new AlertDialog.Builder(MainActivity.this)
			.setTitle(R.string.msgNuevaActualizacion)
			.setMessage(R.string.msgNuevaVersion)
			.setCancelable(false) 
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { 
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
				}
			})
			.setIcon(android.R.drawable.ic_dialog_alert)
			.show();
		}

	}
	
	

}

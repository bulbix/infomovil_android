package com.infomovil.infomovil.gui.principal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mx.com.infomovil.commons.CommonUtils;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appboy.Appboy;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.fragment.gui.nombrar.RedimirActivity;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.LoginRedSocial;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;

public class RegistroUsuarioActivity extends InfomovilActivity implements NoSesion {
	
	private UiLifecycleHelper uiHelper;
	private LoginButton loginBtn;
	
	
	private EditTextWithFont txtNombre, txtPassword, txtConfirmPassword;
	boolean creandoUsuario;
	private RelativeLayout txtInfoPass;
	public static Map<Integer,Integer> mensajes = new HashMap<Integer, Integer>();	

	static{		
		mensajes.put(WsInfomovilProcessStatus.EXITO.getValue(), R.string.redimirExito);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_CAMPANIA_CADUCO.getValue(),R.string.errorCodigo2);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_ASOCIADO.getValue(), R.string.errorCodigo5);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_NOEXISTE.getValue(), R.string.errorCodigo1);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_AGOTADO.getValue(), R.string.errorCodigo6);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_CUENTA_NOCAMP.getValue(), R.string.errorCodigo7);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_CUENTA_YAACTIVADA.getValue(), R.string.errorCodigo8);		
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_CAMPANIA_NOACTIVA.getValue(), R.string.error999);
	}
	
	
	public String validaCampos() {
		String respuesta = "Correcto";

		txtNombre.setText(txtNombre.getText().toString().trim());

		String strEmail = txtNombre.getText().toString();
		String password =  txtPassword.getText().toString();
		String confirmPassword = txtConfirmPassword.getText().toString();

		if(strEmail.isEmpty() || confirmPassword.isEmpty() || password.isEmpty()){
			respuesta = getResources().getString(R.string.verificaDatosFormulario);
		}
		else if(!CommonUtils.isValidEmail(strEmail)) {
			respuesta = getResources().getString(R.string.verificaEmail);
		}
		else if (!password.equals(confirmPassword)){
			respuesta = getResources().getString(R.string.msgPasswordDif);
		}
		else if (password.length() < 8 || password.length() > 15) {
			respuesta = getResources().getString(R.string.passDebe) + " " + getResources().getString(R.string.infoPasswordTamano);
		}
		else if (password.contains("Infomovil") || password.contains("INFOMOVIL") ||
				password.contains("infomovil") || password.contains("fomovil")) {
			respuesta = getResources().getString(R.string.infoPasswordInfomovil);
		}
		else if (password.equalsIgnoreCase(strEmail.split("@")[0])) {
			respuesta = getResources().getString(R.string.infoPasswordCorreo);
		}
		else if (!CommonUtils.isValidPassword(strEmail.split("@")[0], password)) {
			respuesta = getResources().getString(R.string.passDebe) + " " + getResources().getString(R.string.infoPasswordTamano);
		}

		return respuesta;
	}
	
	public void verificarUsuario(View v) throws Exception {
		
		String mensaje = validaCampos();
		
		if (mensaje.equals("Correcto")) {			
			WS_UserDomainVO userDomain = new WS_UserDomainVO();
			userDomain.setEmail(txtNombre.getText().toString());
			userDomain.setPassword(txtPassword.getText().toString());
			datosUsuario = DatosUsuario.getInstance(); 
			datosUsuario.setUserDomainData(userDomain);
			alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.msgBuscandoUsuario));
			alerta.setDelegado(this);
			alerta.show();
			WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
			wsCall.setStrConsulta(userDomain.getEmail());
			wsCall.execute(WSInfomovilMethods.GET_EXIST_USER);
		}
		else {
			AlertView alertError = new AlertView(this, AlertViewType.AlertViewTypeInfo, mensaje);
			alertError.setDelegado(this);
			alertError.show();
		}
	}
	
	private void consultarCodigo(){
		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
			
			@Override
			public void respuestaObj(WSInfomovilMethods metodo,
					WsInfomovilProcessStatus status, Object obj) {
			}
			
			@Override
			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
			}
			
			@Override
			public void respuestaCompletada(WSInfomovilMethods metodo,
					long milisegundo, WsInfomovilProcessStatus status) {
				if(status == WsInfomovilProcessStatus.EXITO){
					//WS_RespuestaVO respuesta = DatosUsuario.getInstance().getRespuestaVOActual();
					//HashMap<String, String> resultMap = StringUtils.convertToStringToHashMap(respuesta.getResultado());
					Intent intent = new Intent(RegistroUsuarioActivity.this, RedimirActivity.class);
		    		startActivity(intent);
		    		overridePendingTransition(R.anim.up_in, R.anim.up_out);
				}
				else{
					redirect();
				}
			}
		},this);
		
		wsCall.execute(WSInfomovilMethods.GET_EXIST_CAMPANIABYMAIL);
	}
	
	
	
	
	private void redirect(){
		InfomovilApp.setEnTramite(true);
		Intent intent = new Intent(this, MenuPasosActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);		
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {

		if(status == WsInfomovilProcessStatus.SIN_EXITO) {
			datosUsuario = DatosUsuario.getInstance();
			WS_UserDomainVO userDomain = datosUsuario.getUserDomainData();
			userDomain.setStatus(9);//Estatus para usuario sin dominio .tel
			userDomain.setSistema("ANDROID");
			userDomain.setTipoAction(5);
			userDomain.setPais(1);
			userDomain.setCanal(0);
			userDomain.setSucursal(1);
			userDomain.setFolio(0);
			datosUsuario.setUserDomainData(userDomain);
			datosUsuario.setNombreUsuario(userDomain.getEmail());
			datosUsuario.setPassword(userDomain.getPassword());
			userDomain.setCodigoCamp(" ");			
			userDomain.setAccion("registrar");		

			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {

				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
					// TODO Auto-generated method stub

				}

				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					if(alerta!=null)
						alerta.dismiss();

				}

				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					if(status == WsInfomovilProcessStatus.EXITO || status.toString().startsWith("ERROR_CODIGO")) {

						if(alerta!=null)
							alerta.dismiss();
						
						try{
							Appboy.getInstance(RegistroUsuarioActivity.this).changeUser(datosUsuario.getNombreUsuario());
							Appboy.getInstance(RegistroUsuarioActivity.this).getCurrentUser().setEmail(datosUsuario.getNombreUsuario());
							Appboy.getInstance(RegistroUsuarioActivity.this).getCurrentUser().
							setCustomUserAttribute("pais", CuentaUtils.getUserCountry(RegistroUsuarioActivity.this));
						}
						catch(Exception e){}
						
						//consultarCodigo();
						
						InfomovilApp.setEnTramite(true);
						Intent intent = new Intent(RegistroUsuarioActivity.this, MenuPasosActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.left_in, R.anim.left_out);
						
						
					}
					else {

						alerta.dismiss();
						AlertView alertError = new AlertView(RegistroUsuarioActivity.this, AlertViewType.AlertViewTypeInfo, 
								getResources().getString(R.string.txtErrorGenericoEmail)) ;
						alertError.setDelegado(RegistroUsuarioActivity.this);
						alertError.show();
					}

				}
			},this);

			wsCall.setUsrDomain(userDomain);
			creandoUsuario = true;						
			wsCall.execute(WSInfomovilMethods.INSERT_USER_DOMAIN);
		}
		else {
			alerta.dismiss();
			AlertView alertError = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.txtErrorName)) ;
			alertError.setDelegado(this);
			alertError.show();
			Log.d("infoLog", "El metodo ya existe");
		}

	}
	
	

	@Override
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBotonEliminar() {
		// TODO Auto-generated method stub
		
	}
	
	public void crearCuenta(View v){
		datosUsuario = DatosUsuario.getInstance();
    	datosUsuario.borrarDatos();
    	
    	Intent i = new Intent(this, RegistroUsuarioActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
		
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
				loginBtn.setUserInfoChangedCallback(new LoginRedSocial.UserInfoChangedCallbackImpl
						(RegistroUsuarioActivity.this, alerta, "Facebook","registro"));
			}
			
		});
	
	}
	

	@Override
	public void initCreate() {
		cargarLayout(R.layout.activity_registro);
		acomodarVistaInicio();
        cambiarBackground();
        
//		acomodarVistaConTitulo(R.string.txtRegistro, R.drawable.plecamorada);
//		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		txtNombre = (EditTextWithFont) findViewById(R.id.txtNombreRegistro);
		txtPassword = (EditTextWithFont) findViewById(R.id.txtPasswordRegistro);		
		txtConfirmPassword = (EditTextWithFont) findViewById(R.id.txtNumeroRegistro);
		
		txtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        @Override
			public void onFocusChange(View v, boolean hasFocus) {
	            if (hasFocus) {
	            	Toast toast  = Toast.makeText(RegistroUsuarioActivity.this, R.string.infoPassword, Toast.LENGTH_LONG);
	            	toast.setGravity(Gravity.TOP, 0, 20);
	            	toast.show();
	            }
	        }
	    });

		//txtInfoPass = (RelativeLayout) findViewById(R.id.labelInfoPass);
		
		creandoUsuario = false;
		validasDatosUsuario = false;

		//txtPassword.setOnFocusChangeListener(new InfoText(txtInfoPass));
		
	}

	@Override
	public void initResume() {
		ocultarMenuInferior();
		uiHelper.onResume();
		CompraInfomovil.configurar(this);
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

}

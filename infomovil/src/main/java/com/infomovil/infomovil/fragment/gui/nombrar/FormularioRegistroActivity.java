package com.infomovil.infomovil.fragment.gui.nombrar;

import java.util.HashMap;
import java.util.Map;

import mx.com.infomovil.commons.CommonUtils;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
 
public class FormularioRegistroActivity extends InfomovilActivity implements NoSesion {
		
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
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_PLANPRO.getValue(), R.string.errorCodigo13);
		mensajes.put(WsInfomovilProcessStatus.ERROR_CODIGO_CAMPANIA_NOACTIVA.getValue(), R.string.error999);
	}
	
	
	public String validaCampos() {
		String respuesta;
		String strEmail = txtNombre.getText().toString();
		if(strEmail.trim().isEmpty() || txtConfirmPassword.getText().toString().trim().isEmpty() ||  txtPassword.getText().toString().trim().isEmpty()){
			respuesta = getResources().getString(R.string.verificaDatosFormulario);
		}else{
		if (!strEmail.isEmpty()) {
			String arrAux[] = strEmail.split("@");
			strEmail = arrAux[0];
		}
		if(!CommonUtils.isValidEmail(txtNombre.getText().toString())) {
			respuesta = getResources().getString(R.string.verificaEmail);
		}
		else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
			respuesta = getResources().getString(R.string.msgPasswordDif);
		}
		else if (txtPassword.getText().toString().length() < 8 || txtPassword.getText().toString().length() > 15) {
			respuesta = getResources().getString(R.string.passDebe) + " " + getResources().getString(R.string.infoPasswordTamano);
		}
		else if (txtPassword.getText().toString().contains("Infomovil") || txtPassword.getText().toString().contains("INFOMOVIL") || txtPassword.getText().toString().contains("infomovil") || txtPassword.getText().toString().contains("fomovil")) {
			respuesta = getResources().getString(R.string.infoPasswordInfomovil);
		}
		else if (txtPassword.getText().toString().equalsIgnoreCase(strEmail)) {
			respuesta = getResources().getString(R.string.infoPasswordCorreo);
		}
		else if (!CommonUtils.isValidPassword(strEmail, txtPassword.getText().toString())) {
			respuesta = getResources().getString(R.string.passDebe) + " " + getResources().getString(R.string.infoPasswordTamano);
		}
		else {
			respuesta = "Correcto";
		}
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
					Intent intent = new Intent(FormularioRegistroActivity.this, RedimirActivity.class);
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
							Appboy.getInstance(FormularioRegistroActivity.this).changeUser(datosUsuario.getNombreUsuario());
							Appboy.getInstance(FormularioRegistroActivity.this).getCurrentUser().setEmail(datosUsuario.getNombreUsuario());
						}
						catch(Exception e){}
						
						//consultarCodigo();
						
						InfomovilApp.setEnTramite(true);
						Intent intent = new Intent(FormularioRegistroActivity.this, MenuPasosActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.left_in, R.anim.left_out);
						
						
					}
					else {

						alerta.dismiss();
						AlertView alertError = new AlertView(FormularioRegistroActivity.this, AlertViewType.AlertViewTypeInfo, 
								getResources().getString(R.string.txtErrorGenericoEmail)) ;
						alertError.setDelegado(FormularioRegistroActivity.this);
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
	public void accionAceptar() {
		super.accionAceptar();
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
		cargarLayout(R.layout.formulario_registro_layout);
		cambiarBackground();
		acomodarVistaInicio();
		
//		acomodarVistaConTitulo(R.string.txtRegistro, R.drawable.plecamorada);
//		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		txtNombre = (EditTextWithFont) findViewById(R.id.txtNombreRegistro);
		txtPassword = (EditTextWithFont) findViewById(R.id.txtPasswordRegistro);		
		txtConfirmPassword = (EditTextWithFont) findViewById(R.id.txtNumeroRegistro);
		
		txtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        @Override
			public void onFocusChange(View v, boolean hasFocus) {
	            if (hasFocus) {
	            	Toast toast  = Toast.makeText(FormularioRegistroActivity.this, R.string.infoPassword, Toast.LENGTH_LONG);
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
		// TODO Auto-generated method stub
		ocultarMenuInferior();
		
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

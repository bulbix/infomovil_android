package com.infomovil.infomovil.fragment.gui.nombrar;

import mx.com.infomovil.commons.CommonUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.fragment.principal.SelectorPaisActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;
import com.madrobot.text.StringUtils;

public class PublicarActivity extends InfomovilActivity {
	
	private TextWithFont txtDominio, labelPais;
	private EditTextWithFont editNombre, editDireccion1, editDireccion2;
	private EditTextWithFont editCorreo;
	boolean crearDominio;
	private String nombrePais;
	private int codigoPais;
	boolean errorPublico;
	int casoError; 
	private String mensajeError;
	private String tipoDominio;
		
	public void buscarPais(View v) {
		Intent intent = new Intent(this, SelectorPaisActivity.class);
		intent.putExtra("tipoSelector", "publicar");
		startActivityForResult(intent, InfomovilConstants.STATIC_INTEGER_VALUE);
	}
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		if(alerta != null)
			alerta.dismiss();

		if (status == WsInfomovilProcessStatus.EXITO) {		
			errorPublico = false;
			WS_UserDomainVO userDomain = datosUsuario.getUserDomainData();

			//Registro de Eventos y Atributos
			Appboy.getInstance(this).logCustomEvent(tipoDominio.equalsIgnoreCase("tel")?"Publicar Tel":"Publicar Subdominio");
			Appboy.getInstance(this).getCurrentUser().setCustomUserAttribute("tipoDominio",tipoDominio);
			Appboy.getInstance(this).getCurrentUser().setCustomUserAttribute("nombreDominio",userDomain.getDomainName());


			Log.d("infoLog", "Se publico el dominio CREATE_DOMAIN");
			Intent intent = new Intent(this, NombrarCompartirActivity.class);
			intent.putExtra("dominio",userDomain.getDomainName() );
			intent.putExtra("fechaFin",datosUsuario.getFechaFin() );
			intent.putExtra("tipoDominio", tipoDominio);
			startActivity(intent);
		}
		else if (status == WsInfomovilProcessStatus.ERROR_PUBLICAR) {
			errorPublico = true;
			casoError = 3;
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.mensajeProcesoPublicacion));
			alerta.setDelegado(this);
			alerta.show();
		}
		else if (status == WsInfomovilProcessStatus.ERROR_DOM_EXISTE) {
			casoError = 4;
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorDomainNombrar));
			alerta.setDelegado(this);
			alerta.show();
		}
		else {
			errorPublico = false;
			casoError = 5;
			Log.d("infoLog", "No se publico el dominio CREATE_DOMAIN");
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorGenericoPublicacion));
			alerta.setDelegado(this);
			alerta.show();
		} 
//		}
	}
		
	public boolean validaDatos() {
		boolean correctos = false;
		if (StringUtils.isEmpty(editNombre.getText())) {
			mensajeError = getResources().getString(R.string.txtLlenarNombre);
			correctos = false;
		}
		else if (StringUtils.isEmpty(editDireccion1.getText()) || StringUtils.isEmpty(editDireccion2.getText())) {
			mensajeError = getResources().getString(R.string.txtLlenarDireccion);
			correctos = false;
		}
		else if(StringUtils.isEmpty(editCorreo.getText())){
			mensajeError = getResources().getString(R.string.txtLlenarCorreo);
			correctos = false;
		}
		else if(!CommonUtils.isValidEmail(editCorreo.getText().toString())){
			mensajeError = getResources().getString(R.string.emailInvalido);
			correctos = false;
		}
		else {
			correctos = true;
			codigoPais = 1;
		}
		
		return correctos;
	}
	
	@Override
	public void accionAceptar() {
		super.accionAceptar();
		switch (casoError) {
		case 3:
//			InfomovilApp.setExisteSesion(true);
			InfomovilApp.setEnTramite(false);
			Intent intent = new Intent(this, MenuPasosActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_out, R.anim.left_in);
			break;
			
		case 4:
			this.finish();
			break;
		
		case 5:
			break;

		default:
			break;
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

	@Override
	public void initCreate() {
		cargarLayout(R.layout.publicar_layout);
		acomodarVistaConTitulo(R.string.txtPublicar, R.drawable.plecaroja);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		datosUsuario = DatosUsuario.getInstance();
		System.out.println("Entra a publicar");
		WS_UserDomainVO userDomain = datosUsuario.getUserDomainData();
		
		txtDominio = (TextWithFont) findViewById(R.id.labelDominioElegido);
		
		tipoDominio = getIntent().getStringExtra("tipoDominio");
		
		final String nombreDominio;
		
		if(tipoDominio.equals("tel")){
			nombreDominio  =  getResources().getString(R.string.txtWWWGeneral) + userDomain.getDomainName() 
					+ getResources().getString(R.string.txtTelGeneral);
		}
		else{
			nombreDominio = InfomovilApp.urlInfomovil;
		}
		
		txtDominio.setText(nombreDominio + " " + getResources().getString(R.string.txtTelDisponible));
		
		
		labelPais = (TextWithFont) findViewById(R.id.labelPaisSeleccionadoPublicar);
		labelPais.setText("Mexico");
		editNombre = (EditTextWithFont) findViewById(R.id.txtNombrePublicar);
		editDireccion1 = (EditTextWithFont) findViewById(R.id.txtDireccion1Publicar);
		editDireccion2 = (EditTextWithFont) findViewById(R.id.txtDireccion2Publicar);
		editCorreo = (EditTextWithFont) findViewById(R.id.txtCorreo);
		editCorreo.setText(CommonUtils.isValidEmail(datosUsuario.getNombreUsuario())?datosUsuario.getNombreUsuario():"");
		
		datosUsuario.setNombreDominio(userDomain.getDomainName());
		
	}
	
	/***
	 * 
	 * @param publicar indica si publica o no
	 */
	public void validarPublicacion(final boolean publicar){
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.app_name),
				getResources().getString(R.string.txtCargandoDefault), true, false);
		
		WsInfomovilCall wsCall = new WsInfomovilCall( new WsInfomovilDelgate() {
			
			@Override
			public void respuestaObj(WSInfomovilMethods metodo,
					WsInfomovilProcessStatus status, Object obj) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
				progressDialog.dismiss();
				
			}
			
			@Override
			public void respuestaCompletada(WSInfomovilMethods metodo,
					long milisegundo, WsInfomovilProcessStatus status) {
				
				progressDialog.dismiss();
				
				switch (status) {
					case EXITO:
						
						WS_UsuarioDominiosVO usuarioDominioFound = null;
						int index = 0;
						
						if(datosUsuario.getUsuarioDominios() != null && 
						(index = datosUsuario.getUsuarioDominios().indexOf(new WS_UsuarioDominiosVO(tipoDominio))) != -1){
							usuarioDominioFound = datosUsuario.getUsuarioDominios().get(index);
						}
						
						
						if(usuarioDominioFound != null && usuarioDominioFound.toString().equalsIgnoreCase("Publicado")){
							Toast.makeText(PublicarActivity.this, R.string.msgDominioYaPublicado, Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(PublicarActivity.this, CuentaActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.left_in, R.anim.left_out);
						}
						else{
							if(publicar){
								publicaDominio();
							}
							else{
								
								Class destino = getIntent().getExtras().getString("source","").
								equalsIgnoreCase("compra")?NombrarVentaDominioActivity.class:MenuPasosActivity.class;
							    Intent intent = new Intent(PublicarActivity.this, destino);
								startActivity(intent);
								overridePendingTransition(R.anim.left_in, R.anim.left_out);
							}
						}
						
						break;
						
					case ERROR_DESCONOCIDO:
						if(publicar){
							publicaDominio();
						}
						else{
							Class destino = getIntent().getExtras().getString("source","").
									equalsIgnoreCase("compra")?NombrarVentaDominioActivity.class:NombrarActivity.class;
							Intent intent = new Intent(PublicarActivity.this, destino);
							startActivity(intent);
							overridePendingTransition(R.anim.left_in, R.anim.left_out);
						}
						
						break;
	
					default:
						break;
				}
				
			}
		}, this);
		
		wsCall.execute(WSInfomovilMethods.GET_STATUS_DOMAIN);
		
	}
	
	public void publicarDominio(View v) {
		
		if (!validaDatos()) {
			
			if(alerta != null)
				alerta.dismiss();
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, mensajeError);
			alerta.setDelegado(this);
			alerta.show();
		}
		else{
			validarPublicacion(true);
		}
		
	}
	
	public void publicaDominio(){
		
		alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, 
				getResources().getString(R.string.msgPublicando));
		alerta.setDelegado(this);
		alerta.show();
		
		crearDominio = true;
		datosUsuario = DatosUsuario.getInstance();
		WS_UserDomainVO userDomain = datosUsuario.getUserDomainData();
		userDomain.setnPais(codigoPais==0?1:codigoPais);
		userDomain.setNombreUsuario(editNombre.getText().toString());
		userDomain.setDireccion1(editDireccion1.getText().toString());
		userDomain.setDireccion2(editDireccion2.getText().toString());
		userDomain.setEmailTel(editCorreo.getText().toString());
		userDomain.setCodigoCamp(datosUsuario.getCodeCamp());
		userDomain.setDomainType(tipoDominio);
		userDomain.setAccion("publicar");
		
	
		WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
		wsCall.setUsrDomain(userDomain);
		wsCall.execute(WSInfomovilMethods.INSERT_USER_DOMAIN);
		
	}

	@Override
	public void initResume() {
		modifico=true;
		
	}

	@Override
	public void guardarInformacion() {
		publicarDominio(null);
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK) {
			nombrePais = data.getStringExtra("paisSeleccionado");
			String auxCod = data.getStringExtra("codigoPais");
			codigoPais = Integer.parseInt(auxCod.substring(1));
			labelPais.setText(nombrePais);
		}
		
	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		validarPublicacion(false);
	}
}


package com.infomovil.infomovil.gui.principal;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appboy.Appboy;
import com.appboy.ui.slideups.AppboySlideupManager;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.fragment.gui.nombrar.FormularioRegistroActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarCompartirActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.PublicarActivity;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.background.VerEjemploPlantillaActivity;
import com.infomovil.infomovil.gui.fragment.principal.LoginActivity;
import com.infomovil.infomovil.gui.fragment.principal.MainActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.fragment.principal.RecuperarPasswordActivity;
import com.infomovil.infomovil.menu.ConfiguracionActivity;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.menu.EstadisticasActivity;
import com.infomovil.infomovil.menu.MenuCompartirActivity;
import com.infomovil.infomovil.menu.NoticiasActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public abstract class InfomovilActivity extends FragmentActivity implements
		AlertViewInterface, WsInfomovilDelgate {
	
	protected Activity context;
	protected DatosUsuario datosUsuario;
	protected AlertView alerta;
	protected RelativeLayout layoutBarra, layoutPrincipal;
	protected ImageView imagenLogo;
	protected LinearLayout menuMapa;
	protected TextWithFont labelVersion;
	protected boolean modifico, actualizacionCorrecta;
	protected Button botonAgregar, botonAceptar, botonEditar, botonPrevisualizar, botonCancelar;
	public boolean isAppWentToBg = false;
	public boolean isWindowFocused = false;
	public boolean isBackPressed = false;
	protected int mensajeActualizacion, indiceSeleccionado;
	protected boolean validasDatosUsuario = true;
	String mensajeTexto;
	
	//Appboy Message in-app
	boolean mRefreshData; 
	
	// variable to track event time
	private long mLastClickTime = 0;
	

	ViewGroup layoutBody;
	
	
	
	/*****
	 * Metodo llamado cuando la accion es si en el boton borrar
	 */
	public abstract void borrarDatosOk();
	
	/****
	 * Metodo para validar si pone el cesto de basura
	 */
	public abstract void checkBotonEliminar();
	public abstract void initCreate();
	public abstract void initResume();
	public abstract void guardarInformacion();
	public abstract void resultActivityCall(int requestCode, int resultCode, Intent data);
	public abstract void acomodaVista();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infomovil_layout);
		context = this;
		layoutBody = (ViewGroup) findViewById(R.id.layoutContent);
		imagenLogo = (ImageView) findViewById(R.id.imagenLogo);
		layoutBarra = (RelativeLayout) findViewById(R.id.layoutBarraNavegacion);
		menuMapa = (LinearLayout) findViewById(R.id.menuInferiorMapa);
		botonAgregar = (Button) findViewById(R.id.botonAgregar);
		botonAceptar = (Button) findViewById(R.id.botonGuardarNavegacion);
		botonEditar = (Button) findViewById(R.id.botonEditarNavegacion);
		botonCancelar = (Button) findViewById(R.id.botonCancelar);
		botonPrevisualizar = (Button) findViewById(R.id.botonVistaPrevia);
//		menuInferior = (LinearLayout) findViewById(R.id.menuInferior);
		labelVersion = (TextWithFont) findViewById(R.id.labelNoVersion);
		layoutPrincipal = (RelativeLayout) findViewById(R.id.layoutGeneralPrincipal);
		mensajeActualizacion = getIntent().getIntExtra("mensaje", R.string.txtCargandoDefault);
		indiceSeleccionado = getIntent().getIntExtra("indiceSeleccionado", 0);
		datosUsuario = DatosUsuario.getInstance();
		setupUI(layoutPrincipal);
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			labelVersion.setText(pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(this instanceof InfomovilActivityFragment){
			validarDatosUsuario();
		}
		
		initCreate();
		acomodaVista();
		
		 Display display = getWindowManager().getDefaultDisplay();
		 Point size = new Point();
		 display.getSize(size);
		 int width = size.x;
		 int height = size.y;
		 DisplayMetrics metrics = getResources().getDisplayMetrics();
		 Log.d("infoLog",
		 "las dimensiones de la pantalla son ancho"+width+" y alto "+height+" densidad "+metrics.density
		 + " " + metrics.densityDpi);
	}
	/*
	 * Mostrar ocultar barra
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		AppboySlideupManager.getInstance().registerSlideupManager(this);
		if (mRefreshData) {
			Appboy.getInstance(this).requestSlideupRefresh();
			mRefreshData = false;
		}
		
		if (getActionBar() != null) {
			getActionBar().show();
		}
		
		validarDatosUsuario();
		initResume();
		checkBotonEliminar();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AppboySlideupManager.getInstance().unregisterSlideupManager(this);
		
		
	}
	
	public void validarDatosUsuario(){
		
		datosUsuario = DatosUsuario.getInstance();
		final Activity activity = this;
		
		
		Log.d("ID getDomainid", datosUsuario.getDomainid()+"");
		
		if (!(activity instanceof NoSesion)  && datosUsuario.getDomainid() <= 0) {
			datosUsuario = DatosUsuario.getInstance();
			datosUsuario.readData();
			Intent i;
			
			if(datosUsuario.getDomainid() > 0){
				
				 String source = getIntent().getStringExtra("source");
				 
				 if(StringUtils.isEmpty(source)){
					 i = new Intent(this, MenuPasosActivity.class);
					 startActivity(i);
					 overridePendingTransition(R.anim.left_in, R.anim.left_out);
				 }
				 else{
					 Log.d("Source Appboy", source);
				 }
//				if(this instanceof NoticiasActivity || this instanceof NombrarActivity 
//					|| this instanceof CuentaActivity || this instanceof 
//					MenuCompartirActivity || this instanceof EstadisticasActivity){
//					i = new Intent(this, this.getClass());
//				}
			}
			else{
				i = new Intent(this, MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
			
			
			
		}		
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if (isIntoApplication() && Appboy.getInstance(this).openSession(this)) {
			mRefreshData = true;
		}
		
	}
	
	
	
	@Override
	protected void onStop() {
		super.onStop();
		if (isIntoApplication()){
			Appboy.getInstance(this).closeSession(this);
		}
		
		applicationdidenterbackground();
	}
	
	public void applicationdidenterbackground() {
		if (!isWindowFocused) {
			isAppWentToBg = true;
			datosUsuario = DatosUsuario.getInstance();
			datosUsuario.saveData();
		}
	}
	
	
	
	/*****
	 * 
	 * @return true si esta en las pantalla de los donde se generaran los eventos
	 */
	private boolean isIntoApplication(){
		return (this instanceof MenuPasosActivity || 
			this instanceof PublicarActivity ||
			this instanceof CuentaActivity || 
			this instanceof ConfiguracionActivity || 
			this instanceof RecuperarPasswordActivity);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		resultActivityCall(requestCode, resultCode, data);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		isWindowFocused = hasFocus;

		if (isBackPressed && !hasFocus) {
			isBackPressed = false;
			isWindowFocused = true;
		}

		super.onWindowFocusChanged(hasFocus);
	}

	public void cargarLayout(int idLayout) {
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(idLayout, layoutBody);
	}

	public void guardarDatos(View v) {
		
		// Preventing multiple clicks, using threshold of 1 second
		if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
			return;
		}

		mLastClickTime = SystemClock.elapsedRealtime();
		
		if (modifico) { 
			if (InfomovilApp.isConnected(this)) {
				alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, getResources().getString(mensajeActualizacion));
				alerta.setDelegado(this);
				alerta.show();
				guardarInformacion();
			}
			else {
				alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtNoConexion));
				alerta.setDelegado(this);
				alerta.show();
			}
		}
		else {
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.mensajeNoingresaInfo));
			alerta.setDelegado(new AlertViewCloseDialog(alerta));
			alerta.show();
		}
		
	}
	
	public void borrarDatos(View v) {
		
		final Activity activity = this;
		
		
		final AlertView alertBorrar = new AlertView(this, AlertViewType.AlertViewTypeQuestion,
		getResources().getString(R.string.msgEliminarDatos));
		alertBorrar.setDelegado(new AlertViewInterface() {
			
			@Override
			public void accionSi() {
				alertBorrar.dismiss();
						
					if (InfomovilApp.isConnected(activity)) {
						alerta = new AlertView(context, AlertViewType.AlertViewTypeActivity, getResources().getString(mensajeActualizacion));
						alerta.setDelegado(this);
						alerta.show();
						borrarDatosOk();
					}
					else{
						AlertView dialog = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
								getResources().getString(R.string.txtNoConexion));
						dialog.setDelegado(this);
						dialog.show();								
					}				
								
			}
			
			@Override
			public void accionNo() {				
				alertBorrar.dismiss();
			}
			
			@Override
			public void accionAceptar() {
				// TODO Auto-generated method stub
				
			}
		});
		
		alertBorrar.show();		
	}
	
	
	
	protected void showMensaje(int idMensaje){	    	
	    	
		 final AlertView dialog = new AlertView(this, AlertViewType.AlertViewTypeInfo,		
				 getResources().getString(idMensaje));

		 dialog.setDelegado(new AlertViewCloseDialog(dialog));
		 dialog.show();    	
	    	
	 }
	
	public void verVistaPrevia(View v) {
//		Log.d("infoLog", "Mostrando Vista Previa");
//		Intent intent = new Intent(this, VistaPrevioDominio.class);
//		intent.putExtra("vistaPrevia", true);
//		startActivity(intent);
		
		String dominio = datosUsuario.getDomainData().getDomainName();
        Log.i("El nombreDominio en preview: ", dominio);
        Log.i("ID Dominio en preview", datosUsuario.getDomainid()+"");
        Log.i("El template en DB es: ", datosUsuario.getDomainData().getTemplate());
        if (dominio.equals(" ") || dominio.indexOf("@") > 0){
            Long idDominio = datosUsuario.getDomainid();
            Intent intent = new Intent(this, VerEjemploPlantillaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("MODO", "vistapreviasindominio");
            bundle.putLong("IDDOMINIO", idDominio);
            intent.putExtras(bundle);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, VerEjemploPlantillaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("MODO", "vistapreviacondominio");
            intent.putExtras(bundle);
            startActivity(intent);
        }
	}

//	public void organizarTabla(View v) {
//		Log.d("infoLog", "Organizar Tabla");
//	}
//
//	public void agregarItem(View v) {
//		Log.d("infoLog", "Agregando Item");
//	}

	@Override
	public void accionSi() {
		guardarInformacion();

	}

	@Override
	public void accionNo() {
		if (alerta != null) {
			alerta.dismiss();
		}

	}

	@Override
	public void accionAceptar() {

	}

	public void acomodarVistaInicio() {
		imagenLogo.setVisibility(View.VISIBLE);
		layoutBarra.setVisibility(View.GONE);
	}

	public void mostrarMenuMapa() {
//		menuInferior.setVisibility(View.GONE);
		menuMapa.setVisibility(View.VISIBLE);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void acomodarVistaConTitulo(int idTitulo, int idImagen) {
		imagenLogo.setVisibility(View.GONE);
		layoutBarra.setVisibility(View.VISIBLE);
		Resources res = getResources();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			layoutBarra.setBackground(res.getDrawable(idImagen));
		} else {
			layoutBarra.setBackgroundDrawable(res.getDrawable(idImagen));
		}
		TextWithFont tituloVista = (TextWithFont) findViewById(R.id.tituloVista);
		tituloVista.setText(res.getString(idTitulo));
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void acomodarVistaConTitulo(String txtTitulo, int idImagen) {
		imagenLogo.setVisibility(View.GONE);
		layoutBarra.setVisibility(View.VISIBLE);
		Resources res = getResources();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			layoutBarra.setBackground(res.getDrawable(idImagen));
		} else {
			layoutBarra.setBackgroundDrawable(res.getDrawable(idImagen));
		}
		TextWithFont tituloVista = (TextWithFont) findViewById(R.id.tituloVista);
		tituloVista.setText(txtTitulo);
	}

	public void acomodarBotones(ButtonStyleShow typeButtonShow) {
		Resources res = getResources();
		switch (typeButtonShow) {
		case ButtonStyleShowAdd:
			botonAgregar.setVisibility(View.VISIBLE);
			botonAceptar.setVisibility(View.GONE);
			botonEditar.setVisibility(View.GONE);
			botonPrevisualizar.setVisibility(View.GONE);
			int px = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 35, res.getDisplayMetrics());
			int rightSpace = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					px, px);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.rightMargin = rightSpace;
			botonAgregar.setLayoutParams(params);
			botonCancelar.setVisibility(View.GONE);
			break;
		case ButtonStyleShowPreview:
			botonAgregar.setVisibility(View.GONE);
			botonAceptar.setVisibility(View.GONE);
			botonEditar.setVisibility(View.GONE);
			botonPrevisualizar.setVisibility(View.VISIBLE);
			botonCancelar.setVisibility(View.GONE);
			break;
		case ButtonStyleShowSave:
			botonAgregar.setVisibility(View.GONE);
			botonAceptar.setVisibility(View.VISIBLE);
			botonEditar.setVisibility(View.GONE);
			botonPrevisualizar.setVisibility(View.GONE);
			botonCancelar.setVisibility(View.GONE);
			break;
		case ButtonStyleShowEdit:
			botonAgregar.setVisibility(View.GONE);
			botonAceptar.setVisibility(View.GONE);
			botonEditar.setVisibility(View.VISIBLE);
			botonPrevisualizar.setVisibility(View.GONE);
			botonCancelar.setVisibility(View.GONE);
			break;
		case ButtonStyleShowAddEdit:
			int px2 = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 35, res.getDisplayMetrics());
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					px2, px2);
			int rightSpace2 = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 55, res.getDisplayMetrics());
			params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params2.addRule(RelativeLayout.CENTER_VERTICAL);
//			params2.addRule(RelativeLayout.ALIGN_RIGHT, R.id.botonEditarNavegacion);
			params2.rightMargin = rightSpace2;
			botonAgregar.setLayoutParams(params2);
			botonAgregar.setVisibility(View.VISIBLE);
			botonAceptar.setVisibility(View.GONE);
			botonEditar.setVisibility(View.VISIBLE);
			botonPrevisualizar.setVisibility(View.GONE);
			botonCancelar.setVisibility(View.GONE);
			break;
		case ButtonStyleShowCancel:
			botonAgregar.setVisibility(View.GONE);
			botonAceptar.setVisibility(View.VISIBLE);
			botonEditar.setVisibility(View.GONE);
			botonPrevisualizar.setVisibility(View.GONE);
			botonCancelar.setVisibility(View.VISIBLE);
			break;
		case ButtonStyleShowNone:
			botonAgregar.setVisibility(View.GONE);
			botonAceptar.setVisibility(View.GONE);
			botonEditar.setVisibility(View.GONE);
			botonPrevisualizar.setVisibility(View.GONE);
			botonCancelar.setVisibility(View.GONE);
			break;
		}
	}

	public boolean verEstadisticas() {
		
		if(!CuentaUtils.isPublicado()){
			CuentaUtils.dialogoPublicar(R.string.msgReportesSinPublicar, this);
			return false;
		}
		
		if (InfomovilApp.getUltimoActivity() == null) {
        	InfomovilApp.setUltimoActivity(this);
        	Intent intent = new Intent(this, EstadisticasActivity.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.up_in, R.anim.up_out);
        }
		else {
			InfomovilApp.mostrarOcultarMenu(this, EstadisticasActivity.class);
		}
		
		return true;
		
	}

	public boolean verCompartir() {
		
		if(!CuentaUtils.isPublicado()){
			CuentaUtils.dialogoPublicar(R.string.msgCompartirSinPublicar, this);
			return false;
		}
		
		if (InfomovilApp.getUltimoActivity() == null) {
        	InfomovilApp.setUltimoActivity(this);
        	Log.d("", "compartir");
    		Intent intent = new Intent(this, MenuCompartirActivity.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.up_in, R.anim.up_out);
        }
		else {
			InfomovilApp.mostrarOcultarMenu(this, MenuCompartirActivity.class);
		}
		
		return true;
		
	}

	public void verCuenta(View v) {
		if (InfomovilApp.getUltimoActivity() == null) {
        	InfomovilApp.setUltimoActivity(this);
        	Intent intent = new Intent(this, CuentaActivity.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.up_in, R.anim.up_out);
//    		menuInferior.setVisibility(View.GONE);
    		if (getActionBar() != null) {
    			getActionBar().hide();
			}
        }
		else {
			InfomovilApp.mostrarOcultarMenu(this, CuentaActivity.class);
		}
		
	}

	public void verConfiguracion(View v) {
		if (InfomovilApp.getUltimoActivity() == null) {
        	InfomovilApp.setUltimoActivity(this);
        	Intent intent = new Intent(this, ConfiguracionActivity.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.up_in, R.anim.up_out);
        }
		else {
			InfomovilApp.mostrarOcultarMenu(this, ConfiguracionActivity.class);
		}
		

	}
	/*
	 * Ocultar el menu y la barra
	 */
	public void ocultarMenuInferior() {
//		menuInferior.setVisibility(View.GONE);
		if (getActionBar() != null) {
			getActionBar().hide();
		}
	}
	
	public void setupUI(View view) {

	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditTextWithFont)) {

	        view.setOnTouchListener(new OnTouchListener() {
	        	@Override
	            public boolean onTouch(View v, MotionEvent event) {
	                hideSoftKeyboard();
	                return false;
	            }


	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView);
	        }
	    }
	}
	
	public void hideSoftKeyboard() {
		try
		{
		    InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Context.INPUT_METHOD_SERVICE);
		    if (inputMethodManager.isAcceptingText()) {
		    	inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
		    }
		}catch (Exception e){
	    	Log.d("infoLog","Error : "+e.getMessage());
		}
	}
	
	public void cambiarBackground() {
		layoutPrincipal.setBackgroundResource(R.drawable.backgroundinicio);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		
		if ((this instanceof MainActivity) || (this instanceof RegistroUsuarioActivity) 
				|| (this instanceof NombrarActivity) || (this instanceof PublicarActivity) || 
				(this instanceof NombrarCompartirActivity) || (this instanceof LoginActivity) || 
				(this instanceof RecuperarPasswordActivity)) {
			return false;
		}
		
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_estadisticas) {
			return verEstadisticas();
		}
		else if (id == R.id.action_compartir) {
			return verCompartir();
		}
		else if (id == R.id.action_cuenta) {
			verCuenta(null);
			return true;
		}
		else if (id == R.id.action_configuracion){
			verConfiguracion(null);
			return true;
		}
		else if (id == R.id.action_noticias){		
			Intent intent = new Intent(this, NoticiasActivity.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.up_in, R.anim.up_out);			
			return true;
		}
		else{ //Salir de la cuenta
			final AlertView dialog = new AlertView(this, AlertViewType.AlertViewTypeQuestion2,		
			getResources().getString(R.string.txtPreguntaCerrar));    	
		    dialog.setDelegado(new SalirCuenta(this, dialog));
		    dialog.show();   
		    return true;
		}
		
//		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
		modifico=true;
		
		if(alerta != null)
			alerta.dismiss();
		
		if(status != WsInfomovilProcessStatus.ERROR_CONEXION){
			AlertView mensajeError = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
					this.getResources().getString(R.string.txtErrorGenerico));
					mensajeError.setDelegado(new AlertViewCloseDialog(mensajeError));
					mensajeError.show();
		}
	}
	
	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		
	}

}

package com.infomovil.infomovil.gui.fragment.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.common.utils.FacebookImage;
import com.infomovil.infomovil.common.utils.ReadFilesUtils;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.TipsActivity;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.background.ColorFondoActivity;
import com.infomovil.infomovil.gui.fragment.background.ElegirTemplateActivity;
import com.infomovil.infomovil.gui.fragment.background.VerEjemploPlantillaActivity;
import com.infomovil.infomovil.gui.fragment.editar.MenuCrearActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivityFragment;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class MenuPasosActivity extends InfomovilActivity {
	
	private Button botonNombrar;
	private AlertView dialog;
	private boolean accionSalir;
	private TextWithFont txtNombreDominio;
	private WsInfomovilDelgate delegado;
	private Button btnAgregarMasContenido;
	
	public void cargarElegirTemplate(View v) {
		Intent i = new Intent(this, ElegirTemplateActivity.class );
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
	
	public void cargarMenuCrear(View v) {
		datosUsuario = DatosUsuario.getInstance();
		Log.i("El plantilla en DB: ", datosUsuario.getDomainData().getTemplate());
		
		if (!StringUtils.isEmpty(datosUsuario.getDomainData().getTemplate())) {
			Intent i = new Intent(this, InfomovilActivityFragment.class);
			i.putExtra("tipoMenuCrear", "menuCrear");
			i.putExtra("source", "agregarMasContenido");
			startActivity(i);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		else {
			dialog = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.eligeColor));
			dialog.setDelegado(this);
			dialog.show();
		}
	}
	
	public void cargarNombrar(View v) {
		if (CuentaUtils.isPublicado()) {
			Intent i = new Intent(this, TipsActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);			
		}
		else{
			CuentaUtils.redireccionarPublicar(this, true);
		}
	}
	
	public void cargarMenuCrearQuickStart(View v) {
		datosUsuario = DatosUsuario.getInstance();
		Log.i("El plantilla en DB: ", datosUsuario.getDomainData().getTemplate());
		
		if (!StringUtils.isEmpty(datosUsuario.getDomainData().getTemplate())) {
			Intent i = new Intent(this, InfomovilActivityFragment.class);
			
			if (CuentaUtils.isPublicado()) {
				i.putExtra("tipoMenuCrear", "menuCrear");
			}
			else{
				i.putExtra("tipoMenuCrear", "menuCrearQuickStart");
			}
			
			startActivity(i);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		else {
			dialog = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.eligeColor));
			dialog.setDelegado(this);
			dialog.show();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	public void salirCuenta(){
		DatosUsuario.getInstance().borrarDatos();	
    	startActivity(new Intent(getBaseContext(), MainActivity.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    	finish();
   
	}
	
	public void verEjemplo(View v) {
		String direccion = "";
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod")){
			direccion = "http://infomovil.com/divertido?vistaPrevia=true";
		}
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
			direccion = "http://qa.mobileinfo.io:8080/divertido?vistaPrevia=true";
		}
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev")){
			direccion = "http://172.17.3.181:8080/templates/Divertido/index.html";
		}
		
		Intent i = new Intent(this, VerEjemploPlantillaActivity.class );
    	Bundle bundle = new Bundle();
    	bundle.putString("MODO", "verejemplo");
    	bundle.putString("URL", direccion);
    	i.putExtras(bundle);
        startActivity(i);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
	
	public void verTutorial(View v) {
		String url = "https://www.youtube.com/watch?v=XyHTERaAlXg";
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	public void accionSi() {
		super.accionSi();
		if (accionSalir) {
			
		}
		else {
			Log.d("infomovilLog", "Accion si");
			dialog.dismiss();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		
	}

	public void accionNo() {
		super.accionNo();
		Log.d("infomovilLog", "Accion no");
		dialog.dismiss();
	}

	public void accionAceptar() {
		super.accionAceptar();
	}
	
	/****
	 * Valida si tiene 4 elementos para publicar
	 * @return
	 */
	public static boolean fueEditado(Resources res, boolean soloUnEditado) {		
		
		String[] arregloTitulos = new String[]{res.getString(R.string.txtNombreoEmpresa),
	    		res.getString(R.string.txtDescripcionCortaTitulo), res.getString(R.string.txtContacto), 
	    		res.getString(R.string.productoServicio)};
		
		Map<String,Boolean> estatusTitulos = ReadFilesUtils.buscaEditados(res);
		List<String> listaTitulos = new ArrayList<String>(Arrays.asList(arregloTitulos));
		
		int totalItems = 0;
		for(String titulo: listaTitulos){
			if(estatusTitulos.containsKey(titulo) && estatusTitulos.get(titulo)){
				++totalItems;
			}
		}
		
		if(soloUnEditado){
			return totalItems >= 1;
		}
		else{
			return totalItems == listaTitulos.size();
		}
		
	}
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		if(accionSalir) {
			salirCuenta();
		}
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		if (accionSalir) {
			salirCuenta();
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
		delegado = this;
		accionSalir = false;
		
		cargarLayout(R.layout.menu_pasos_layout);
		acomodarVistaConTitulo(R.string.tituloPrincipal, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		txtNombreDominio = (TextWithFont) findViewById(R.id.labelTituloPantalla);
		btnAgregarMasContenido  = (Button) findViewById(R.id.btnAgregarMasContenido);
		
		botonNombrar = (Button) findViewById(R.id.btnNombrar);
		datosUsuario = DatosUsuario.getInstance();
		
	}
	
	/***
	 * Revisa si ya esta publicado el domoinio
	 */
	public void validarDominio(){
		if(StringUtils.isEmpty(datosUsuario.getDomainData().getDomainName())){
			WsInfomovilCall wsCall = new WsInfomovilCall(null, this);
			wsCall.execute(WSInfomovilMethods.GET_STATUS_DOMAIN);
		}
	}
	
	

	@Override
	public void initResume() {
		
		validarDominio();
		
		final String nombreDominio;
		
		if (CuentaUtils.isPublicado()) {
			botonNombrar.setBackgroundResource(R.drawable.tips);
			txtNombreDominio.setVisibility(View.VISIBLE);
			btnAgregarMasContenido.setVisibility(View.GONE);
			
			if(InfomovilApp.tipoInfomovil.equals("tel")){
				nombreDominio  = InfomovilApp.urlInfomovil;
			}
			else{
				nombreDominio = InfomovilApp.urlInfomovil;
			}
			
			txtNombreDominio.setText(nombreDominio);
			txtNombreDominio.setTypeface(null, Typeface.BOLD); 
			txtNombreDominio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+nombreDominio));
					startActivity(browserIntent);
					
				}
			});
			
		}
		else{
			txtNombreDominio.setVisibility(View.GONE);
			btnAgregarMasContenido.setVisibility(View.VISIBLE);
		}
		
		if(datosUsuario.getArrayTipoContacto() == null) {
			ReadFilesUtils.leerArchivo(getResources());
		}	
		
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

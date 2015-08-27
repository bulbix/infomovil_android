package com.infomovil.infomovil.gui.fragment.principal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;

public class PoliticasActivity extends InfomovilActivity  implements NoSesion{
	
	String tituloVista;
	private WebView webPoliticas;
	int vistaSeleccionada;
	
	@Override
	public void onResume() {
		super.onResume();
		ocultarMenuInferior();
	}
	
	public void guardarDatos(View v) {
		this.finish();
		overridePendingTransition(R.anim.up_in, R.anim.up_out);
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
	@SuppressLint("SetJavaScriptEnabled")
	public void initCreate() {
		cargarLayout(R.layout.politicas_layout);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		tituloVista = getIntent().getStringExtra("tituloVista");
		vistaSeleccionada = getIntent().getExtras().getInt("vistaSeleccionada", 0);
		acomodarVistaConTitulo(tituloVista, R.drawable.plecamorada);
		webPoliticas = (WebView) findViewById(R.id.idPoliticasWeb);
		webPoliticas.getSettings().setJavaScriptEnabled(true);
		webPoliticas.setWebChromeClient(new WebChromeClient());
		validasDatosUsuario = false;
		if(vistaSeleccionada == 1) {
			webPoliticas.loadUrl("http://infomovil.com/pages/legal/terminos.html");
		}else {
			webPoliticas.loadUrl("http://infomovil.com/pages/legal/aviso.html");
			
		}		
	}

	@Override
	public void initResume() {
		// TODO Auto-generated method stub
		
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

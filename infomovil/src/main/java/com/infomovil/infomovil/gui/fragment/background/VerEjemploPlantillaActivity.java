package com.infomovil.infomovil.gui.fragment.background;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;

public class VerEjemploPlantillaActivity extends InfomovilActivity{
	
	Bundle bundle;
	private WebView webViewDominio;
	String nombreDominio = " ";
	String url = " ";
	AlertViewInterface delegado;

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
		datosUsuario = DatosUsuario.getInstance();
		delegado = this;
		cargarLayout(R.layout.vista_previa_dominio_layout);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		
		webViewDominio = (WebView) findViewById(R.id.webViewDominio);
		webViewDominio.getSettings().setJavaScriptEnabled(true);
		webViewDominio.getSettings().setPluginState(PluginState.ON);
		webViewDominio.setWebChromeClient(new WebChromeClient());
		
		bundle = this.getIntent().getExtras();
		webViewDominio.setWebViewClient(new WebViewClient(){
			
			@Override  
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {  
		        if (url.startsWith("tel:")) { 
		            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url)); 
		            startActivity(intent); 
		        }
		        else if (url.startsWith("mailto:")) {
	                String body = "Enter your Question, Enquiry or Feedback below:\n\n";
	                Intent mail = new Intent(Intent.ACTION_SEND);
	                mail.setType("application/octet-stream");
	                mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"email address"});
	                mail.putExtra(Intent.EXTRA_SUBJECT, "Info");
	                mail.putExtra(Intent.EXTRA_TEXT, body);
	                startActivity(mail);
	                return true;
	            }
		        else if(url.startsWith("http:") || url.startsWith("https:")) {
		            view.loadUrl(url);
		        }
		        return true;  
		    }
			
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				alerta = new AlertView(context,	AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.txtCargandoDefault));
				alerta.setDelegado(delegado);
				alerta.show();
			}

			public void onPageFinished(WebView view, String url) {

				if (alerta != null)
					alerta.dismiss();
			}
			
		});
		
		if(bundle.getString("MODO").equals("verejemplo")){
			acomodarVistaConTitulo(R.string.tituloEjemplo, R.drawable.plecaturquesa);
			webViewDominio.loadUrl(bundle.getString("URL"));
		}
		
		if(bundle.getString("MODO").equals("vistapreviasindominio")){
			acomodarVistaConTitulo(getResources().getString(R.string.tituloVistaPrevia), R.drawable.plecaverde);
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod")){
				url = "http://infomovil.com/xxx?vistaPrevia=true&idDominio=" + bundle.getLong("IDDOMINIO");
			}
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
				url = "http://qa.mobileinfo.io:8080/xxx?vistaPrevia=true&idDominio=" + bundle.getLong("IDDOMINIO");
			}
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev")){
				url = "http://172.17.3.181:8080/xxx?vistaPrevia=true&idDominio=" + bundle.getLong("IDDOMINIO");
			}
			webViewDominio.loadUrl(url);
		}
		
		if(bundle.getString("MODO").equals("vistapreviacondominio")){
			acomodarVistaConTitulo(getResources().getString(R.string.tituloVistaPrevia), R.drawable.plecaverde);
			nombreDominio = datosUsuario.getDomainData().getDomainName();
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod")){
				url = "http://infomovil.com/" + nombreDominio + "?vistaPrevia=true";
			}
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
				url = "http://qa.mobileinfo.io:8080/" + nombreDominio + "?vistaPrevia=true";
			}
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev")){
				url = "http://172.17.3.181:8080/" + nombreDominio + "?vistaPrevia=true";
			}
			
			Log.i("El dominio: ", nombreDominio);
			Log.i("La url final: ", url);
			webViewDominio.loadUrl(url);
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

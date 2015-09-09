package com.infomovil.infomovil.gui.fragment.editar;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.server.JSONParserVideos;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class PlayVideoFragment extends InfomovilFragment {
	
	private WebView videoYoutube;
	private Button botonBorrar;
	private ItemSelectModel video;
	private AlertView progressDialog = null;

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.play_video_layout, container, false);
		videoYoutube = (WebView) view.findViewById(R.id.webViewYoutube);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarVideo);		
		return view;	
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initCreate() {
		
		progressDialog = new AlertView(getActivity(), AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.txtCargandoDefault));
		progressDialog.show();
		acomodarVistaConTitulo(R.string.txtTituloVideo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		mensajeActualizacion  = R.string.msgGuardarVideo;
		modifico = false;
		video = (ItemSelectModel)getArguments().getSerializable("videoSeleccionado");
		String idVideo = JSONParserVideos.getYoutubeVideoId(video.getLinkVideo());			
		videoYoutube.getSettings().setJavaScriptEnabled(true);
		videoYoutube.getSettings().setPluginState(PluginState.ON);
		videoYoutube.setWebChromeClient(new WebChromeClient());
		videoYoutube.setWebViewClient(new WebViewClient() {

			   public void onPageFinished(WebView view, String url) {
			       progressDialog.dismiss();
			    }
			});
		videoYoutube.loadUrl("http://www.youtube.com/embed/" + idVideo);
	}
	
	@Override
	public void accionAceptar() {		
		super.accionAceptar();
		if (actualizacionCorrecta) {
			videoYoutube.loadUrl("about:blank");
			infomovilInterface.returnFragment("MenuCrear");
		}		
	}
	
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		if(alerta != null)
			alerta.dismiss();

		if (status == WsInfomovilProcessStatus.EXITO) {
			actualizacionCorrecta = true;
			AlertView alertBien = new AlertView(activity, 
					AlertViewType.AlertViewTypeInfo, getResources().
					getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}

		else {
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(activity, AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.txtErrorGenerico));
			alertError.setDelegado(this);
			alertError.show();
		}
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarDatosOk() {
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setActividad("Borro Video");
		wsCall.execute(WSInfomovilMethods.DELETE_VIDEO);		
	}

	@Override
	public void checkBotonEliminar() {
		ItemSelectModel videoSel = DatosUsuario.getInstance().getVideoSeleccionado();

		if(videoSel != null &&
				!StringUtils.isEmpty(videoSel.getLinkVideo())){					
			botonBorrar.setVisibility(View.VISIBLE);
		}
		else{//Viene de uno nuevo 
			modifico = true;
		}
		
	}

	@Override
	public void initResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarInformacion() {
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());		
		wsCall.setModeloVideo(video);
		wsCall.setActividad("Edito Video");
		wsCall.execute(WSInfomovilMethods.INSERT_VIDEO);
		
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
	public void agregarNuevoItem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void organizarTabla() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String validarCampos() {
		// TODO Auto-generated method stub
		return "Correcto";
	}
	
	@Override
	public void keyDownAction(){
		videoYoutube.loadUrl("about:blank");
		infomovilInterface.returnFragment("");
	}
	
	

}

package com.infomovil.infomovil.gui.fragment.editar;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class VistaPreviaVideoActivity extends InfomovilFragment {

	private ImageView imgYoutube;
	private ItemSelectModel video;       
	private TextView txtTitulo, txtDescripcion;
	private Button botonBorrar;	

	@Override
	public void accionAceptar() {		
		super.accionAceptar();
		if (actualizacionCorrecta) {			
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
	public View getView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.vista_previa_video_layout, container, false);
		imgYoutube = (ImageView)view.findViewById(R.id.imgYoutube);
		txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
		txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarVideo);
		return view;	
	}

	@Override
	public void borrarDatosOk() {
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
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
	@SuppressLint("SetJavaScriptEnabled") 
	public void initCreate() {

		try{
			acomodarVistaConTitulo(R.string.txtTituloVideo, R.drawable.plecaverde);
			acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);			

			Bundle b = getArguments();
			video = (ItemSelectModel) b.get("videoSeleccionado");			
			mensajeActualizacion  = R.string.msgGuardarVideo;				
			Log.d("Link de la imagen", video.getLinkImagen());		

			// show The Image
			new DownloadImageTask(imgYoutube).execute(video.getLinkImagen());			
			txtTitulo.setText(video.getTitulo());
			txtDescripcion.setText(video.getDescripcion());			
			
			imgYoutube.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {				
					playVideo(v);
				}
			});			
			
			txtTitulo.addTextChangedListener(new EditTextWithFontWatcher());
			txtDescripcion.addTextChangedListener(new EditTextWithFontWatcher());
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void playVideo(View v){
		PlayVideoFragment playVideo = new PlayVideoFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("video", video);
		playVideo.setArguments(bundle);
		infomovilInterface.loadFragment(playVideo, null, "PlayVideo");
	}


	class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}	
	
	@Override
	public void guardarInformacion() {
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setModeloVideo(video);
		wsCall.execute(WSInfomovilMethods.INSERT_VIDEO);

	}

	@Override
	public void initResume() {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
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
	public void agregarNuevoItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void organizarTabla() {
		// TODO Auto-generated method stub

	}

	@Override
	public String validarCampos() {
		return "Correcto";
	}
}

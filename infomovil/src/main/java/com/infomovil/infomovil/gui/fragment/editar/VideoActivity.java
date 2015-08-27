package com.infomovil.infomovil.gui.fragment.editar;

import java.util.List;

import org.apache.commons.validator.routines.RegexValidator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.Progressable;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.server.JSONParserVideos;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class VideoActivity extends InfomovilFragment {
	
	private AlertView progressDialog = null;
	private Button botonVideo;
	private TextView txtVideo;	
	
	public void buscarVideo(View v) {		
		String regExp = 
		"(https?://)?(www\\.)?(yotu\\.be/|youtube\\.com/)?((.+/)?(watch(\\?v=|.+&v=))?(v=)?)([\\w_-]{11})(&.+)?";
		RegexValidator validator = new RegexValidator(regExp, false);		
		try{	
			
			String criterio = txtVideo.getText().toString().trim();
			
			if(validator.isValid(criterio)){
				JSONParserVideos jsonParser = new JSONParserVideos(new ProgressableAdapter() {					
					@Override
					public void execute(List<ItemSelectModel> videos) {
						if(videos != null && videos.size() > 0){
							PlayVideoFragment playVideo = new PlayVideoFragment();
							Bundle bundle = new Bundle();			
							bundle.putSerializable("videoSeleccionado", videos.get(0));
							playVideo.setArguments(bundle);
							infomovilInterface.loadFragment(playVideo, null, "VistaPreviaVideo");
						}
						else{
							Toast.makeText(getActivity(), "Video no disponible", Toast.LENGTH_SHORT).show();
						}						
					}
				},				
				criterio,1, false);				
				jsonParser.readAndParseJSONVideos();
			}
			else{
				
				JSONParserVideos jsonParser = new JSONParserVideos(new ProgressableAdapter() {
					@Override
					public void execute(List<ItemSelectModel> videos) {
						if(videos != null && videos.size() > 0){
							SeleccionarVideoActivity selectVideo = new SeleccionarVideoActivity();
							selectVideo.setListaVideo(videos);
							infomovilInterface.loadFragment(selectVideo, null, "SelectVideo");
						}
						else{
							Toast.makeText(getActivity(), "Refine su búsqueda", Toast.LENGTH_SHORT).show();
						}						
					}
				},
				criterio,20, true);				
				jsonParser.readAndParseJSONVideos();				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	class ProgressableAdapter implements Progressable{

		@Override
		public void showDialog() {
			progressDialog = new AlertView(getActivity(), AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.txtCargandoDefault));
			progressDialog.show();
		}
		
		@Override
		public void hideDialog() {
			progressDialog.dismiss();
		}

		@Override
		public void execute(List<ItemSelectModel> videos) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtTituloVideo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		mensajeActualizacion  = R.string.msgGuardarVideo;
		
		
		botonVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				buscarVideo(v);
			}
		});		
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View view = inflater.inflate(R.layout.video_layout, container, false);
		botonVideo = (Button) view.findViewById(R.id.btnBuscarVideo);
		txtVideo = (TextView) view.findViewById(R.id.txtVideo);
		return view;
	}
	
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
	}
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
	}	

	@Override
	public void borrarDatosOk() {		
	}

	@Override
	public void checkBotonEliminar() {		
				
	}

	@Override
	public void initResume() {	
	}

	@Override
	public void guardarInformacion() {		
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
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}

}

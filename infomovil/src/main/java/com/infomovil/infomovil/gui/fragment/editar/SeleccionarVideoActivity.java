package com.infomovil.infomovil.gui.fragment.editar;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.ItemListView;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.server.JSONParserVideos;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class SeleccionarVideoActivity extends InfomovilFragment{
	
	private AlertView progressDialog = null;
	private EditTextWithFont textBuscar;
	private List<ItemSelectModel> listaVideo;
	private ItemListView gridView;
	Context context;
	JSONParserVideos parserVideos;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.seleccionar_video_layout, container, false);
		gridView = (ItemListView) view.findViewById(R.id.videosListView);
		gridView.setOnItemClickListener(new GridItemViewClickListener());
//		textBuscar = (EditTextWithFont) view.findViewById(R.id.txtVideoBuscar);
		return view;
	}
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtBuscarVideo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		context = activity;		
		gridView.setVideos(listaVideo);
	}
	
	private class GridItemViewClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ItemSelectModel video = (ItemSelectModel) parent.getItemAtPosition(position);
			Log.d("infoLog", "la posicion es "+position+" y el video es "+video.getLinkVideo());
			
			PlayVideoFragment playVideo = new PlayVideoFragment();
			Bundle bundle = new Bundle();			
			bundle.putSerializable("videoSeleccionado", video);
			playVideo.setArguments(bundle);
			infomovilInterface.loadFragment(playVideo, null, "VistaPreviaVideo");
		}
		
	}
	
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == InfomovilConstants.STATIC_INTEGER_VALUE) {
			if (resultCode == Activity.RESULT_OK) {
				int respuesta = data.getIntExtra("seleccionoVideo", 0);
				if (respuesta == 1) {
					Log.d("infoLog", "Si obtuvo el resultado ****************************");
					Intent intent = new Intent();
					intent.putExtra("selecciono", respuesta);
					activity.setResult(Activity.RESULT_OK, intent);
					activity.finish();
				}
				else {
					Log.d("infoLog", "No obtuvo el resultado ****************************");
				}
			}
		}
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
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
	public void initResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarInformacion() {
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

	public List<ItemSelectModel> getListaVideo() {
		return listaVideo;
	}

	public void setListaVideo(List<ItemSelectModel> listaVideo) {
		this.listaVideo = listaVideo;
	}
	
	
}

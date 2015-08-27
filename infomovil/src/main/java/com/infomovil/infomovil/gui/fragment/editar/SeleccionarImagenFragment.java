package com.infomovil.infomovil.gui.fragment.editar;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.ItemListView;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class SeleccionarImagenFragment extends InfomovilFragment {
	
	private List<ItemSelectModel> listaImagenes;
	private ItemListView gridView;
	private Context context;
	private OnItemClickListener onCLickListener;
	private int titulo;
	
	
	
	public SeleccionarImagenFragment(List<ItemSelectModel> listaImagenes,OnItemClickListener onCLickListener, int titulo) {
		this.listaImagenes = listaImagenes;
		this.onCLickListener = onCLickListener;
		this.titulo = titulo;
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
	public View getView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.seleccionar_video_layout, container, false);
		gridView = (ItemListView) view.findViewById(R.id.videosListView);
		gridView.setOnItemClickListener(onCLickListener);
		return view;
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
		acomodarVistaConTitulo(titulo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		context = activity;		
		gridView.setVideos(listaImagenes);
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

}

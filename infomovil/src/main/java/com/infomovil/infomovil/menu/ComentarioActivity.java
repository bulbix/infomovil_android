package com.infomovil.infomovil.menu;

import android.content.Intent;

import com.appboy.ui.AppboyFeedbackFragment;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;


public class ComentarioActivity extends InfomovilActivity{

	
	private AppboyFeedbackFragment fragment;
	
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
		// TODO Auto-generated method stub
		cargarLayout(R.layout.menu_comentario);
		acomodarVistaConTitulo(R.string.txtComentarioMenu, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		
		
		fragment = (AppboyFeedbackFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentFeed);	
		fragment.setEmail(DatosUsuario.getInstance().getNombreUsuario());
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

package com.infomovil.infomovil.menu;

import android.content.Intent;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;

public class NoticiasActivity extends InfomovilActivity {

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
		cargarLayout(R.layout.menu_noticias);
		acomodarVistaConTitulo(R.string.txtNoticiasMenu, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		
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
	
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		Intent intent = new Intent(this, MenuPasosActivity.class);
//		startActivity(intent);
//		overridePendingTransition(R.anim.left_in, R.anim.left_out);
//	}

}

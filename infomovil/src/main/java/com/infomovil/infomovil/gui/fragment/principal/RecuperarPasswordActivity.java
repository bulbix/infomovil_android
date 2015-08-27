package com.infomovil.infomovil.gui.fragment.principal;

import mx.com.infomovil.commons.CommonUtils;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RecuperarPasswordActivity extends InfomovilActivity implements NoSesion {
	
	private EditTextWithFont txtEmail;
	ProgressDialog progressDialog; 
	
	public void cancelarOperacion(View v) {
		this.finish();
		overridePendingTransition(R.anim.up_in, R.anim.up_out);
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		progressDialog.dismiss();

		if (status == WsInfomovilProcessStatus.EXITO) {
			
			Appboy appBoyInstance = Appboy.getInstance(this);
			appBoyInstance.changeUser(txtEmail.getText().toString());
			appBoyInstance.logCustomEvent("ChangePassword");
			
			new AlertDialog.Builder(this)
			.setMessage(R.string.cambiarPassLabel2)
			.setPositiveButton(R.string.txtAceptar,  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					overridePendingTransition(R.anim.up_in, R.anim.up_out);
					
				}
			})
			.create().show();
		} else {
			new AlertDialog.Builder(this)
			.setMessage(R.string.txtUsuarioNoEncontrado)
			.setPositiveButton(R.string.txtAceptar,null)
			.create().show();
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
		cargarLayout(R.layout.recuperar_password_layout);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowCancel);
		acomodarVistaConTitulo(R.string.cambioPassword, R.drawable.plecamorada);
		txtEmail = (EditTextWithFont) findViewById(R.id.txtEmailCambiaPass);
		validasDatosUsuario = false;
	}

	@Override
	public void initResume() {
		modifico=true;
		
	}

	@Override
	public void guardarInformacion() {
		alerta.dismiss();
		if (CommonUtils.isValidEmail(txtEmail.getText().toString())) {
			progressDialog= ProgressDialog.show(this,  getString(R.string.app_name), getResources().getString(R.string.txtCargando), true, false);
			
			WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
			wsCall.setStrConsulta(txtEmail.getText().toString());
			wsCall.execute(WSInfomovilMethods.GET_HASH_CAMBIO_PASSWORD);
		} else {
			showMensaje(R.string.emailInvalido);
		}		
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

package com.infomovil.infomovil.fragment.gui.nombrar;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.LoginRedSocial;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;

public class PreRegistroActivity extends InfomovilActivity implements NoSesion {
	
	private UiLifecycleHelper uiHelper;
	private LoginButton loginBtn;

	@Override
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBotonEliminar() {
		// TODO Auto-generated method stub
		
	}
	
	public void crearCuenta(View v){
		datosUsuario = DatosUsuario.getInstance();
    	datosUsuario.borrarDatos();
    	
    	Intent i = new Intent(this, FormularioRegistroActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, LoginRedSocial.statusCallback);
		uiHelper.onCreate(savedInstanceState);
		
		SalirCuenta.callFacebookLogout(this);
		
		loginBtn = (LoginButton) findViewById(R.id.authButton);
		loginBtn.setReadPermissions(Arrays.asList("email","user_photos"));
		loginBtn.setUserInfoChangedCallback(new LoginRedSocial.UserInfoChangedCallbackImpl
		(this, alerta, "Facebook","registro"));
	}
	

	@Override
	public void initCreate() {
		cargarLayout(R.layout.activity_preregistro);
		acomodarVistaInicio();
        cambiarBackground();
		
	}

	@Override
	public void initResume() {
		ocultarMenuInferior();
		uiHelper.onResume();
		CompraInfomovil.configurar(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
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

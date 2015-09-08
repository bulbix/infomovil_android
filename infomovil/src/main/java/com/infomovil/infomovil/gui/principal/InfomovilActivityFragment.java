package com.infomovil.infomovil.gui.principal;

import org.apache.commons.lang3.StringUtils;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.fragment.InfomovilFragmentInterface;
import com.infomovil.infomovil.gui.fragment.editar.GaleriaPaso2Activity;
import com.infomovil.infomovil.gui.fragment.editar.MenuCrearActivity;
import com.infomovil.infomovil.gui.fragment.editar.MenuCrearQuickStartFragment;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class InfomovilActivityFragment extends InfomovilActivity implements InfomovilFragmentInterface {

	InfomovilFragment infomovilFragment;

	public static boolean isAppWentToBg = false;
	public static boolean isWindowFocused = false;
	public static boolean isBackPressed = false;
	private String nombreFragmentoActual;
	

	ViewGroup layoutBody;
	
	
//	public void toggleMenu(boolean visible) {
//		if (visible) {
//			menuInferior.setVisibility(View.VISIBLE);
//		}
//		else {
//			menuInferior.setVisibility(View.GONE);
//		}
//	}


	public void guardarDatos(View v) {
		infomovilFragment.guardarDatos();
		
	}
	
	public void borrarDatos(View v) {
		infomovilFragment.borrarDatos();
	}
	
	public void agregarItem(View v) {
		Log.d("infoLog", "Agregando Item");
		infomovilFragment.agregarNuevoItem();		
	}
	
	public void organizarTabla(View v) {
		Log.d("infoLog", "Agregando Item");
		infomovilFragment.organizarTabla();		
	}

	
	public void cambiarBackground() {
		layoutPrincipal.setBackgroundResource(R.drawable.backgroundinicio);
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
		// TODO Auto-generated method stub
		cargarLayout(R.layout.infomovil_fragment_layout);
		
		Intent i = getIntent();
		String tipoMenuCrear = i.getStringExtra("tipoMenuCrear");
		
		InfomovilFragment menuCrear = null;
		
		if(tipoMenuCrear.equals("menuCrear") && CuentaUtils.isPublicado()){
			menuCrear = new MenuCrearActivity();
		}
		else{
			menuCrear = new MenuCrearQuickStartFragment();
		}
		
		if(!StringUtils.isEmpty(i.getStringExtra("source")) 
			&& i.getStringExtra("source").equalsIgnoreCase("agregarMasContenido")){
			menuCrear = new MenuCrearActivity();
		}
			
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.fragmentPrincipalMenu, menuCrear, "MenuCrear");
		fragmentTransaction.addToBackStack("MenuCrear");
		nombreFragmentoActual = "MenuCrear";
		fragmentTransaction.commit();
		context = this;
	}

	@Override
	public void initResume() {
		
	}

	@Override
	public void guardarInformacion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		infomovilFragment.resultActivityCall(requestCode, resultCode, data);
	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acomodarBotonesActivity(
			com.infomovil.infomovil.common.utils.ButtonStyleShow typeButtonShow) {
		super.acomodarBotones(typeButtonShow);
		
	}

	@Override
	public void loadFragment(InfomovilFragment fragmento, Bundle data, String name) {
		// TODO Auto-generated method stub
		infomovilFragment = fragmento;
		
		FragmentManager fragmentManager = getFragmentManager();	
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragmentPrincipalMenu, fragmento, name);
		fragmentTransaction.addToBackStack(name);
		fragmentTransaction.commit();
	}
	
	@Override
	public void removeFragment(String name) {
		FragmentManager fragmentManager = getFragmentManager();	
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(fragmentManager.findFragmentByTag(name));
		fragmentTransaction.commit();
	}

	@Override
	public void returnFragment(String name) {
		// TODO Auto-generated method stub
		FragmentManager fm = getFragmentManager();
		if (name.length() == 0) {
			fm.popBackStack();
			FragmentManager.BackStackEntry backEntry=getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-2);
		    String str=backEntry.getName();
			infomovilFragment = (InfomovilFragment) getFragmentManager().findFragmentByTag(str);
		}
		else {
		    infomovilFragment = (InfomovilFragment)fm.findFragmentByTag(name);
		    fm.popBackStack(name, 0);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(infomovilFragment instanceof GaleriaPaso2Activity){
			GaleriaPaso2Activity galeriaPaso2 = (GaleriaPaso2Activity)infomovilFragment;
			if(!galeriaPaso2.isValidarKeydown()){
				infomovilFragment.setModifico(false);
			}
		}
		
		return checkKeyBack(keyCode, event);		
	}
	
	private boolean checkKeyBack(int keyCode, KeyEvent event){
		if((keyCode == KeyEvent.KEYCODE_BACK)) {
			FragmentManager fm = getFragmentManager();
			int nFragments = fm.getBackStackEntryCount();
			if (nFragments > 1) {
				if (infomovilFragment == null) {
					return super.onKeyDown(keyCode, event);
				}
				else {
				if(infomovilFragment.isModifico()){
					
					final AlertView alertPregunta = new AlertView(this,
					AlertViewType.AlertViewTypeQuestion, getResources().getString(R.string.txtPreguntaSalir));
					alertPregunta.setDelegado( new AlertViewInterface() {
						
						@Override
						public void accionSi() {
							alertPregunta.dismiss();
							infomovilFragment.guardarDatos();							
						}
						
						@Override
						public void accionNo() {
							alertPregunta.dismiss();
							infomovilFragment.keyDownAction();
						}
						
						@Override
						public void accionAceptar() {
							// TODO Auto-generated method stub
							
						}
					});
					alertPregunta.show();					
				}
				else{
					infomovilFragment.keyDownAction();
				}
			}
				
				return false;
			}
			else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return false;
		
	}

	public String getNombreFragmentoActual() {
		return nombreFragmentoActual;
	}

	public void setNombreFragmentoActual(String nombreFragmentoActual) {
		this.nombreFragmentoActual = nombreFragmentoActual;
	}

	@Override
	public void toggleMenu(boolean visible) {
		// TODO Auto-generated method stub
	}

}

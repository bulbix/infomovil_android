package com.infomovil.infomovil.gui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.NoSesion;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.editar.MenuCrearActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public abstract class InfomovilFragment extends Fragment implements AlertViewInterface, WsInfomovilDelgate {
	
	protected InfomovilFragmentInterface infomovilInterface;
	protected DatosUsuario datosUsuario;
	protected AlertView alerta;
	protected boolean modifico, actualizacionCorrecta;
	protected int mensajeActualizacion, indiceSeleccionado;
	protected Activity activity;
	
	// variable to track event time
	private long mLastClickTime = 0;
	
	
	public abstract View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	/*****
	 * Metodo llamado cuando la accion es si en el boton borrar
	 */
	public abstract void borrarDatosOk();
	
	/****
	 * Metodo para validar si pone el cesto de basura
	 */
	public abstract void checkBotonEliminar();
	public abstract void initCreate();
	public abstract void initResume();
	public abstract void guardarInformacion();
	public abstract void resultActivityCall(int requestCode, int resultCode, Intent data);
	public abstract void acomodaVista();
	public abstract void agregarNuevoItem();
	public abstract void organizarTabla();	
	public void keyDownAction(){}	
	public abstract String validarCampos();
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		resultActivityCall(requestCode, resultCode, data);
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		View view = getView(inflater, container, savedInstanceState);
		return  view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		infomovilInterface = (InfomovilFragmentInterface) activity;
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datosUsuario = DatosUsuario.getInstance();
		activity = (Activity) infomovilInterface;
		initCreate();
		acomodaVista();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		validarDatosUsuario();
		initResume();
		checkBotonEliminar();
	}
	
//	public void addItem() {
//		agregarNuevoItem();
//	}
//
	
	public void validarDatosUsuario(){
		
		datosUsuario = DatosUsuario.getInstance();
		final Activity activity = getActivity();
		
		
		Log.d("ID getDomainid", datosUsuario.getDomainid()+"");
		
		if (!(activity instanceof NoSesion)  && datosUsuario.getDomainid() <= 0) {
			datosUsuario = DatosUsuario.getInstance();
			datosUsuario.readData();
			
			Intent i = new Intent(getActivity(), MenuPasosActivity.class);
			startActivity(i);
			getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		
	}
	
	public void guardarDatos() {
		
		// Preventing multiple clicks, using threshold of 1 second
		if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
			return;
		}
		
		mLastClickTime = SystemClock.elapsedRealtime();
		
		Log.d("infoLog", "Guardando la informacion");
		
		
		if (modifico) {			
			
			String msgValidacion = validarCampos();
			
			if (InfomovilApp.isConnected(activity)) {

				if(msgValidacion.equals("Correcto")){
					alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity, activity.getResources().getString(mensajeActualizacion));
					alerta.setDelegado(this);
					alerta.show();
					guardarInformacion();						
				}
				else{
					final AlertView alertModif = new AlertView(getActivity(), AlertViewType.
							AlertViewTypeInfo, msgValidacion);
					alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
					alertModif.show();
				}

			}
			else {
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo, activity.getResources().getString(R.string.txtNoConexion));
				alerta.setDelegado(this);
				alerta.show();
			}
		}
		else {
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo, activity.getResources().getString(R.string.mensajeNoingresaInfo));
			alerta.setDelegado(new AlertViewCloseDialog(alerta));
			alerta.show();
		}
	}
	
	public void borrarDatos() {
		final AlertView alertBorrar = new AlertView(activity, AlertViewType.AlertViewTypeQuestion,
				getResources().getString(R.string.msgEliminarDatos));
				alertBorrar.setDelegado(new AlertViewInterface() {
					
					@Override
					public void accionSi() {
						alertBorrar.dismiss();
								
							if (InfomovilApp.isConnected(activity)) {
								alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity, getResources().getString(mensajeActualizacion));
								alerta.setDelegado(this);
								alerta.show();
								borrarDatosOk();
							}
							else{
								AlertView dialog = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
										getResources().getString(R.string.txtNoConexion));
								dialog.setDelegado(this);
								dialog.show();								
							}				
										
					}
					
					@Override
					public void accionNo() {				
						alertBorrar.dismiss();
					}
					
					@Override
					public void accionAceptar() {
						// TODO Auto-generated method stub
						
					}
				});
				
				alertBorrar.show();
	}
	
	
	public void accionSi() {
		if (alerta != null)
			alerta.dismiss();
		
		guardarDatos();
	}
	
	public void accionNo() {
		if (alerta != null)
			alerta.dismiss();
	}
	
	public void accionAceptar() {
		
	}
	
	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
		modifico=true;
		
		if(alerta != null)
			alerta.dismiss();
		
		if(status != WsInfomovilProcessStatus.ERROR_CONEXION){
			AlertView mensajeError = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
					getResources().getString(R.string.txtErrorGenerico));
			mensajeError.setDelegado(this);
			mensajeError.show();
		}
	}
	
	public void acomodarBotones(ButtonStyleShow typeButtonShow) {
		infomovilInterface.acomodarBotonesActivity(typeButtonShow);
	}
	
	public void acomodarVistaConTitulo(int idTitulo, int idImagen) {
		infomovilInterface.acomodarVistaConTitulo(idTitulo, idImagen);
	}
	

	public class EditTextWithFontWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {


		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			modifico = true;
			//banderaModifico = 1;
		}

	}


	public boolean isModifico() {
		return modifico;
	}
	public void setModifico(boolean modifico) {
		this.modifico = modifico;
	}
	
}

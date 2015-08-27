package com.infomovil.infomovil.gui.fragment.editar;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;

public class DescripcionActivity extends InfomovilFragment {
	
	TextView labelDescripcion;
	EditText txtDescripcion;
	int banderaModifico;
	boolean actualizacionCorrecta;
	boolean arregloEstatus[];
	private WS_DomainVO datosDominio;
	Button botonBorrar;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.descripcion_layout, container, false);
		labelDescripcion = (TextView) view.findViewById(R.id.labelDescripcion);
		txtDescripcion = (EditText) view.findViewById(R.id.txtDescripcion);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarDescripcion);
		return view;
		
	}
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtDescripcionCortaTitulo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
		txtDescripcion.addTextChangedListener(new EditTextWithFontWatcher());
		
		datosDominio = datosUsuario.getDomainData();
		txtDescripcion.setText(datosDominio.getDisplayString());
		modifico = false;
		actualizacionCorrecta = false;
		banderaModifico = 0;
		arregloEstatus = datosUsuario.getEstatusEdicion();  
		
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = getArguments().getInt("mensaje");
		
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if((keyCode == KeyEvent.KEYCODE_BACK)) {
//			Log.d("infomovilLog", "Presionando back button");
//			if (modifico == true) {
//				alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion, getResources().getString(R.string.txtPreguntaSalir));
//				alerta.setDelegado(this);
//				alerta.show();
//				return false;
//			} 
//			else {
//				Intent intent = new Intent();
//				intent.putExtra("banderaModifico", banderaModifico);
//				setResult(Activity.RESULT_OK, intent);
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public void guardarInformacion() {
		datosDominio = datosUsuario.getDomainData();
		datosDominio.setDisplayString(txtDescripcion.getText().toString());
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setActividad("Edito Descripcion Corta");
		wsCall.setDomain(datosDominio);
		wsCall.execute(WSInfomovilMethods.UPDATE_DES_CORTA);

	}
	
	/*public void initBorrar(){
		Button cestoBasura = (Button) findViewById(R.id.botonBorrarDescripcion);
		
		if (datosUsuario.getDomainData().getDisplayString() != null && 
				!datosUsuario.getDomainData().getDisplayString().isEmpty()) {
			cestoBasura.setEnabled(true);
		}
		else{
			cestoBasura.setEnabled(false);
		}
	}*/
	
	public void borrarDatosOk() {		
		datosDominio = datosUsuario.getDomainData();
		datosDominio.setDisplayString("");
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setActividad("Borro Descripcion Corta");
		wsCall.setDomain(datosDominio);
		wsCall.execute(WSInfomovilMethods.UPDATE_DES_CORTA);		
	}
	
	public void checkBotonEliminar(){		
				
		if(!StringUtils.isEmpty(txtDescripcion.getText())){
			botonBorrar.setVisibility(View.VISIBLE);
		}	
	}
	
	public void accionAceptar() {
		super.accionAceptar();
		if (actualizacionCorrecta) {
			banderaModifico = 1;
			Intent intent = new Intent();
			intent.putExtra("banderaModifico", banderaModifico);
			activity.setResult(Activity.RESULT_OK, intent);
			infomovilInterface.returnFragment("MenuCrear");
		}
	}
	
//	public void accionSi() {
//		super.accionSi();
//		if (InfomovilApp.isExisteSesion()) {
//			if (InfomovilApp.isConnected(this)) {
//				dialog = new AlertView(this, AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.msgGuardarDescripcion));
//				dialog.setDelegado(this);
//				dialog.show();
//				datosUsuario = DatosUsuario.getInstance();
//				datosDominio = datosUsuario.getDomainData();
//				datosDominio.setDisplayString(txtDescripcion.getText().toString());
//				WsInfomovilCall wsCall = new WsInfomovilCall(this);
//				wsCall.setDomain(datosDominio);
//				wsCall.execute(WSInfomovilMethods.UPDATE_DOMAIN);
//			}
//			else {
//				dialog = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtNoConexion));
//				dialog.setDelegado(this);
//				dialog.show();
//			}
//		}
//		else {
//			datosUsuario = DatosUsuario.getInstance();
//			datosDominio = datosUsuario.getDomainData();
//			datosDominio.setDisplayString(txtDescripcion.getText().toString());
//			datosUsuario.setDomainData(datosDominio);
//			arregloEstatus[indiceSeleccionado] = true;
//			datosUsuario.setEstatusEdicion(arregloEstatus);
//			modificado = false;
//			Intent intent = new Intent();
//			intent.putExtra("banderaModifico", banderaModifico);
//			setResult(Activity.RESULT_OK, intent);
//			this.finish();
//		}
//	}
	
	public void accionNo() {
		super.accionNo();
		Log.d("infomovilLog", "Accion no");
//		dialog.dismiss();
		banderaModifico = 0;
		Intent intent = new Intent();
		intent.putExtra("banderaModifico", banderaModifico);
		activity.setResult(Activity.RESULT_OK, intent);
		infomovilInterface.returnFragment("MenuCrear");
	}
	
//	private class EditTextWithFontWatcher implements TextWatcher {
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			modifico = true;
//			banderaModifico = 1;
//		}
//		
//	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		
		alerta.dismiss();
		if (status == WsInfomovilProcessStatus.EXITO) {
			modifico = false;
			actualizacionCorrecta = true;
			datosUsuario = DatosUsuario.getInstance();
//			if (datosUsuario.getDomainData().getDisplayString() != null && 
//				!datosUsuario.getDomainData().getDisplayString().isEmpty()) {
//				arregloEstatus[indiceSeleccionado] = true;
//			}
//			else {
//				arregloEstatus[indiceSeleccionado] = false;
//			}
			
			//datosUsuario.setEstatusEdicion(arregloEstatus);
			AlertView alertBien = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(activity,  AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorActualizacion));
			alertError.setDelegado(this);
			alertError.show();
		}
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void initResume() {
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

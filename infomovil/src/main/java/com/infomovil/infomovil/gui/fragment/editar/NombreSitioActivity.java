package com.infomovil.infomovil.gui.fragment.editar;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;

public class NombreSitioActivity extends InfomovilFragment {
	
	protected TextView labelTitulo;
	protected EditTextWithFont textoNombre;
	int banderaModifico;
	boolean arregloEstatus[], actualizacionCorrecta;
	private WS_DomainVO datosDominio;
	Button botonBorrar;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nombre_sitio_layout, container, false);
		labelTitulo = (TextView) view.findViewById(R.id.labelNombrar);
		textoNombre = (EditTextWithFont) view.findViewById(R.id.txtNombre);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarNombreSitio);
		return view;
	}
	 
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtNombreoEmpresa, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
		textoNombre.addTextChangedListener(new EditTextWithFontWatcher());
		datosDominio = datosUsuario.getDomainData();
		textoNombre.setText(datosDominio.getTextRecord().equals("(null)") || datosDominio.getTextRecord().equals(getResources().getString(R.string.txtTitulo))?"":datosDominio.getTextRecord());
		modifico = false;
		banderaModifico = 0;
		arregloEstatus = datosUsuario.getEstatusEdicion();
//		indiceSeleccionado = activity.getIntent().getIntExtra("indiceSeleccionado", 0);
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = getArguments().getInt("mensaje", R.string.txtCargandoDefault);
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
//			
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public void guardarInformacion() {
		datosDominio = datosUsuario.getDomainData();
		datosDominio.setTextRecord(textoNombre.getText().toString());
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setDomain(datosDominio);
		wsCall.setActividad("Edito Nombre o Empresa");
		wsCall.execute(WSInfomovilMethods.UPDATE_TEXT_RECORD);
	}
	
	/*public void initBorrar(){
		Button cestoBasura = (Button) findViewById(R.id.botonBorrarNombreSitio);
		
		if (datosUsuario.getDomainData().getTextRecord() != null &&
			!datosUsuario.getDomainData().getTextRecord().equals("(null)") 
			&& !datosUsuario.getDomainData().getTextRecord().isEmpty()) {
			cestoBasura.setEnabled(true);
		}
		else{
			cestoBasura.setEnabled(false);
		}
	}*/
	
	public void borrarDatosOk(){		
		datosDominio = datosUsuario.getDomainData();
		datosDominio.setTextRecord("(null)");
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setDomain(datosDominio);
		wsCall.setActividad("Borro Nombre o Empresa");
		wsCall.execute(WSInfomovilMethods.UPDATE_TEXT_RECORD);
	}
	
	public void checkBotonEliminar(){		
		if(!StringUtils.isEmpty(textoNombre.getText()) && !textoNombre.getText().equals("(null)") 
				&& !textoNombre.getText().equals(getResources().getString(R.string.txtTitulo))){
			botonBorrar.setVisibility(View.VISIBLE);
		}	
	}
	
//	public void accionSi() {
//		super.accionSi();
//		Log.d("infomovilLog", "Accion si");
//		dialog.dismiss();
//		if(InfomovilApp.isExisteSesion()) {
//			if (InfomovilApp.isConnected(this)) {
//				dialog = new AlertView(this, AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.msgGuardarNombre));
//				dialog.setDelegado(this);
//				dialog.show();
//				datosUsuario = DatosUsuario.getInstance();
//				datosDominio = datosUsuario.getDomainData();
//				datosDominio.setTextRecord(textoNombre.getText().toString());
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
//			modificado = false;
//			datosUsuario = DatosUsuario.getInstance();
//			datosDominio.setTextRecord(textoNombre.getText().toString());
//			datosUsuario.setDomainData(datosDominio);
//			arregloEstatus[indiceSeleccionado] = true;
//			datosUsuario.setEstatusEdicion(arregloEstatus);
//			Intent intent = new Intent();
//			intent.putExtra("banderaModifico", banderaModifico);
//			setResult(Activity.RESULT_OK, intent);
//			this.finish();
//		}
//		
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
	
//	private class EditTextWithFontWatcher implements TextWatcher {
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			
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
		alerta.dismiss();
		if (status == WsInfomovilProcessStatus.EXITO) {
			modifico = false;
			actualizacionCorrecta = true;
			datosUsuario = DatosUsuario.getInstance();
//			if (datosUsuario.getDomainData().getTextRecord() != null &&
//				!datosUsuario.getDomainData().getTextRecord().equals("(null)") && !datosUsuario.getDomainData().getTextRecord().isEmpty()) {
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

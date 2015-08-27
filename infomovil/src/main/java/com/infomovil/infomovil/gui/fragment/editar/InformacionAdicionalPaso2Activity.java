package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DeleteItem;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;

public class InformacionAdicionalPaso2Activity extends InfomovilFragment {
	
	private EditTextWithFont txtTitulo, txtInfo;
	private Vector<WS_KeywordVO> arrayInformacion;
	boolean actualizacionCorrecta;
	boolean estaEditando;
	Button botonBorrar;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.informacio_adicional_paso2_layout, container, false);
		txtTitulo = (EditTextWithFont) view.findViewById(R.id.txtAgregarTituloInfo);
		txtInfo = (EditTextWithFont) view.findViewById(R.id.txtAgregarInfo);
		botonBorrar = (Button) view.findViewById(R.id.btnEliminarInfo);
		return view;
	}
	
	@Override
	public void initCreate() {
		modifico = false;
		acomodarVistaConTitulo(R.string.txtAddInfo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		estaEditando = getArguments().getBoolean("editado", false);
		mensajeActualizacion = getArguments().getInt("mensaje", R.string.txtCargandoDefault);
		
		txtTitulo.addTextChangedListener(new EditTextWithFontWatcher());
		txtInfo.addTextChangedListener(new EditTextWithFontWatcher());
		
		
		int maxLength = 32;    
		txtTitulo.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
		
	}
	
	public void borrarDatosOk() {
		if(!(txtTitulo.toString().trim().isEmpty())&& !(txtInfo.toString().trim().isEmpty())){
//			if(InfomovilApp.isExisteSesion()) {
//				if (InfomovilApp.isConnected(this)) {
					
					if(arrayInformacion!= null && arrayInformacion.size() > 0){
						datosUsuario = DatosUsuario.getInstance();					
						WS_DeleteItem deleteItem = new WS_DeleteItem(datosUsuario.getDomainid(),arrayInformacion.get(indiceSeleccionado).getIdKeyword());
						WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
						wsCall.setDeleteItem(deleteItem);
						wsCall.setActividad("Borro Informacion adicional");
						wsCall.execute(WSInfomovilMethods.DELETE_KEY_WORD_DATA);
					}
//				}
//				else {
//					alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtNoConexion));
//					alerta.setDelegado(this);
//					alerta.show();
//				}
//			}
//			else {
//				if(estaEditando){
//					datosUsuario = DatosUsuario.getInstance();
//					arrayInformacion = datosUsuario.getArregloInformacionAdicional();
//					arrayInformacion.remove(indiceSeleccionado);
//					datosUsuario.setArregloInformacionAdicional(arrayInformacion);
//				}
//				Intent intent = new Intent();
//				intent.putExtra("opcion", 1);
//				setResult(Activity.RESULT_OK, intent);
//				this.finish();
//			}
		}
		else{
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.mensajeNoingresaInfo));
			alerta.setDelegado(this);
			alerta.show();
		}
	}
	
	public void checkBotonEliminar(){		
		if(!StringUtils.isEmpty(txtTitulo.getText()) || !StringUtils.isEmpty(txtInfo.getText())){
			botonBorrar.setVisibility(View.VISIBLE);
		}	
	}	
	
	@Override
	public void guardarInformacion(){
		
		WS_KeywordVO info;

		arrayInformacion = datosUsuario.getArregloInformacionAdicional();

		if(arrayInformacion == null){
			arrayInformacion = new Vector<WS_KeywordVO>();
		}										

		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		
		wsCall.setActividad("Edito Informacion adicional");

		if (estaEditando) {

			info = arrayInformacion.get(indiceSeleccionado);
			info.setKeywordField(txtTitulo.getText().toString());
			info.setKeywordValue(txtInfo.getText().toString());
			arrayInformacion.set(indiceSeleccionado,info);

			Vector<WS_KeywordVO> lstKeywordVO = new Vector<WS_KeywordVO>();
			lstKeywordVO.add(info);
			wsCall.setLstKeyword(lstKeywordVO);

			wsCall.execute(WSInfomovilMethods.UPDATE_KEY_WORD_DATA);
		}
		else {

			info = new WS_KeywordVO();
			info.setKeywordField(txtTitulo.getText().toString());
			info.setKeywordValue(txtInfo.getText().toString());
			arrayInformacion.add(info);
			
			Vector<WS_KeywordVO> lstKeywordVO = new Vector<WS_KeywordVO>();
			lstKeywordVO.add(info);
			wsCall.setLstKeyword(lstKeywordVO);			

			wsCall.execute(WSInfomovilMethods.INSERT_KEY_WORD_DATA);
		}
		
	}
	
	public void accionNo() {
		super.accionNo();
		if(alerta != null)
			alerta.dismiss();
		
		infomovilInterface.returnFragment("InformacionAdicional");
	}
	
	public void accionAceptar() {
		super.accionAceptar();
		if(actualizacionCorrecta) {
//			Intent intent = new Intent();
//			intent.putExtra("opcion", 1);
//			activity.setResult(Activity.RESULT_OK, intent);
//			activity.finish();
			infomovilInterface.returnFragment("InformacionAdicional");
		}
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		if(alerta != null)
			alerta.dismiss();		
		
		Log.i("informacion adicional", status+"");	
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			actualizacionCorrecta = true;
			AlertView alertBien = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			arrayInformacion.remove(indiceSeleccionado);
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
	
	
	private class EditTextWithFontWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			modifico = true;
		}

		@Override
		public void afterTextChanged(Editable s) {
			modifico = true;
		}
	}
	
	private boolean listaPerfilLlena() {
		boolean listaLlena = false;
		datosUsuario = DatosUsuario.getInstance();
		Vector <WS_KeywordVO> listaDire = datosUsuario.getArregloPerfil();
		for (int i = 0; i < listaDire.size(); i++) {
			WS_KeywordVO keyAux = listaDire.get(i);
			if (keyAux.getKeywordValue() != null && keyAux.getKeywordValue().length() > 0) {
				listaLlena = true;
				break;
			}
		}
		return listaLlena;
	}

	@Override
	public void initResume() {
		
		if (datosUsuario.getArregloInformacionAdicional() == null || !listaPerfilLlena()) {
			arrayInformacion = new Vector<WS_KeywordVO>();
		} 
		else {
			arrayInformacion = datosUsuario.getArregloInformacionAdicional();
		}
		
		if (estaEditando) {
			arrayInformacion = datosUsuario.getArregloInformacionAdicional();
			txtTitulo.setText(arrayInformacion.elementAt(indiceSeleccionado).getKeywordField().toString());
			txtInfo.setText(arrayInformacion.elementAt(indiceSeleccionado).getKeywordValue().toString());
			modifico = false;
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
		
		Resources res = getResources();
		
		String mensaje = "Correcto";
		
		String titulo = txtTitulo.getText().toString().trim();
		
		if(titulo.isEmpty()){
			mensaje = res.getString(R.string.faltaTituloInfoAdicional);
			return mensaje;
		}
		
		if(txtInfo.getText().toString().trim().isEmpty()){
			mensaje = res.getString(R.string.faltaDesInfoAdicional);
			return mensaje;
		}
		
		try{
			if( !estaEditando && arrayInformacion.contains(new WS_KeywordVO(titulo, ""))){//Si es nuevo consulta los anteriores
				mensaje = res.getString(R.string.tituloDuplicadoInfoAdicional);
			}
			else{//Quita el actual y pregunta por los anteriores
				Vector<WS_KeywordVO> clonArrayInformacion = new Vector<WS_KeywordVO>(arrayInformacion);
				clonArrayInformacion.remove(indiceSeleccionado);
				if(clonArrayInformacion.contains(new WS_KeywordVO(titulo, ""))){
					mensaje = res.getString(R.string.tituloDuplicadoInfoAdicional);
				}
			}
		}
		catch(Exception e){}
		
		
		return mensaje;
	}	
}


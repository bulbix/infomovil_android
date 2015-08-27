package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;

public class DireccionActivity extends InfomovilFragment {

	private EditTextWithFont txtDireccion1, txtDireccion2, txtDireccion3, txtCiudad, txtEstado, txtPais, txtCodigo;
	private Vector<WS_KeywordVO> arregloDireccion;
	boolean actualizacionCorrecta;
	int indiceSeleccionado;
	int banderaModifico;
	boolean arregloEstatus[];
	boolean estaEditando;
	Button botonBorrar;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.direccion_layout, container, false);
		txtDireccion1 = (EditTextWithFont) view.findViewById(R.id.txtDireccion1);
		txtDireccion2 = (EditTextWithFont) view.findViewById(R.id.txtDireccion2);
		txtDireccion3 = (EditTextWithFont) view.findViewById(R.id.txtDireccion3);
		txtCiudad = (EditTextWithFont) view.findViewById(R.id.txtCiudad);
		txtEstado = (EditTextWithFont) view.findViewById(R.id.txtEstado);
		txtPais = (EditTextWithFont) view.findViewById(R.id.txtPaisDireccion);
		txtCodigo = (EditTextWithFont) view.findViewById(R.id.txtCP);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarDireccion);
		return view;
	}

	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtTituloDireccion, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
		modifico = false;
		actualizacionCorrecta = false;
		//indiceSeleccionado = activity.getIntent().getIntExtra("indiceSeleccionado", 0);
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		arregloEstatus = datosUsuario.getEstatusEdicion();

		if (datosUsuario.getArregloDireccion() != null && datosUsuario.getArregloDireccion().size() != 0 ) {
			arregloDireccion = datosUsuario.getArregloDireccion();
			estaEditando = true;
		} 
		else {
			arregloDireccion = new Vector<WS_KeywordVO>();
			estaEditando = false;
		}

		txtDireccion1.addTextChangedListener(new EditTextWithFontWatcher());
		txtDireccion2.addTextChangedListener(new EditTextWithFontWatcher());
		txtDireccion3.addTextChangedListener(new EditTextWithFontWatcher());
		txtCiudad.addTextChangedListener(new EditTextWithFontWatcher());
		txtEstado.addTextChangedListener(new EditTextWithFontWatcher());
		txtPais.addTextChangedListener(new EditTextWithFontWatcher());
		txtCodigo.addTextChangedListener(new EditTextWithFontWatcher());
		
		mensajeActualizacion = getArguments().getInt("mensaje");
	}

	@Override
	public void acomodaVista() {
		if(arregloDireccion.size() > 0) {
			txtDireccion1.setText(arregloDireccion.get(0).getKeywordValue());
			txtDireccion2.setText(arregloDireccion.get(1).getKeywordValue());
			txtDireccion3.setText(arregloDireccion.get(2).getKeywordValue());
			txtCiudad.setText(arregloDireccion.get(3).getKeywordValue());
			txtEstado.setText(arregloDireccion.get(4).getKeywordValue());
			txtPais.setText(arregloDireccion.get(5).getKeywordValue());
			txtCodigo.setText(arregloDireccion.get(6).getKeywordValue());
			modifico = false;
		}
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
	public void guardarInformacion(){
		if (estaEditando) {
			WS_KeywordVO dataModel = arregloDireccion.get(0);
			dataModel.setKeywordValue(txtDireccion1.getText().toString());
			arregloDireccion.remove(0);
			arregloDireccion.insertElementAt(dataModel, 0);

			dataModel = arregloDireccion.get(1);
			dataModel.setKeywordValue(txtDireccion2.getText().toString());
			arregloDireccion.remove(1);
			arregloDireccion.insertElementAt(dataModel, 1);

			dataModel = arregloDireccion.get(2);
			dataModel.setKeywordValue(txtDireccion3.getText().toString());
			arregloDireccion.remove(2);
			arregloDireccion.insertElementAt(dataModel, 2);

			dataModel = arregloDireccion.get(3);
			dataModel.setKeywordValue(txtCiudad.getText().toString());
			arregloDireccion.remove(3);
			arregloDireccion.insertElementAt(dataModel, 3);

			dataModel = arregloDireccion.get(4);
			dataModel.setKeywordValue(txtEstado.getText().toString());
			arregloDireccion.remove(4);
			arregloDireccion.insertElementAt(dataModel, 4);

			dataModel = arregloDireccion.get(5);
			dataModel.setKeywordValue(txtPais.getText().toString());
			arregloDireccion.remove(5);
			arregloDireccion.insertElementAt(dataModel, 5);

			dataModel = arregloDireccion.get(6);
			dataModel.setKeywordValue(txtCodigo.getText().toString());
			arregloDireccion.remove(6);
			arregloDireccion.insertElementAt(dataModel, 6);
		}
		else {
			WS_KeywordVO dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("a1");
			dataModel.setKeywordValue(txtDireccion1.getText().toString());
			arregloDireccion.add(dataModel);

			dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("a2");
			dataModel.setKeywordValue(txtDireccion2.getText().toString());
			arregloDireccion.add(dataModel);

			dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("a3");
			dataModel.setKeywordValue(txtDireccion3.getText().toString());
			arregloDireccion.add(dataModel);

			dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("tc");
			dataModel.setKeywordValue(txtCiudad.getText().toString());
			arregloDireccion.add(dataModel);

			dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("sp");
			dataModel.setKeywordValue(txtEstado.getText().toString());
			arregloDireccion.add(dataModel);

			dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("c");
			dataModel.setKeywordValue(txtPais.getText().toString());
			arregloDireccion.add(dataModel);

			dataModel = new WS_KeywordVO();
			dataModel.setKeywordField("pc");
			dataModel.setKeywordValue(txtCodigo.getText().toString());
			arregloDireccion.add(dataModel);
		}
		DatosUsuario.getInstance().setArregloDireccion(arregloDireccion);

		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setLstKeyword(arregloDireccion);
		wsCall.setActividad("Edito Direccion");

		if (estaEditando) {						
			wsCall.execute(WSInfomovilMethods.UPDATE_KEY_WORD_DATA);
		}
		else {
			wsCall.execute(WSInfomovilMethods.INSERT_KEYWORD_DATA_ADDRESS);
		}

	}

	public void borrarDatosOk() {

//		alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity, 
//				getResources().getString(R.string.msgGuardarDireccion));
//		alerta.setDelegado(this);
//		alerta.show();

		//Blanqueamos las direcciones
		for(WS_KeywordVO item :arregloDireccion){
			item.setKeywordValue("");	
		}

		DatosUsuario.getInstance().setArregloDireccion(arregloDireccion);		
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());	
		wsCall.setLstKeyword(arregloDireccion);
		wsCall.setActividad("Borro Direccion");
		wsCall.execute(WSInfomovilMethods.UPDATE_KEY_WORD_DATA);
	}	

	public void checkBotonEliminar(){		

		if(listaLlena()){
			botonBorrar.setVisibility(View.VISIBLE);
		}	
	}


	public void accionSi() {
		super.accionSi();
	}

	public void accionNo() {
		super.accionNo();
		Log.d("infomovilLog", "Accion no");
		if(alerta != null)
			alerta.dismiss();
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


	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		if(alerta != null)
			alerta.dismiss();
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			actualizacionCorrecta=true;

			if(listaLlena()){
				arregloEstatus[indiceSeleccionado] = true;			
			}
			else{
				arregloEstatus[indiceSeleccionado] = false;
			}

			datosUsuario.setEstatusEdicion(arregloEstatus);


			AlertView alertBien = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorGenerico));
			alertError.setDelegado(this);
			alertError.show();
		}

	}


	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		Log.d("infoLog", "Ocurrio un error **************");

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

	private boolean listaLlena() {
		boolean lleno = false;
		datosUsuario = DatosUsuario.getInstance();
		Vector <WS_KeywordVO> listaDire = datosUsuario.getArregloDireccion();

		if ( listaDire == null || listaDire.size() == 0 )
			return lleno;

		for (int i = 0; i < listaDire.size(); i++) {
			WS_KeywordVO keyAux = listaDire.get(i);
			if (keyAux.getKeywordValue() != null && keyAux.getKeywordValue().trim().length() > 0) {
				lleno = true;
				break;
			}
		}
		return lleno;
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

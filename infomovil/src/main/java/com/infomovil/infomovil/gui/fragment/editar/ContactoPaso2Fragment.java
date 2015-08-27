package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import mx.com.infomovil.commons.CommonUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.StringUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.fragment.principal.SelectorPaisActivity;
import com.infomovil.infomovil.model.TipoContacto;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DeleteItem;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;

public class ContactoPaso2Fragment extends InfomovilFragment {
	
	TipoContacto tipoSeleccionado;
	private TextWithFont labelTipoContacto, labelMensajeContacto, labelPaisContacto, labelCodigoContacto;
	private EditTextWithFont txtNumeroIngresado, txtDescripcionContacto;
	private RelativeLayout layoutSelectorPais;
	private Vector<WS_RecordNaptrVO> arrayContactos;
	WS_RecordNaptrVO contactoSeleccionado;
	private ImageView imagenTipoContacto;
	int indiceEdicion;
	boolean estaEditando, estaBorrando;
	private Button botonEliminar, btnSeleccionarPais;
	private TextWithFont labelInfoMexico, labelNumeroMexico;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contacto_paso2_layout, container, false);
		labelTipoContacto = (TextWithFont) view.findViewById(R.id.labelTipoContacto);
		labelMensajeContacto = (TextWithFont) view.findViewById(R.id.labelMensajeContacto);
		layoutSelectorPais = (RelativeLayout) view.findViewById(R.id.layoutSelectorPais);
		txtNumeroIngresado = (EditTextWithFont) view.findViewById(R.id.txtTelefonoIngresado);
		labelPaisContacto = (TextWithFont) view.findViewById(R.id.labelPaisContacto);
		labelCodigoContacto = (TextWithFont) view.findViewById(R.id.labelCodigoContacto);
		txtDescripcionContacto = (EditTextWithFont) view.findViewById(R.id.textDescripcionContacto);
		imagenTipoContacto = (ImageView) view.findViewById(R.id.imagenTipoContacto);
		botonEliminar = (Button) view.findViewById(R.id.btnEliminarContacto);
		labelInfoMexico = (TextWithFont) view.findViewById(R.id.labelInfoMexico);
		labelNumeroMexico = (TextWithFont) view.findViewById(R.id.labelNo1);		
		btnSeleccionarPais = (Button) view.findViewById(R.id.btnSeleccionarPais);
		
		final ContactoPaso2Fragment contactoPaso2 = this;
		btnSeleccionarPais.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				contactoPaso2.seleccionarPais(v);
			}
		});		
		
		
		return view;
	}
	
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtContacto, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);		
		actualizacionCorrecta = false;				
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		indiceEdicion = getArguments().getInt("indiceSeleccionado", 0);
		estaEditando = getArguments().getBoolean("editado", false); 
		mensajeActualizacion = R.string.msgGuardarContacto;
		
		estaBorrando = false;
		
		txtNumeroIngresado.addTextChangedListener(new EditTextWithFontWatcher());
		txtDescripcionContacto.addTextChangedListener(new EditTextWithFontWatcher());		
	}
	
	@Override
	public void initResume() {
		if (datosUsuario.getListaContactos() == null) {
			arrayContactos = new Vector<WS_RecordNaptrVO>();
		}  
		else {
			arrayContactos = datosUsuario.getListaContactos();
		}
		if (estaEditando) {
			
			if(indiceSeleccionado < arrayContactos.size()){
				contactoSeleccionado = arrayContactos.get(indiceSeleccionado);
				tipoSeleccionado = datosUsuario.getArrayTipoContacto().
				get(contactoSeleccionado.getIdTipoContacto());
				indiceSeleccionado = contactoSeleccionado.getIdTipoContacto();
			}		

		}
		else {
			tipoSeleccionado = datosUsuario.getArrayTipoContacto().get(indiceSeleccionado);
		}
		Resources res = getResources();
		int resId = res.getIdentifier(tipoSeleccionado.getImagen(), "drawable", activity.getPackageName());
		imagenTipoContacto.setImageDrawable(res.getDrawable(resId));
		labelTipoContacto.setText(tipoSeleccionado.getTexto());
		labelMensajeContacto.setText(tipoSeleccionado.getMensaje());
		txtNumeroIngresado.setHint(tipoSeleccionado.getPlaceholder());
		
		initControls();
		
	}
	
	public void initControls() {
		if(estaEditando) {
			if (contactoSeleccionado.getIdTipoContacto() == 0 || contactoSeleccionado.getIdTipoContacto() == 1 || contactoSeleccionado.getIdTipoContacto() == 2 || contactoSeleccionado.getIdTipoContacto() == 4) {
				layoutSelectorPais.setVisibility(View.VISIBLE);
				txtNumeroIngresado.setRawInputType(InputType.TYPE_CLASS_PHONE);
			}
			else {
				layoutSelectorPais.setVisibility(View.GONE);
				if (contactoSeleccionado.getIdTipoContacto() == 3) {
					txtNumeroIngresado.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				}
				else {
					txtNumeroIngresado.setRawInputType(InputType.TYPE_TEXT_VARIATION_URI );
				}
			}
			if (contactoSeleccionado.getIdTipoContacto() == 1) {
				if (contactoSeleccionado.getCodCountry().equals("+52")) {
					labelInfoMexico.setVisibility(View.VISIBLE);
					labelNumeroMexico.setVisibility(View.VISIBLE);
					txtNumeroIngresado.setText(contactoSeleccionado.getNoContacto().substring(1,contactoSeleccionado.getNoContacto().length()));
				}else{
				txtNumeroIngresado.setText(contactoSeleccionado.getNoContacto());
				}
			}
			else {
				txtNumeroIngresado.setText(contactoSeleccionado.getNoContacto());
			}
			txtDescripcionContacto.setText(contactoSeleccionado.getLongLabelNaptr());
			labelPaisContacto.setText(contactoSeleccionado.getPais());
			labelCodigoContacto.setText(contactoSeleccionado.getCodCountry());
			botonEliminar.setEnabled(true);
			
		}
		else {
			if(indiceSeleccionado == 0 || indiceSeleccionado == 1 || indiceSeleccionado == 2 || indiceSeleccionado == 4) {
				layoutSelectorPais.setVisibility(View.VISIBLE);
				txtNumeroIngresado.setRawInputType(InputType.TYPE_CLASS_PHONE);
			}
			else {
				layoutSelectorPais.setVisibility(View.GONE);
				if (indiceSeleccionado == 3) {
					txtNumeroIngresado.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				}
				else {
					txtNumeroIngresado.setRawInputType(InputType.TYPE_TEXT_VARIATION_URI );
				}
			}
			
			if(org.apache.commons.lang3.StringUtils.isEmpty(labelCodigoContacto.getText())){
				labelPaisContacto.setText("Mexico");
				labelCodigoContacto.setText("+52");
			}
			
			botonEliminar.setEnabled(false);
		}
		
		modifico = false;
		
	}
	
	@Override
	public void acomodaVista() {
		

	}
	
	public void seleccionarPais(View v) {
		Intent intent = new Intent(getActivity(), SelectorPaisActivity.class);
		startActivityForResult(intent, InfomovilConstants.STATIC_INTEGER_VALUE);
	}
	
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		if(requestCode == InfomovilConstants.STATIC_INTEGER_VALUE) {
			if (resultCode == Activity.RESULT_OK) {
				modifico = true;
				String nombrePais = data.getStringExtra("paisSeleccionado");
				String codigoPais = data.getStringExtra("codigoPais");
				labelPaisContacto.setText(nombrePais);
				labelCodigoContacto.setText(codigoPais);
				if (codigoPais.equals("+52") && indiceSeleccionado == 1) {
					labelInfoMexico.setVisibility(View.VISIBLE);
					labelNumeroMexico.setVisibility(View.VISIBLE);
				}
				else {
					labelInfoMexico.setVisibility(View.GONE);
					labelNumeroMexico.setVisibility(View.GONE);
				}
			}
		}
	}
	
	@Override
	public void guardarInformacion(){
		
		WS_RecordNaptrVO contactoAux = contactoSeleccionado;
		contactoSeleccionado = new WS_RecordNaptrVO();
		contactoSeleccionado.setLongLabelNaptr(txtDescripcionContacto.getText().toString());
		if(indiceSeleccionado == 0 || indiceSeleccionado == 1 || indiceSeleccionado == 2 || indiceSeleccionado == 4) {
			contactoSeleccionado.setCodCountry(labelCodigoContacto.getText().toString());
			contactoSeleccionado.setPais(labelPaisContacto.getText().toString());

			if (indiceSeleccionado == 1 && labelCodigoContacto.getText().toString().equals("+52")) {
				contactoSeleccionado.setRegExp(tipoSeleccionado.getExpresion() + labelCodigoContacto.getText().toString() + "1" + txtNumeroIngresado.getText().toString() + "!");
				contactoSeleccionado.setNoContacto("1"+txtNumeroIngresado.getText().toString());
			}
			else {
				contactoSeleccionado.setRegExp(tipoSeleccionado.getExpresion() + labelCodigoContacto.getText().toString() + txtNumeroIngresado.getText().toString() + "!");
				contactoSeleccionado.setNoContacto(txtNumeroIngresado.getText().toString());
			}

		}else {
			contactoSeleccionado.setRegExp(tipoSeleccionado.getExpresion()+txtNumeroIngresado.getText().toString()+"!");
			contactoSeleccionado.setNoContacto(txtNumeroIngresado.getText().toString());
		}
		//contactoSeleccionado.setNoContacto(txtNumeroIngresado.getText().toString());
		if (!estaEditando) {
			contactoSeleccionado.setIdTipoContacto(indiceSeleccionado);
		}
		contactoSeleccionado.setExpAux(tipoSeleccionado.getExpresion());
		contactoSeleccionado.setServicesNaptr(tipoSeleccionado.getServicio());
		contactoSeleccionado.setSubCategory(tipoSeleccionado.getSubcategoria());
		contactoSeleccionado.setVisible(1);
		contactoSeleccionado.setCategoryNaptr("");
		Vector<WS_RecordNaptrVO> arrayAux = new Vector<WS_RecordNaptrVO>();
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setLstContactos(arrayAux);
		wsCall.setActividad("Edito Contacto");		
		
		if (estaEditando) {
			contactoSeleccionado.setClaveContacto(contactoAux.getClaveContacto());
			arrayAux = datosUsuario.getListaContactos();
			arrayAux.remove(indiceEdicion);
			arrayAux.add(indiceEdicion, contactoSeleccionado);
			wsCall.setLstContactos(arrayAux);
			wsCall.execute(WSInfomovilMethods.UPDATE_RECORD_NAPTR);
		}
		else {
			arrayAux.add(contactoSeleccionado);
			wsCall.setLstContactos(arrayAux);
			wsCall.execute(WSInfomovilMethods.INSERT_RECORD_NAPTR);
		}
		
	}
	
	public void borrarDatosOk() {
		Log.d("infoLog", "presionando boton borrar");
		estaBorrando = true;
//		alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity,
//		getResources().getString(R.string.msgGuardarContacto));
//		alerta.setDelegado(this);
//		alerta.show();
		WS_DeleteItem deleteItem = new WS_DeleteItem(datosUsuario.getDomainid(), contactoSeleccionado.getClaveContacto());
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setDeleteItem(deleteItem);
		wsCall.setActividad("Borro Contacto");
		wsCall.execute(WSInfomovilMethods.DELETE_RECORD_NAPTR);
		
	}
	
	@Override
	public String validarCampos() {
		Resources res = getResources();
		
		String mensaje = "Correcto";
		
		if (indiceSeleccionado == 0 || indiceSeleccionado == 1 || indiceSeleccionado == 2 || indiceSeleccionado == 4) {
			if(labelCodigoContacto.getText().toString().length() == 0) {
				mensaje = res.getString(R.string.seleccionaPais);
				return mensaje;
			}
			if (txtNumeroIngresado.getText().toString().length() == 0 || !StringUtils.validaTelefono(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.numeroInvalido);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 3) {
			if (!CommonUtils.isValidEmail(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.emailInvalido);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 5) {
			if (!CommonUtils.isValidFacebook(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.urlInvalida);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 6) {
			
			txtNumeroIngresado.setText(txtNumeroIngresado.getText().toString().replace("www.", ""));
			
			if (!CommonUtils.isValidTwitter(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.urlInvalida);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 7) {
			if (!CommonUtils.isValidGoogle(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.urlInvalida);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 8) {
			if(!CommonUtils.isValidSkype(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.urlInvalida);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 9) {
			if (txtNumeroIngresado.getText().toString().indexOf("linkedin.com") < 0) {
				mensaje = res.getString(R.string.urlInvalida);
				return mensaje;
			}
		}
		if (indiceSeleccionado == 10) {
			if (!CommonUtils.isValidSecureWeb(txtNumeroIngresado.getText().toString())) {
				mensaje = res.getString(R.string.urlInvalida);
				return mensaje;
			}
		}
		return mensaje;
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if((keyCode == KeyEvent.KEYCODE_BACK)) {
//			if (modifico) {
//				alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion, getResources().getString(R.string.txtPreguntaSalir));
//				alerta.setDelegado(this);
//				alerta.show();
//				return false;
//			}
//
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	public void accionSi() {
		super.accionSi();
		
		if(alerta != null)
			alerta.dismiss();		
		
	}
	
	public void accionNo() {
		super.accionNo();
		
		if(alerta != null)
			alerta.dismiss();
		
		infomovilInterface.returnFragment("ContactoPaso1");
	}
	
	public void accionAceptar() {
		super.accionAceptar();
		if(actualizacionCorrecta) {
			Intent intent = new Intent();
			intent.putExtra("opcion", 1);
			activity.setResult(Activity.RESULT_OK, intent);
			infomovilInterface.returnFragment("ContactoPaso1");
		}
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		if(alerta != null)		
			alerta.dismiss();
		
		Log.d("infoLog", "El estatus de los contactos es " + status + "*******************");
		if (status == WsInfomovilProcessStatus.EXITO) {
			actualizacionCorrecta = true;
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
	public void checkBotonEliminar() {
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
}

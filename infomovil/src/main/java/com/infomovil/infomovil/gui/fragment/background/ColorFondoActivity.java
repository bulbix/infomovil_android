package com.infomovil.infomovil.gui.fragment.background;

import afzkl.development.colorpickerview.view.ColorPanelView;
import afzkl.development.colorpickerview.view.ColorPickerView;
import afzkl.development.colorpickerview.view.ColorPickerView.OnColorChangedListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;

public class ColorFondoActivity extends InfomovilActivity implements OnColorChangedListener, 
	OnClickListener {
	
	private ColorPickerView	mColorPickerView;
	private ColorPanelView	mNewColorPanelView;	
	private Button	mOkButton;
	private Button	mCancelButton;
	private WS_DomainVO datosDominio;	
	
	private void init() {
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		datosDominio = datosUsuario.getDomainData();
		int initialColor;
		if (datosDominio.getColour() == null || datosDominio.getColour().length() < 6) {
			initialColor = Color.parseColor("#BDBDBD");
		}
		else {
			initialColor = Color.parseColor(datosDominio.getColour());
		}
	
		mColorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
//		mOldColorPanelView = (ColorPanelView) findViewById(R.id.color_panel_old);
		mNewColorPanelView = (ColorPanelView) findViewById(R.id.color_panel_new);
		
		mOkButton = (Button) findViewById(R.id.okButton);
		mCancelButton = (Button) findViewById(R.id.cancelButton);
		
		
//		((LinearLayout) mOldColorPanelView.getParent()).setPadding(Math
//				.round(mColorPickerView.getDrawingOffset()), 0, Math
//				.round(mColorPickerView.getDrawingOffset()), 0);
		
		
		mColorPickerView.setOnColorChangedListener(this);
		mColorPickerView.setColor(initialColor, true);
//		mOldColorPanelView.setColor(initialColor);
		modifico = false;
		
		mOkButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);
		
	}
	
	@Override
	public void onColorChanged(int newColor) {
		modifico = true;
		mNewColorPanelView.setColor(mColorPickerView.getColor());		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.okButton:			
			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
			edit.putInt("color_3", mColorPickerView.getColor());
			edit.commit();
			
			finish();			
			break;
		case R.id.cancelButton:
			finish();
			break;
		}
		
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (modifico) {
				alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion, getResources().getString(R.string.txtPreguntaSalir));
				alerta.setDelegado(this);
				alerta.show();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		alerta.dismiss();
		if (status == WsInfomovilProcessStatus.EXITO) {
			datosUsuario.setEligioColor(true);
			modifico = false;
			actualizacionCorrecta = true;
			AlertView alertBien = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(this,  AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorActualizacion));
			alertError.setDelegado(this);
			alertError.show();
		}
		
	}
	
	@Override
	public void accionNo() {
		super.accionNo();
		if(alerta != null)
			alerta.dismiss();
		this.finish();
	}
	
	@Override
	public void accionAceptar() {
		super.accionAceptar();
		if (actualizacionCorrecta) {
			this.finish();
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
		getWindow().setFormat(PixelFormat.RGBA_8888);
		cargarLayout(R.layout.color_fondo_layout);
		acomodarVistaConTitulo(R.string.txtColorFondo, R.drawable.plecaturquesa);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		modifico = false;
		mensajeActualizacion = R.string.msgGuardarColor;
		init();
	}

	@Override
	public void initResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarInformacion() {
		datosDominio = datosUsuario.getDomainData();
		datosDominio.setColour(String.format("#%06X", (0xFFFFFF & mColorPickerView.getColor())));
		WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
		wsCall.setDomain(datosDominio);
		wsCall.execute(WSInfomovilMethods.UPDATE_COLOR);
		
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

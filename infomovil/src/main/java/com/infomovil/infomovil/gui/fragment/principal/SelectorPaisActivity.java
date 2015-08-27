package com.infomovil.infomovil.gui.fragment.principal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.ReadFilesUtils;
import com.infomovil.infomovil.gui.adapter.CountryListAdapter;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.CallingCodes;
import com.infomovil.infomovil.model.DatosUsuario;

public class SelectorPaisActivity extends InfomovilActivity {
	
	private ListView listaPais;
	private EditTextWithFont txtBuscar;
	ArrayList<CallingCodes> arrayListPaises;
	CountryListAdapter countryCode;
	private String tipoSelector;
	Context context;
	
	
	private class EditTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
//			if (s.length() < 4) {
				countryCode.getFilter().filter(s);
//			}

			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
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
		cargarLayout(R.layout.selector_pais_layout);
		acomodarVistaConTitulo(R.string.txtPais, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		listaPais = (ListView) findViewById(R.id.listaPaises);
		txtBuscar = (EditTextWithFont) findViewById(R.id.txtBuscarPais);
		txtBuscar.addTextChangedListener(new EditTextWatcher());
		context = this;
		
		tipoSelector = getIntent().getStringExtra("tipoSelector");
		if (tipoSelector == null || tipoSelector.length() == 0) {
			tipoSelector = "";
		}
		datosUsuario = DatosUsuario.getInstance();
		arrayListPaises = new ArrayList<CallingCodes>();
		if(datosUsuario.getArrayCodigos() == null) {
			arrayListPaises = ReadFilesUtils.leerArchivoCodigos(getResources());
		}
		else {
			arrayListPaises = datosUsuario.getArrayCodigos();
		}
		countryCode = new CountryListAdapter(this, arrayListPaises, tipoSelector);
//		txtBuscar.setAdapter(countryCode);
		listaPais.setAdapter(countryCode);
		
		
		OnItemClickListener listenerList = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("infoLog", "Dando click");
				List<CallingCodes> callAux = countryCode.getCallingCodes();
				CallingCodes codigo = callAux.get(position);
				hideSoftKeyboard();
				Intent intent = new Intent();
				intent.putExtra("paisSeleccionado", codigo.getCountryName());
				intent.putExtra("codigoPais", codigo.getPhoneCode());
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		};
		listaPais.setOnItemClickListener(listenerList);
		
	}

	@Override
	public void initResume() {
		// TODO Auto-generated method stub
		
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

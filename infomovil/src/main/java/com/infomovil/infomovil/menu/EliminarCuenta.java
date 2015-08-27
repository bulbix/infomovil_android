package com.infomovil.infomovil.menu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.principal.MainActivity;
import com.infomovil.infomovil.gui.fragment.principal.RecuperarPasswordActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;

public class EliminarCuenta extends InfomovilActivity implements OnItemSelectedListener{
	
	private TextWithFont labelEliminarCuenta,labelBoton,labelContrasena,labelEspecifica;
	private Button botonInferior;
	private EditTextWithFont contrasena,especifica;
	private Spinner spiner;	
	boolean cuentaEliminada;
	Context context;	
	String[] state;
	private String descripcionBaja;
	int i;
	
	@Override
	public void guardarDatos(View v) {
		
//		super.guardarDatos(v);
		datosUsuario = DatosUsuario.getInstance();
		
		if(!datosUsuario.getPassword().equals(contrasena.getText().toString())){
			
			final AlertView dialog = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.passwordInvalido));
			dialog.setDelegado(new AlertViewInterface() {
				
				@Override
				public void accionSi() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void accionNo() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void accionAceptar() {
					// TODO Auto-generated method stub							
					dialog.dismiss();
					
				}
			});
			
			dialog.show();

		}
		else{
			
			alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.msgEliminandoCuenta));
			alerta.setDelegado(this);
			alerta.show();
			
			WS_UserDomainVO userDomain = new WS_UserDomainVO();
			userDomain.setDomainName(datosUsuario.getDomainData().getDomainName());
			userDomain.setEmail(datosUsuario.getNombreUsuario());
			
			userDomain.setPassword(contrasena.getText().toString());
			
			if (i == 1) {
				userDomain.setNotificacion(descripcionBaja);
			}
			else {
				userDomain.setNotificacion(especifica.getText().toString());
			}
			WsInfomovilCall wsCall=new WsInfomovilCall(this,this);
			wsCall.setUsrDomain(userDomain);
//			if(i==1){
//				wsCall.setStrConsulta(contrasena.getText().toString());
//			}else if(i==2){
//				wsCall.setStrConsulta(especifica.getText().toString());
//			}
			wsCall.execute(WSInfomovilMethods.CANCELAR_CUENTA);
			
		}
		
		
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		spiner.setSelection(position);
		String selState = (String) spiner.getSelectedItem();
		descripcionBaja = selState;
		if(position>=0 && position<4 ){
			labelContrasena.setVisibility(View.VISIBLE);
			contrasena.setVisibility(View.VISIBLE);
			
			labelEspecifica.setVisibility(View.GONE);
			especifica.setVisibility(View.GONE);
			i=1;
		}else if(position == 4){
			labelEspecifica.setVisibility(View.VISIBLE);
			especifica.setVisibility(View.VISIBLE);
			i=2;
		}
	}

	 @Override
	 public void onNothingSelected(AdapterView<?> arg0) {
			  // TODO Auto-generated method stub

	 }

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		if(alerta != null)
			alerta.dismiss();
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			cuentaEliminada = true;
			AlertView alertBien = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			cuentaEliminada = false;
			AlertView alertMal = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorPassword));
			alertMal.setDelegado(this);
			alertMal.show();
		}
	}
	
	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {	
		cuentaEliminada = false;		
		super.respuestaErronea(status, e);
	}
	
	@Override
	public void accionAceptar() {
		super.accionAceptar();
		if (cuentaEliminada) {
			DatosUsuario.getInstance().borrarDatos();
	    	startActivity(new Intent(getBaseContext(), MainActivity.class)
	        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
	    	finish();
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
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void initCreate() {
		cargarLayout(R.layout.eliminar_uenta_layout);
		acomodarVistaConTitulo(R.string.txtTituloConfiguracionElimina, R.drawable.plecamorada);
//		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		labelEliminarCuenta = (TextWithFont)findViewById(R.id.labelEliminarCuenta);
		labelContrasena = (TextWithFont)findViewById(R.id.labelContrasenaEliminar2);
		labelEspecifica = (TextWithFont)findViewById(R.id.labelEspecifica);
		botonInferior = (Button) findViewById(R.id.btnConfiguracion);
		Resources res = getResources();
		contrasena = (EditTextWithFont)findViewById(R.id.txtContrasena);
		especifica = (EditTextWithFont)findViewById(R.id.txtEspecifica);
		
		spiner = (Spinner)findViewById(R.id.spinner1);
		
		state = res.getStringArray(R.array.listaEliminar);
		
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,R.layout.spinner_item, state) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Avenir LT 45 Book.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
			}
			
			@Override
			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Avenir LT 45 Book.ttf");
               ((TextView) v).setTypeface(externalFont);
               v.setBackgroundColor(Color.WHITE);

               return v;
       }
		};
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiner.setAdapter(adapter_state);
		spiner.setOnItemSelectedListener(this);
		
		//labelContrasena.setVisibility(View.VISIBLE);
		//contrasena.setVisibility(View.VISIBLE);
		labelEspecifica.setVisibility(View.GONE);
		especifica.setVisibility(View.GONE);
		
		labelEliminarCuenta.setText(res.getString(R.string.motivoEliminacion));
		labelContrasena.setText(res.getString(R.string.eliminarLabel2));
		labelEspecifica.setText(res.getString(R.string.eliminarLabel3));
		 
		int botonWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 77, res.getDisplayMetrics());
        
        int botonHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 68, res.getDisplayMetrics());
        
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(botonWidth, botonHeight);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	botonInferior.setBackgroundDrawable(res.getDrawable(R.drawable.configuracionon));
        }
        else {
        	botonInferior.setBackground(res.getDrawable(R.drawable.configuracionon));
        }
        botonInferior.setLayoutParams(lParams);
		
	}
	
	public void recuperarPassword(View v) {
		Intent intent = new Intent(this, RecuperarPasswordActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.up_in, R.anim.up_out);
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

package com.infomovil.infomovil.menu;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appboy.Appboy;
import com.facebook.Session;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.ViewItem;
import com.infomovil.infomovil.gui.fragment.principal.MainActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.madrobot.text.StringUtils;

	
public class ConfiguracionActivity  extends InfomovilActivity {
	private TextWithFont label;
	private TableView tabla1,tabla2;
	private Button btnConfiguracion;
	Context context;	
	Resources res;
	boolean cerrarSesion = false;
	boolean cambiandoPass = false;
	ProgressDialog progressDialog; 
	
	public TableView getUITableView() {
		return tabla1;
	}
	
	private void llenarTabla() {
		
		res = getResources();
		CustomClickListener listener = new CustomClickListener();
		tabla1.setClickListener(listener);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		if(!org.apache.commons.lang3.StringUtils.isNumeric(datosUsuario.getNombreUsuario().substring(0, 1))){
			RelativeLayout celdaInformacion = (RelativeLayout) mInflater.inflate(R.layout.celda_menu_configuracion , null);
			((TextWithFont) celdaInformacion.findViewById(R.id.labelCeldaConfiguracion)).setText(res.getString(R.string.cambioPassword));
			ViewItem itemView = new ViewItem(celdaInformacion);
			tabla1.addViewItem(itemView);
		}
		
//		RelativeLayout celdaInformacion2 = (RelativeLayout) mInflater.inflate(R.layout.celda_menu_configuracion , null);
//		((TextWithFont) celdaInformacion2.findViewById(R.id.labelCeldaConfiguracion)).setText(res.getString(R.string.eliminaCuenta));
//		ViewItem itemView2 = new ViewItem(celdaInformacion2);
//		tabla1.addViewItem(itemView2);
		
		RelativeLayout celdaInformacion3 = (RelativeLayout) mInflater.inflate(R.layout.celda_menu_configuracion , null);
		((TextWithFont) celdaInformacion3.findViewById(R.id.labelCeldaConfiguracion)).setText(res.getString(R.string.salirCuenta));
		ViewItem itemView3 = new ViewItem(celdaInformacion3);
		tabla1.addViewItem(itemView3);

	}
	
	private void llenarTabla2() throws NameNotFoundException {
		CustomClickListener2 listener = new CustomClickListener2();
		tabla2.setClickListener(listener);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		RelativeLayout celdaInformacion = (RelativeLayout) mInflater.inflate(R.layout.celda_menu_configuracion , null);
		((TextWithFont) celdaInformacion.findViewById(R.id.labelCeldaConfiguracion)).
		setText(res.getString(R.string.soportaEmail));
		ViewItem itemView = new ViewItem(celdaInformacion);
		tabla2.addViewItem(itemView);
		
		PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		RelativeLayout celdaInformacion2 = (RelativeLayout) mInflater.inflate(R.layout.celda_menu_configuracion , null);
		((TextWithFont) celdaInformacion2.findViewById(R.id.labelCeldaConfiguracion)).setText(res.getString(R.string.version));
		((ImageView) celdaInformacion2.findViewById(R.id.imageView1)).setVisibility(View.GONE);
		((TextWithFont) celdaInformacion2.findViewById(R.id.labelVersion)).setText(pInfo.versionName);
		((TextWithFont) celdaInformacion2.findViewById(R.id.labelVersion)).setVisibility(View.VISIBLE);
		ViewItem itemView2 = new ViewItem(celdaInformacion2);
		tabla2.addViewItem(itemView2);
		

	}
	
	private class CustomClickListener implements ClickListener {
		@Override
		public void onClick(int index) {
			switch(index){
			case 0:
				cambiopassword();
				break;
			case 1://Salir cuenta
				salirCuenta();
				break;
			}
		}
	}
		
	public void cambiopassword(){
		cambiandoPass = true;
		//return false;
		res = getResources();
		alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion2, res.getString(R.string.mensajeContrasenia));
		alerta.setDelegado(this);
		alerta.show();
	}
	
	public void eliminarCuenta(){
		Intent intent = new Intent(this, EliminarCuenta.class);
		startActivity(intent);
	}
	
	public void salirCuenta(){
		final AlertView dialog = new AlertView(this, AlertViewType.AlertViewTypeQuestion2,		
		getResources().getString(R.string.txtPreguntaCerrar));    	
		dialog.setDelegado(new SalirCuenta(this, dialog));
		dialog.show();
	}
	
	
	public void accionSi() {
		super.accionSi();
		
		if(alerta != null)
			alerta.dismiss();
		
		if (cerrarSesion) {
			salirCuenta();
		}
		else if(cambiandoPass) {
			progressDialog= ProgressDialog.show(this,  getString(R.string.app_name), 
					getResources().getString(R.string.txtCargando), true, false);
			WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
			wsCall.setStrConsulta(datosUsuario.getNombreUsuario());
			wsCall.execute(WSInfomovilMethods.GET_HASH_CAMBIO_PASSWORD);
		}
	}
	
	public void accionNo() {
		super.accionNo();
		
		if(alerta != null)
			alerta.dismiss();
	}
	
	private class CustomClickListener2 implements ClickListener {
		@Override
		public void onClick(int index) {
			if(index==0){
				mandarComentarios();
			}
		}
	}
	
	public void mandarComentarios(){
		Intent intent = new Intent(this, ComentarioActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.up_in, R.anim.up_out);			

	}
	
	

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		
		if (cambiandoPass) {
			
			progressDialog.dismiss();
			Appboy.getInstance(this).logCustomEvent("ChangePassword");
			
			new AlertDialog.Builder(this)
			.setMessage(R.string.cambiarPassLabel2)
			.setPositiveButton(R.string.txtAceptar,  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.create().show();
			
		}
		
	}

	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
		// TODO Auto-generated method stub
		if(alerta != null)
			alerta.dismiss();
		
		if (cerrarSesion) {
			DatosUsuario.getInstance().borrarDatos();
			//Intent i = new Intent(this, MainActivity.class);
	    	//startActivity(i);
	    	startActivity(new Intent(getBaseContext(), MainActivity.class)
	        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
	    	finish();
		}
		else {
			Log.d("infoLog", "Entrando al error");
			AlertView mensajeError = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorGenerico));
			mensajeError.setDelegado(this);
			mensajeError.show();
		}
		
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
////		InfomovilApp.setPressBack(true);
//		InfomovilApp.setUltimoActivity(null);
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MenuPasosActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
		cargarLayout(R.layout.menu_configuracion);
		acomodarVistaConTitulo(R.string.txtTituloConfiguracion, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		
		label = (TextWithFont)findViewById(R.id.labelConfiguracion);
		tabla1 = (TableView)findViewById(R.id.tablaConfiguracion1);
		tabla2 = (TableView)findViewById(R.id.tablaConfiguracion2);
		
		tabla1.setTableType(TableType.TableTypeClassic);
		tabla2.setTableType(TableType.TableTypeClassic);
		context = this;
		res = getResources();
		int botonWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 77, res.getDisplayMetrics());
        
        int botonHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 68, res.getDisplayMetrics());
        
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(botonWidth, botonHeight);
		
		btnConfiguracion = (Button)findViewById(R.id.btnConfiguracion);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			btnConfiguracion.setBackgroundDrawable(getResources().getDrawable(R.drawable.configuracionon));
		}
		else {
			btnConfiguracion.setBackground(getResources().getDrawable(R.drawable.configuracionon));
		}
		
		btnConfiguracion.setLayoutParams(lParams);
		//bt4.setGravity(Gravity.RIGHT);
		//bt4.setTextColor(getResources().getColor(R.color.colorFuenteVerde));
		
	}

	@Override
	public void initResume() {
		tabla1.clear(); 
		llenarTabla();
		tabla1.commit();
		
		tabla2.clear(); 
		try {
			llenarTabla2();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tabla2.commit();
		
		datosUsuario = DatosUsuario.getInstance();
		
		if(!org.apache.commons.lang3.StringUtils.isNumeric(datosUsuario.getNombreUsuario().substring(0, 1))){
			label.setText(datosUsuario.getNombreUsuario());
		}
		
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

	

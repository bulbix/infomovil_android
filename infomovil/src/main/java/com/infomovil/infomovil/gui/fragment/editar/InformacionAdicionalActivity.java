package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.ViewItem;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;

public class InformacionAdicionalActivity extends InfomovilFragment {
	private Vector<WS_KeywordVO> listaInformacion;
	private RelativeLayout layoutInformacion;
	private TableView tablaInformacion;
	private ScrollView scrollInformacion;
	private int indiceSeleccionado, maxPerfil;
	private Context context;
	private TextView labelAgregaInformacion;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.informacion_adicional_layout, container, false);
		layoutInformacion = (RelativeLayout) view.findViewById(R.id.layoutInformacionAdicional);
		tablaInformacion = (TableView) view.findViewById(R.id.tablaInformacion);
		scrollInformacion = (ScrollView) view.findViewById(R.id.scrollInformacion);
		labelAgregaInformacion = (TextView)view.findViewById(R.id.labelAgregaInformacion);		
		return view;
	}
	 
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtTituloInfoAdicional, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
		tablaInformacion.setTableType(TableType.TableTypeClassic);
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = getArguments().getInt("mensaje", R.string.txtCargandoDefault);
	}
	
	@Override
	public void initResume() {
		if(CuentaUtils.isDowngrade()){
			maxPerfil = CuentaUtils.getStatusDomain(getResources().getString(R.string.txtTituloInfoAdicional),
					datosUsuario.getItemsDominioGratuito()).getEstatusItem();
		}
		else{
			maxPerfil = CuentaUtils.getStatusDomain(getResources().getString(R.string.txtTituloInfoAdicional),
					datosUsuario.getItemsDominio()).getEstatusItem();
		}
		
		
		if (datosUsuario.getArregloInformacionAdicional() != null && datosUsuario.getArregloInformacionAdicional().size() > 0) {
			tablaInformacion.clear(); 
			listaInformacion = datosUsuario.getArregloInformacionAdicional();
			llenarTabla();
			tablaInformacion.commit();
		}
		else {
			listaInformacion = new Vector<WS_KeywordVO>();
		}
		acomodaVista();
		showLeyendas();
	}
	
	/**
	 * Desplega texto informativo si es cuenta pro o gratuita
	 */
	private void showLeyendas(){
		
		int status;
		
		if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
			labelAgregaInformacion.setText(R.string.txtAgregaInfoAdicional1);
			status = View.VISIBLE;
		}					
		else{
			labelAgregaInformacion.setText(R.string.txtAgregaInfoAdicional2);
			status = View.GONE;
		}		
		
		for(int i=0;i<layoutInformacion.getChildCount();i++){
		       View child=layoutInformacion.getChildAt(i);
		       if(child.getId() != R.id.labelAgregaInformacion){
		    	   child.setVisibility(status);
		       }
		}
	}
	
	
	
	public void accionSi() {
//		super.accionSi();
		if(alerta != null)
			alerta.dismiss();
		
		Intent intent = new Intent(activity, CuentaActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
	
	public void accionNo() {
		super.accionNo();
		if(alerta != null)
			alerta.dismiss();
	}
	
	public void accionAceptar() {
		super.accionAceptar();
	}
	
	@Override
	public void acomodaVista() {
		if (listaInformacion != null) {
			if(listaInformacion.size() > 0) {
				scrollInformacion.setVisibility(View.VISIBLE);
				layoutInformacion.setVisibility(View.GONE);
			}
			else {
				scrollInformacion.setVisibility(View.GONE);
				layoutInformacion.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public TableView getUITableView() {
		return tablaInformacion;
	}
	
	@SuppressLint("InflateParams") 
	private void llenarTabla() {
		CustomClickListener listener = new CustomClickListener();
		tablaInformacion.setClickListener(listener);
		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for (int i = 0; i < listaInformacion.size(); i++) {
			RelativeLayout celdaInformacion = (RelativeLayout) mInflater.inflate(R.layout.celda_informacion_adicional , null);
			WS_KeywordVO info  = listaInformacion.get(i);
			((TextWithFont) celdaInformacion.findViewById(R.id.labelInformacionAdicional)).setText(info.getKeywordField());
			ViewItem itemView = new ViewItem(celdaInformacion);
			tablaInformacion.addViewItem(itemView);
			

			if(CuentaUtils.isDowngrade() ){
				if(i >= maxPerfil){
					celdaInformacion.setBackgroundColor(Color.parseColor("#D3D3D3"));
				}
			}
		}
	}
	
	private class CustomClickListener implements ClickListener {
		@Override
		public void onClick(int index) {
			// TODO Auto-generated method stub
			
			if(index < maxPerfil){
				InformacionAdicionalPaso2Activity informacionPaso2 = new InformacionAdicionalPaso2Activity();
				Bundle dataBundle = new Bundle();
				dataBundle.putInt("indiceSeleccionado", index);
				dataBundle.putBoolean("editado", true);
				dataBundle.putInt("mensaje", R.string.msgGuardarInfoAd);
				informacionPaso2.setArguments(dataBundle);
				infomovilInterface.loadFragment(informacionPaso2, null, "InformacionAdicional2");
			}
		}
	}
	
	@Override
	public void keyDownAction() {
		datosUsuario = DatosUsuario.getInstance();
		if ( datosUsuario.getArregloInformacionAdicional() != null &&
			 datosUsuario.getArregloInformacionAdicional().size() > 0 ) {
			datosUsuario.getEstatusEdicion()[10] = true;
		}
		else {
			datosUsuario.getEstatusEdicion()[10] = false;
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
	public void guardarInformacion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	public void agregarNuevoItem() {
		// TODO Auto-generated method stub
		
		
		if (listaInformacion.size() < maxPerfil) {
			InformacionAdicionalPaso2Activity informacionPaso2 = new InformacionAdicionalPaso2Activity();
			Bundle dataBundle = new Bundle();
			dataBundle.putInt("mensaje", R.string.msgGuardarInfoAd);
			informacionPaso2.setArguments(dataBundle);
			infomovilInterface.loadFragment(informacionPaso2, null, "InformacionAdicional2");
		}
		else {			
			if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeQuestion, 
				getResources().getString(R.string.mensajeInformacionAdicionalPrueba));
				alerta.setDelegado(this);
				alerta.show();				
			}
			else{
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
				getResources().getString(R.string.mensajeInformacionPro));
				alerta.setDelegado(this);
				alerta.show();				
			}			
		}
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
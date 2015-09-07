package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.adapter.ImagenesAdapter;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.sortlist.DragSortListView;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;

public class GaleriaPaso1Activity extends InfomovilFragment {

	private Context context;
	private Vector<WS_ImagenVO> arrayGaleria, arrayGaleriaAux;
	private DragSortListView tablaImagenes;
	private RelativeLayout scrollGaleria;
	private RelativeLayout layoutGaleria;
	private int indiceSeleccionado, maxImagenes;
	private boolean isEditingMode;
	private ImagenesAdapter imagenAdapter;
	private TextView labelMaxImagenes;

	private DragSortListView.DropListener onDrop =
			new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			
			if(from < maxImagenes && to < maxImagenes){
				WS_ImagenVO item = imagenAdapter.getListAdapter().get(from);
				modifico = true;
				imagenAdapter.getListAdapter().remove(from);
				imagenAdapter.getListAdapter().add(to, item);
					imagenAdapter.notifyDataSetChanged();
			}
			else{
				tablaImagenes.cancelDrag();
			}
		}
	};

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.galeria_paso1_layout, container, false);
		tablaImagenes = (DragSortListView) view.findViewById(R.id.tablaGaleria);
		scrollGaleria = (RelativeLayout) view.findViewById(R.id.scrollGaleria);
		layoutGaleria = (RelativeLayout) view.findViewById(R.id.layoutGaleria);
		labelMaxImagenes = (TextView) view.findViewById(R.id.labelMaxImagenes);
		return view;
	}

	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtImagenesTitulo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = getArguments().getInt("mensaje", R.string.txtCargandoDefault);
	}
	
	private void cargarImagenes(){
		final AlertView progressDialog = new AlertView(activity, 
				AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.txtCargandoDefault));
		progressDialog.show();	
		
		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
			
			@Override
			public void respuestaObj(WSInfomovilMethods metodo,
					WsInfomovilProcessStatus status, Object obj) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
				progressDialog.dismiss();
				initAdapter();
			}
			
			@Override
			public void respuestaCompletada(WSInfomovilMethods metodo,
					long milisegundo, WsInfomovilProcessStatus status) {
				progressDialog.dismiss();
				initAdapter();
				
			}
		}, activity);
		wsCall.execute(WSInfomovilMethods.GET_IMAGENES);
		
	}

	@Override
	public void initResume() {
		
		cargarImagenes();
		//initAdapter();
		showLeyendas();
		
	}
	
	/**
	 * Desplega texto informativo si es cuenta pro o gratuita
	 */
	private void showLeyendas(){
		int status;
		
		if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
			labelMaxImagenes.setText(R.string.txtMaxImagenes1);
			status = View.VISIBLE;
		}					
		else{
			labelMaxImagenes.setText(R.string.txtMaxImagenes2);
			status = View.GONE;
		}
		
		for(int i=0;i<layoutGaleria.getChildCount();i++){
		       View child=layoutGaleria.getChildAt(i);
		       if(child.getId() != R.id.labelMaxImagenes){
		    	   child.setVisibility(status);
		       }
		}
		
	}
	
	

	@Override
	public void keyDownAction() {

		datosUsuario = DatosUsuario.getInstance();
		if (isEditingMode && modifico) {
			datosUsuario.setListaImagenes(arrayGaleriaAux);
		}
		boolean arregloEstatus[] = datosUsuario.getEstatusEdicion();
		if (arrayGaleria.size() > 0) {
			arregloEstatus[indiceSeleccionado] = true;
		}
		else {
			arregloEstatus[indiceSeleccionado] = false;
		}
	}

	protected void initAdapter() {
		datosUsuario = DatosUsuario.getInstance();

		if(CuentaUtils.isDowngrade()){
			maxImagenes = CuentaUtils.getStatusDomain(getResources().getString(R.string.txtGaleriaImagenesTitulo),
					datosUsuario.getItemsDominioGratuito()).getEstatusItem();
		}
		else{
			maxImagenes = CuentaUtils.getStatusDomain(getResources().getString(R.string.txtGaleriaImagenesTitulo), 
					datosUsuario.getItemsDominio()).getEstatusItem();
		}
		
		
		if (datosUsuario.getListaImagenes() != null && datosUsuario.getListaImagenes().size() > 0) {
			if (datosUsuario.getListaImagenes().size() == 1) {
				acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
			}
			else {
				acomodarBotones(ButtonStyleShow.ButtonStyleShowAddEdit);
			}
			
			arrayGaleria = datosUsuario.getListaImagenes();
			imagenAdapter = new ImagenesAdapter(activity, arrayGaleria);
			tablaImagenes.setAdapter(imagenAdapter);
			tablaImagenes.setDropListener(onDrop);
			tablaImagenes.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
					if(position < maxImagenes){
						if(!isEditingMode) {
							GaleriaPaso2Activity galeriaPaso2 = new GaleriaPaso2Activity();
							Bundle dataBundle = new Bundle();
							dataBundle.putInt("indiceTabla", position);
							dataBundle.putBoolean("editando", true);
							dataBundle.putSerializable("galeryType", PhotoGaleryType.PhotoGaleryTypeImage);
							dataBundle.putInt("tituloVista", R.string.txtAddImage);
							galeriaPaso2.setArguments(dataBundle);
							infomovilInterface.loadFragment(galeriaPaso2, null, "GaleriaPaso2");
						}
					}
				}
			});
		}
		else {
			acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
			acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
			arrayGaleria = new Vector<WS_ImagenVO>();
		}
		acomodarVista(); 
	}

	private void acomodarVista() {
		datosUsuario = DatosUsuario.getInstance();
		if (datosUsuario.getListaImagenes() != null && datosUsuario.getListaImagenes().size() > 0) {
			scrollGaleria.setVisibility(View.VISIBLE);
			layoutGaleria.setVisibility(View.GONE);
			arrayGaleria = datosUsuario.getListaImagenes();
			//			try {
			//				tablaGaleria.clear();
			//				llenarTabla();
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//			}
			//			tablaGaleria.commit();
		}
		else {
			arrayGaleria = new Vector<WS_ImagenVO>();
			scrollGaleria.setVisibility(View.GONE);
			layoutGaleria.setVisibility(View.VISIBLE);
		}
	}

//	public void organizarTabla(View v) {
//		if (isEditingMode) {
//			isEditingMode = false;
//		}
//		else {
//			isEditingMode = true;
//		}
//		imagenAdapter.setEditar(isEditingMode);
//		imagenAdapter.notifyDataSetChanged();
//		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
//	}

	public void guardarInformacion() {
		
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		modifico = false;
		wsCall.setLstImagen((Vector<WS_ImagenVO>)imagenAdapter.getListAdapter());
		wsCall.execute(WSInfomovilMethods.UPDATE_IMAGE);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowAddEdit);
		
		isEditingMode = false;
		imagenAdapter.setEditar(false);
		imagenAdapter.notifyDataSetChanged();
	}


	@Override
	public void agregarNuevoItem() {
		
		if (arrayGaleria.size() < maxImagenes) {
			GaleriaPaso2Activity galeriaPaso2 = new GaleriaPaso2Activity();
			Bundle dataBundle = new Bundle();
			dataBundle.putBoolean("editando", false);
			dataBundle.putSerializable("galeryType", PhotoGaleryType.PhotoGaleryTypeImage);
			dataBundle.putInt("tituloVista", R.string.txtAddImage);
			galeriaPaso2.setArguments(dataBundle);
			infomovilInterface.loadFragment(galeriaPaso2, null, "GaleriaPaso2");
			
		}
		else {
			
			if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeQuestion, 
				getResources().getString(R.string.mensajeImagenesPrueba));
				alerta.setDelegado(this);
				alerta.show();				
			}
			else{
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
				getResources().getString(R.string.mensajeImagenesPro));
				alerta.setDelegado(this);
				alerta.show();				
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


	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		// TODO Auto-generated method stub
		if(alerta != null)
			alerta.dismiss();
		datosUsuario = DatosUsuario.getInstance();
		imagenAdapter.setListAdapter(datosUsuario.getListaImagenes());
		arrayGaleria = datosUsuario.getListaImagenes();
		imagenAdapter.notifyDataSetChanged();
		if (status == WsInfomovilProcessStatus.EXITO) {
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alerta.setDelegado(this);
			alerta.show();
		}
		else {
			if (arrayGaleriaAux != null && arrayGaleriaAux.size()>0) {
				datosUsuario.setListaImagenes(arrayGaleriaAux);
			}
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorActualizacion));
			alerta.setDelegado(this);
			alerta.show();
		}

	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub

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
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub

	}


	@Override
	public void organizarTabla() {
		// TODO Auto-generated method stub
		if (isEditingMode) {
			isEditingMode = false;
		}
		else {
			isEditingMode = true;
		}
		arrayGaleriaAux = new Vector<WS_ImagenVO>(imagenAdapter.getListAdapter());
		imagenAdapter.setEditar(isEditingMode);
		imagenAdapter.notifyDataSetChanged();
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
	}

	@Override
	public String validarCampos() {
		return "Correcto";
	}
}
package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.adapter.ContactosAdapter;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.sortlist.DragSortListView;
import com.infomovil.infomovil.gui.widget.sortlist.DragSortListView.EnableContactListener;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.menu.EstadisticasActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;

public class ContactosPaso1Activity extends InfomovilFragment implements EnableContactListener {
	
	private Vector<WS_RecordNaptrVO> listaContactos, listaAuxiliarContactos;
	private RelativeLayout layoutContactos;
	private DragSortListView tablaContactos;
	private RelativeLayout scrollContactos;
	private Context context;
	private WS_RecordNaptrVO contactoEditado;
	boolean isEditingMode;
	private TextView labelAgregarContacto, labelTituloContacto;
	
	ContactosAdapter contactoAdapter;
	int maxNumeroContactos, indiceSeleccionado;
	private RelativeLayout layoutTitleContacto;
	
	public void agregarTelefono(View v) {
		ContactoPaso2Fragment contactoPaso2 = new ContactoPaso2Fragment();
		Bundle dataBundle = new Bundle();
		dataBundle.putBoolean("editado", false);
		dataBundle.putInt("indiceSeleccionado", 0);//Telefono
		dataBundle.putInt("mensaje", R.string.msgGuardarContacto);						
		contactoPaso2.setArguments(dataBundle);
		infomovilInterface.loadFragment(contactoPaso2, null, "ContactoPaso2");
	}
	
	public void agregarEmail(View v) {
		ContactoPaso2Fragment contactoPaso2 = new ContactoPaso2Fragment();
		Bundle dataBundle = new Bundle();
		dataBundle.putBoolean("editado", false);
		dataBundle.putInt("indiceSeleccionado", 3);//Email
		dataBundle.putInt("mensaje", R.string.msgGuardarContacto);						
		contactoPaso2.setArguments(dataBundle);
		infomovilInterface.loadFragment(contactoPaso2, null, "ContactoPaso2");
	}
	
	
	private DragSortListView.DropListener onDrop =
	        new DragSortListView.DropListener() {
	            @Override
	            public void drop(int from, int to) {
	            	if(from < maxNumeroContactos && to < maxNumeroContactos){
		                WS_RecordNaptrVO item = contactoAdapter.getItem(from);
		                contactoAdapter.notifyDataSetChanged();
		                contactoAdapter.getListAdapter().remove(item);
		                contactoAdapter.getListAdapter().add(to, item);
	            	}
	            	else{
	            		tablaContactos.cancelDrag();
	            	}
	            }
	        };
	
	        
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contactos_paso1_layout, container, false);
		layoutTitleContacto = (RelativeLayout) view.findViewById(R.id.layoutTituloContactos);
		layoutContactos = (RelativeLayout) view.findViewById(R.id.layoutContactos);
		tablaContactos = (DragSortListView) view.findViewById(R.id.tablaContactos);
		scrollContactos = (RelativeLayout) view.findViewById(R.id.scrollContactos);
		labelAgregarContacto = (TextView) view.findViewById(R.id.labelAgregarContacto);
		labelTituloContacto = (TextView) view.findViewById(R.id.labelTituloContacto);
		
		ImageView imgTelefono = (ImageView) view.findViewById(R.id.imgTelefono);
		TextView labelPrimerNumero = (TextView) view.findViewById(R.id.labelPrimerNumero);
		ClickTelefono clickTelefono = new ClickTelefono();
		imgTelefono.setOnClickListener(clickTelefono);
		labelPrimerNumero.setOnClickListener(clickTelefono);
		
		ImageView imgEmail = (ImageView) view.findViewById(R.id.imgEmail);
		TextView labelPrimerEmail = (TextView) view.findViewById(R.id.labelPrimerEmail);
		ClickEmail clickEmail = new ClickEmail();
		imgEmail.setOnClickListener(clickEmail);
		labelPrimerEmail.setOnClickListener(clickEmail);
		
		
		
		ImageView imgInfo = (ImageView) view.findViewById(R.id.imgContactoInfo);
		ClickInfoContacto clickInfoContacto = new ClickInfoContacto();
		imgInfo.setOnClickListener(clickInfoContacto);
		labelTituloContacto.setOnClickListener(clickInfoContacto);
		
		return view;
	}
	
	class ClickTelefono implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			agregarTelefono(v);
		}	
	}
	
	class ClickEmail implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			agregarEmail(v);
		}
	}
	
	
	private class ClickInfoContacto implements OnClickListener {
		@Override
		public void onClick(View v) {
			FragmentManager fm = getFragmentManager();
			ContactoInfoDialog editNameDialog = ContactoInfoDialog.newInstance();
		    editNameDialog.show(fm, "ContactoInstruccion");
		}
		
	}
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtContacto, R.drawable.plecaverde);		
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = getArguments().getInt("mensaje", R.string.txtCargandoDefault);
	}
	 
	@Override
	public void initResume() {
		initAdapter();		
		showLeyendas();
	} 
	
	/**
	 * Desplega texto informativo si es cuenta pro o gratuita
	 */
	private void showLeyendas(){
		int status;
		
		if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
			labelAgregarContacto.setText(R.string.txtAgregaContacto1);
			status = View.VISIBLE;
		}					
		else{
			labelAgregarContacto.setText(R.string.txtAgregaContacto2);
			status = View.GONE;
		}
		
		for(int i=0;i<layoutContactos.getChildCount();i++){
		       View child=layoutContactos.getChildAt(i);
		       if(child.getId() != R.id.labelAgregarContacto){
		    	   child.setVisibility(status);
		       }
		}
		
	}
	
	protected void initAdapter(){
		
		if(CuentaUtils.isDowngrade()){
			maxNumeroContactos = CuentaUtils.getStatusDomain(getResources().getString(R.string.txtContacto),
					datosUsuario.getItemsDominioGratuito()).getEstatusItem();
		}
		else{
			maxNumeroContactos = CuentaUtils.getStatusDomain(getResources().getString(R.string.txtContacto), 
					datosUsuario.getItemsDominio()).getEstatusItem();
		}
		
		  
		if (datosUsuario.getListaContactos() != null && datosUsuario.getListaContactos().size() > 0) {
			if (datosUsuario.getListaContactos().size() == 1) {
				acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
			}
			else {
				acomodarBotones(ButtonStyleShow.ButtonStyleShowAddEdit);
			}
			listaContactos = datosUsuario.getListaContactos();
			contactoAdapter = new ContactosAdapter(activity, datosUsuario.getListaContactos(), false, this);
			tablaContactos.setAdapter(contactoAdapter);
			tablaContactos.setDropListener(onDrop);
//			tablaContactos.setDragScrollProfile(ssProfile);
			OnItemClickListener contactoClickListener = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					if(position < maxNumeroContactos){
						if (!isEditingMode) {
							ContactoPaso2Fragment contactoPaso2 = new ContactoPaso2Fragment();
							Bundle dataBundle = new Bundle();
							dataBundle.putBoolean("editado", true);
							dataBundle.putInt("indiceSeleccionado", position);
							dataBundle.putInt("mensaje", R.string.msgGuardarContacto);						
							contactoPaso2.setArguments(dataBundle);
							infomovilInterface.loadFragment(contactoPaso2, null, "ContactoPaso2");
						}
					}
				}
			};
			tablaContactos.setOnItemClickListener(contactoClickListener);
		}
		else {
			acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
			listaContactos = new Vector<WS_RecordNaptrVO>();
		}
		acomodaVista();
	}
	
	@Override
	public void keyDownAction() {
		
		Log.d("Contacto", "keydowndContacto1");
		if (isEditingMode && modifico) {
			datosUsuario.setListaContactos(listaAuxiliarContactos);
		}
		infomovilInterface.returnFragment("");
	}
	 
	public void acomodaVista() {
		if (listaContactos != null) {
			if(listaContactos.size() > 0) {
				layoutTitleContacto.setVisibility(View.VISIBLE);
				scrollContactos.setVisibility(View.VISIBLE);
				layoutContactos.setVisibility(View.GONE);
			}
			else {
				scrollContactos.setVisibility(View.GONE);
				layoutContactos.setVisibility(View.VISIBLE);
				layoutTitleContacto.setVisibility(View.GONE);
			}
		}
	}
	
	public void activarContacto(View v) {
		Log.d("infoLog", "se dio click en el boton con tag " + v.getTag());
		int indice = Integer.parseInt(v.getTag().toString());
		
		contactoEditado = datosUsuario.getListaContactos().get(indice);
		if (contactoEditado.getVisible() == 0) {
			contactoEditado.setVisible(1);
		}
		else {
			contactoEditado.setVisible(0);
		}
		
			if (InfomovilApp.isConnected(activity)) {
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.msgGuardarContacto));
				alerta.setDelegado(this);
				alerta.show();
				WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
//				wsCall.setDelegate(this);
				Vector<WS_RecordNaptrVO> auxVector = new Vector<WS_RecordNaptrVO>();
				auxVector.add(contactoEditado);
				wsCall.setLstContactos(auxVector);
				wsCall.execute(WSInfomovilMethods.UPDATE_RECORD_NAPTR);
			}
	}
	
	public void accionSi() {
		super.accionSi();
		if(alerta != null)
			alerta.dismiss();
		
		Intent intent = new Intent(activity, CuentaActivity.class);
		startActivity(intent);
	}
	
	public void accionNo() {
		super.accionNo();		
		if(alerta != null)
			alerta.dismiss();
		//datosUsuario.setListaContactos(listaAuxiliarContactos);
	}
	
	public void accionAceptar() {
		Log.d("clickBotonAceptar", "Ha sido clickeado");
		initAdapter();
	}
	
	public void guardarInformacion() {
		isEditingMode = false;
				WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
//				wsCall.setDelegate(this);
				Vector<WS_RecordNaptrVO> auxVector = (Vector<WS_RecordNaptrVO>)contactoAdapter.getListAdapter();
				wsCall.setLstContactos(auxVector);
				wsCall.execute(WSInfomovilMethods.UPDATE_RECORD_NAPTR);
		contactoAdapter.setEditar(false);
		modifico=false;
		contactoAdapter.notifyDataSetChanged();
		acomodarBotones(ButtonStyleShow.ButtonStyleShowAddEdit);
	}  
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		if(alerta != null)
			alerta.dismiss();
		
		Log.d("infoLog", "Terminando 1");
		if (status == WsInfomovilProcessStatus.EXITO) {
			AlertView alertaBien = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
			getResources().getString(R.string.txtActualizacionCorrecta));
			alertaBien.setDelegado(this);
			alertaBien.show();
		}
		else {
			if (listaAuxiliarContactos != null && listaAuxiliarContactos.size() > 0) {
				datosUsuario.setListaContactos(listaAuxiliarContactos);
			}
		}
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		if(alerta != null)
			alerta.dismiss();
		
		Log.d("infoLog", "Terminando 2");
		
	}

	@Override
	public void enable(int index) {
		
		alerta = new AlertView(activity, AlertViewType.AlertViewTypeActivity,
		getResources().getString(R.string.msgGuardarContacto));
		alerta.setDelegado(this);
		alerta.show();
		
		
		listaContactos = (Vector<WS_RecordNaptrVO>)contactoAdapter.getListAdapter();
		WS_RecordNaptrVO recAux = listaContactos.get(index);
		if(recAux.getVisible() == 1) {
			recAux.setVisible(0);
		}
		else {
			recAux.setVisible(1);
		}
		listaContactos.remove(index);
		listaContactos.add(index, recAux);

		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		//				wsCall.setDelegate(this);
		Vector<WS_RecordNaptrVO> auxVector = (Vector<WS_RecordNaptrVO>)contactoAdapter.getListAdapter();
		wsCall.setLstContactos(auxVector);
		wsCall.execute(WSInfomovilMethods.UPDATE_RECORD_NAPTR);
		
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
	public void agregarNuevoItem() {
		
		
		if(listaContactos.size() < maxNumeroContactos ) {
			TipoContactoActivity tipoContacto = new TipoContactoActivity();
			infomovilInterface.loadFragment(tipoContacto, null, "tipoContacto");
			
//			Intent intent = new Intent(activity, TipoContactoActivity.class);
//			startActivity(intent);
		}
		else {
			
			if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeQuestion, 
				getResources().getString(R.string.mensajeContactosPrueba));
				alerta.setDelegado(this);
				alerta.show();				
			}
			else{
				alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo,
				getResources().getString(R.string.mensajeContactosPro));
				alerta.setDelegado(this);
				alerta.show();				
			}			
		}
		
	}

	@Override
	public void organizarTabla() {
		if (isEditingMode) {
			isEditingMode = false;
		}
		else {
			isEditingMode = true;
		}
		listaAuxiliarContactos = new Vector<WS_RecordNaptrVO>(contactoAdapter.getListAdapter());
		modifico = true;
		contactoAdapter.setEditar(isEditingMode);
		contactoAdapter.notifyDataSetChanged();
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
	}

	@Override
	public String validarCampos() {
		return "Correcto";
	}
}

class ContactoInfoDialog extends DialogFragment {

    private ContactoInfoDialog() {
        // Empty constructor required for DialogFragment
    }

    static ContactoInfoDialog newInstance() {
    	ContactoInfoDialog frag = new ContactoInfoDialog();
        return frag;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      Dialog dialog = super.onCreateDialog(savedInstanceState);

      // request a window without the title
      dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
      return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacto_instrucciones, container); 
        //getDialog().setTitle(getString(R.string.txtInstrucciones));
        
        Button btnOk = (Button) view.findViewById(R.id.botonAceptar1);
        btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				((DialogFragment)fm.findFragmentByTag("ContactoInstruccion")).dismiss();
			}
		});
        
        return view;
    }
}





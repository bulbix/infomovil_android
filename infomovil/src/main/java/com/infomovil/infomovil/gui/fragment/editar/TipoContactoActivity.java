package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.ReadFilesUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.ViewItem;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.TipoContacto;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class TipoContactoActivity extends InfomovilFragment {
	
	private TableView tablaContactos;
	Vector<TipoContacto> listaTipo;
	Context context;
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tipo_contacto_layout, container, false);
		tablaContactos = (TableView) view.findViewById(R.id.tablaTipoContacto);
		return view;
	}
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtTipoContacto, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		listaTipo = new Vector<TipoContacto>();
		tablaContactos.setTableType(TableType.TableTypeClassic);
		leerArchivo();
		tablaContactos.commit();
	}
	
	private void leerArchivo () {
		datosUsuario = DatosUsuario.getInstance();
		if(datosUsuario.getArrayTipoContacto() == null || datosUsuario.getArrayTipoContacto().size() == 0) {
			listaTipo = ReadFilesUtils.leerArchivo(getResources());
		}
		else {
			listaTipo = datosUsuario.getArrayTipoContacto();
		}
		
		llenarTabla();
	}
	 
	public void llenarTabla() {
		CustomClickListener listener = new CustomClickListener();
		tablaContactos.setClickListener(listener);
		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resources res = getResources();
		for(int i=0; i < listaTipo.size(); i++) {
			TipoContacto tipoActual = listaTipo.get(i);
			RelativeLayout cellLayout = (RelativeLayout) mInflater.inflate(R.layout.celda_tipo_contacto, null);
			int imageID = res.getIdentifier(tipoActual.getImagen(), "drawable", activity.getPackageName());
			((ImageView)cellLayout.findViewById(R.id.imagenCeldaTipoContacto)).setImageDrawable(res.getDrawable(imageID));
			((TextWithFont)cellLayout.findViewById(R.id.labelNombreTipoContacto)).setText(tipoActual.getTexto());
			ViewItem viewItem = new ViewItem(cellLayout);
			tablaContactos.addViewItem(viewItem);
		}
	}
	
	protected TableView getUITableView() {
		return tablaContactos;
	}
	
	private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
//			Intent intent = new Intent();
//			intent.setClass(context, ContactoPaso2Fragment.class);
//			intent.putExtra("indiceSeleccionado", index);
//			intent.putExtra("editado", false);
//			startActivityForResult(intent, InfomovilConstants.ADD_CONTACT);
			
			ContactoPaso2Fragment contactoPaso2 = new ContactoPaso2Fragment();
			Bundle dataBundle = new Bundle();
			dataBundle.putBoolean("editado", false);
			dataBundle.putInt("indiceSeleccionado", index);
			dataBundle.putInt("mensaje", R.string.msgGuardarContacto);						
			contactoPaso2.setArguments(dataBundle);
			infomovilInterface.loadFragment(contactoPaso2, null, "ContactoPaso2");
			
		}
		
	}
	
	@Override
	public void resultActivityCall (int requestCode, int resultCode, Intent data) {
		if (requestCode == InfomovilConstants.ADD_CONTACT) {
			if (resultCode == Activity.RESULT_OK) {
				if (data.getIntExtra("opcion", 0) == 1) {
					activity.finish();
				}
			}
		}
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
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void checkBotonEliminar() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}
}

package com.infomovil.infomovil.gui.fragment.editar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class PerfilPaso1Activity extends InfomovilFragment {
	
	private TableView tablaPerfil;
	private Context context;
	Resources res;
	String arregloTitulos[]; //= 
	boolean arregloEstatusPerfil[] = {false, false, false, false, false, false, false};
	int banderaModifico;
	int indiceSeleccionado;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.perfil_paso1_layout, container, false);
		tablaPerfil = (TableView) view.findViewById(R.id.tablaPerfil);
		return view;
	}
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtPerfilTitulo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		res = getResources();
		String arrayTitAux[] = {res.getString(R.string.productoServicio), 
				res.getString(R.string.areaServicio), res.getString(R.string.horario), 
				res.getString(R.string.metodosPago), res.getString(R.string.asociaciones),
				res.getString(R.string.biografia)};
		
		arregloTitulos = arrayTitAux;
		
		tablaPerfil.setTableType(TableType.TableTypeClassic);
		loadTableProfile();
		tablaPerfil.commit();
		banderaModifico = 0;
		
		if(datosUsuario.getEstatusPerfil() == null) {
			datosUsuario.setEstatusPerfil(arregloEstatusPerfil);
			
		}
		else {
			arregloEstatusPerfil = datosUsuario.getEstatusPerfil();
		}
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado",0);
	}
	
	@Override
	public void initResume() {
		tablaPerfil.clear();
		loadTableProfile();
		tablaPerfil.commit();
	}
	
	private void loadTableProfile() {
		CustomClickListener listener = new CustomClickListener();
		tablaPerfil.setClickListener(listener);
		for (int i = 0; i < arregloTitulos.length; i++) {
			//if(!(arregloTitulos[i].toString().equals("Business | Profession"))){
			tablaPerfil.addBasicItem(arregloTitulos[i], arregloEstatusPerfil[i]);
			//}
		}
	}
	
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			banderaModifico = data.getIntExtra("banderaModifico", 0);
			if (banderaModifico == 1) {				
				datosUsuario.getEstatusEdicion()[indiceSeleccionado] = true;
			}
			else{
				datosUsuario.getEstatusEdicion()[indiceSeleccionado] = false;
			}
		}
	}
	
	private class CustomClickListener implements ClickListener {
		@Override
		public void onClick(int index) {
			PerfilPaso2Activity perfilPaso2 = new PerfilPaso2Activity();
			Bundle dataBundle = new Bundle();
			
			int arrayTititulos[] = {R.string.productoServicio, 
					R.string.areaServicio, R.string.horario, 
					R.string.metodosPago, R.string.asociaciones,
					R.string.biografia};
			
			dataBundle.putInt("tituloVistaPerfil", arrayTititulos[index]);
			dataBundle.putInt("indiceSeleccionado", indiceSeleccionado);
			dataBundle.putInt("opcionSeleccionada", index);			
			dataBundle.putInt("mensaje", R.string.msgGuardarPerfil);	
			dataBundle.putString("ventana", "PerfilPaso1");
			perfilPaso2.setArguments(dataBundle);
			infomovilInterface.loadFragment(perfilPaso2, null, "PerfilPaso2");			
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
	public void acomodaVista() {
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

package com.infomovil.infomovil.gui.fragment.editar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.common.utils.ReadFilesUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.MenuQuickStartItems;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;

public class MenuCrearQuickStartFragment extends InfomovilFragment {
	
	private TableView tableView;
	String arregloTitulos[] ;
	private Button btnNombrarDominio;
	
	
	@Override
	public void initResume() {
		
		tableView.clear();
		tableView.setTableType(TableType.TableTypeClassic);
		createList();
	    tableView.commit();
		
	}
	
	
	private void createList(){
		
		CustomClickListener listener = new CustomClickListener();
    	tableView.setClickListener(listener);
    	datosUsuario = DatosUsuario.getInstance();
    	Resources res = getResources();
    	
    	arregloTitulos = new String[]{res.getString(R.string.txtNombreoEmpresa),
    		res.getString(R.string.txtDescripcionCortaTitulo), res.getString(R.string.txtContacto), 
    		res.getString(R.string.productoServicio), res.getString(R.string.txtMapaTituloOpcional)};
    	
    	Map<String,Boolean> estatusTitulos = ReadFilesUtils.buscaEditados(res);
    	
    	for(String titulo: arregloTitulos){
    		boolean estatus = false;
    		
    		if(estatusTitulos.containsKey(titulo)){
    			estatus = estatusTitulos.get(titulo);
    		}
    		
    		tableView.addBasicItem(titulo, estatus);
    	}
    	
		
	}
	
	
	
	private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			Intent intent;
			Bundle dataBundle;
			
			MenuQuickStartItems menuItems = MenuQuickStartItems.byValue(index);			
			
			switch(menuItems){
				case NOMBRE_EMPRESA:
					NombreSitioActivity nombreSitio = new NombreSitioActivity();
					dataBundle = new Bundle();
					dataBundle.putInt("mensaje", R.string.msgGuardarNombre);
					dataBundle.putInt("indiceSeleccionado", 0);
					nombreSitio.setArguments(dataBundle);
					infomovilInterface.loadFragment(nombreSitio, null, "Nombrar");
					break;
				case DESCRIPCION_CORTA:					
					DescripcionActivity descripcion = new DescripcionActivity();
					dataBundle = new Bundle();
					dataBundle.putInt("mensaje", R.string.msgGuardarDescripcion);
					dataBundle.putInt("indiceSeleccionado", 3);
					descripcion.setArguments(dataBundle);
					infomovilInterface.loadFragment(descripcion, null, "Descripcion");
					break;
				case CONTACTO:					
					ContactosPaso1Activity contactoPaso1 = new ContactosPaso1Activity();
					dataBundle = new Bundle();				
					dataBundle.putInt("indiceSeleccionado", 4);
					contactoPaso1.setArguments(dataBundle);
					infomovilInterface.loadFragment(contactoPaso1, null, "ContactoPaso1");
					break;
					
				case PRODUCTOS_SERVICIOS:
					PerfilPaso2Activity negocioPromocion = new PerfilPaso2Activity();
					dataBundle = new Bundle();
					dataBundle.putInt("indiceSeleccionado", 9);
					dataBundle.putInt("opcionSeleccionada", 0); //Negocio Promocion
					dataBundle.putInt("tituloVistaPerfil", R.string.negocioProfesion);
					dataBundle.putInt("mensaje", R.string.msgGuardarPerfil);
					dataBundle.putString("ventana", "InicioRapido");
					negocioPromocion.setArguments(dataBundle);
					infomovilInterface.loadFragment(negocioPromocion, null, "NegocioPromocion");	
					break;
					
				case MAPA:
					intent = new Intent(getActivity().getApplicationContext(), 
					MapaUbicacionActivity.class);
					intent.putExtra("indiceSeleccionado", 5);
					startActivityForResult(intent, InfomovilConstants.STATIC_INTEGER_VALUE);
					break;
				
//				case PUBLICAR:
//					
//					Map<String,Boolean> estatusTitulos = ReadFilesUtils.buscaEditados(res);
//					List<String> listaTitulos = new ArrayList<String>(Arrays.asList(arregloTitulos));
//					listaTitulos.remove(res.getString(R.string.txtMapaTitutlo));
//					listaTitulos.remove(res.getString(R.string.txtPublicar));
//					
//					int totalItems = 0;
//					for(String titulo: listaTitulos){
//						if(estatusTitulos.containsKey(titulo) && estatusTitulos.get(titulo)){
//							++totalItems;
//						}
//					}
//					
//					if(totalItems == listaTitulos.size()){
//						Intent i = new Intent(getActivity(), NombrarActivity.class);
//						i.putExtra("tipoDominio", InfomovilApp.tipoInfomovil);
//						startActivity(i);
//						getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);	
//					}
//					else{
//						final AlertView alertMsg = new AlertView(activity, AlertViewType.AlertViewTypeInfo, 
//								res.getString(R.string.txtErrorInicioRapido));
//						alertMsg.setDelegado(new AlertViewCloseDialog(alertMsg));
//						alertMsg.show();
//					}
//					break;
					
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
	public View getView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_crear_layout, container,false);
		tableView = (TableView) view.findViewById(R.id.tableView);
		
		TextWithFont lblDescripcionInicioRapido = (TextWithFont)
				view.findViewById(R.id.lblDescripcionInicioRapido);
		lblDescripcionInicioRapido.setText(getResources().getString(R.string.txtDescripcionInicioRapido));
		lblDescripcionInicioRapido.setVisibility(View.VISIBLE);
		
		btnNombrarDominio = (Button) view.findViewById(R.id.btnNombrarDominio);
		
		btnNombrarDominio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CuentaUtils.redireccionarPublicar(getActivity(),false);
			}
		});
		
		
		
		return view;
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
		acomodarVistaConTitulo(R.string.txt2CrearEditar, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowPreview);
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
		return "Correcto";
	}
	
	public void nombrarSitio(View v){
		CuentaUtils.redireccionarPublicar(getActivity(),false);
	}

}

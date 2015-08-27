package com.infomovil.infomovil.gui.fragment.editar;

import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.common.utils.ReadFilesUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.MenuItems;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.fragment.background.ColorFondoActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;


public class MenuCrearActivity extends InfomovilFragment {
	
	private TableView tableView;
	private Activity context;
	private String arregloTitulos[];
	boolean arregloEstatus[] = new boolean[12];
	private Vector<WS_StatusDomainVO> arrayItems;
	private Button btnNombrarDominio;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_crear_layout, container,false);
		tableView = (TableView) view.findViewById(R.id.tableView);
		btnNombrarDominio = (Button) view.findViewById(R.id.btnNombrarDominio);
		
		btnNombrarDominio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CuentaUtils.redireccionarPublicar(getActivity(),true);
			}
		});
		return view;
	}
	
	@Override
	public void initCreate() {
		Resources res = getResources();		
		Log.d("infoLog", "Ejecutando on create");
		acomodarVistaConTitulo(R.string.txtAgregaMasContenido, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowPreview);
	} 
	
	@Override
	public void initResume() {
		Resources res = getResources();		
		
		ReadFilesUtils.buscaEditados(res);
		
		String arrayTitAux[] = {res.getString(R.string.negocioProfesion)};
		arregloTitulos = arrayTitAux;
		
		if(datosUsuario.getEstatusEdicion() != null) {
			arregloEstatus = datosUsuario.getEstatusEdicion();
		}
		
		if(CuentaUtils.isPublicado()){
			btnNombrarDominio.setVisibility(View.GONE);
		}
		else{
			btnNombrarDominio.setVisibility(View.VISIBLE);
		}
		
		
		
		tableView.clear();
		tableView.setTableType(TableType.TableTypeClassic);
		createList();
	    tableView.commit();
	}
	
	public void mostrarAlerta() {
		alerta = new AlertView(context, AlertViewType.AlertViewTypeQuestion, getResources().getString(R.string.txtPreguntaVideo));
		alerta.setDelegado(this);
		alerta.show();
	}
	
	private void createList() {
    	CustomClickListener listener = new CustomClickListener();
    	tableView.setClickListener(listener);
    	datosUsuario = DatosUsuario.getInstance();
    	arrayItems = datosUsuario.getItemsDominio();    	
    	Resources res = getResources();
    	
    	String arregloTitulos[] = {
    			res.getString(R.string.txtColorFondo),
    			res.getString(R.string.txtNombreoEmpresa),
    			res.getString(R.string.txtLogo), res.getString(R.string.negocioProfesion),
    			res.getString(R.string.txtDescripcionCortaTitulo), res.getString(R.string.txtContacto), 
    			res.getString(R.string.txtMapaTitutlo), res.getString(R.string.txtTituloVideo), 
    			res.getString(R.string.txtPromociones), res.getString(R.string.txtGaleriaImagenesTitulo),
    			res.getString(R.string.txtPerfilTitulo), res.getString(R.string.txtTituloDireccion),
    			res.getString(R.string.txtTituloInfoAdicional)};

    	for (int i = 0; i < arregloTitulos.length; i++) {
    		WS_StatusDomainVO itemAux = CuentaUtils.getStatusDomain(arregloTitulos[i], arrayItems);
    		if(itemAux != null){
    			itemAux.setDescripcionItem(arregloTitulos[i]);
        		arrayItems.set(i, itemAux);    			
    		}
    		else{
    			arrayItems.add(i, new WS_StatusDomainVO(arregloTitulos[i], 1));    	
    		}
    		
    		tableView.addBasicItem(arregloTitulos[i], arregloEstatus[i]);
    	}
    	
//    	if(MenuPasosActivity.fueEditado(getResources()) && !CuentaUtils.isPublicado()){
//    		tableView.addBasicItem(res.getString(R.string.txtPublicar), false);
//    	}	
    	
    }
	
	protected TableView getUITableView() {
		return tableView;
	}
	
	private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			Intent intent;
			Bundle dataBundle;
			datosUsuario = DatosUsuario.getInstance();
			datosUsuario.setEstatusEdicion(arregloEstatus);
			
			MenuItems menuItems = MenuItems.byValue(index);			
			
			switch(menuItems){
				case COLOR_FONDO:
			        intent = new Intent(getActivity().getApplicationContext(), 
			        		ColorFondoActivity.class);
			        startActivity(intent);
			        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
					break;
			
				case NOMBRE_EMPRESA:
					NombreSitioActivity nombreSitio = new NombreSitioActivity();
					dataBundle = new Bundle();
					dataBundle.putInt("mensaje", R.string.msgGuardarNombre);
					dataBundle.putInt("indiceSeleccionado", index);
					nombreSitio.setArguments(dataBundle);
					infomovilInterface.loadFragment(nombreSitio, null, "Nombrar");
					break;
				case LOGO:
					dataBundle = new Bundle();
					dataBundle.putInt("indiceSeleccionado", index);
					dataBundle.putInt("tituloVista", R.string.txtLogo);
					dataBundle.putSerializable("galeryType", PhotoGaleryType.PhotoGaleryTypeLogo);
					dataBundle.putInt("mensaje", R.string.msgGuardarLogo);
					if (datosUsuario.getEstatusEdicion()[index] == true) {
						dataBundle.putBoolean("editando", true);
					}
					GaleriaPaso2Activity galeriaPaso2 = new GaleriaPaso2Activity();
					galeriaPaso2.setArguments(dataBundle);
					infomovilInterface.loadFragment(galeriaPaso2, null, "ImagenLogo");
					break;
				case NEGOCIO_PROFESION:
					PerfilPaso2Activity negocioPromocion = new PerfilPaso2Activity();
					dataBundle = new Bundle();
					dataBundle.putInt("indiceSeleccionado", index);
					dataBundle.putInt("opcionSeleccionada", 6); //Negocio Promocion
					dataBundle.putInt("tituloVistaPerfil", R.string.negocioProfesion);
					dataBundle.putInt("mensaje", R.string.msgGuardarPerfil);				
					negocioPromocion.setArguments(dataBundle);
					infomovilInterface.loadFragment(negocioPromocion, null, "NegocioPromocion");	
					break;
				case DESCRIPCION_CORTA:					
					DescripcionActivity descripcion = new DescripcionActivity();
					dataBundle = new Bundle();
					dataBundle.putInt("mensaje", R.string.msgGuardarDescripcion);
					dataBundle.putInt("indiceSeleccionado", index);
					descripcion.setArguments(dataBundle);
					infomovilInterface.loadFragment(descripcion, null, "Descripcion");
					break;
				case CONTACTO:					
					ContactosPaso1Activity contactoPaso1 = new ContactosPaso1Activity();
					dataBundle = new Bundle();				
					dataBundle.putInt("indiceSeleccionado", index);
					contactoPaso1.setArguments(dataBundle);
					infomovilInterface.loadFragment(contactoPaso1, null, "ContactoPaso1");
					break;
				case MAPA:
					intent = new Intent(getActivity().getApplicationContext(), 
					MapaUbicacionActivity.class);
					intent.putExtra("indiceSeleccionado", index);
					startActivityForResult(intent, InfomovilConstants.STATIC_INTEGER_VALUE);
					break;
				case PROMOCIONES:
					PromocionesActivity promocionesActivity = new PromocionesActivity();
					dataBundle = new Bundle();
					dataBundle.putInt("indiceSeleccionado", index);
					promocionesActivity.setArguments(dataBundle);
					infomovilInterface.loadFragment(promocionesActivity, null, "PromocionesFragment");
					break;
				case GALERIA_IMAGENES:
					GaleriaPaso1Activity galeriaPaso1 = new GaleriaPaso1Activity();
					dataBundle = new Bundle();
					dataBundle.putInt("indiceSeleccionado", index);
					galeriaPaso1.setArguments(dataBundle);
					infomovilInterface.loadFragment(galeriaPaso1, null, "GaleriaPaso1");
					break;
				case PERFIL:					
					PerfilPaso1Activity perfilPaso1 = new PerfilPaso1Activity();
					dataBundle = new Bundle();				
					dataBundle.putInt("indiceSeleccionado", index);
					perfilPaso1.setArguments(dataBundle);
					infomovilInterface.loadFragment(perfilPaso1, null, "PerfilPaso1");	
					break;
				case DIRECCION:
					DireccionActivity direccion = new DireccionActivity();
					dataBundle = new Bundle();				
					dataBundle.putInt("mensaje", R.string.msgGuardarDireccion);
					dataBundle.putInt("indiceSeleccionado", index);
					direccion.setArguments(dataBundle);
					infomovilInterface.loadFragment(direccion, null, "Direccion");
					break;
				case INFORMACION_ADICIONAL:
					InformacionAdicionalActivity informacionActivity = new InformacionAdicionalActivity();
					dataBundle = new Bundle();
					dataBundle.putInt("indiceSeleccionado", index);
					dataBundle.putInt("mensaje", R.string.msgGuardarInfoAd);
					informacionActivity.setArguments(dataBundle);
					infomovilInterface.loadFragment(informacionActivity, null, "InformacionAdicional");
					break;
				case VIDEO:	
					
					if(CuentaUtils.isDowngrade() || !CuentaUtils.isCuentaPro()){
						new CuentaUtils.RedirectCuenta(getActivity(),R.string.mensajeVideoPrueba);
						return;
					}
					
					ItemSelectModel videoSel = DatosUsuario.getInstance().getVideoSeleccionado();
					
					if(videoSel != null &&
					   !StringUtils.isEmpty(videoSel.getLinkVideo())){						
						PlayVideoFragment playVideo = new PlayVideoFragment();
						Bundle bundle = new Bundle();			
						bundle.putSerializable("videoSeleccionado", videoSel);
						playVideo.setArguments(bundle);
						infomovilInterface.loadFragment(playVideo, null, "VistaPreviaVideo");						
					}
					else{
						VideoActivity video = new VideoActivity();						
						infomovilInterface.loadFragment(video, null, "Video");						
					}					
					
					break;
//				case PUBLICAR:
//					Intent i = new Intent(getActivity(), NombrarActivity.class);
//					i.putExtra("tipoDominio", InfomovilApp.tipoInfomovil);
//					startActivity(i);
//					getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);					
//					break;
					
			}
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
	}
	
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		if (requestCode == InfomovilConstants.STATIC_INTEGER_VALUE) {
			if (resultCode == Activity.RESULT_OK) {
				tableView.clear();
				createList();
				tableView.commit();
			}
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
	
	public void nombrarSitio(View v){
		
	}
	
	
}

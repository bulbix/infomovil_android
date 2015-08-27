
package com.infomovil.infomovil.gui.fragment.editar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.android.lib.toolbox.ToolBox;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.ViewItem;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.PromocionesTipo;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DeleteItem;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_OffertRecordVO;

public class PromocionesActivity extends InfomovilFragment {
	
	private EditTextWithFont txtNombrePromocion, txtDescripcionPromocion, txtInformacionAdicional;
	private TextWithFont labelVigencia;
	private WS_OffertRecordVO promociones;
	private DatePicker datePromocion;
	private String redencionOferta;
	boolean actualizacionCorrecta, arregloEstatus[];
	int banderaModifico, indiceSeleccionado;
	private File outFile = null;
	private RelativeLayout layoutPicker;
	private TableView tablaFoto, tablaRedencion;
	private boolean storeImage = false;
	ArrayList<PromocionesTipo> opcionesTablaFoto, opcionesTablaRedencion;
	Context context;
	Bitmap imagenBitmapPromocion;
	private boolean hayFoto;
	String imagePath;
	boolean red=false;
	Button botonBorrar;
	String fechaAux;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.promociones_layout, container, false);
		txtNombrePromocion = (EditTextWithFont) view.findViewById(R.id.txtNombrePromocion);
		txtDescripcionPromocion = (EditTextWithFont) view.findViewById(R.id.txtDescripcionPromocion);
		txtInformacionAdicional = (EditTextWithFont) view.findViewById(R.id.txtInformacionAdicional);
		labelVigencia = (TextWithFont) view.findViewById(R.id.txtFechaVigencia);
		layoutPicker = (RelativeLayout) view.findViewById(R.id.layoutDatePicker);
		datePromocion = (DatePicker) view.findViewById(R.id.datePickerPromocion);
		tablaFoto = (TableView) view.findViewById(R.id.tablaFoto);
		tablaRedencion = (TableView) view.findViewById(R.id.tablaRedencion);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarPromocion);
		//Button botonPromociones = (Button) view.findViewById(R.id.botonConsultarFecha);
		labelVigencia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mostrarFecha(v);
			}
		});
		
		Button botonAceptarFecha = (Button) view.findViewById(R.id.botonAceptarFecha);
		botonAceptarFecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aceptarFecha(v);
			}
		});
		return view;
	}
	
	@Override
	public void initCreate() {
		acomodarVistaConTitulo(R.string.txtPromociones, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
		tablaFoto.setTableType(TableType.TableTypeClassic);
		tablaRedencion.setTableType(TableType.TableTypeClassic);
		redencionOferta = "";
		banderaModifico = 0;
		actualizacionCorrecta = false;
		promociones = datosUsuario.getPromocionDominio();
		Resources res = getResources();
		String arregloTitulos[] = {res.getString(R.string.txtSinFoto), res.getString(R.string.txtTomarFoto)};
		String arregloTitulos2[] = {res.getString(R.string.txtNoEspecificado), res.getString(R.string.txtLlamanos), res.getString(R.string.txtEnviaEmail), res.getString(R.string.txtVisitanos)};
		String arregloTitulosAux[] = {res.getString(R.string.txtNoEspecificadoEs), res.getString(R.string.txtLlamanosEs), res.getString(R.string.txtEnviaEmailEs), res.getString(R.string.txtVisitanosEs)};
		if(promociones == null) {
			promociones = new WS_OffertRecordVO();
			opcionesTablaFoto = new ArrayList<PromocionesTipo>();
			for (int i = 0; i < arregloTitulos.length; i++) {
				opcionesTablaFoto.add(new PromocionesTipo(arregloTitulos[i], false));
			}
			
			if(opcionesTablaRedencion == null || opcionesTablaRedencion.size() == 0){
				opcionesTablaRedencion = new ArrayList<PromocionesTipo>();
				for (int i = 0; i < arregloTitulos2.length; i++) {
					opcionesTablaRedencion.add(new PromocionesTipo(arregloTitulos2[i], false, arregloTitulosAux[i]));
				}
			}
			
		}
		else {
			opcionesTablaFoto = new ArrayList<PromocionesTipo>();
			if (promociones.getImageClobOffer().length() > 10) {
				hayFoto = true;
				for (int i = 0; i < arregloTitulos.length; i++) {
					if (i == 1) {
						opcionesTablaFoto.add(new PromocionesTipo(arregloTitulos[i], true));
					}
					else {
						opcionesTablaFoto.add(new PromocionesTipo(arregloTitulos[i], false));
					}
				}
			}
			else {
				hayFoto = false;
				for (int i = 0; i < arregloTitulos.length; i++) {
					if (i == 0) {
						opcionesTablaFoto.add(new PromocionesTipo(arregloTitulos[i], true));
					}
					else {
						opcionesTablaFoto.add(new PromocionesTipo(arregloTitulos[i], false));
					}
					
				}
			}
			
			if(opcionesTablaRedencion == null || opcionesTablaRedencion.size() == 0){
				opcionesTablaRedencion = new ArrayList<PromocionesTipo>();
				for (int i = 0; i < arregloTitulos2.length; i++) {
					
					Log.i("infomovil", "RedeemOffer: " + promociones.getRedeemOffer());
					Log.i("infomovil", "arregloTitulos: " + arregloTitulos2[i]);
					
					if (promociones.getRedeemOffer().equalsIgnoreCase(arregloTitulosAux[i])) {
						opcionesTablaRedencion.add(new PromocionesTipo(arregloTitulos2[i], true, arregloTitulosAux[i]));
					}
					else {
						opcionesTablaRedencion.add(new PromocionesTipo(arregloTitulos2[i], false, arregloTitulosAux[i]));
					}
				}
			}
		}
		arregloEstatus = datosUsuario.getEstatusEdicion();
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = R.string.msgGuardarPromocion;
		txtNombrePromocion.addTextChangedListener(new EditTextWithFontWatcherPromociones());
		txtDescripcionPromocion.addTextChangedListener(new EditTextWithFontWatcherPromociones());
		txtInformacionAdicional.addTextChangedListener(new EditTextWithFontWatcherPromociones());
		modifico = false;
	}
	@Override
	public void acomodaVista() {
		txtNombrePromocion.setText(promociones.getTitleOffer());
		txtDescripcionPromocion.setText(promociones.getDescOffer());
		txtInformacionAdicional.setText(promociones.getTermsOffer());
		if(!(promociones.getEndDateOffer().equals("01/01/1970"))){
			labelVigencia.setText(promociones.getEndDateOffer());
		}
		
		FotoClickListener fotoListener = new FotoClickListener();
		RedencionClickListener redencionListener = new RedencionClickListener();
		tablaFoto.setClickListener(fotoListener);
		tablaRedencion.setClickListener(redencionListener);
		llenarTablasFoto();
		llenarTablaRedencion();
		tablaFoto.commit();
		tablaRedencion.commit();
		modifico = false;
	}
	
	@Override
	public void initResume() {
		validaCamposEditados();
		Log.d("infomovilTag", "Llegando al resume de promociones");
		int indice;
		imagePath = datosUsuario.getRutaPromocion();
		if (!StringUtils.isEmpty(imagePath)) {
			if (imagePath.length() > 2) {
				hayFoto = true;
				Log.d("infoLog", "el path es ++++++++" + imagePath);
				indice = 1;
			}
			else {
				hayFoto = false;
				Log.d("infoLog", "Entrando donde no hay foto");
				indice = 0;
			}
			if (!modifico && datosUsuario.isAgregoImagenPromocion()) {
				modifico = true;
			}
			for (int i=0; i < opcionesTablaFoto.size(); i++) {
				PromocionesTipo tipo = opcionesTablaFoto.get(i);
				if (i == indice) {
					tipo.setStatus(true);
					Log.d("infoLog", "Es true indice "+indice);
				}
				else { 
					tipo.setStatus(false);
					Log.d("infoLog", "Es false indice "+indice);
				}
				opcionesTablaFoto.remove(i);
				opcionesTablaFoto.add(i, tipo);
			}
			tablaFoto.clear();
			llenarTablasFoto();
			tablaFoto.commit();
		}
		
	}
		
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
	private void llenarTablasFoto() {
		LayoutInflater mInflate = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resources res = getResources();
		for (int i = 0; i < opcionesTablaFoto.size(); i++) {
			RelativeLayout celdaPromocion = (RelativeLayout) mInflate.inflate(R.layout.celda_promocion, null);
			PromocionesTipo tipoPromocion = opcionesTablaFoto.get(i);
			((TextWithFont)celdaPromocion.findViewById(R.id.tituloDescripcion)).setText(tipoPromocion.getTitulo());
			if (tipoPromocion.isStatus()) {
				((TextWithFont)celdaPromocion.findViewById(R.id.tituloDescripcion)).setTextColor(res.getColor(R.color.colorFuenteAzul));
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackgroundDrawable(res.getDrawable(R.drawable.check));
				}
				else {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackground(res.getDrawable(R.drawable.check));
				}
				
			}
			else {
				((TextWithFont)celdaPromocion.findViewById(R.id.tituloDescripcion)).setTextColor(res.getColor(R.color.colorFuenteVerde));
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackgroundDrawable(res.getDrawable(R.drawable.circulocheck));
				}
				else {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackground(res.getDrawable(R.drawable.circulocheck));
				}
				
			}
			ViewItem viewItem = new ViewItem(celdaPromocion);
			tablaFoto.addViewItem(viewItem);
		}
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
	private void llenarTablaRedencion() {
		LayoutInflater mInflate = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resources res = getResources();
		for (int i = 0; i < opcionesTablaRedencion.size(); i++) {
			RelativeLayout celdaPromocion = (RelativeLayout) mInflate.inflate(R.layout.celda_promocion, null);
			PromocionesTipo tipoPromocion = opcionesTablaRedencion.get(i);
			((TextWithFont)celdaPromocion.findViewById(R.id.tituloDescripcion)).setText(tipoPromocion.getTitulo());
			if (tipoPromocion.isStatus()) {
				((TextWithFont)celdaPromocion.findViewById(R.id.tituloDescripcion)).setTextColor(res.getColor(R.color.colorFuenteAzul));
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackgroundDrawable(res.getDrawable(R.drawable.check));
				}
				else {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackground(res.getDrawable(R.drawable.check));
				}
				redencionOferta = tipoPromocion.getTituloAux();
				red=true;
			}
			else {
				((TextWithFont)celdaPromocion.findViewById(R.id.tituloDescripcion)).setTextColor(res.getColor(R.color.colorFuenteVerde));
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackgroundDrawable(res.getDrawable(R.drawable.circulocheck));
				}
				else {
					((ImageView) celdaPromocion.findViewById(R.id.imagenEstatus)).setBackground(res.getDrawable(R.drawable.circulocheck));
				}
				
			}
			ViewItem viewItem = new ViewItem(celdaPromocion);
			tablaRedencion.addViewItem(viewItem);
		}
	}

	@Override
	public void keyDownAction() {
		Log.d("infomovilLog", "Presionando back button");		
//		datosUsuario.setAgregoImagenPromocion(false);
//		datosUsuario.setRutaPromocion(null);
	}

	public void mostrarFecha(View v) {
		if(layoutPicker.getVisibility() == View.GONE) {
			layoutPicker.setVisibility(View.VISIBLE);
		}
		else {
			layoutPicker.setVisibility(View.GONE);
		}
	}
	
	public void aceptarFecha(View v) {
		modifico = true;
		int year = datePromocion.getYear();
		int month = datePromocion.getMonth();
		int day = datePromocion.getDayOfMonth();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String fechaString = df.format(new GregorianCalendar(year, month, day).getTime());
		labelVigencia.setText(fechaString);
		layoutPicker.setVisibility(View.GONE);
	}
	
	public void accionSi() {
		super.accionSi();
		guardarInformacion();
	}
	
	public void accionNo() {
		super.accionNo();
		Log.d("infomovilLog", "Accion no");
		if(alerta != null)
			alerta.dismiss();
		
		//datosUsuario.setRutaPromocion(null);
		
		banderaModifico = 0;
//		Intent intent = new Intent();
//		intent.putExtra("banderaModifico", banderaModifico);
//		activity.setResult(Activity.RESULT_OK, intent);
//		activity.finish();
		infomovilInterface.returnFragment("MenuCrear");
	}
	
	public void accionAceptar() {
		super.accionAceptar();
		if (actualizacionCorrecta) {
			datosUsuario.setAgregoImagenPromocion(false);
			infomovilInterface.returnFragment("MenuCrear");
		}
	}
	
	private class FotoClickListener implements ClickListener {
 
		@Override
		public void onClick(int index) {
			
			if(index == 0) {
				modifico=true;
				hayFoto = false;
			}
			else if(index == 1) {
				//datosUsuario.setAgregoImagenPromocion(false);
				GaleriaPaso2Activity galeriaPaso2 = new GaleriaPaso2Activity();
				Bundle dataBundle = new Bundle();
				dataBundle.putSerializable("galeryType", PhotoGaleryType.PhotoGaleryTypeOffer);
				dataBundle.putBoolean("editando", hayFoto);
				dataBundle.putInt("tituloVista",R.string.txtAddImage);
				dataBundle.putString("imagePath", imagePath);
				dataBundle.putBoolean("validarKeyDown", false);
				galeriaPaso2.setArguments(dataBundle);
				infomovilInterface.loadFragment(galeriaPaso2, null, "PromocionesImagen");
			}
			
			for (int i=0; i < opcionesTablaFoto.size(); i++) {
				PromocionesTipo tipo = opcionesTablaFoto.get(i);
				if (i == index) {
					tipo.setStatus(true);
				}
				else {
					tipo.setStatus(false);
				}
				opcionesTablaFoto.set(i, tipo);
			}
			tablaFoto.clear();
			llenarTablasFoto();
			tablaFoto.commit();
		}
		
	}
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			int indice;
			imagePath = data.getExtras().getString("pathImage", "");
			if (imagePath.length() > 2) {
				hayFoto = true;
				Log.d("infoLog", "el path es ++++++++" + imagePath);
				indice = 1;
			}
			else {
				hayFoto = false;
				Log.d("infoLog", "Entrando donde no hay foto");
				indice = 0;
			}
			for (int i=0; i < opcionesTablaFoto.size(); i++) {
				PromocionesTipo tipo = opcionesTablaFoto.get(i);
				if (i == indice) {
					tipo.setStatus(true);
					Log.d("infoLog", "Es true indice "+indice);
				}
				else { 
					tipo.setStatus(false);
					Log.d("infoLog", "Es false indice "+indice);
				}
				opcionesTablaFoto.remove(i);
				opcionesTablaFoto.add(i, tipo);
			}
			tablaFoto.clear();
			llenarTablasFoto();
			tablaFoto.commit();
		}
	}
	 
	private class RedencionClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			modifico = true; 
			red=true;
			if(index == 0) {
				redencionOferta = "No especificado";
			}
			else if(index == 1) {
				redencionOferta = "Llámanos";
			}
			else if (index == 2) {
				redencionOferta = "Envíanos un email";
			}
			else {
				redencionOferta = "Visítanos";
			}
			
			for (int i=0; i < opcionesTablaRedencion.size(); i++) {
				PromocionesTipo tipo = opcionesTablaRedencion.get(i);
				if (i == index) {
					tipo.setStatus(true);
				}
				else {
					tipo.setStatus(false);
				}
				opcionesTablaRedencion.set(i,tipo);
			}
			
			tablaRedencion.clear();
			llenarTablaRedencion();
			tablaRedencion.commit();
		}
	}
	
	public void seleccionarImagen(View v) {
		int opcionSeleccionada = Integer.parseInt(v.getTag().toString());
		
		if(opcionSeleccionada == 1) {
			Log.d("infoLog", "Selecciono Sin Foto");
			modifico = true;
		}
		else if(opcionSeleccionada == 2) {
			Log.d("infoLog", "Selecciono Tomar una Foto");
			boolean cameraAvailable = ToolBox.device_isHardwareFeatureAvailable(activity, PackageManager.FEATURE_CAMERA);
			if(cameraAvailable && ToolBox.system_isIntentAvailable(activity, MediaStore.ACTION_IMAGE_CAPTURE)){

		        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		        //We prepare the intent to store the taken picture
		        try{
		            File outputDir = ToolBox.storage_getExternalPublicFolder(Environment.DIRECTORY_PICTURES, "testApp", true);
		            outFile = ToolBox.storage_createUniqueFileName("cameraPic", ".jpg", outputDir);

		            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));             
		            storeImage = true;
		        }catch(Exception e){
		            ToolBox.dialog_showToastAlert(activity, "Error setting output destination.", false);
		        }
			startActivityForResult(takePicIntent, InfomovilConstants.REQUEST_CAMERA);
		}
		}
		else {
			Log.d("infoLog", "Selecciono Foto Existente");
			Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
			intent.setType("image/*"); 
			startActivityForResult( Intent.createChooser(intent, "Select File"), InfomovilConstants.REQUEST_OTHER);
		}
	}
	
	public void seleccionarRedimir(View v) {
		int opcionSeleccionada = Integer.parseInt(v.getTag().toString());
		modifico = true;
		red=true;
		if(opcionSeleccionada == 1) {
			redencionOferta = "No especificado";
		}
		else if(opcionSeleccionada == 2) {
			redencionOferta = "Llámanos";
		}
		else if (opcionSeleccionada == 3) {
			redencionOferta = "Envíanos un email";
		}
		else {
			redencionOferta = "Visítanos";
		}
	}
	
	@Override
	public void guardarInformacion() {				
		promociones = new WS_OffertRecordVO();
		datosUsuario = DatosUsuario.getInstance();
		if (InfomovilApp.isConnected(activity)) {
			promociones.setTitleOffer(txtNombrePromocion.getText().toString());
			promociones.setDescOffer(txtDescripcionPromocion.getText().toString());
			promociones.setTermsOffer(txtInformacionAdicional.getText().toString());
			promociones.setRedeemOffer(redencionOferta);
			if (hayFoto) {
				if (promociones.getImageClobOffer().length() < 2 && !StringUtils.isEmpty(imagePath)) {
					File f = new File(imagePath);
					try {
						promociones.setImageClobOffer(Base64.encodeToString(read(f),Base64.DEFAULT)) ;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ( datosUsuario.getPromocionDominio().getImageClobOffer().length() > 10 )
					promociones.setImageClobOffer(datosUsuario.getPromocionDominio().getImageClobOffer());
			}
			else{
				promociones.setImageClobOffer("");
				promociones.setPathImageOffer("");
				datosUsuario.setRutaPromocion("");
				datosUsuario.setAgregoImagenPromocion(false);
			}
			
			promociones.setEndDateOffer(labelVigencia.getText().toString());

			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
			wsCall.setOfferRecord(promociones);
			wsCall.setActividad("Edito Promocion");
			wsCall.execute(WSInfomovilMethods.UPDATEINSERT_OFFER_RECORD);
		}else {
			alerta = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtNoConexion));
			alerta.setDelegado(this);
			alerta.show();
		}
	}
	
	public void borrarDatosOk() {
		
		WS_DeleteItem deleteItem = new WS_DeleteItem(datosUsuario.getDomainid(), promociones.getIdOffer());
		WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
		wsCall.setDeleteItem(deleteItem);
		wsCall.setActividad("Borro Promocion");
		wsCall.execute(WSInfomovilMethods.DELETE_OFFER_RECORD);
	}	
	
	public void checkBotonEliminar(){		
		if(!StringUtils.isEmpty(txtNombrePromocion.getText()) ||
		  !StringUtils.isEmpty(txtDescripcionPromocion.getText()) ||
		  !StringUtils.isEmpty(txtInformacionAdicional.getText()) ){
			
			botonBorrar.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		if(alerta != null)
			alerta.dismiss();
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			modifico = false;
			banderaModifico = 1;
			actualizacionCorrecta = true;
			
			if(metodo == WSInfomovilMethods.UPDATEINSERT_OFFER_RECORD){
				arregloEstatus[indiceSeleccionado] = true;
			}
			else{
				datosUsuario.setRutaPromocion(null);
				datosUsuario.setAgregoImagenPromocion(false);
				arregloEstatus[indiceSeleccionado] = false;
			}
			
			datosUsuario.setEstatusEdicion(arregloEstatus);
			AlertView alertBien = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(activity,  AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorActualizacion));
			alertError.setDelegado(this);
			alertError.show();
		}
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	private class EditTextWithFontWatcherPromociones implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			modifico = true;
			banderaModifico = 1;
		}
		
	}
	
	public byte[] read(File file) throws IOException
	{
	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ( (read = ios.read(buffer)) != -1 ) {
	            ous.write(buffer, 0, read);
	        }
	    } finally { 
	        try {
	             if ( ous != null ) 
	                 ous.close();
	        } catch ( IOException e) {
	        }

	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        }
	    }
	    return ous.toByteArray();
	}
	

	private void validaCamposEditados() {
		// TODO Auto-generated method stub
		if (promociones != null) {
			if (promociones.getTitleOffer().equals(txtNombrePromocion.getText().toString()) && 
				promociones.getDescOffer().equals(txtDescripcionPromocion.getText().toString()) && 
				promociones.getTermsOffer().equals(txtInformacionAdicional.getText().toString())) {
				modifico = false;
			}
		}
		else {
			modifico = false;
		}
		
		
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
		
		Resources res = getResources();
		
		String mensaje = "Correcto";
		
		if(txtNombrePromocion.getText().toString().trim().isEmpty()){			
			mensaje = res.getString(R.string.mensajeNotitle);
			return mensaje;
		}
		if(txtDescripcionPromocion.getText().toString().trim().isEmpty()){
			mensaje = res.getString(R.string.mensajeNodesc);
			return mensaje;
		}
		
		if(labelVigencia.getText().toString().isEmpty()){
			mensaje = res.getString(R.string.mensajeNoVigen);
			return mensaje;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		LocalDate fechaVigencia  = null;
		
		try {
			fechaVigencia = new LocalDate(sdf.parse(labelVigencia.getText().toString().trim()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fechaVigencia.isBefore(new LocalDate())){
			mensaje = res.getString(R.string.mensajeVigenFechaMenorActual);
			return mensaje;
		}		
		
		if(red==false){			
			mensaje = res.getString(R.string.mensajeNoRem);
			return mensaje;
		}		
		
		return "Correcto";
	}
	
}

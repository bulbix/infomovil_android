package com.infomovil.infomovil.gui.fragment.editar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.map.PlaceProvider;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_LocalizacionVO;

public class MapaUbicacionActivity extends InfomovilActivity implements ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener, OnMyLocationButtonClickListener,
LoaderCallbacks<Cursor>{

	private GoogleApiClient googleApiClient;
	private GoogleMap mMap;    

	//****Pueden ir en la clase padre***/
	int banderaModifico;
	boolean arregloEstatus[], actualizacionCorrecta;
	//////FIN

	/***Solo muestra una vez el cuadro de dialogo***/
	boolean mostrarCambiarUbicacion = true;

	WS_LocalizacionVO localizacionVO;		

	Marker markerSelect;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);    

	/********EVENTOS ACTIVITY******/
	@Override
	public void initCreate() {      
		cargarLayout(R.layout.mapa_ubicacion_layout);
		acomodarVistaConTitulo(R.string.txtMapaTitutlo, R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		mostrarMenuMapa();
		arregloEstatus = datosUsuario.getEstatusEdicion();		
		mensajeActualizacion  = R.string.msgGuardarMapa;
		
		 ImageView image = new ImageView(this);
	        image.setImageResource(R.drawable.mapa2);

	        AlertDialog.Builder builder = 
	                new AlertDialog.Builder(this).
	                setMessage(getString(R.string.txtInitMapa)).
	                setPositiveButton(R.string.txtAceptar, new OnClickListener() {                     
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                            dialog.dismiss();
	                    }
	                }).
	                setView(image);
	        builder.create().show();		

	}    

	@Override
	public void initResume() {
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		//getPosicionGuardada();
		mostrarCambiarUbicacion = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (googleApiClient != null) {
			googleApiClient.disconnect();
		}
	}  

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (modifico == true) {
				alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion,
						getResources().getString(R.string.txtPreguntaSalir));
				alerta.setDelegado(this);
				alerta.show();
				return false;
			} 
			else {
				Intent intent = new Intent();
				intent.putExtra("banderaModifico", banderaModifico);
				setResult(Activity.RESULT_OK, intent);
			}

		}
		return super.onKeyDown(keyCode, event);
	}


	/************FIN EVENTOS ACTIVITY*********/ 


	/*****CONFIGURACION*****/ 
	private void setUpMapIfNeeded() {     
		if (mMap == null) {     
			mMap = ((MapFragment) getFragmentManager().
					findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {	
		mMap.setMyLocationEnabled(false);
		mMap.setOnMyLocationButtonClickListener(this);            	
	}

	private void setUpLocationClientIfNeeded() {
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();

			googleApiClient.connect();
		}
	}
	/******FIN CONFIGURACION********/

	@Override
	public void guardarInformacion(){

		datosUsuario = DatosUsuario.getInstance();
		datosUsuario.setCoordenadasUbicacion(localizacionVO);

		WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
		wsCall.setActividad("Edito Mapa");
		wsCall.setUbicacion(localizacionVO);
		wsCall.execute(WSInfomovilMethods.UPDATE_INSERT_LOC_RECORD);
	}


	//	@Override
	//	public void guardarDatos(View v) {
	//		super.guardarDatos(v);
	//		guardarMapa();
	//	}
	//	
	//	public void accionSi(){
	//		dialog.dismiss();
	//		guardarMapa();		
	//	}

	public void accionNo() {
		alerta.dismiss();
		banderaModifico = 0;
		Intent intent = new Intent();
		intent.putExtra("banderaModifico", banderaModifico);
		setResult(Activity.RESULT_OK, intent);
		this.finish();		
	}

	public void accionAceptar() {		
		super.accionAceptar();
		if (actualizacionCorrecta) {
			banderaModifico = 1;
			Intent intent = new Intent();
			intent.putExtra("banderaModifico", banderaModifico);
			setResult(Activity.RESULT_OK, intent);
			this.finish();
		}		
	}

	public void buscarPosicion(View v){
		onSearchRequested();
	}

	public void buscarPosicionActual(View v){

		class AlertCambiarPosicion implements AlertViewInterface{

			@Override
			public void accionSi() {

				LatLng center = mMap.getCameraPosition().target;

				//Coordenadas seleccionadas
				localizacionVO = new WS_LocalizacionVO();
				localizacionVO.setLatitudeLoc(center.latitude);			
				localizacionVO.setLongitudeLoc(center.longitude);
				modifico = true;

				if(markerSelect != null){
					markerSelect.remove();
				}

				markerSelect = mMap.addMarker(new MarkerOptions().position(
						new LatLng(localizacionVO.getLatitudeLoc(),localizacionVO.getLongitudeLoc())));

				mostrarCambiarUbicacion = false;

//				if(alerta != null){
//					alerta.dismiss();
//				}

			}

			@Override
			public void accionNo() {
//				if(alerta != null){
//					alerta.dismiss();
//				}
			}

			@Override
			public void accionAceptar() {
				// TODO Auto-generated method stub				
			}

		}

		AlertCambiarPosicion alertCambiarPosicion = new AlertCambiarPosicion();

		alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion2,		
				getResources().getString(R.string.txtInfoCambiarUbicacion));

		alerta.setDelegado(alertCambiarPosicion);

		if(mostrarCambiarUbicacion && tieneMapa()){
			alerta.show();
		}
		else{
			alertCambiarPosicion.accionSi();
		}

	}

	private boolean tieneMapa(){
		return 	localizacionVO !=null &&
				localizacionVO.getLatitudeLoc() != 0.0 && 
				localizacionVO.getLongitudeLoc() != 0.0;
	}


	public void eliminarPosicion(View v){		

		if(tieneMapa()){
			final AlertView alertaEliminar = new AlertView(this, AlertViewType.AlertViewTypeQuestion,		
					getResources().getString(R.string.txtPreguntarEliminarUbicacion));
			

			alertaEliminar.setDelegado(new AlertViewInterface() {

				@Override
				public void accionSi() {
					
					alerta = new AlertView(MapaUbicacionActivity.this, AlertViewType.AlertViewTypeActivity, 
					getResources().getString(R.string.msgGuardarMapa));
					alerta.setDelegado(this);
					alerta.show();

					if(markerSelect != null){
						markerSelect.remove();
					}

					datosUsuario = DatosUsuario.getInstance();
					datosUsuario.setCoordenadasUbicacion(null);    	
					//arregloEstatus[indiceSeleccionado] = false;
					//datosUsuario.setEstatusEdicion(arregloEstatus);
					modifico = false;


					WsInfomovilCall wsCall = new WsInfomovilCall(MapaUbicacionActivity.this,MapaUbicacionActivity.this);
					wsCall.setUbicacion(localizacionVO);
					wsCall.setActividad("Borro Mapa");
					wsCall.execute(WSInfomovilMethods.DELETE_LOC_RECORD);

					alertaEliminar.dismiss();

				}

				@Override
				public void accionNo() {
					alertaEliminar.dismiss();				
				}

				@Override
				public void accionAceptar() {
					// TODO Auto-generated method stub				
				}
			});

			alertaEliminar.show();			
		}		

	}

	private void getPosicionGuardada(){
		datosUsuario = DatosUsuario.getInstance();
		localizacionVO = datosUsuario.getCoordenadasUbicacion();
		posicionActual(localizacionVO);		
	}

	private void posicionActual(WS_LocalizacionVO localizacionVO) {

		if (googleApiClient != null && googleApiClient.isConnected()) {

			if(localizacionVO == null || (localizacionVO.getLatitudeLoc() == 0.0 && localizacionVO.getLongitudeLoc() == 0.0) ){
				//Posicion Actual
				localizacionVO = new WS_LocalizacionVO();

				Location location =  LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

				if( location != null){
					localizacionVO.setLatitudeLoc(location.getLatitude());
					localizacionVO.setLongitudeLoc(location.getLongitude());
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(localizacionVO.getLatitudeLoc(),
							localizacionVO.getLongitudeLoc()), 15));					
				}
				else{

					final MapaUbicacionActivity mapaActivity = this;

					final AlertView dialog = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
							getResources().getString(R.string.msgActivarGPS));
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
							mapaActivity.finish();

						}
					});
					dialog.show();
				}

			}
			else{
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(localizacionVO.getLatitudeLoc(),
						localizacionVO.getLongitudeLoc()), 15));

				String title = "";

				/*try{
					Geocoder geocoder = new Geocoder(this, Locale.getDefault());
					List<Address>  addressIn  = geocoder.
					getFromLocation(localizacionVO.getLatitudeLoc(), localizacionVO.getLongitudeLoc(), 1);
					Address address = addressIn.get(0);					
					title += address.getAddressLine(0) + ", ";
					title += address.getAddressLine(1) + ", ";
					title += address.getAddressLine(2);
				}
				catch(Exception e){	
					title = "";
				}*/			

				markerSelect = mMap.addMarker(new MarkerOptions().title(title).position(
						new LatLng(localizacionVO.getLatitudeLoc(),localizacionVO.getLongitudeLoc())));

			}
		}  
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {   	
		//Log.d("Ha cambiado de localizacion " + location);
	}

	/**
	 * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, REQUEST, this);
		getPosicionGuardada();
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	//##########AQUI INICIA BUSQUEDA PARA LOS SITIOS##################################3///

	private void handleIntent(Intent intent){

		if(intent.getAction().equals(Intent.ACTION_SEARCH)){
			doSearch(intent.getStringExtra(SearchManager.QUERY));
		}else if(intent.getAction().equals(Intent.ACTION_VIEW)){
			getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {		
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	private void doSearch(String query){		
		Bundle data = new Bundle();
		data.putString("query", query);		
		getLoaderManager().restartLoader(0, data, this);
	}

	private void getPlace(String query){		
		Bundle data = new Bundle();
		data.putString("query", query);		
		getLoaderManager().restartLoader(1, data, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
		CursorLoader cLoader = null;
		if(arg0==0)
			cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
		else if(arg0==1)
			cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		showLocations(c);		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	private void showLocations(Cursor c){

		MarkerOptions markerOptions = null;
		LatLng position = null;
		mMap.clear();
		while(c.moveToNext()){
			markerOptions = new MarkerOptions();
			position = new LatLng(Double.parseDouble(c.getString(1)),Double.parseDouble(c.getString(2)));

			//Coordenadas seleccionadas
			localizacionVO = new WS_LocalizacionVO();
			localizacionVO.setLatitudeLoc(position.latitude);			
			localizacionVO.setLongitudeLoc(position.longitude);
			modifico = true;

			markerOptions.position(position);
			markerOptions.title(c.getString(0));
			markerSelect =  mMap.addMarker(markerOptions);
		}
		if(position!=null){
			CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(position, 15);

			mMap.animateCamera(cameraPosition);			
		}
	}

	/**************METODOS ESTATUS****************/

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {

		if(alerta != null)
			alerta.dismiss();

		if (status == WsInfomovilProcessStatus.EXITO) {
			modifico = false;
			actualizacionCorrecta = true;

//			if(metodo == WSInfomovilMethods.DELETE_LOC_RECORD){
//				arregloEstatus[indiceSeleccionado] = false;
//			}	
//			else if(metodo == WSInfomovilMethods.UPDATE_INSERT_LOC_RECORD){
//				arregloEstatus[indiceSeleccionado] = true;
//			}
//
//			datosUsuario.setEstatusEdicion(arregloEstatus);		

			AlertView alertBien = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(this,  AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorActualizacion));
			alertError.setDelegado(this);
			alertError.show();
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
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub

	}
	
}

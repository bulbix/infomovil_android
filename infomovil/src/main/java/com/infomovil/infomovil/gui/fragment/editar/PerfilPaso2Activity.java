package com.infomovil.infomovil.gui.fragment.editar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.ViewItem;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.gui.widget.TableView;
import com.infomovil.infomovil.gui.widget.TableView.ClickListener;
import com.infomovil.infomovil.gui.widget.TableView.TableType;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.HorariosModel;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DeleteItem;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;

public class PerfilPaso2Activity extends InfomovilFragment  {

	int opcionSeleccionada;
	int indiceArreglo;
	boolean existeKey;

	private RelativeLayout layoutHorario, layoutDatosPerfil;
	WS_KeywordVO keySeleccionado;
	ArrayList<HorariosModel> listaHorarios;
	Vector<WS_KeywordVO> listaPerfil;
	private TableView tablaHorarios;
	private RelativeLayout layoutPicker;
	private EditTextWithFont textoDescripcion;
	private TextWithFont labelTituloSeccion;
	private Button botonBorrar;
	String horaInicio, horaCierre;
	int indiceHorario;
	Resources res;
	String arregloTitulos[];
	boolean actualizacionCorrecta;
	String arrayKeys[] = {"pos", "sas", "oh", "pm", "tpa", "mnu", "np"};
	String arrayDescripcion[] = {"(Perfil)Productos|Servicios", "(Perfil)Areas de Servicio", "(Perfil)Horario", "(Perfil)Medios de Pago", "(Perfil)Asociaciones", "(Perfil)Biografia","(Perfil)Negocio|Profesion"};
	View view;
	private String ventanaProcedencia = "";

	// Time scrolled flag
	private boolean timeScrolled = false;

	private final int HORARIO_SELECCIONADO = 2;
	private final int NEGOCIO_PROMOCION = 6;
	private final int PRODUCTOS_SERVICIOS = 0;
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.perfil_paso2_layout, container, false);
		botonBorrar = (Button) view.findViewById(R.id.botonBorrarPerfil);

		final PerfilPaso2Activity perfilPaso2 = this;
		
		Button btnAceptarHorario = (Button) view.findViewById(R.id.btnAceptarHorario);
		btnAceptarHorario.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				perfilPaso2.aceptarHorario(v);		
			}
		});	
		
		textoDescripcion = (EditTextWithFont) view.findViewById(R.id.txtDatosSeccion);
		labelTituloSeccion = (TextWithFont) view.findViewById(R.id.labelTituloSeccion);
		layoutHorario = (RelativeLayout) view.findViewById(R.id.layoutDatosHorario);
		layoutDatosPerfil = (RelativeLayout) view.findViewById(R.id.layoutDatosPerfil);
		layoutPicker = (RelativeLayout) view.findViewById(R.id.layoutPicker);
		tablaHorarios = (TableView) view.findViewById(R.id.tablaHorarios);
		return view;
	}

	@Override
	public void initCreate() {		
		res = getResources();
		String arrayTitAux[]  = {res.getString(R.string.describeProductos), res.getString(R.string.areaGeografica), res.getString(R.string.horario), res.getString(R.string.formasPago), res.getString(R.string.miembroAsociacion), res.getString(R.string.queMasQuiere), res.getString(R.string.negocioProfesion)};
		arregloTitulos = arrayTitAux;
		acomodarVistaConTitulo(getArguments().getInt("tituloVistaPerfil"), R.drawable.plecaverde);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		//Intent intent = activity.getIntent();
		opcionSeleccionada = getArguments().getInt("opcionSeleccionada", 0);
		indiceSeleccionado = getArguments().getInt("indiceSeleccionado", 0);
		mensajeActualizacion = getArguments().getInt("mensaje");		
		ventanaProcedencia  = getArguments().getString("ventana", "InicioRapido");
		
		tablaHorarios.setTableType(TableType.TableTypeClassic);
		
		if (datosUsuario.getArregloPerfil() == null || datosUsuario.getArregloPerfil().size() == 0) {
			Vector<WS_KeywordVO> listPerfil = new Vector<WS_KeywordVO>();
			for (int i = 0; i < 7; i++) {
				listPerfil.add(new WS_KeywordVO());
			}
			datosUsuario.setArregloPerfil(listPerfil);
		}
		

		keySeleccionado = new WS_KeywordVO(obtenerKeySeleccionada());	

		if(opcionSeleccionada != HORARIO_SELECCIONADO) {
			//keySeleccionado = datosUsuario.getArregloPerfil().get(indiceSeleccionado);
			textoDescripcion.setText(keySeleccionado.getKeywordValue());
			textoDescripcion.addTextChangedListener(new EditTextWithFontWatcher());
			if (keySeleccionado.getKeywordValue().length() > 0) {
				botonBorrar.setEnabled(true);
			}
			else {
				botonBorrar.setEnabled(false);
			}
		}
		labelTituloSeccion.setText(arregloTitulos[opcionSeleccionada]);

		modifico = false;
		acomodarVista(); 
	}

	private WS_KeywordVO obtenerKeySeleccionada() {		
		datosUsuario = DatosUsuario.getInstance();
		String llave = arrayKeys[opcionSeleccionada];

		WS_KeywordVO keyAux = new WS_KeywordVO();		
		Log.d("infomovilLog", "Numero de elementos: " + datosUsuario.getArregloPerfil().size());

		int index = 0;

		for(WS_KeywordVO keywordVO:datosUsuario.getArregloPerfil()){			
			Log.d("infomovilLog", keywordVO.toString());			
			if (keywordVO.getKeywordField() != null && keywordVO.getKeywordField().equals(llave)) {
				//indiceArreglo = index;
				
				if(keywordVO.getIdKeyword() != 0){
					existeKey = true;
				}
				
				keyAux = keywordVO;				
				//break;
			}

			index++;
		}


		return keyAux;
	}

	private void acomodarVista() {
		if(opcionSeleccionada == HORARIO_SELECCIONADO) {

			layoutHorario.setVisibility(View.VISIBLE);
			layoutDatosPerfil.setVisibility(View.GONE);
			datosUsuario = DatosUsuario.getInstance();

			//keySeleccionado = datosUsuario.getArregloPerfil().get(indiceSeleccionado);

			listaHorarios = new ArrayList<HorariosModel>();

			Log.d("Infomovil key antes de acomodar",keySeleccionado.toString());

			if(keySeleccionado.getKeywordValue() == null || keySeleccionado.getKeywordValue().length() < 4) {
				
				String arregloDias[] = {getResources().getString(R.string.horarioLun),
										getResources().getString(R.string.horarioMar),
										getResources().getString(R.string.horarioMie),
										getResources().getString(R.string.horarioJue),
										getResources().getString(R.string.horarioVie),
										getResources().getString(R.string.horarioSab),
										getResources().getString(R.string.horarioDom)};
				
				for (int i = 0; i < arregloDias.length; i++) {
					listaHorarios.add(new HorariosModel(arregloDias[i], "00:00", "00:00"));
				}
			}
			else{
				String[] cadenas = keySeleccionado.getKeywordValue().split("\\|");
				Integer index = 1;
				String dia = "";

				for(String cadena:cadenas){

					if(cadena.isEmpty()){
						continue;
					}				

					if(index % 2 == 0){//Horario
						String[] horarios = cadena.split("-");
						Log.d("horarios", cadena);

						listaHorarios.add(new HorariosModel(dia, horarios[0].trim(),
								horarios[1].trim()));
					}
					else{//Dia
						
						dia = cadena.trim();
						
						String lang = Locale.getDefault().getLanguage();
						
						if(lang.equals("en")){
							dia = dia.replace("Lun","Mon").replace("Mar","Thu")
									.replace(res.getString(R.string.horarioMieEsp),"Wen")
									.replace(res.getString(R.string.horarioMie2Esp),"Wen")
									.replace(res.getString(R.string.horarioMie3Esp),"Wen")
									.replace("Jue","Tue")
									.replace("Vie","Fri")
									.replace(res.getString(R.string.horarioSabEsp),"Sat")
									.replace(res.getString(R.string.horarioSab2Esp),"Sat")
									.replace("Dom","Sun");
						}
						else if(lang.equals("es")){
							dia = dia.replace("Mon","Lun").replace("Thu","Mar")
									.replace("Wen",res.getString(R.string.horarioMie)).replace("Tue","Jue")
									.replace("Fri","Vie").replace("Sat",res.getString(R.string.horarioSab))
									.replace("Sun","Dom");
						}
						
					}				
					index++;
				}

			}

			llenarTabla();
			tablaHorarios.commit();

			acomodarPicker();

		}
		else {
			layoutHorario.setVisibility(View.GONE);
			layoutDatosPerfil.setVisibility(View.VISIBLE);
		}
	}

	private void llenarTabla() {
		CustomClickListener listener = new CustomClickListener();
		tablaHorarios.setClickListener(listener);
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < listaHorarios.size(); i++) {
			HorariosModel horario = listaHorarios.get(i);
			RelativeLayout celdaHorario = (RelativeLayout) inflater.inflate(R.layout.celda_horario_layout, null);
			((TextWithFont) celdaHorario.findViewById(R.id.labelDiaHorario)).setText(horario.getDia());
			((TextWithFont) celdaHorario.findViewById(R.id.labelHorasHorario)).setText(horario.getInicio() + " - " + horario.getCierre());
			if (!horario.getInicio().equals("00:00") || !horario.getCierre().equals("00:00")) {
				((TextWithFont) celdaHorario.findViewById(R.id.labelHorasHorario)).setTextColor(getResources().getColor(R.color.colorFuenteAzul));
			}
			ViewItem itemView = new ViewItem(celdaHorario);
			tablaHorarios.addViewItem(itemView);
		}
	}

	private void acomodarPicker() {
		final WheelView wheelInicio = (WheelView) view.findViewById(R.id.idHoraInicio);
		wheelInicio.setViewAdapter(new NumericWheelAdapter(activity));
		final WheelView wheelCierre = (WheelView) view.findViewById(R.id.idHoraCierre);
		wheelCierre.setViewAdapter(new NumericWheelAdapter(activity));

		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					horaInicio = wheelInicio.getItemAtIndex(newValue);
					horaCierre = wheelCierre.getItemAtIndex(newValue);
					
				}
			}
		};

		wheelInicio.addChangingListener(wheelListener);
		wheelCierre.addChangingListener(wheelListener);

		OnWheelClickedListener click = new OnWheelClickedListener() {
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		};
		wheelInicio.addClickingListener(click);
		wheelCierre.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				horaInicio = wheelInicio.getItemAtIndex(wheelInicio.getCurrentItem());
				horaCierre = wheelCierre.getItemAtIndex(wheelCierre.getCurrentItem());
			}
		};

		wheelInicio.addScrollingListener(scrollListener);
		wheelCierre.addScrollingListener(scrollListener);
	}
	
	@Override
	public void guardarInformacion(){
		Log.d("infoLog", "Indice Seleccionado " + opcionSeleccionada);
		Log.d("infoLog", "Modifico " + modifico);
		Log.d("infoLog", "ExisteKey " + existeKey);	
		Log.d("infoLog", "IndiceArreglo " + indiceArreglo);

		if (modifico) {

			String value = "";

			if (opcionSeleccionada == HORARIO_SELECCIONADO) {

				StringBuilder stringHorarios = new StringBuilder("|");

				for (int i = 0; i < listaHorarios.size(); i++) {
					HorariosModel horario = listaHorarios.get(i);
					stringHorarios.append(horario.getDia()+"|");
					stringHorarios.append(horario.getInicio() + " - " +
							horario.getCierre() + "|");
				}	

				value = stringHorarios.toString();
				Log.d("infoLog", "el valor del horario es "+value);
			}
			else{
				value = textoDescripcion.getText().toString();
			}

			Log.d("infomovilLog", "Descripcion " + value);

			keySeleccionado.setKeywordValue(value);
			keySeleccionado.setKeywordField(arrayKeys[opcionSeleccionada]);
			datosUsuario = DatosUsuario.getInstance();
			
			modifico = false;

			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
			wsCall.setActividad("Edito " + arrayDescripcion[opcionSeleccionada]);					

			Vector<WS_KeywordVO> elementoModificar = new Vector<WS_KeywordVO>(1);
			elementoModificar.add(keySeleccionado);

			wsCall.setLstKeyword(elementoModificar);

			if(existeKey){
				wsCall.execute(WSInfomovilMethods.UPDATE_KEY_WORD_DATA);
			}
			else{
				wsCall.execute(WSInfomovilMethods.INSERT_KEY_WORD_DATA);
			}
			
			
		}
		else {
			String textoMostrar;
			if (opcionSeleccionada == HORARIO_SELECCIONADO) {
				textoMostrar = getResources().getString(R.string.txtMensajeErrorHorario);
			}
			else {
				textoMostrar = getResources().getString(R.string.txtMensajeErrorPerfil);
			}
			final AlertView alertModif = new AlertView(activity, AlertViewType.AlertViewTypeInfo, textoMostrar);
			alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
			alertModif.show();
		}

	}

	public void accionNo() {
		super.accionNo();
		
		if(alerta != null )
			alerta.dismiss();
		
		switch (opcionSeleccionada) {
			case NEGOCIO_PROMOCION:
				infomovilInterface.returnFragment("MenuCrear");
				break;
			case PRODUCTOS_SERVICIOS:
				if(ventanaProcedencia!=null && ventanaProcedencia.equals("InicioRapido")){
					infomovilInterface.returnFragment("MenuCrear");
				}
				else{
					infomovilInterface.returnFragment("PerfilPaso1");
				}
				
				break;
			default:
				infomovilInterface.returnFragment("PerfilPaso1");
				break;
		}
		
		
	}

	public void accionAceptar() {		
		super.accionAceptar();
		if (actualizacionCorrecta) {
			//banderaModifico = 1;
			Intent intent = new Intent();
			
			if(Arrays.equals(datosUsuario.getEstatusPerfil(),
					new boolean[]{false, false, false, false, false, false})){
				intent.putExtra("banderaModifico", 0);
			}
			else{
				intent.putExtra("banderaModifico", 1);
			}		
			
			//activity.setResult(Activity.RESULT_OK, intent);
			
			switch(opcionSeleccionada){
				case NEGOCIO_PROMOCION:
					infomovilInterface.returnFragment("MenuCrear");
					break;
				case PRODUCTOS_SERVICIOS:
					if(ventanaProcedencia!=null &&  ventanaProcedencia.equals("InicioRapido")){
						infomovilInterface.returnFragment("MenuCrear");
					}
					else{
						infomovilInterface.returnFragment("PerfilPaso1");
					}
					break;
				default:
					infomovilInterface.returnFragment("PerfilPaso1");
					break;
			}
			
						
		}		
	}
	
	
	public void borrarDatosOk(){
		
		if(opcionSeleccionada == HORARIO_SELECCIONADO){
			
			StringBuilder stringHorarios = new StringBuilder("|");
			
			for (int i = 0; i < listaHorarios.size(); i++) {
				HorariosModel horario = listaHorarios.get(i);
				horario.setInicio("00:00");
				horario.setCierre("00:00");
				listaHorarios.set(i, horario);
				stringHorarios.append(horario.getDia());
				stringHorarios.append("|00:00 - 00:00|");
			}	
			
			Log.d("horario Vacio", stringHorarios.toString());
			
			keySeleccionado.setKeywordValue(stringHorarios.toString());
			keySeleccionado.setKeywordField(arrayKeys[opcionSeleccionada]);
			
			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());	
			wsCall.setActividad("Borro " + arrayDescripcion[opcionSeleccionada]);
			Vector<WS_KeywordVO> elementoModificar = new Vector<WS_KeywordVO>(1);
			elementoModificar.add(keySeleccionado);			
			wsCall.setLstKeyword(elementoModificar);
			
			wsCall.execute(WSInfomovilMethods.UPDATE_KEY_WORD_DATA);
			
			
		}
		else{
			
			WS_DeleteItem deleteItem = new WS_DeleteItem(datosUsuario.getDomainid(), 
					keySeleccionado.getIdKeyword());
			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
			wsCall.setDeleteItem(deleteItem);
			wsCall.setActividad("Borro " + arrayDescripcion[opcionSeleccionada]);
			wsCall.execute(WSInfomovilMethods.DELETE_KEY_WORD_DATA);
		}	
	}
	
	
	public void checkBotonEliminar(){		
		Button botonBorrar;
		
		if(opcionSeleccionada == HORARIO_SELECCIONADO){
			botonBorrar = (Button) view.findViewById(R.id.botonBorrarHorario);
			if(!horariosEnCero()){
				botonBorrar.setVisibility(View.VISIBLE);
			}
			
		}
		else{
			
			botonBorrar = (Button) view.findViewById(R.id.botonBorrarPerfil);			
			if(!StringUtils.isEmpty(textoDescripcion.getText())){
				botonBorrar.setVisibility(View.VISIBLE);
			}			
		}	
			
	}
	
	

	public void aceptarHorario(View v) {
		
		if(!timeScrolled){
			modifico = true;
			HorariosModel hModel = listaHorarios.get(indiceHorario);
			
			if(horaInicio == null){
				horaInicio="00:00";
			}
			
			if(horaCierre == null){
				horaCierre="00:00";
			}
			
			hModel.setCierre(horaCierre);
			hModel.setInicio(horaInicio);
			listaHorarios.set(indiceHorario, hModel);
			tablaHorarios.clear();
			llenarTabla();
			layoutPicker.setVisibility(View.GONE);
			tablaHorarios.commit();
		}
	}

	public void mostrarOcultarPicker() {
		layoutPicker.setVisibility(View.VISIBLE);
	}	

	private class CustomClickListener implements ClickListener {
		@Override
		public void onClick(int index) {
			indiceHorario = index;
			mostrarOcultarPicker();
		}

	}	

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {

		alerta.dismiss();

		if (status == WsInfomovilProcessStatus.EXITO) {
			modifico = false;
			actualizacionCorrecta = true;

			if(metodo == WSInfomovilMethods.DELETE_KEY_WORD_DATA){				
				
				if(opcionSeleccionada == NEGOCIO_PROMOCION){
					(datosUsuario.getEstatusEdicion())[indiceSeleccionado] = false;
				}
				else{
					(datosUsuario.getEstatusPerfil())[opcionSeleccionada] = false;
				}			
				
			}	
			else{
				
				if(opcionSeleccionada == NEGOCIO_PROMOCION){
					(datosUsuario.getEstatusEdicion())[indiceSeleccionado] = true;
				}
				else if (opcionSeleccionada == HORARIO_SELECCIONADO &&  horariosEnCero()){
					(datosUsuario.getEstatusPerfil())[opcionSeleccionada] = false;
				}
				else{
					(datosUsuario.getEstatusPerfil())[opcionSeleccionada] = true;
				}
			}				

			//datosUsuario.setEstatusEdicion(arregloEstatus);		

			AlertView alertBien = new AlertView(activity, AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		}
		else {
			Log.d("infomovil", status.toString());
			modifico = true;
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(activity,  AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.txtErrorActualizacion));
			alertError.setDelegado(this);
			alertError.show();
		}		

	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Validacion para comprobar que todos los dias esten en cero
	 * @return
	 */
	private boolean horariosEnCero(){
		
		boolean result = false;
		
		int totalCeros = 0;
		
		for(HorariosModel horario : listaHorarios){
			if(horario.getInicio().equals("00:00") && horario.getCierre().equals("00:00")){
				++totalCeros;
			}
		}
		
		if(totalCeros ==  listaHorarios.size()){
			result = true;
		}
		
		return result;
		
	}

	@Override
	public void initResume() {
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
		return "Correcto";
	}


}

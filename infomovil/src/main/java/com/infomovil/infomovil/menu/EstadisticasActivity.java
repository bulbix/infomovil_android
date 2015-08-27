package com.infomovil.infomovil.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import mlk.androidchartapi.MlkLine;
import mlk.androidchartapi.MlkLinesView;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.ButtonWithFont;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_VisitaVO;

public class EstadisticasActivity extends InfomovilActivity{

	LinkedList<WS_VisitaVO> listVisitas;
	private MlkLinesView chart;
	Vector<WS_VisitaVO> arrayTotales;
	private Button botonEstadisticas;
	private ButtonWithFont botonTotales, botonUnicos;
	ButtonWithFont[] botonesOpcion = new ButtonWithFont[7];	
	long visitasTotales,visitantesUnicos;
	

	private void llenarGrafica() {
		try {
			ArrayList<MlkLine> lines = new ArrayList<MlkLine>();		
			
			visitantesUnicos = listVisitas.pollLast().getVisitas();
			
			
			float[] values = new float[listVisitas.size()];
			String[] labelValues = new String[listVisitas.size()];

			boolean reducir = listVisitas.size() >10;
			int j=0;

			visitasTotales = 0;
			

			for (int i = listVisitas.size()-1; i >= 0; i--) {
				WS_VisitaVO visitaVO = listVisitas.get(i);
				values[j] = visitaVO.getVisitas();
				
				visitasTotales += visitaVO.getVisitas();

				if(reducir) {
					String arrAux[]=visitaVO.getFecha().split("/");
					labelValues[j] = arrAux[0];
				}
				else {
					labelValues[j] = visitaVO.getFecha();
				}
				j++;
			}
			int lineColor = 0xFF2FA399;
			int gradientTopColor = 0xFF0083ff;
			int gradientBottomColor = 0x000083ff;
			MlkLine line = new MlkLine(values, labelValues);
			line.setLineColor(lineColor);
			line.setGradientColorsAreaFill(gradientTopColor, gradientBottomColor);
			lines.add(line);

			chart.setLines(lines);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void llenarTotales() {
		try {
			botonTotales.setText(getResources().getString(R.string.txtVisitasTotales)+" "+
					visitasTotales);		
			
			String strVisitantesUnicos = CuentaUtils.isCuentaPro() && !CuentaUtils.isDowngrade() ? " " + visitantesUnicos:" ?";			
			botonUnicos.setText(getResources().getString(R.string.txtVisitantesUnicos)+ strVisitantesUnicos);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void cambiarTotales(View v) {		
		
		int opcion = Integer.parseInt(v.getTag().toString());	

		if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
			if(opcion == 2){//Unicos
				alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion, 
						getResources().getString(R.string.preguntaEstadisticas));
				alerta.setDelegado(this);
				alerta.show();
			}
		}		
	}	
	
	private void generarEstadistica(int opcion){
		
		alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity,
				getResources().getString(R.string.msgObtieneEstad));
		alerta.setDelegado(this);
		alerta.show();
		WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
		wsCall.setStrConsulta(opcion+"");
		wsCall.execute(WSInfomovilMethods.GET_ESTADISTICAS);		
		botonesOpcion[opcion].setTextColor(getResources().getColor(R.color.colorBlanco));
		botonesOpcion[opcion].setBackgroundResource(R.drawable.btnrepazul);

		for (int index = 2; index < botonesOpcion.length; index++) {
			if(index == opcion)
				continue;

			botonesOpcion[index].setTextColor(getResources().getColor(R.color.colorFuenteAzul));
			botonesOpcion[index].setBackgroundResource(R.drawable.btnrepbln);
		}	
	}

	public void cambiarEstadistica(View v) {

		int opcion  = Integer.parseInt(v.getTag().toString());		
		
		if(!CuentaUtils.isCuentaPro() || CuentaUtils.isDowngrade()){
			if(opcion == 2 || opcion == 6){//Solo Activo Semana y Anio
				generarEstadistica(opcion);
			}
			else{
				alerta = new AlertView(this, AlertViewType.AlertViewTypeQuestion, 
						getResources().getString(R.string.preguntaEstadisticas));
				alerta.setDelegado(this);
				alerta.show();
			}
		}
		else{
			generarEstadistica(opcion);
		}
		
	}

	@Override
	public void accionSi() {
		super.accionSi();

		if(alerta != null)
			alerta.dismiss();

		Intent inten = new Intent(this, CuentaActivity.class);
		startActivity(inten);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	public void accionNo() {
		super.accionNo();

		if(alerta != null)
			alerta.dismiss();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {			
		if(alerta != null)
			alerta.dismiss();


		if (status == WsInfomovilProcessStatus.EXITO) {
			listVisitas = new LinkedList<WS_VisitaVO>((Vector<WS_VisitaVO>)obj);
			llenarGrafica();
			llenarTotales();
		}
	}	

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		InfomovilApp.setUltimoActivity(null);
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MenuPasosActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void initCreate() {
		cargarLayout(R.layout.estadisticas_layout);
		acomodarVistaConTitulo(R.string.txtReportes, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		chart = (MlkLinesView) findViewById(R.id.lineChart);

		botonTotales = (ButtonWithFont) findViewById(R.id.btnVisitantesTotales);
		botonUnicos = (ButtonWithFont) findViewById(R.id.btnVisitantesUnicos);

		botonesOpcion[2] = (ButtonWithFont) findViewById(R.id.botonVOp1);
		botonesOpcion[3] = (ButtonWithFont) findViewById(R.id.botonVOp2);
		botonesOpcion[4] = (ButtonWithFont) findViewById(R.id.botonVOp3);
		botonesOpcion[5] = (ButtonWithFont) findViewById(R.id.botonVOp4);
		botonesOpcion[6] = (ButtonWithFont) findViewById(R.id.botonVOp5);
	
		Resources res = getResources();
		botonEstadisticas = (Button) findViewById(R.id.btnEstadisticas);
		int botonWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 77, res.getDisplayMetrics());

		int botonHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 68, res.getDisplayMetrics());

		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(botonWidth, botonHeight);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			botonEstadisticas.setBackgroundDrawable(getResources().getDrawable(R.drawable.reporteson));
		}
		else {
			botonEstadisticas.setBackground(getResources().getDrawable(R.drawable.reporteson));
		}
		botonEstadisticas.setLayoutParams(lParams);
		
		
		
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
		generarEstadistica(2);

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
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {		
	}
	
}

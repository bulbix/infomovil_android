package com.infomovil.infomovil.gui.fragment.background;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class ElegirTemplateActivity extends InfomovilActivity {

    ViewPager mPager;
    PageIndicator mIndicator;
    String selectTemplate;

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
		cargarLayout(R.layout.activity_elegirtemplate);
		acomodarVistaConTitulo(R.string.txtElegirTemplate, R.drawable.plecaturquesa);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		
		mPager.setAdapter(new TemplatePageAdapter(getSupportFragmentManager()));
		mIndicator.setViewPager(mPager);
		selectTemplate = "Divertido";
		modifico = true;
	}

	@Override
	public void initResume() {

		mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override 
			public void onPageSelected(int position) {
				
				selectTemplate = TemplatePageAdapter.mapTemplates.get(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
	}

	@Override
	public void guardarInformacion() {
		WS_DomainVO datosDominio = datosUsuario.getDomainData();
		datosDominio.setTemplate(selectTemplate);
		WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
		wsCall.setDomain(datosDominio);
		wsCall.execute(WSInfomovilMethods.UPDATE_TEMPLATE);
		
	}
	
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo, long milisegundo, WsInfomovilProcessStatus status) {
		
		if (status == WsInfomovilProcessStatus.EXITO) {
			actualizacionCorrecta = true;
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtActualizacionCorrecta));
			alerta.setDelegado(this);
			alerta.show();
		}
		else {
			actualizacionCorrecta = false;
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorActualizacion));
			alerta.setDelegado(this);
			alerta.show();
		}
		
	}
	
	@Override
	public void accionAceptar() {
		super.accionAceptar();
		if (actualizacionCorrecta) {
			this.finish();
		}
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

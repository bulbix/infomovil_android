package com.infomovil.infomovil.menu;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.billing.Premium;
import com.infomovil.infomovil.billing.Purchasable;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.billing.util.IabResult;
import com.infomovil.infomovil.billing.util.Purchase;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.fragment.gui.nombrar.FormularioRegistroActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarVentaDominioActivity;
import com.infomovil.infomovil.fragment.gui.nombrar.PublicarActivity;
import com.infomovil.infomovil.gui.adapter.CustomArrayAdapter;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.common.ButtonWithFont;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.SalirCuenta;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.CompraModel;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;

public class CuentaActivity extends InfomovilActivity {
	
	private Button btnCuenta;			
	private ButtonWithFont botonPro, botonDominio, botonPromocion;		
	private PlanProFragment fragmentPlanPro; 
	private DominiFragment fragmentDominio;
	private RedimeFragment fragmentRedime;
	private LeyendaCuentaFragment fragmentLeyendaCuenta;
	
	static CompraInfomovil compraInfomovil;

	private enum TabDominio{
		INDETERMINADO, PLAN_PRO, DOMINIO, PROMOCION;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	
		Intent intent = new Intent(this, MenuPasosActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	    
	}
	
	static  TabDominio tabDominio = TabDominio.INDETERMINADO;

	public void cambiarplan(View v) {	
		int opcion = Integer.parseInt(v.getTag().toString());
		cambiarTab(opcion);
		
	}	
	
	public void cambiarTab(Integer opcion){
		switch (opcion) {	

			case 1:	
				tabDominio = TabDominio.PLAN_PRO;
				botonPro.setTextColor(getResources().getColor(R.color.colorBlanco));
				botonPro.setBackgroundResource(R.drawable.btn_morado);
				botonDominio.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
				botonDominio.setBackgroundResource(R.drawable.btn_blanco);
				botonPromocion.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
				botonPromocion.setBackgroundResource(R.drawable.btn_blanco);
				break;
			case 2:				
				tabDominio = TabDominio.DOMINIO;
				botonDominio.setTextColor(getResources().getColor(R.color.colorBlanco));
				botonDominio.setBackgroundResource(R.drawable.btn_morado);
				botonPro.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
				botonPro.setBackgroundResource(R.drawable.btn_blanco);
				botonPromocion.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
				botonPromocion.setBackgroundResource(R.drawable.btn_blanco);
				break;
			case 3:
				tabDominio = TabDominio.PROMOCION;
				botonPromocion.setTextColor(getResources().getColor(R.color.colorBlanco));
				botonPromocion.setBackgroundResource(R.drawable.btn_morado);
				botonDominio.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
				botonDominio.setBackgroundResource(R.drawable.btn_blanco);
				botonPro.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
				botonPro.setBackgroundResource(R.drawable.btn_blanco);
				break;
		
		}
		
		checkStatusCuenta();
		
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		InfomovilApp.setUltimoActivity(null);
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void initCreate() {
		cargarLayout(R.layout.cuenta_activity);
		acomodarVistaConTitulo(R.string.txtTitleCuenta, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		
		tabDominio = TabDominio.PLAN_PRO;
		
		datosUsuario = DatosUsuario.getInstance();	
		Resources res = getResources();		
		botonPro = (ButtonWithFont) findViewById(R.id.btnPlanPro);
		botonDominio = (ButtonWithFont) findViewById(R.id.btnDominio);	
		botonPromocion = (ButtonWithFont) findViewById(R.id.btnPromocion);	
		fragmentPlanPro = (PlanProFragment)getFragmentManager().findFragmentById(R.id.fragment_planpro);
		fragmentDominio = (DominiFragment)getFragmentManager().findFragmentById(R.id.fragment_dominio);	
		fragmentLeyendaCuenta = (LeyendaCuentaFragment)getFragmentManager().findFragmentById(R.id.fragment_leyendacuenta);
		fragmentRedime = (RedimeFragment)getFragmentManager().findFragmentById(R.id.fragment_codigopromocion);

		int botonWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 77, res.getDisplayMetrics());

		int botonHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 68, res.getDisplayMetrics());

		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(botonWidth, botonHeight);
		
		btnCuenta = (Button)findViewById(R.id.btnCuenta);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			btnCuenta.setBackgroundDrawable(getResources().getDrawable(R.drawable.cuentaon));
		}
		else {
			btnCuenta.setBackground(getResources().getDrawable(R.drawable.cuentaon));
		}

		btnCuenta.setLayoutParams(lParams);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void initResume() {
		
		//Preguntar si ya esta publicado
//		WsInfomovilCall wsCall = new WsInfomovilCall(null, this);
//		wsCall.execute(WSInfomovilMethods.GET_STATUS_DOMAIN);
		
		CompraInfomovil.configurar(this);
		compraInfomovil = new CompraInfomovil(this, new RespuestaCompra(this,"cuenta"));
		checkStatusCuenta();
	}
	
//	private void consultarExistenciaCodigo(){
//		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
//			@Override
//			public void respuestaObj(WSInfomovilMethods metodo,
//					WsInfomovilProcessStatus status, Object obj) {}
//			
//			@Override
//			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
//				if(progressDialog != null)
//					progressDialog.dismiss();
//				replaceFragment(new EmptyFragment());
//			}
//			
//			@Override
//			public void respuestaCompletada(WSInfomovilMethods metodo,
//					long milisegundo, WsInfomovilProcessStatus status) {
//				if(progressDialog != null)
//					progressDialog.dismiss();
//				if(status == WsInfomovilProcessStatus.EXITO){
//					replaceFragment(new RedimeFragment());
//					
//				}
//				else{
//					replaceFragment(new EmptyFragment());
//				}
//			}
//			
//			private void replaceFragment(Object obj){
//				FragmentTransaction ft = getFragmentManager().beginTransaction();
//			    ft.replace(R.id.fragment_redime, (Fragment)obj);
//			    ft.commitAllowingStateLoss();
//			}
//			
//		},this);
//		
//		progressDialog = new AlertView(this, AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.txtCargandoDefault));
//		progressDialog.show();		
//		wsCall.execute(WSInfomovilMethods.GET_EXIST_CAMPANIABYMAIL);
//		
//	}
	
	public void checkStatusCuenta(){
			
		if(CuentaUtils.isCuentaPro() && !CuentaUtils.isDowngrade()){

			if(tabDominio == TabDominio.PLAN_PRO){
				
				fragmentLeyendaCuenta.setStatusDominio(getResources().getString(R.string.txtCuentaPro));
				fragmentLeyendaCuenta.setInicio(getResources().getString(R.string.txtFechaInicio) + 
						datosUsuario.getFechaInicio());
				fragmentLeyendaCuenta.setFin(getResources().getString(R.string.txtFechaFin) +
						datosUsuario.getFechaFin());
			}
		}
		
		switch(tabDominio){
		
			case PLAN_PRO:
				if (datosUsuario.getStatusDominio().equalsIgnoreCase("Gratuito") || 
						datosUsuario.getStatusDominio().equalsIgnoreCase("Tramite") || 
						CuentaUtils.isDowngrade()){
					fragmentPlanPro.getView().setVisibility(View.VISIBLE);
					//fragmentPlanPro.pintar();
					//consultarExistenciaCodigo();
					fragmentLeyendaCuenta.getView().setVisibility(View.GONE);				
				}
				else{
					fragmentPlanPro.getView().setVisibility(View.GONE);
					fragmentLeyendaCuenta.getView().setVisibility(View.VISIBLE);
					fragmentLeyendaCuenta.pintarEtiquetas();
					fragmentLeyendaCuenta.getLblStatusDominio().setOnClickListener(null);
				}
	
				fragmentDominio.getView().setVisibility(View.GONE);
				fragmentRedime.getView().setVisibility(View.GONE);
				break;
				
			case DOMINIO:
				if(!CuentaUtils.isPublicado()){
					CuentaUtils.dialogoPublicar(R.string.msgDominioSinPublicar, this);
					
					fragmentDominio.getView().setVisibility(View.GONE);
					fragmentLeyendaCuenta.setStatusDominio("");
					fragmentLeyendaCuenta.setInicio("");
					fragmentLeyendaCuenta.setFin("");
					fragmentLeyendaCuenta.getView().setVisibility(View.VISIBLE);
					fragmentLeyendaCuenta.pintarEtiquetas();	
				}
				else{
	
					if(InfomovilApp.tipoInfomovil.equals("tel")){
						
						fragmentDominio.getView().setVisibility(View.GONE);
						
						final String nombreDominio = InfomovilApp.urlInfomovil;
						fragmentLeyendaCuenta.setInicio(getResources().getString(R.string.txtFechaInicio) + datosUsuario.getFechaInicioTel());
						fragmentLeyendaCuenta.setFin(getResources().getString(R.string.txtFechaFin) +datosUsuario.getFechaFinTel());
						fragmentLeyendaCuenta.setStatusDominio(nombreDominio);
						fragmentLeyendaCuenta.getLblStatusDominio().setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+nombreDominio));
								startActivity(browserIntent);
	
							}
						});
						
						fragmentLeyendaCuenta.pintarEtiquetas();
						fragmentLeyendaCuenta.getView().setVisibility(View.VISIBLE);	
						
					}
					else{
						final String nombreDominio = InfomovilApp.urlInfomovil;
						fragmentLeyendaCuenta.getView().setVisibility(View.GONE);	
						fragmentDominio.setDominio(nombreDominio);
						
						fragmentDominio.getLabelDominio().setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+nombreDominio));
								startActivity(browserIntent);
	
							}
						});
						
						fragmentDominio.getView().setVisibility(View.VISIBLE);
						
					}
					
				}
				
				fragmentPlanPro.getView().setVisibility(View.GONE);
				fragmentRedime.getView().setVisibility(View.GONE);
				
				break;
			case PROMOCION:
				
				fragmentRedime.getView().setVisibility(View.VISIBLE);
				fragmentLeyendaCuenta.getView().setVisibility(View.GONE);
				fragmentPlanPro.getView().setVisibility(View.GONE);
				fragmentDominio.getView().setVisibility(View.GONE);
				
				break;
			default:
				break;
		
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
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
	    // Pass on the activity result to the helper for handling
	    if (!CompraInfomovil.getBillingHelper().handleActivityResult(requestCode, resultCode, data)) {
	        // not handled, so handle it ourselves (here's where you'd
	        // perform any handling of activity results not related to in-app
	        // billing...
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	    
	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub	
	}	
		
	public static class RedimeFragment extends Fragment{	
				
		private AlertView progressDialog = null;
		private EditTextWithFont txtRedimirCodigo;
		private ButtonWithFont btnRedimir;
		
		DatosUsuario datosUsuario;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_redimir,
					container, false);			
			txtRedimirCodigo = (EditTextWithFont) rootView.findViewById(R.id.txtRedimirCodigo);	
			btnRedimir = (ButtonWithFont) rootView.findViewById(R.id.btnRedimir);
			btnRedimir.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					redimirCodigo(v);
				}
			});
			
			return rootView;
		}				
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			datosUsuario = DatosUsuario.getInstance();			
		}	
		
		public void redimirCodigo(View v) {

			if(StringUtils.isEmpty(txtRedimirCodigo.getText().toString())){
				Toast.makeText(getActivity(), "Ingrese un código", Toast.LENGTH_SHORT).show();
				return;
			}

			datosUsuario = DatosUsuario.getInstance();
			WS_UserDomainVO userDomain = datosUsuario.getUserDomainData()==null?
					new WS_UserDomainVO(): datosUsuario.getUserDomainData();
			userDomain.setEmail(datosUsuario.getNombreUsuario());
			userDomain.setPassword(datosUsuario.getPassword());
			userDomain.setDomainName(datosUsuario.getDomainData().getDomainName());		
			userDomain.setCodigoCamp(txtRedimirCodigo.getText().toString());	
			userDomain.setAccion("redimir");	
			
			
			final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), getString(R.string.app_name),
					getResources().getString(R.string.txtCargandoDefault), true, false);

			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {

				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
					// TODO Auto-generated method stub

				}

				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					redirect(status);
				}

				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, final WsInfomovilProcessStatus status) {
					redirect(status);
				}
				
				private void redirect(final WsInfomovilProcessStatus status){
					if(progressDialog != null)
						progressDialog.dismiss();

					if(!FormularioRegistroActivity.mensajes.containsKey(status.getValue())){
						return;
					}
					
					final AlertView alerta = new AlertView(getActivity(), AlertViewType.AlertViewTypeInfo,
							getResources().getString(FormularioRegistroActivity.mensajes.get(status.getValue())));
					alerta.setDelegado(new AlertViewInterface() {

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

							if(status == WsInfomovilProcessStatus.EXITO){						
								Intent intent = new Intent(getActivity(), MenuPasosActivity.class);
								startActivity(intent);
								getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
							}
							else{
								alerta.dismiss();
							}
						}
					});
					alerta.show();
					
				}
				
				
				
				
			},getActivity());
			wsCall.setUsrDomain(userDomain);		
			wsCall.execute(WSInfomovilMethods.INSERT_USER_CAMP);		
		}
		
	}
	
	public static class DominiFragment extends Fragment {
		
		private ButtonWithFont btnComprarTel;
		private TextWithFont labelDominio, labelAdquiereTel1, labelPrecioTel;
		private String dominio;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_dominio, container, false);	
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			
			labelDominio = (TextWithFont) rootView.findViewById(R.id.labelDominio);
			labelAdquiereTel1  = (TextWithFont) rootView.findViewById(R.id.labelAdquiereTel1);
			btnComprarTel = (ButtonWithFont) rootView.findViewById(R.id.btnComprarTel);
			labelPrecioTel =  (TextWithFont) rootView.findViewById(R.id.labelPrecioTel);
			labelPrecioTel.setText(getString(R.string.txtPrecioAnual, datosUsuario.getPrecioTel()));
			
			labelDominio.setTypeface(null, Typeface.BOLD);
			labelPrecioTel.setTypeface(null, Typeface.BOLD);
			
			btnComprarTel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), NombrarVentaDominioActivity.class);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
					
				}
			});
			
			return rootView;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			
			if(CuentaUtils.dominioTelComprado()){
				labelAdquiereTel1.setText(R.string.msgTelComprado1);
				labelAdquiereTel1.setLines(1);
				btnComprarTel.setText(R.string.txtRegistralo);
				labelPrecioTel.setVisibility(View.GONE);
			}
			
		}
		

		public TextWithFont getLabelDominio() {
			return labelDominio;
		}

		public void setDominio(String dominio) {
			this.dominio = dominio;
			labelDominio.setText(dominio);
		}
		
		
	}
	
	public static class DominioFragment extends ListFragment implements WsInfomovilDelgate {
		
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		private ButtonWithFont btnComprarTel;
		private AlertView progressDialog = null;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_dominio, container, false);	
			
			btnComprarTel = (ButtonWithFont) rootView.findViewById(R.id.btnComprarTel);
			btnComprarTel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					CompraModel compra = new CompraModel();
					compra.setPlan("DOMINIO TEL");			
					compra.setComision(30);
					compra.setPagoId("0");
					compra.setStatusPago("INTENTO PAGO");
					compra.setCodigoCobro(" ");
					compra.setReferencia(" ");
					compra.setTipoCompra("tel");
					DatosUsuario.getInstance().setCompra(compra);
					
					final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),  getString(R.string.app_name),
							getResources().getString(R.string.txtCargandoDefault), true, false);
					
					
					WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
						
						@Override
						public void respuestaObj(WSInfomovilMethods metodo,
								WsInfomovilProcessStatus status, Object obj) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
							progressDialog.dismiss();
							
						}
						
						@Override
						public void respuestaCompletada(WSInfomovilMethods metodo,
								long milisegundo, WsInfomovilProcessStatus status) {
							
							progressDialog.dismiss();
							
							if(status == WsInfomovilProcessStatus.EXITO){
								compraInfomovil.startBuyProcess("DOMINIO TEL");
							}
							
						}
					},getActivity());	
					
					wsCall.execute(WSInfomovilMethods.COMPRA_INTENTO);
					
				}
			});
			
			return rootView;
		}
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			
			WS_UsuarioDominiosVO usuarioDominios = datosUsuario.getUsuarioDominios().get(position);
			
			datosUsuario.setUsuarioDominioVOActual(usuarioDominios);
			
			if(usuarioDominios.getDomainType().equalsIgnoreCase("recurso")){
				FragmentManager fm = getFragmentManager();
				RenombrarRecursoFragment renombrarDialog = RenombrarRecursoFragment.newInstance(usuarioDominios,this);
				renombrarDialog.show(fm, "RenombrarRecursoDialogo");
			}
			else{
				
				if(!usuarioDominios.getStatusCtrlDominio().equalsIgnoreCase("Gratuito")){ //Sea distinto de publicado
					Intent i = new Intent(getActivity(), NombrarActivity.class);
					i.putExtra("tipoDominio", usuarioDominios.getDomainType());
					getActivity().finish();
					startActivity(i);
					getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
				}
				else{
					FragmentManager fm = getFragmentManager();
					FechasDominioFragment renombrarDialog = FechasDominioFragment.newInstance(usuarioDominios);
					renombrarDialog.show(fm, "FechasDominioDialogo");
				}
			}
			
		}
		
		public void refrescarDominios(){
			progressDialog = new AlertView(this.getActivity(), AlertViewType.AlertViewTypeActivity, 
					getResources().getString(R.string.txtObteniendoDominio));
			progressDialog.setDelegado(null);
			progressDialog.show();
			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
			wsCall.execute(WSInfomovilMethods.GET_STATUS_DOMAIN);
		}

		@Override
		public void respuestaCompletada(WSInfomovilMethods metodo,
				long milisegundo, WsInfomovilProcessStatus status) {
			
			if(progressDialog != null)
				progressDialog.dismiss();
			
			if(status == WsInfomovilProcessStatus.EXITO){
				setListAdapter(new CustomArrayAdapter<WS_UsuarioDominiosVO>(getActivity(), datosUsuario.getUsuarioDominios()));
				
				for(WS_UsuarioDominiosVO dominio: datosUsuario.getUsuarioDominios()){
					if(dominio.getDomainType().equalsIgnoreCase("tel")){
						btnComprarTel.setVisibility(View.GONE);
					}
				}
				
			}
		}		
		
		@Override
		public void respuestaErronea(WsInfomovilProcessStatus status,
				Exception e) {
			if(progressDialog != null)
				progressDialog.dismiss();
		}

		@Override
		public void respuestaObj(WSInfomovilMethods metodo,
				WsInfomovilProcessStatus status, Object obj) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class PlanProFragment extends Fragment{
		
		private String plan;
		private ImageView imageUno, imageDoce;
		private TextWithFont lblConoceMas;
		// variable to track event time
		private long mLastClickTime = 0;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_planpro, container, false);
			
			imageUno = (ImageView)rootView.findViewById(R.id.imageUno);
//			imageSeis = (ImageView)rootView.findViewById(R.id.imageSeis);
			imageDoce = (ImageView)rootView.findViewById(R.id.imageDoce);
			lblConoceMas = (TextWithFont)rootView.findViewById(R.id.lblConoceMas);
			
			lblConoceMas.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), NoticiasActivity.class);
		    		startActivity(intent);
		    		getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
				}
			});
			
			
			TextView lblPrecioMes = (TextView)rootView.findViewById(R.id.lblPrecioMes);
			TextView lblPrecioAnual = (TextView)rootView.findViewById(R.id.lblPrecioAnual);
			
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			
			lblPrecioMes.setTypeface(null, Typeface.BOLD); 
			lblPrecioMes.setText(datosUsuario.getPrecioMensual());
			lblPrecioAnual.setTypeface(null, Typeface.BOLD); 
			
			try{
				String precioAnual = datosUsuario.getPrecioAnual().replaceAll("[^0-9?!\\.]","");
				String currencyAnual = datosUsuario.getPrecioAnual().substring(0, datosUsuario.getPrecioAnual().indexOf(precioAnual));
				Double precioAnualXMes = Math.floor(Double.parseDouble(precioAnual) / 12);
				DecimalFormat f = new DecimalFormat("##.00");
				DecimalFormatSymbols dfs = f.getDecimalFormatSymbols();
			    dfs.setDecimalSeparator('.');
			    f.setDecimalFormatSymbols(dfs);
				lblPrecioAnual.setText(currencyAnual + f.format(precioAnualXMes));
			}
			catch(Exception e){
				lblPrecioAnual.setText("");
			}
			
			
			TextView lblMasFuncionalidad = (TextView)rootView.findViewById(R.id.lblMasFuncionalidad);
			lblMasFuncionalidad.setTypeface(null, Typeface.BOLD); 
			TextView lblMasAlmacenamiento = (TextView)rootView.findViewById(R.id.lblMasAlmacenamiento);
			lblMasAlmacenamiento.setTypeface(null, Typeface.BOLD); 
			TextView lblAtencionPremium = (TextView)rootView.findViewById(R.id.lblAtencionPremium);
			lblAtencionPremium.setTypeface(null, Typeface.BOLD); 
			
			TextView lblBeneficiosPlanPro = (TextView)rootView.findViewById(R.id.lblBeneficiosPlanPro);
			lblBeneficiosPlanPro.setTypeface(null, Typeface.BOLD); 
			
			IntentoCompraClick intentoCompraClick = new IntentoCompraClick();
			imageUno.setOnClickListener(intentoCompraClick);
			///imageSeis.setOnClickListener(intentoCompraClick);
			imageDoce.setOnClickListener(intentoCompraClick);
			 return rootView;
		}
		
		
		
		private class IntentoCompraClick implements View.OnClickListener{
			@Override
			public void onClick(View v) {
				intentoCompra(v);				
			}	
		}
		
		public void intentoCompra(View v){
			
			// Preventing multiple clicks, using threshold of 1 second
			if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
				return;
			}
			
			mLastClickTime = SystemClock.elapsedRealtime();
			
			int idPlan = ((ImageView)v).getId();
			
			plan = ""; 
			String precio="";
			
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			CompraModel compra = new CompraModel();
			
			switch(idPlan) {
				case R.id.imageUno:
						plan="PLAN PRO 1 MES";
						precio="60";
						compra.setMontoOrigen(datosUsuario.getPrecioMensual());
					break;
				case R.id.imageDoce:
						plan="PLAN PRO 12 MESES";
						precio="599";
						compra.setMontoOrigen(datosUsuario.getPrecioAnual());
					break;
			}
			
			compra.setPlan(plan);	
			compra.setMontoBruto(precio);
			compra.setComision(30);
			compra.setPagoId("0");
			compra.setStatusPago("INTENTO PAGO");
			compra.setCodigoCobro(" ");
			compra.setReferencia(" ");
			compra.setTipoCompra("PP");
			DatosUsuario.getInstance().setCompra(compra);
			
			final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),  getString(R.string.app_name),
					getResources().getString(R.string.txtCargandoDefault), true, false);
			
			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
				
				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					progressDialog.dismiss();
				}
				
				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					
					progressDialog.dismiss();
					
					if(status == WsInfomovilProcessStatus.EXITO){
						compraInfomovil.startBuyProcess(plan);
					}
					
				}
			} ,getActivity());		
			
			wsCall.execute(WSInfomovilMethods.COMPRA_INTENTO);
		}
		
	}

	public static class LeyendaCuentaFragment extends Fragment {
		
		private TextWithFont lblStatusDominio, labelInicio, labelFin, labelProceso;
		private TextWithFont lblMovilizaSitio1,lblMovilizaSitio2;
		private String statusDominio,inicio,fin,proceso;
		private ButtonWithFont btnMovilizaSitio;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_leyendacuenta, container, false);
			
			lblStatusDominio = (TextWithFont)rootView.findViewById(R.id.lblStatusDominio);
			lblStatusDominio.setTypeface(null, Typeface.BOLD); 
			
			labelInicio = (TextWithFont)rootView.findViewById(R.id.labelFechaInicio);
			labelFin = (TextWithFont)rootView.findViewById(R.id.labelFechaFin);
			labelProceso = (TextWithFont)rootView.findViewById(R.id.labelAuxNoPublicado);
			
			//Plan Pro
			lblMovilizaSitio1 = (TextWithFont)rootView.findViewById(R.id.lblMovilizaSitio1);
			lblMovilizaSitio2 = (TextWithFont)rootView.findViewById(R.id.lblMovilizaSitio2);
			btnMovilizaSitio = (ButtonWithFont)rootView.findViewById(R.id.btnMovilizaSitio);
			
			lblMovilizaSitio2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), NoticiasActivity.class);
		    		startActivity(intent);
		    		getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
				}
			});
			
			btnMovilizaSitio.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getFragmentManager();
					MovilizaSitioFragment movilizaFragment = new MovilizaSitioFragment();
					movilizaFragment.show(fm, "MovilizaSitioDialogo");
					
				}
			});
			
			
			return rootView;
		}
		
		
		@Override
		public void onResume() {
			super.onResume();
			if(CuentaUtils.validosDatosUsuario(getActivity())){
				pintarEtiquetas();
			}
			
		}
		
		public void pintarEtiquetas(){
			
//			DatosUsuario datosUsuario = DatosUsuario.getInstance();
//			
//			if (datosUsuario.getStatusDominio().equalsIgnoreCase("Gratuito") || 
//				datosUsuario.getStatusDominio().equalsIgnoreCase("Tramite") ){
//				
//				if(tabDominio == TabDominio.PLAN_PRO){
//					lblStatusDominio.setVisibility(View.GONE);
//					labelInicio.setVisibility(View.GONE);
//					labelFin.setVisibility(View.GONE);
//					
//				}
//				else if(tabDominio == TabDominio.DOMINIO){
//					lblStatusDominio.setVisibility(View.VISIBLE);
//					labelInicio.setVisibility(View.VISIBLE);
//					labelFin.setVisibility(View.VISIBLE);
//				}
//					
//			}
//			else{
//				lblStatusDominio.setVisibility(View.VISIBLE);
//				labelInicio.setVisibility(View.VISIBLE);
//				labelFin.setVisibility(View.VISIBLE);							
//			}
			if(CuentaUtils.isCuentaPro()){
				if(tabDominio == TabDominio.PLAN_PRO){
					lblMovilizaSitio1.setVisibility(View.VISIBLE);
					lblMovilizaSitio2.setVisibility(View.VISIBLE);
					btnMovilizaSitio.setVisibility(View.VISIBLE);
				}
				else{
					lblMovilizaSitio1.setVisibility(View.GONE);
					lblMovilizaSitio2.setVisibility(View.GONE);
					btnMovilizaSitio.setVisibility(View.GONE);
				}
			}
			
			
		}

		public String getStatusDominio() {
			return statusDominio;
		}


		public void setStatusDominio(String statusDominio) {
			this.statusDominio = statusDominio;
			lblStatusDominio.setText(statusDominio);
		}


		public String getInicio() {
			return inicio;
		}


		public void setInicio(String inicio) {
			this.inicio = inicio;
			labelInicio.setText(inicio);
		}


		public String getFin() {
			return fin;
		}


		public void setFin(String fin) {
			this.fin = fin;
			labelFin.setText(fin);
		}


		public String getProceso() {
			return proceso;
		}


		public void setProceso(String proceso) {
			this.proceso = proceso;
			if(!StringUtils.isEmpty(proceso))labelProceso.setText(proceso);
		}

		public TextWithFont getLblStatusDominio() {
			return lblStatusDominio;
		}

		public void setLblStatusDominio(TextWithFont lblStatusDominio) {
			this.lblStatusDominio = lblStatusDominio;
		}
	}
	
	public static class MovilizaSitioFragment extends DialogFragment {
		
		private TextWithFont lblMovilizaSitio, lblMovilizaSitioWebActual;
		private DatosUsuario datosUsuario;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			getDialog().setTitle(R.string.txtCuenta);
			
			View rootView = inflater.inflate(R.layout.fragment_movilizasitio, container, false);
			lblMovilizaSitio = (TextWithFont)rootView.findViewById(R.id.lblMovilizaSitio);
			lblMovilizaSitioWebActual = (TextWithFont)rootView.findViewById(R.id.lblMovilizaSitioWebActual);
			lblMovilizaSitioWebActual.setTypeface(null, Typeface.BOLD);
			datosUsuario = DatosUsuario.getInstance();
			
			
			lblMovilizaSitio.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(StringUtils.isEmpty(datosUsuario.getHashMovilizaSitio())){
						generarHashMovilizaSitio();
					}
					else{
						compartirEmail();
					}
					
					
				}
			});
			
			return rootView;
		}
		
		private void generarHashMovilizaSitio(){
			final ProgressDialog progress = 
					ProgressDialog.show(getActivity(),  getString(R.string.app_name),getResources().getString(R.string.txtCargando), true);
			
			WsInfomovilCall wsInfomovilCall = new WsInfomovilCall(new WsInfomovilDelgate() {
				
				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					progress.dismiss();
					
				}
				
				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					progress.dismiss();
					compartirEmail();
					
				}
			}, getActivity());
			
			wsInfomovilCall.execute(WSInfomovilMethods.GET_HASH_MOVILIZA_SITIO);
		}
		
		public void compartirEmail(){
			
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.txtMovilizaCorreo1));
			i.putExtra(Intent.EXTRA_TEXT   , Html.fromHtml(getResources().getString(R.string.txtMovilizaCorreo2, datosUsuario.getHashMovilizaSitio())));
			try {
			    startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				
			}
			
		}
	
	}
	
	public static class RenombrarRecursoFragment extends DialogFragment {
		
		TextWithFont lblNombreRecurso, lblWWWNombrar;
		EditTextWithFont txtNombreRecurso;
		WS_UsuarioDominiosVO recurso;
		DatosUsuario datosUsuario = DatosUsuario.getInstance();	
		DominioFragment dominioFragment;
		
		static RenombrarRecursoFragment newInstance(WS_UsuarioDominiosVO recurso, DominioFragment dominioFragment){
			RenombrarRecursoFragment frag = new RenombrarRecursoFragment();
			frag.recurso = recurso;
			frag.dominioFragment = dominioFragment;
			return frag;
		}
		
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
		    
		    LayoutInflater inflater = LayoutInflater.from(getActivity());
		    final View rootView = inflater.inflate(R.layout.fragment_renombrarecurso, null);
		    
		    lblNombreRecurso  = (TextWithFont)rootView.findViewById(R.id.lblNombreRecurso);
			lblWWWNombrar  = (TextWithFont)rootView.findViewById(R.id.lblWWWNombrar);
			txtNombreRecurso  = (EditTextWithFont)rootView.findViewById(R.id.txtNombreRecurso);
			
			lblNombreRecurso.setText(recurso.toString());
		    
		    return new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.txtRenombrarRecurso)
		    .setView(rootView)
		    .setCancelable(true)
		    .setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {
		    		actualizar();
		    	}
		    })
		    .setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {
		    		dialog.cancel();
		    	}
		    }).create();
		    
		}
		
		private void actualizar(){
			if(StringUtils.isEmpty(txtNombreRecurso.getText())){
				Toast.makeText(getActivity(), "Nombre Requerido", Toast.LENGTH_SHORT).show();
				return;
			}
			
			final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),  getString(R.string.app_name),
					getResources().getString(R.string.txtCargando), true, false);
				
			
			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
				
				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					progressDialog.dismiss();
					
				}
				
				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					
					progressDialog.dismiss();
					if(status == WsInfomovilProcessStatus.EXITO){
						dominioFragment.refrescarDominios();
						datosUsuario.getDomainData().setDomainName(txtNombreRecurso.getText().toString());
					}
					
				}
			},getActivity());		
			
			wsCall.setStrConsulta(txtNombreRecurso.getText().toString());
			wsCall.execute(WSInfomovilMethods.CAMBIA_NOMBRE_RECURSO);	
		}
		
		
	}
	
	public static class FechasDominioFragment extends DialogFragment {
		
		TextWithFont lblStatusDominio, lblFechaInicio, lblFechaFin;
		WS_UsuarioDominiosVO recurso;
		DatosUsuario datosUsuario = DatosUsuario.getInstance();		
		
		static FechasDominioFragment newInstance(WS_UsuarioDominiosVO recurso){
			FechasDominioFragment frag = new FechasDominioFragment();
			frag.recurso = recurso;
			return frag;
		}
		
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
		    final View rootView = inflater.inflate(R.layout.fragment_fechasdominio, null);
		    
			lblStatusDominio  = (TextWithFont)rootView.findViewById(R.id.lblStatusDominio);
			lblFechaInicio  = (TextWithFont)rootView.findViewById(R.id.labelFechaInicio);
			lblFechaFin  = (TextWithFont)rootView.findViewById(R.id.labelFechaFin);
			
			lblStatusDominio.setText(recurso.toString());
			lblFechaInicio.setText(getResources().getString(R.string.txtFechaInicio) +
					recurso.getFechaCtrlIni().substring(0,10));
			lblFechaFin.setText(getResources().getString(R.string.txtFechaFin) +
					recurso.getFechaCtrlFin().substring(0,10));
		    
		    
		    return new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.txtFechasVigencia)
		    .setView(rootView)
		    .setCancelable(true)
		    .setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {
		    		dialog.cancel();
		    	}
		    }).create();
		    
		    
		}
		
		
	}
	
	public static class EmptyFragment extends Fragment{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_empty,
					container, false);
			return rootView;
		}
		
	}
	
	public static class RespuestaCompra implements Purchasable{
		
		Activity activity;
		String pantalla;
		
		public RespuestaCompra(Activity activity, String pantalla) {
			this.activity = activity;
			this.pantalla = pantalla;
		}

		@Override
		public void compraCorrecta(IabResult result, Purchase info, final String tipo, BigDecimal precio) {
			
			final ProgressDialog progressDialog = ProgressDialog.show(activity,  
					activity.getResources().getString(R.string.app_name),
					activity.getResources().getString(R.string.txtCargandoDefault), true, false);
			
			CompraModel compra = DatosUsuario.getInstance().getCompra();
			compra.setStatusPago("PAGADO");
			compra.setCodigoCobro(info.getOrderId());	
			
			try{
				Appboy.getInstance(activity).logPurchase(Premium.planpro.get(tipo),"MXN", precio);
			} catch(Exception e){Log.e("Error","Compra",e);}
			
			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
				
				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					progressDialog.dismiss();
					
					AlertView alertDialog = new AlertView(activity, AlertViewType.AlertViewTypeInfo, 
				    activity.getResources().getString(R.string.txtErrorCompra));
					alertDialog.setDelegado(new SalirCuenta(activity, alertDialog));
					alertDialog.show();
					
				}
				
				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					
					progressDialog.dismiss();
					
					if(status == WsInfomovilProcessStatus.EXITO){
						
						if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
							compraInfomovil.consumirProducto(CompraInfomovil.ANDROID_TEST_PURCHASED);
						}
						else{
							compraInfomovil.consumirProducto(tipo);
						}
						
						//Trackear fecha de terminacion
						try{
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							DatosUsuario datosUsuario = DatosUsuario.getInstance();
							Log.d("FechaTerminacion1", datosUsuario.getFechaFin());
							Log.d("FechaTerminacion2", sdf1.format(sdf.parse(datosUsuario.getFechaFin())));
							//Appboy.getInstance(activity).getCurrentUser().setCustomUserAttribute("fechaExpPlanPro", sdf1.format(sdf.parse(datosUsuario.getFechaFin())));
						}
						catch(Exception e){}
						
					}
					
					if(status == WsInfomovilProcessStatus.EXITO && activity instanceof CuentaActivity 
						&& !tipo.equals(Premium.SKU_DOMINIO_TEL)){
						((CuentaActivity)activity).checkStatusCuenta();
					}
					
					redirect(tipo);
					
				}
			},activity);		
			
			wsCall.execute(WSInfomovilMethods.COMPRA_DOMINIO);	
			
		}

		@Override
		public void compraFallida(final String tipo) {
			
			final ProgressDialog progressDialog = ProgressDialog.show(activity,  activity.getResources().getString(R.string.app_name),
					activity.getResources().getString(R.string.txtCargandoDefault), true, false);
			
			
			CompraModel compra = DatosUsuario.getInstance().getCompra();
			compra.setStatusPago("FALLADO");
			
			
			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
				
				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
				}
				
				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					progressDialog.dismiss();
				}
				
				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					progressDialog.dismiss();
				}
			},activity);		
			
			wsCall.execute(WSInfomovilMethods.COMPRA_DOMINIO);		
			//redirect(tipo);
			
		}	
		
		
		public static void publicarDominioTel(Activity activity){
			DatosUsuario datosUsuario = DatosUsuario.getInstance();
			
			WS_UserDomainVO userDomain = datosUsuario.getUserDomainData() == null ? 
			new WS_UserDomainVO() : datosUsuario.getUserDomainData();
			
			userDomain.setPassword(datosUsuario.getPassword());
			userDomain.setEmail(datosUsuario.getNombreUsuario());
			userDomain.setSistema("ANDROID");
			
			userDomain.setTipoAction(5);
			userDomain.setPais(1);
			userDomain.setCanal(0);
			userDomain.setSucursal(1);
			userDomain.setFolio(0);
			userDomain.setStatus(1);
			
			userDomain.setDomainType("tel");				
			userDomain.setIdDominio((int)datosUsuario.getDomainid());		
			userDomain.setAccion("publicar");
			userDomain.setIdCatDominio("1");//infomovil.com por default
			datosUsuario.setUserDomainData(userDomain);
			
			
			Intent intent = new Intent(activity, PublicarActivity.class);
			intent.putExtra("tipoDominio", "tel");
			intent.putExtra("source", "compra");
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
		}
		
		protected void redirect(String tipo){
			
			if(pantalla.equalsIgnoreCase("cuenta")){
				if(tipo.equals(Premium.SKU_DOMINIO_TEL)){
					//((CuentaActivity)activity).cambiarTab(2);//Dominio
				}
				else{
//					CompraFragment fragmentCompra = 
//							(CompraFragment)activity.getFragmentManager().findFragmentByTag("CompraDialogo");	
//					fragmentCompra.dismiss();	
				}
			}
			else if(pantalla.equalsIgnoreCase("nombrarcompartir")){
				InfomovilApp.setEnTramite(false);
				Intent intent = new Intent(activity, 
						MenuPasosActivity.class);
				activity.finish();
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
			else if(pantalla.equalsIgnoreCase("nombrarTel")){
				if(tipo.equals(Premium.SKU_DOMINIO_TEL)){
					publicarDominioTel(activity);
				}
			}
			

		}
		
	}
	
}


package com.infomovil.infomovil.fragment.gui.nombrar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.billing.Purchasable;
import com.infomovil.infomovil.billing.util.IabResult;
import com.infomovil.infomovil.billing.util.Purchase;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.ButtonWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.principal.CompartirActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.menu.CuentaActivity.RespuestaCompra;
import com.infomovil.infomovil.model.CompraModel;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;

public class NombrarCompartirActivity extends CompartirActivity{
	
	private String dominio,fechaFin;
	private TextWithFont labelNombreDominioCompartir,labelRenova;
	private int tipoVista;	
	private ButtonWithFont btnComprarTel;
	private String tipoDominio;
	CompraInfomovil compraInfomovil;
	private AlertView progressDialog = null;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (tipoVista == 5) {
			return super.onKeyDown(keyCode, event);
		}
		else {
			InfomovilApp.setEnTramite(false);
			Intent intent = new Intent(this, MenuPasosActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_out, R.anim.left_in);
			return false;
		}
	}	

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void initCreate() {
		cargarLayout(R.layout.compartir_layout);
		tipoVista = getIntent().getIntExtra("tipoPantalla", 0);
		
		
		
		if (tipoVista == 5) {
			acomodarVistaConTitulo(R.string.txtFelicidadesTitulo, R.drawable.plecamorada);
		}
		else {
			acomodarVistaConTitulo(R.string.txtFelicidadesTitulo, R.drawable.plecaroja);
		}
		
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		dominio =getIntent().getStringExtra("dominio");
		fechaFin=getIntent().getStringExtra("fechaFin");
		tipoDominio = getIntent().getStringExtra("tipoDominio");
		
		labelNombreDominioCompartir = (TextWithFont)
		findViewById(R.id.labelNombreDominioCompartir);		
		btnComprarTel = (ButtonWithFont)findViewById(R.id.btnComprarTel);	
		
		final String nombreDominio;
		
		 if(tipoDominio.equals("tel")){
			nombreDominio = InfomovilApp.urlInfomovil;
		 }
		 else{
			 nombreDominio= InfomovilApp.urlInfomovil;
		 }
		 
		
		labelNombreDominioCompartir.setText(nombreDominio);
		labelNombreDominioCompartir.setTextColor(getResources().getColor(R.color.colorMorado));
		labelNombreDominioCompartir.setTypeface(null, Typeface.BOLD); 
		//Linkify.addLinks(labelDominio, Linkify.ALL);
		
		
		labelNombreDominioCompartir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+nombreDominio));
				startActivity(browserIntent);
			}
		});
		
		
		
		labelRenova = (TextWithFont)findViewById(R.id.labelRenova);
		String renueva=getResources().getString(R.string.txtRenova);
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat formato2 = new SimpleDateFormat("dd-mm-yyyy");
		try {
			labelRenova.setText(renueva+" "+formato2.format(formato.parse(fechaFin)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		Log.d("Infomovil","Dominio: "+dominio);
		
		final AlertView alertModif = new AlertView(this, AlertViewType.
		AlertViewTypeInfo, getResources().getString(R.string.txtAlertaPlubicar));
		alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
		alertModif.show();		
	}
	
	@Override
	public void initResume() {
		
		CompraInfomovil.configurar(this);
		compraInfomovil = new CompraInfomovil(this, new CuentaActivity.RespuestaCompra(this,"nombrarcompartir"));
		
		if(tipoDominio.equals("recurso")){
			//btnComprarTel.setVisibility(View.VISIBLE);
			labelRenova.setVisibility(View.GONE);
			labelNombreDominioCompartir.setText(InfomovilApp.urlInfomovil);
		}
		else{
			//btnComprarTel.setVisibility(View.GONE);
			labelRenova.setVisibility(View.VISIBLE);
			labelNombreDominioCompartir.setText(InfomovilApp.urlInfomovil);
		}
		
		
//		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
//			
//			@Override
//			public void respuestaObj(WSInfomovilMethods metodo,
//					WsInfomovilProcessStatus status, Object obj) {
//			}
//			
//			@Override
//			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
//				if(progressDialog!=null)
//					progressDialog.dismiss();
//			}
//			
//			@Override
//			public void respuestaCompletada(WSInfomovilMethods metodo,
//					long milisegundo, WsInfomovilProcessStatus status) {
//				if(progressDialog!=null)
//					progressDialog.dismiss();
//			}
//		},this);
//		
//		progressDialog = new AlertView(this, AlertViewType.AlertViewTypeActivity,
//				getResources().getString(R.string.txtCargandoDefault));
//		progressDialog.show();
//		wsCall.execute(WSInfomovilMethods.GET_STATUS_DOMAIN);
	}
	
	
	public void terminarPublicacion(View v) {
		if (tipoVista == 5) {
			this.finish();
		}
		else {
			InfomovilApp.setEnTramite(false);
			Intent intent = new Intent(this, MenuPasosActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_out, R.anim.left_in);
		}
	}
	
	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
	    // Pass on the activity result to the helper for handling
	    if (!compraInfomovil.getBillingHelper().handleActivityResult(requestCode, resultCode, data)) {
	        // not handled, so handle it ourselves (here's where you'd
	        // perform any handling of activity results not related to in-app
	        // billing...
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	    
	}
	
	public void comprarTelnames(View v) {
		
		CompraModel compra = new CompraModel();
		compra.setPlan("DOMINIO TEL");			
		compra.setComision(30);
		compra.setPagoId("0");
		compra.setStatusPago("INTENTO PAGO");
		compra.setCodigoCobro(" ");
		compra.setReferencia(" ");
		compra.setTipoCompra("tel");
		DatosUsuario.getInstance().setCompra(compra);
		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
			
			@Override
			public void respuestaObj(WSInfomovilMethods metodo,
					WsInfomovilProcessStatus status, Object obj) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void respuestaCompletada(WSInfomovilMethods metodo,
					long milisegundo, WsInfomovilProcessStatus status) {
				if(status == WsInfomovilProcessStatus.EXITO){
					compraInfomovil.startBuyProcess("DOMINIO TEL");
				}
				
			}
		},this);		
		wsCall.execute(WSInfomovilMethods.COMPRA_INTENTO);
		
		
		
	}
	
	
}

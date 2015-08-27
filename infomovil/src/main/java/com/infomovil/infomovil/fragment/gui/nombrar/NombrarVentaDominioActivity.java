package com.infomovil.infomovil.fragment.gui.nombrar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.billing.CompraInfomovil;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity.PublicarRecursoFragment;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.ButtonWithFont;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.menu.CuentaActivity.RespuestaCompra;
import com.infomovil.infomovil.model.CompraModel;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;

public class NombrarVentaDominioActivity extends InfomovilActivity{
	
	private EditTextWithFont txtNombre;
	static CompraInfomovil compraInfomovil;
	
	public class PublicarFragment extends DialogFragment {
		
		TextWithFont lblNombreRecurso, lblDominio;
		String dominio;
		boolean sitioDisponible;
		
		public PublicarFragment (String dominio, boolean sitioDisponible){
			this.dominio = dominio;
			this.sitioDisponible = sitioDisponible;
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {

		    LayoutInflater inflater = LayoutInflater.from(getActivity());
		    final View rootView = inflater.inflate(R.layout.fragment_publicar, null);
		    
		    lblNombreRecurso  = (TextWithFont)rootView.findViewById(R.id.labelRecurso);
		    lblDominio = (TextWithFont)rootView.findViewById(R.id.labelDominio);
		    
		    
		    lblNombreRecurso.setText(getResources().getString(R.string.txtWWWGeneral)+
					dominio+
					getResources().getString(R.string.txtTelGeneral));
		    lblNombreRecurso.setTypeface(null, Typeface.BOLD); 
		    
		    if(sitioDisponible){
		    	lblDominio.setText(R.string.msgSitioDisponible);
		    }
		    else{
		    	lblDominio.setText(R.string.msgSitioNoDisponible);
		    }
		    
		    
		   AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.txtPublicar)
		    .setView(rootView)
		    .setCancelable(true);
		   
		   if(sitioDisponible){
			   alertDialog.setPositiveButton(CuentaUtils.dominioTelComprado() ?R.string.txtPublicar: R.string.txtComprar,
				    	new DialogInterface.OnClickListener() {
			    	@Override
			    	public void onClick(DialogInterface dialog, int which) {
			    		
			    		if(CuentaUtils.dominioTelComprado()){
							RespuestaCompra.publicarDominioTel(NombrarVentaDominioActivity.this);
						}
						else{
							compraTel();
						}
			    		
			    	}
			   })
			   .setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
			    	@Override
			    	public void onClick(DialogInterface dialog, int which) {
			    		dialog.cancel();
			    	}
			    });
		   }
		   else{
			   alertDialog.setNegativeButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
			    	@Override
			    	public void onClick(DialogInterface dialog, int which) {
			    		dialog.cancel();
			    	}
			    });
		   }
		   
		    
		    
		   
		    
		    return alertDialog.create();
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
	public void initCreate() {
		cargarLayout(R.layout.nombrar_layout);
		acomodarVistaConTitulo(R.string.txtNombrarTitulo, R.drawable.plecaroja);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);	
		txtNombre = (EditTextWithFont) findViewById(R.id.txtNombre);
	}
	
	public void buscarSitio(View v){
		
		
//		WS_DomainVO domVO = datosUsuario.getDomainData() 
//				== null ? new WS_DomainVO() : datosUsuario.getDomainData();  
//		
//		domVO.setDomainName(txtNombreSitio.getText().toString());
//		datosUsuario.setDomainData(domVO);
		
		WS_UserDomainVO userDomain = datosUsuario.getUserDomainData() == null ? 
				new WS_UserDomainVO() : datosUsuario.getUserDomainData();
		
		if(NombrarActivity.esAlfaNumerica(txtNombre.getText().toString())){
			userDomain.setDomainName(txtNombre.getText().toString());
			datosUsuario.setUserDomainData(userDomain);
					
			final ProgressDialog progress = ProgressDialog.show(this, getString(R.string.app_name),
					getResources().getString(R.string.msgBuscandoSitio), true, false);
			
			WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
				
				@Override
				public void respuestaObj(WSInfomovilMethods metodo,
						WsInfomovilProcessStatus status, Object obj) {
				}
				
				@Override
				public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
					progress.dismiss();
				}
				
				@Override
				public void respuestaCompletada(WSInfomovilMethods metodo,
						long milisegundo, WsInfomovilProcessStatus status) {
					
					progress.dismiss();
					
					if (status == WsInfomovilProcessStatus.SIN_EXITO) {
						FragmentManager fm = getFragmentManager();
						PublicarFragment publicarDialog = 
						new PublicarFragment(txtNombre.getText().toString(),true);
						publicarDialog.show(fm, "PublicarTelDialogo");
					}
					else {
						FragmentManager fm = getFragmentManager();
						PublicarFragment publicarDialog = 
						new PublicarFragment(txtNombre.getText().toString(),false);
						publicarDialog.show(fm, "PublicarTelDialogo");
					}
					
				}
			},this);
			
			datosUsuario.setDomainType("tel");
			
			wsCall.execute(WSInfomovilMethods.GET_EXIST_DOMAIN);
			
		}
		
		else{
			AlertView alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
					getResources().getString(R.string.txtVerificaDomain));
			alerta.setDelegado(new AlertViewCloseDialog(alerta));
			alerta.show();
		}
	
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
	
	public void compraTel(){
		CompraModel compra = new CompraModel();
		compra.setPlan("DOMINIO TEL");
		compra.setMontoBruto("199");
		compra.setComision(30);
		compra.setPagoId("0");
		compra.setStatusPago("INTENTO PAGO");
		compra.setCodigoCobro(" ");
		compra.setReferencia(" ");
		compra.setTipoCompra("tel");
		DatosUsuario.getInstance().setCompra(compra);
		
		
		final ProgressDialog progress = ProgressDialog.show(this, getString(R.string.app_name),
				getResources().getString(R.string.txtCargandoDefault), true, false);
		
		
		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {
			
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
				
				if(status == WsInfomovilProcessStatus.EXITO){
					compraInfomovil.startBuyProcess("DOMINIO TEL");
				}
				
			}
		},this);	
		
		wsCall.execute(WSInfomovilMethods.COMPRA_INTENTO);
		
	}
	

	@Override
	public void initResume() {
		CompraInfomovil.configurar(this);
		compraInfomovil = new CompraInfomovil(this, new RespuestaCompra(this,"nombrarTel"));
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
	public void onBackPressed() {
		Intent intent = new Intent(this, CuentaActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
	

}

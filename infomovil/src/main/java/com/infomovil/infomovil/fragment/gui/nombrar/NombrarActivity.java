 package com.infomovil.infomovil.fragment.gui.nombrar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.appboy.Appboy;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.adapter.CustomArrayAdapter;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.ButtonWithFont;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.principal.MainActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivityFragment;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.model.Catalogo;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;

public class NombrarActivity extends InfomovilActivity {
	
	private EditTextWithFont txtNombre;
	private boolean estaCerrandoSesion;
	private WS_UserDomainVO userDomain;
	private String tipoDominio;
	private ButtonWithFont btnBuscar;
	
	//Para la publicacion de recursos
	private Spinner dominios_spinner;
	private TextWithFont lblDominio;
	
	
	public class PublicarRecursoFragment extends DialogFragment {
		
		TextWithFont lblNombreRecurso;
		String dominio;
		
		public PublicarRecursoFragment (String dominio){
			this.dominio = dominio;
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {

		    LayoutInflater inflater = LayoutInflater.from(getActivity());
		    final View rootView = inflater.inflate(R.layout.fragment_publicar, null);
		    lblNombreRecurso  = (TextWithFont)rootView.findViewById(R.id.labelRecurso);
		    lblNombreRecurso.setText(datosUsuario.getCatDominioActual().getDescripcion() + "/" + dominio);
		    lblNombreRecurso.setTypeface(null, Typeface.BOLD); 
		    
		    return new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.txtPublicar)
		    .setView(rootView)
		    .setCancelable(true)
		    .setPositiveButton(R.string.txtPublicar, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {
		    		publicarDominio();
		    	}
		    })
		    .setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {
		    		dialog.cancel();
		    	}
		    }).create();
		}
	}
	
	public void nombrarRecurso(View v) {
		estaCerrandoSesion = false;
		
		userDomain = datosUsuario.getUserDomainData();
		if (userDomain == null) {
			userDomain = new WS_UserDomainVO();
		}
		
		if(esAlfaNumerica(txtNombre.getText().toString())){
			userDomain.setDomainName(txtNombre.getText().toString());
			datosUsuario.setUserDomainData(userDomain);
			
			if (!txtNombre.getText().toString().equals(getResources().getString(R.string.txtMisitio))) {
				
				final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.app_name),
						getResources().getString(R.string.msgBuscandoSitio), true, false);
				
				
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
						
						if (status == WsInfomovilProcessStatus.SIN_EXITO) {
							FragmentManager fm = getFragmentManager();
							PublicarRecursoFragment publicarDialog = new PublicarRecursoFragment(txtNombre.getText().toString());
							publicarDialog.show(fm, "PublicarRecursoDialogo");
						}
						else {
							validarPublicacion(true);
						}
						
					}
				},this);
				
				//wsCall.setStrConsulta(userDomain.getDomainName());
				datosUsuario.setDomainType(tipoDominio);
				
				wsCall.execute(WSInfomovilMethods.GET_EXIST_DOMAIN);
			}else{
				alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
				getResources().getString(R.string.txtVerificaDomain));
				alerta.setDelegado(this);
				alerta.show();
			}
		}else{
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
			getResources().getString(R.string.txtVerificaDomain));
			alerta.setDelegado(this);
			alerta.show();
		}
		
	}
	
	public void nombrarSitio(View v) {
		estaCerrandoSesion = false;
		
		userDomain = datosUsuario.getUserDomainData();
		if (userDomain == null) {
			userDomain = new WS_UserDomainVO();
		}
		
		if(esAlfaNumerica(txtNombre.getText().toString())){
			userDomain.setDomainName(txtNombre.getText().toString());
			datosUsuario.setUserDomainData(userDomain);
			
			if (!txtNombre.getText().toString().equals(getResources().getString(R.string.txtMisitio))) {
				
				alerta = new AlertView(this, AlertViewType.AlertViewTypeActivity, 
						getResources().getString(R.string.msgNombrarSitio));
				alerta.setDelegado(this);
				alerta.show();
				
				WsInfomovilCall wsCall = new WsInfomovilCall(this,this);
				//wsCall.setStrConsulta(userDomain.getDomainName());
				datosUsuario.setDomainType(tipoDominio);
				
				wsCall.execute(WSInfomovilMethods.GET_EXIST_DOMAIN);
			}else{
				alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
				getResources().getString(R.string.txtVerificaDomain));
				alerta.setDelegado(this);
				alerta.show();
			}
		}else{
			alerta = new AlertView(this, AlertViewType.AlertViewTypeInfo, 
			getResources().getString(R.string.txtVerificaDomain));
			alerta.setDelegado(this);
			alerta.show();
		}
	}
  
	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		if (estaCerrandoSesion) {
			salirCuenta();
		}
		else {

			if(alerta != null)
				alerta.dismiss();

			if (status == WsInfomovilProcessStatus.SIN_EXITO) {
				publicarDominio();
			}
			else {
				Toast.makeText(NombrarActivity.this, R.string.txtErrorDomainNombrar, Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	public void publicarDominio(){
		
		datosUsuario = DatosUsuario.getInstance();
		userDomain.setPassword(datosUsuario.getPassword());
		userDomain.setEmail(datosUsuario.getNombreUsuario());
		userDomain.setSistema("ANDROID");
		
		userDomain.setTipoAction(5);
		userDomain.setPais(1);
		userDomain.setCanal(0);
		userDomain.setSucursal(1);
		userDomain.setFolio(0);
		userDomain.setStatus(1);
		
		userDomain.setDomainType(tipoDominio);				
		userDomain.setIdDominio((int)datosUsuario.getDomainid());		
		userDomain.setAccion("publicar");
		//infomovil.com por default
		userDomain.setIdCatDominio(tipoDominio.equals("tel")?"1":datosUsuario.getCatDominioActual().getId()+"");
		datosUsuario.setUserDomainData(userDomain);
		
		if(tipoDominio.equals("tel")){
			Intent intent = new Intent(this, PublicarActivity.class);
			intent.putExtra("tipoDominio", tipoDominio);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		else{	//recurso
			
			final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.app_name),
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
					
					if (status == WsInfomovilProcessStatus.EXITO) {	
						
						WS_UserDomainVO userDomain = datosUsuario.getUserDomainData();
						
						//Registro de Eventos y Atributos
						Appboy.getInstance(NombrarActivity.this).logCustomEvent(
						InfomovilApp.tipoInfomovil.equalsIgnoreCase("tel")?"Publicar Tel":"Publicar Subdominio");
						Appboy.getInstance(NombrarActivity.this).getCurrentUser()
						.setCustomUserAttribute("tipoDominio", InfomovilApp.tipoInfomovil);
						Appboy.getInstance(NombrarActivity.this).getCurrentUser()
						.setCustomUserAttribute("nombreDominio",userDomain.getDomainName());
						
						Intent intent = new Intent(NombrarActivity.this, 
						NombrarCompartirActivity.class);
						intent.putExtra("dominio",userDomain.getDomainName() );
						intent.putExtra("fechaFin",datosUsuario.getFechaFin() );
						intent.putExtra("tipoDominio", tipoDominio);
						startActivity(intent);
					}
					else {
						alerta = new AlertView(NombrarActivity.this,
						AlertViewType.AlertViewTypeInfo, 
						getResources().getString(R.string.
						txtErrorGenericoPublicacion));
						alerta.setDelegado(NombrarActivity.this);
						alerta.show();
					}
					
				}
			},this);
			wsCall.setUsrDomain(userDomain);
			wsCall.execute(WSInfomovilMethods.INSERT_USER_DOMAIN);
			
		}
		
	}
	
	/***
	 * 
	* @param existeDominio indica si publica o no
	 */
	public void validarPublicacion(final boolean existeDominio){
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.app_name),
				getResources().getString(R.string.txtCargandoDefault), true, false);
		
		WsInfomovilCall wsCall = new WsInfomovilCall( new WsInfomovilDelgate() {
			
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
				
				switch (status) {
					case EXITO:
						
						WS_UsuarioDominiosVO usuarioDominioFound = null;
						int index = 0;
						
						if(datosUsuario.getUsuarioDominios() != null && 
							(index = datosUsuario.getUsuarioDominios().indexOf(new WS_UsuarioDominiosVO(tipoDominio))) != -1){
							usuarioDominioFound = datosUsuario.getUsuarioDominios().get(index);
						}
						
						if(usuarioDominioFound != null && usuarioDominioFound.toString().equalsIgnoreCase("Publicado")){
							Toast.makeText(NombrarActivity.this, R.string.msgDominioYaPublicado, Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(NombrarActivity.this, CuentaActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.left_in, R.anim.left_out);
						}
						else{
							if(existeDominio){
								Toast.makeText(NombrarActivity.this, R.string.txtErrorDomainNombrar, 
										Toast.LENGTH_SHORT).show();
							}
							else{
								 Intent intent = new Intent(NombrarActivity.this, MenuPasosActivity.class);
								 startActivity(intent);
								 overridePendingTransition(R.anim.left_in, R.anim.left_out);
							}
						}
						break;
						
					case ERROR_DESCONOCIDO:
						if(existeDominio){
							Toast.makeText(NombrarActivity.this, R.string.txtErrorDomainNombrar, 
									Toast.LENGTH_SHORT).show();
						}
						else{
						    Intent intent = new Intent(NombrarActivity.this, MenuPasosActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.left_in, R.anim.left_out);
						}
						
						break;
	
					default:
						break;
				}
				
			}
		}, this);
		
		wsCall.execute(WSInfomovilMethods.GET_STATUS_DOMAIN);
		
	}
	
	@Override
	public void accionNo() {
		super.accionNo();
		
		if(alerta != null)
			alerta.dismiss();
		estaCerrandoSesion = false;
	}
	
	@Override
	public void accionSi()  {
		super.accionSi();
		
		if(alerta != null)
			alerta.dismiss();
		
		estaCerrandoSesion = true;
	}
	
	public void salirCuenta(){
		DatosUsuario.getInstance().borrarDatos();	
    	startActivity(new Intent(getBaseContext(), MainActivity.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    	finish();
   
	}

	public static boolean esAlfaNumerica(final String cadena) {
		String[] sPlit=null;
		if(cadena.trim()!=null && !(cadena.trim().isEmpty())){
			if(!(cadena.startsWith("-")) && cadena.indexOf('-')!=2 && cadena.indexOf('-')!=3 && !(cadena.endsWith("-"))){
				if (cadena.length()>=2 && cadena.length()<=63) {
					sPlit=cadena.split(" ");
					if(sPlit.length==1){
						if(!(cadena.toLowerCase().equals("infomovil"))){
							Pattern pat = Pattern.compile("^[_a-z0-9-]+([a-z0-9])$");
							Matcher mat = pat.matcher(cadena);
						     if (mat.matches()) {
						    	 return true;
						     } else {
						    	 return false;
						     }
						}else{
							return false;
						}
					}else{
						sPlit=null;
						return false;
					}
				}
				else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
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
		
		tipoDominio = getIntent().getStringExtra("tipoDominio") == null ? InfomovilApp.tipoInfomovil : 
		getIntent().getStringExtra("tipoDominio");		
		
		if(tipoDominio.equalsIgnoreCase("recurso")){
			cargarLayout(R.layout.nombrar_seleccionar_dominio_layout);
		}
		else{
			cargarLayout(R.layout.nombrar_layout);
		}
		
		
		acomodarVistaConTitulo(R.string.txtNombrarTitulo, R.drawable.plecaroja);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);	
		
		txtNombre = (EditTextWithFont) findViewById(R.id.txtNombre);
		btnBuscar = (ButtonWithFont) findViewById(R.id.btnBuscar);
		
		if(tipoDominio.equalsIgnoreCase("recurso")){
			
			dominios_spinner = (Spinner) findViewById(R.id.dominios_spinner);
			lblDominio = (TextWithFont) findViewById(R.id.lblDominio);
			
//			if(datosUsuario.getCatalogoDominios() == null || datosUsuario.getCatalogoDominios().size() == 0){
				final ProgressDialog progress = ProgressDialog.show(this, getString(R.string.app_name),
						getResources().getString(R.string.txtCargando), true, true);

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
						dominios_spinner.setAdapter(
						new CustomArrayAdapter<Catalogo>(NombrarActivity.this, datosUsuario.getCatalogoDominios()));

					}
				},this);
				wsCall.execute(WSInfomovilMethods.GET_CATALOGO_DOMINIOS);

//			}
//			else{
//				dominios_spinner.setAdapter(
//				new CustomArrayAdapter<Catalogo>(NombrarActivity.this, datosUsuario.getCatalogoDominios()));
//			}
			
			
			dominios_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					Catalogo catSelect = datosUsuario.getCatalogoDominios().get(position);
					lblDominio.setText(catSelect.getDescripcion() +"/");
					datosUsuario.setCatDominioActual(catSelect);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
		
		
	}

	@Override
	public void initResume() {
		modifico=true;
		
//		if(CuentaUtils.isPublicado()){
//			Intent intent = new Intent(context, InfomovilActivityFragment.class);
//			intent.putExtra("tipoMenuCrear", "menuCrear");
//			startActivity(intent);
//			overridePendingTransition(R.anim.left_in, R.anim.left_out);
//		}
		
		if(tipoDominio.equals("recurso")){
			btnBuscar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					nombrarRecurso(v);
				}
			});
		}
		else{
			btnBuscar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					nombrarSitio(v);
				}
			});
		}		
	}

	@Override
	public void guardarInformacion() {
		nombrarSitio(null);
		
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
	public void onBackPressed() {
		validarPublicacion(false);
		
	}

}

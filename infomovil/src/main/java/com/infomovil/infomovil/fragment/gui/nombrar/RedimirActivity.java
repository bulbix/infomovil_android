package com.infomovil.infomovil.fragment.gui.nombrar;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;

public class RedimirActivity extends InfomovilActivity  {
	
	private EditTextWithFont txtRedimirCodigo;
	private AlertView progressDialog = null;

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
		cargarLayout(R.layout.activity_redimir);
		acomodarVistaInicio();
		cambiarBackground();
		
		txtRedimirCodigo = (EditTextWithFont) findViewById(R.id.txtRedimirCodigo);
		
	}
	
	public void activar(View v) {

		if(StringUtils.isEmpty(txtRedimirCodigo.getText())){
			Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show();
			return;
		}

		datosUsuario = DatosUsuario.getInstance();
		WS_UserDomainVO userDomain = datosUsuario.getUserDomainData();
		userDomain.setEmail(datosUsuario.getNombreUsuario());
		userDomain.setPassword(datosUsuario.getPassword());
		userDomain.setDomainName(datosUsuario.getNombreUsuario());//el mismo nombre de la cuenta ya que esta en tramite		
		userDomain.setCodigoCamp(txtRedimirCodigo.getText().toString());	
		userDomain.setAccion("redimir");	

		WsInfomovilCall wsCall = new WsInfomovilCall(new WsInfomovilDelgate() {

			@Override
			public void respuestaObj(WSInfomovilMethods metodo,
					WsInfomovilProcessStatus status, Object obj) {
				// TODO Auto-generated method stub

			}

			@Override
			public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
				if(progressDialog!=null)
					progressDialog.dismiss();
			}

			@Override
			public void respuestaCompletada(WSInfomovilMethods metodo,
					long milisegundo, WsInfomovilProcessStatus status) {

				if(progressDialog!=null)
					progressDialog.dismiss();

				final WsInfomovilProcessStatus estatus = status;

				alerta = new AlertView(RedimirActivity.this, AlertViewType.AlertViewTypeInfo,
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

						if(estatus == WsInfomovilProcessStatus.EXITO){
							saltar(null);
						}
						else{
							alerta.dismiss();
						}
					}
				});
				alerta.show();

			}
		},this);

		progressDialog = new AlertView(this, AlertViewType.AlertViewTypeActivity, 
				getResources().getString(R.string.txtCargandoDefault));
		progressDialog.show();		
		wsCall.setUsrDomain(userDomain);		
		wsCall.execute(WSInfomovilMethods.INSERT_USER_CAMP);		
	}
	
	
	public void saltar(View v){
		InfomovilApp.setEnTramite(true);
		Intent intent = new Intent(RedimirActivity.this, MenuPasosActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	public void initResume() {
		ocultarMenuInferior();
		
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	
	
	
}

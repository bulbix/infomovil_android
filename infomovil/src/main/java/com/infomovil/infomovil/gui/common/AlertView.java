package com.infomovil.infomovil.gui.common;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

public class AlertView extends DialogFragment {
	
	public enum AlertViewType {
		AlertViewTypeActivity,
		AlertViewTypeInfo,
		AlertViewTypeQuestion,
		AlertViewTypeQuestion2
	}
	
	private AlertViewInterface delegado;
	private AlertViewType alertViewType;
	private String mensaje;
	private Activity ctx;
	
	public AlertView(Activity ctx, AlertViewType alertViewType,
			String mensaje) {
		super();
		this.ctx = ctx;
		this.alertViewType = alertViewType;
		this.mensaje = mensaje;
		
	}
	
	public void show(){
		FragmentManager fm = ctx.getFragmentManager();
		this.setCancelable(false);
		this.show(fm, "AlertViewDialog");
	}



	/**
	 * @return the delegado
	 */
	public AlertViewInterface getDelegado() {
		return delegado;
	}

	/**
	 * @param delegado the delegado to set
	 */
	public void setDelegado(AlertViewInterface delegado) {
		this.delegado = delegado;
	}
	

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = 
				new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
		
		builder.setCancelable(true)
		.setMessage(mensaje)
		.setTitle(R.string.app_name);
		
		int txtAccionOk = -1, txtAccionCancel = -1;
		
		if(alertViewType == AlertViewType.AlertViewTypeQuestion){
			txtAccionOk = R.string.txtSi;
			txtAccionCancel = R.string.txtNo;
		}
		else if(alertViewType == AlertViewType.AlertViewTypeQuestion2){
			txtAccionOk = R.string.txtAceptar;
			txtAccionCancel = R.string.txtCancelar;
		}
		
		
		switch(alertViewType){
			case AlertViewTypeQuestion:
			case AlertViewTypeQuestion2:
				builder.setPositiveButton(txtAccionOk,
						new DialogInterface.OnClickListener() {
			    	@Override
			    	public void onClick(DialogInterface dialog, int which) {
			    		delegado.accionSi();
			    	}
			    })
			    .setNegativeButton(txtAccionCancel, 
			    		new DialogInterface.OnClickListener() {
			    	@Override
			    	public void onClick(DialogInterface dialog, int which) {
			    		delegado.accionNo();
			    	}
			    }).setIcon(android.R.drawable.ic_dialog_info);
			break;
		case AlertViewTypeInfo:
			builder.setPositiveButton(R.string.txtAceptar,
					new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {
		    		delegado.accionAceptar();
		    	}
		    }).setIcon(android.R.drawable.ic_dialog_alert);
			break;
		case AlertViewTypeActivity:
			final ProgressDialog progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle(ctx.getResources().getString(R.string.app_name));
			progressDialog.setMessage(mensaje);
			progressDialog.setCancelable(false);
			return progressDialog;
		}
	   
	    return builder.create();
	    
	}
}

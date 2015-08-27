package com.infomovil.infomovil.gui.common;

import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;

public class AlertViewCloseDialog implements AlertViewInterface {
	
	AlertView dialog;
	
	public AlertViewCloseDialog(AlertView dialog){
		this.dialog = dialog;
	}
	
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
		dialog.dismiss();
		
	}

}

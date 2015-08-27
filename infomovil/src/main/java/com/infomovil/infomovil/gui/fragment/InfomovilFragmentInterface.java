package com.infomovil.infomovil.gui.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.infomovil.infomovil.common.utils.ButtonStyleShow;


public interface InfomovilFragmentInterface {
	public void toggleMenu(boolean visible);
	public void acomodarBotonesActivity(ButtonStyleShow typeButtonShow);
	public void acomodarVistaConTitulo(int idTitulo, int idImagen);
	public void loadFragment(InfomovilFragment fragmento, Bundle data, String name);
	public void returnFragment(String name);
	public void removeFragment(String name);
}

package com.infomovil.infomovil.menu;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.principal.CompartirActivity;
import com.infomovil.infomovil.gui.fragment.principal.MenuPasosActivity;
import com.infomovil.infomovil.model.DatosUsuario;

public class MenuCompartirActivity extends CompartirActivity {

	private TextWithFont labelDominio;	
	Button btnCompartir;

	@Override
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void initCreate() {
		cargarLayout(R.layout.menu_compartir);
		acomodarVistaConTitulo(R.string.txtTituloCompartir, R.drawable.plecamorada);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowAdd);
		datosUsuario = DatosUsuario.getInstance();
		
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		btnCompartir = (Button)findViewById(R.id.btnCompartir);
		Resources res = getResources();
		int botonWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 77, res.getDisplayMetrics());
        
        int botonHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 68, res.getDisplayMetrics());
        
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(botonWidth, botonHeight);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	btnCompartir.setBackgroundDrawable(getResources().getDrawable(R.drawable.compartiron));
        }
        else {
        	btnCompartir.setBackground(getResources().getDrawable(R.drawable.compartiron));
        }
		
		btnCompartir.setLayoutParams(lParams);
		labelDominio = (TextWithFont)findViewById(R.id.label2);
	
		labelDominio.setTextColor(getResources().getColor(R.color.colorMorado));
		labelDominio.setTypeface(null, Typeface.BOLD); 
		
		
		labelDominio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+nombreDominio));
				startActivity(browserIntent);
			}
		});
	}
	
	@Override
	public void initResume() {
		// TODO Auto-generated method stub
		super.initResume();
		labelDominio.setText(nombreDominio);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MenuPasosActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
}

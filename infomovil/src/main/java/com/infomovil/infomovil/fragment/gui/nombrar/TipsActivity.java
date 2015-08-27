package com.infomovil.infomovil.fragment.gui.nombrar;

import java.util.List;

import org.apache.http.protocol.HTTP;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.plus.PlusShare;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.fragment.principal.CompartirActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class TipsActivity extends CompartirActivity  {
	
	TestFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    private RelativeLayout layoutCompartir;

	@Override
	public void initCreate() {
		cargarLayout(R.layout.tips_layout);        
        acomodarVistaConTitulo(R.string.txtTipTitutlo, R.drawable.plecabermellon);
        acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
        
        mAdapter = new TestFragmentAdapter(getSupportFragmentManager(), getApplicationContext());
        layoutCompartir = (RelativeLayout) findViewById(R.id.layoutTipsCompartir);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
		
		mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        
		@Override
        public void onPageSelected(int position) {
			if(position == 2){
				  layoutCompartir.setVisibility(View.VISIBLE);
			}
			else{
				 layoutCompartir.setVisibility(View.GONE);
			}
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
		});		
	}

}

package com.infomovil.infomovil.fragment.gui.nombrar;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.model.DatosUsuario;

class TestFragmentAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[3];   

    private int mCount = CONTENT.length;

    public TestFragmentAdapter(FragmentManager fm, Context context) {    	
        super(fm);
        
        Resources resources = context.getResources();
        DatosUsuario datosUsuario = DatosUsuario.getInstance();
        String nombreDominio  = "";
       
        
        if(InfomovilApp.tipoInfomovil.equals("tel")){
        	 nombreDominio  = InfomovilApp.urlInfomovil;   
        	
        }
        else{
        	nombreDominio = InfomovilApp.urlInfomovil;
        }
        
        
        CONTENT[0] = resources.getString(R.string.txtTip1, nombreDominio);
        //CONTENT[1] = resources.getString(R.string.txtTip2);
        CONTENT[1] = resources.getString(R.string.txtTip3);
        CONTENT[2] = resources.getString(R.string.txtTip4);        
    }

    @Override
    public Fragment getItem(int position) {    	
        return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TestFragmentAdapter.CONTENT[position % CONTENT.length];
    }    

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
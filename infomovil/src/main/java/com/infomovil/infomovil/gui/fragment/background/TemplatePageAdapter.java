package com.infomovil.infomovil.gui.fragment.background;

import java.util.HashMap;
import java.util.Map;

import com.infomovil.infomovil.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TemplatePageAdapter extends FragmentPagerAdapter  {
	
	static Map<Integer, String> mapTemplates;
	
	
	static{
		mapTemplates = new HashMap<Integer,String>();
		mapTemplates.put(0,"Divertido");
		mapTemplates.put(1,"Clasico");
		mapTemplates.put(2,"Creativo");
		mapTemplates.put(3,"Moderno");
		mapTemplates.put(4,"Estandar1");
		mapTemplates.put(5,"Coverpage1azul");
	}
	
	public TemplatePageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return new TemplateFragment(mapTemplates.get(position));
	}

	@Override
	public int getCount() {
		return mapTemplates.size();
	}

}

package com.infomovil.infomovil.gui.principal;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.principal.PoliticasActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TerminosCondicionesFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_terminoscondiciones, container, false);	
		
		TextWithFont labelCondiciones2 = (TextWithFont)rootView.findViewById(R.id.labelCondiciones2);
		TextWithFont labelPoliticas2 = (TextWithFont)rootView.findViewById(R.id.labelPoliticas2);
		
		VerPoliticasClick verPoliticasClick = new VerPoliticasClick();
		labelCondiciones2.setOnClickListener(verPoliticasClick);
		labelPoliticas2.setOnClickListener(verPoliticasClick);
		
		return rootView;
	}
	
	private class VerPoliticasClick implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			verPoliticas(v);				
		}	
	}
	
	public void verPoliticas(View v) {
    	Intent intent = new Intent(getActivity(), PoliticasActivity.class);
    	if(v.getId() == R.id.labelCondiciones2) {
    		intent.putExtra("tituloVista", getResources().getString(R.string.txtTerminos));
    		intent.putExtra("vistaSeleccionada", 1);
    	}
    	else {
    		intent.putExtra("tituloVista", getResources().getString(R.string.txtPoliticas));
    		intent.putExtra("vistaSeleccionada", 2);
    	}
    	startActivity(intent);
    	getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
    	
    }

}

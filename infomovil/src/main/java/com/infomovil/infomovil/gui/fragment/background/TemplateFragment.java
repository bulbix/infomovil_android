package com.infomovil.infomovil.gui.fragment.background;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;




public class TemplateFragment extends Fragment implements WsInfomovilDelgate, AlertViewInterface {
	
	String template;
	static Map<String,Integer> mapTemplates;
	static Map<String,Integer> mapTitulo;
	static Map<String,String> mapBoton;
	static Map<String,Integer> mapTexto;
	boolean actualizacionCorrecta;
	
	static{
		mapTemplates = new HashMap<String,Integer>();
		mapTitulo = new HashMap<String,Integer>();
		mapBoton = new HashMap<String,String>();
		mapTexto = new HashMap<String,Integer>();
		String urlDivertido ="";
		String urlClasico = "";
		String urlCreativo = "";
		String urlModerno = "";
		String urlEstandar1 = "";
		String urlCoverpageazul1 ="";
		
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod")){
			urlDivertido = "http://infomovil.com/divertido?vistaPrevia=true";
			urlClasico = "http://infomovil.com/clasico?vistaPrevia=true";
			urlCreativo = "http://infomovil.com/creativo?vistaPrevia=true";
			urlModerno = "http://infomovil.com/moderno?vistaPrevia=true";
			urlEstandar1 = "http://infomovil.com/estandar1?vistaPrevia=true";
			urlCoverpageazul1 = "http://infomovil.com/coverpageazul1?vistaPrevia=true";
		}
		
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
			urlDivertido = "http://qa.mobileinfo.io:8080/divertido?vistaPrevia=true";
			urlClasico = "http://qa.mobileinfo.io:8080/clasico?vistaPrevia=true";
			urlCreativo = "http://qa.mobileinfo.io:8080/creativo?vistaPrevia=true";
			urlModerno = "http://qa.mobileinfo.io:8080/moderno?vistaPrevia=true";
			urlEstandar1 = "http://qa.mobileinfo.io:8080/estandar1?vistaPrevia=true";
			urlCoverpageazul1 = "http://qa.mobileinfo.io:8080/coverpageazul1?vistaPrevia=true";
		}
		
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev")){
			urlDivertido = "http://172.17.3.181:8080/templates/Divertido/index.html";
			urlClasico = "http://172.17.3.181:8080/templates/Clasico/index.html";
			urlCreativo = "http://172.17.3.181:8080/templates/Creativo/index.html";
			urlModerno = "http://172.17.3.181:8080/templates/Moderno/index.html";
			urlEstandar1 = "http://172.17.3.181:8080/templates/Estandar1/index.html";
			urlCoverpageazul1 = "http://172.17.3.181:8080/coverpageazul1?vistaPrevia=true";
		}
		
		mapTemplates.put("Divertido",R.drawable.temadivertido);
		mapTemplates.put("Clasico",R.drawable.temaclasico);
		mapTemplates.put("Creativo",R.drawable.temacreativo);
		mapTemplates.put("Moderno",R.drawable.temamoderno);
		mapTemplates.put("Estandar1",R.drawable.temaestandar1);
		mapTemplates.put("Coverpage1azul",R.drawable.temacoverpage1azul);
		
		mapTitulo.put("Divertido",R.string.txtEstiloDivertido);
		mapTitulo.put("Clasico",R.string.txtEstiloClasico);
		mapTitulo.put("Creativo",R.string.txtEstiloCreativo);
		mapTitulo.put("Moderno",R.string.txtEstiloModerno);
		mapTitulo.put("Estandar1",R.string.txtEstiloEstandar);
		mapTitulo.put("Coverpage1azul",R.string.txtEstiloPortadaAzul);
		
		mapBoton.put("Divertido",urlDivertido);
		mapBoton.put("Clasico",urlClasico);
		mapBoton.put("Creativo",urlCreativo);
		mapBoton.put("Moderno",urlModerno);
		mapBoton.put("Estandar1",urlEstandar1);
		mapBoton.put("Coverpage1azul",urlCoverpageazul1);
		
		mapTexto.put("Divertido",R.string.txtDescEstiloDivertido);
		mapTexto.put("Clasico",R.string.txtDescEstiloClasico);
		mapTexto.put("Creativo",R.string.txtDescEstiloCreativo);
		mapTexto.put("Moderno",R.string.txtDescEstiloModerno);
		mapTexto.put("Estandar1",R.string.txtDescEstiloEstandar);
		mapTexto.put("Coverpage1azul",R.string.txtDescEstiloPortadaAzul);
		
	}
	
	public TemplateFragment(){
		
	}
	
	public TemplateFragment(String template){
		this.template = template;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_template, container, false);
		
		if (this.template == null){
	        return rootView;
	    }

		ImageView imgIcono = (ImageView)rootView.findViewById(R.id.imgIcono);
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		imgIcono.setImageResource(datosUsuario.getDomainData().
		getTemplate().trim().equals(template)?R.drawable.temp_act:R.drawable.temp_inact);
		
		ImageView imgTemplate = (ImageView)rootView.findViewById(R.id.imagenTemplate);
		imgTemplate.setImageResource(mapTemplates.get(template));
		
		TextView txtTituloTemplate = (TextView)rootView.findViewById(R.id.TxtTituloTemplate);
		txtTituloTemplate.setText(getResources().getString(mapTitulo.get(template)));
		
		TextView txtDescripcion = (TextView)rootView.findViewById(R.id.TxtDescripcion);
		txtDescripcion.setText(getResources().getString(mapTexto.get(template)));
		
		Button btnVerEjemplo = (Button)rootView.findViewById(R.id.BtnVerEjemplo);
		btnVerEjemplo.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	//Toast.makeText(TemplateFragment.this.getActivity(), "La pagina: " + mapBoton.get(template), Toast.LENGTH_SHORT).show();
	        	Intent i = new Intent(getActivity(), VerEjemploPlantillaActivity.class );
	        	Bundle bundle = new Bundle();
	        	bundle.putString("MODO", "verejemplo");
	        	bundle.putString("URL", mapBoton.get(template).toString());
	        	i.putExtras(bundle);
	            startActivity(i);
	            
	        }
	    });
		
		return rootView;
	}
	

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		
		
	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
		// TODO Auto-generated method stub
		
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
//		if (actualizacionCorrecta) {
//			Intent intent = new Intent();
//			getActivity().setResult(Activity.RESULT_OK, intent);
//			
//		}
		
	}	

}

package com.infomovil.infomovil.billing;

import java.util.HashMap;
import java.util.Map;

import com.infomovil.infomovil.app.InfomovilApp;


public class Premium {  
	
	public static  String SKU_PREMIUM_MENSUAL;
	//public static  String SKU_PREMIUM_SEMESTRAL;
	public static  String SKU_PREMIUM_ANUAL;
	public static  String SKU_DOMINIO_TEL;
	
	public static Map<String,String> planpro;
	
	static { 
		
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || 
		   InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa") || 
		   InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod")){
			SKU_PREMIUM_MENSUAL = "planpro_mensual";
			//SKU_PREMIUM_SEMESTRAL = "planpro_semestral";
			SKU_PREMIUM_ANUAL = "planpro_anual";
			SKU_DOMINIO_TEL = "dominio.tel";
		}
		else{
			SKU_PREMIUM_MENSUAL = "prueba_tres";
			//SKU_PREMIUM_SEMESTRAL = "prueba_seis";
			SKU_PREMIUM_ANUAL = "prueba_doce";
			SKU_DOMINIO_TEL = "dominio_tel";
		}
		
		
		planpro = new HashMap<String, String>();
		planpro.put(SKU_PREMIUM_MENSUAL, "PP1");
		//planpro.put(SKU_PREMIUM_SEMESTRAL, "PP6");
		planpro.put(SKU_PREMIUM_ANUAL, "PP12");
		planpro.put(SKU_DOMINIO_TEL, "TEL");
	
	}
	

}

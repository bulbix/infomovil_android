package com.infomovil.infomovil.common.utils;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.util.Log;

public class StringUtils {
	
	public static String eliminarAcentos(String texto) {
		texto = texto.replace("á", "a");
		texto = texto.replace("é", "e");
		texto = texto.replace("í", "i");
		texto = texto.replace("ó", "o");
		texto = texto.replace("ú", "u");
		return texto;
	}
	
	public static String stripAccents(String strToStrip)
	{    
		String strStripped = null;
		//Normalizamos en la forma NFD (Canonical decomposition)
		strToStrip = Normalizer.normalize(strToStrip, Normalizer.Form.NFD);
		//Reemplazamos los acentos con una una expresi�n regular de Bloque Unicode
		strStripped = strToStrip.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		return strStripped;
	}
	
	public static boolean validaTelefono(String noTelefonico) {
		if (noTelefonico.matches("[0-9]{2,100}")) {
			return true;
		}
		return false;
	}
	
    public static String quitarSaltos(String cadena) {
    	return cadena.replaceAll("\n", "");
    }
    
    public static HashMap<String,String> convertToStringToHashMap(String text){
    	HashMap<String,String> data = new HashMap<String,String>();
    	Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
    	String[] split = p.split(text.replace(" ", ""));
    	for ( int i=1; i+2 <= split.length; i+=2 ){
    		data.put( split[i], split[i+1] );
    		Log.d(split[i], split[i+1]);
    	}
    	return data;
    }

}

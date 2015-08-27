package com.infomovil.infomovil.webservicecalls.util;

import android.annotation.SuppressLint;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {
	      private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	      //private static String key="1nf0P30d";
	  
	      @SuppressLint("TrulyRandom")
		public static String encryptDES(String encryptString, String encryptKey)
	              throws Exception {
	    	  if ( encryptString == null || encryptString.isEmpty() )
		    		encryptString = " ";
	          IvParameterSpec zeroIv = new IvParameterSpec(iv);
	          SecretKeySpec key = new SecretKeySpec(getKey(encryptKey).getBytes(), "DES");
	          Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	          cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
	         byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
	         return Base64.encode(encryptedData);
	     }
	      
	         
	    public static String decryptDES(String decryptString, String decryptKey)
	                throws Exception {
	    	if ( decryptString == null || decryptString.isEmpty() )
	    		return decryptString = "";
	    	
            byte[] byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(getKey(decryptKey).getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);

            return new String(decryptedData,"UTF-8");
        }
	    
	    public static String getKey(String key){
	    	String keyvalu="";
	    	if(key.equals("") || key==null ){
	    		keyvalu="1nf0P30d";
	    	}else{
	    		keyvalu=key;
	    	}
	    	//System.out.println("KEY:" + keyvalu);
	    	return keyvalu;
	    }
}

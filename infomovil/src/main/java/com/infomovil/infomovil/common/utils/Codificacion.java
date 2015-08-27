package com.infomovil.infomovil.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;



public class Codificacion {

	private static final String ALGO = "AES";
	private static final byte[] keyValue = new byte[] { 'S', '3', 'c', 'r',
			'E', 't', 'P', 'I', 'N', 'F', '0', 'M', 'I', 'V', '1', 'l' };

	public static String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}
	
	private static SecretKey desGenerateKey() throws Exception {
		KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
	    SecretKey myDesKey = keygenerator.generateKey();
		return myDesKey;
	}
	
	public static String desEncrypt(String Data) throws Exception {
		
	    SecretKey myDesKey = desGenerateKey();
		Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, myDesKey);
		byte[] encVal = Data.getBytes();
		byte[] encryptedValue = c.doFinal(encVal);
		return new String(encryptedValue);
	}

	public static String desDecrypt(String encryptedData) throws Exception {
		SecretKey myDesKey = desGenerateKey();
		Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, myDesKey);
		byte[] decordedValue = encryptedData.getBytes();
		byte[] decValue = c.doFinal(decordedValue);
		return new String(decValue);
	}
	
	
	
//	public static String desEncrypt(){
//		
//		String res=null;
//		try{
//			 
//		    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
//		    SecretKey myDesKey = keygenerator.generateKey();
// 
//		    Cipher desCipher;
// 
//		    // Create the cipher 
//		    desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
// 
//		    // Initialize the cipher for encryption
//		    desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
// 
//		    //sensitive information
//		    byte[] text = "No body can see me".getBytes();
// 
//		    System.out.println("Text [Byte Format] : " + text);
//		    System.out.println("Text : " + new String(text));
// 
//		    // Encrypt the text
//		    byte[] textEncrypted = desCipher.doFinal(text);
// 
//		    System.out.println("Text Encryted : " + textEncrypted);
// 
//		    // Initialize the same cipher for decryption
//		    desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
// 
//		    // Decrypt the text
//		    byte[] textDecrypted = desCipher.doFinal(textEncrypted);
// 
//		    System.out.println("Text Decryted : " + new String(textDecrypted));
// 
//		}catch(NoSuchAlgorithmException e){
//			e.printStackTrace();
//		}catch(NoSuchPaddingException e){
//			e.printStackTrace();
//		}catch(InvalidKeyException e){
//			e.printStackTrace();
//		}catch(IllegalBlockSizeException e){
//			e.printStackTrace();
//		}catch(BadPaddingException e){
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	

}

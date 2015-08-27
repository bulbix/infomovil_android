package com.infomovil.infomovil.billing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;

import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.billing.util.*;
import com.infomovil.infomovil.model.DatosUsuario;


public  class CompraInfomovil implements IabHelper.OnIabPurchaseFinishedListener {

	public static IabHelper billingHelper;	
	private Activity activity;
	private Purchasable purchasable;
	private String sku = "dummy_peso";
	private BigDecimal precio;
	private String skuReturn = "dummy_peso";
	public static final String ANDROID_TEST_PURCHASED="android.test.purchased";
	
	
	public static void configurar(final Activity activity) {
		String clave = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk6Za3efFqzWnN/mr0CFimbyCf8jcgQamhex97"
				+ "swugShO1/KJQQyU+opETLnCxZnDjaOP3yjVeCA1ldE8MkxkCYhafg4fUOzTxY5dUqMhVNjEChHSb3Egk21vpbDO"
				+ "aSAZQpGi22yng6ace6fnEP4t1evV/JDoLD8cQAwau+HDKZGqVsBDH50qDVgyUJ+6GNGTjRKSKaEoVzyOiLQehx5"
				+ "F4pjBqdaBItZZZBA5l9W5BbvZaw8X/VJxAFPjREln8PDR+Eu6W0dFUbAKmPNyTuLHB5oTnWA6KXzX1Zkjoc+xMpl"
				+ "GtvV4LjOT7a1A6VxB/vKLYO4HOZx6RfUdpWSMWwFjcwIDAQAB";
		
		if(billingHelper == null){
			billingHelper = new IabHelper(activity, clave);
			billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				
				@Override
				public void onIabSetupFinished(IabResult result) {
					if (result.isSuccess()) {//Cargos precios
						DatosUsuario datosUsuario =  DatosUsuario.getInstance();
						datosUsuario.setPrecioMensual(getPrice(Premium.SKU_PREMIUM_MENSUAL));
						datosUsuario.setPrecioAnual(getPrice(Premium.SKU_PREMIUM_ANUAL));
						datosUsuario.setPrecioTel(getPrice(Premium.SKU_DOMINIO_TEL));
					} else {
						//Toast.makeText(activity, "Configuracion Compra Incorrecta", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
		}
		else{
			DatosUsuario datosUsuario =  DatosUsuario.getInstance();
			datosUsuario.setPrecioMensual(getPrice(Premium.SKU_PREMIUM_MENSUAL));
			datosUsuario.setPrecioAnual(getPrice(Premium.SKU_PREMIUM_ANUAL));
			datosUsuario.setPrecioTel(getPrice(Premium.SKU_DOMINIO_TEL));
		}
	}
	
	public static String getPrice(String sku) {
		try {
			String[] skus = {Premium.SKU_PREMIUM_MENSUAL,Premium.SKU_PREMIUM_ANUAL,Premium.SKU_DOMINIO_TEL}; 
			
			Inventory inventory = billingHelper.queryInventory(true, Arrays.asList(skus));
			SkuDetails articulo = inventory.getSkuDetails(sku);
			return articulo.getPrice();
		} catch (Exception e) {
			return "";
		}
	}
	
	public CompraInfomovil(Activity activity){
		this.activity = activity;
	}
	
	public CompraInfomovil(Activity activity, Purchasable purchasable ) {
		this(activity);
		this.purchasable = purchasable;
	}
	

	public void startBuyProcess(String elemento){
		
		if(elemento.equalsIgnoreCase("PLAN PRO 1 MES")){
			sku = Premium.SKU_PREMIUM_MENSUAL;
			
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod")){
				precio = new BigDecimal(60);
			}
			else if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod") ){
				precio = new BigDecimal(9.90);
			}
			else{
				precio = new BigDecimal(1);
			}
			
		}
//		else if(elemento.equalsIgnoreCase("PLAN PRO 6 MESES")){
//			sku = Premium.SKU_PREMIUM_SEMESTRAL;
//			
//			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod")){
//				precio = new BigDecimal(350);
//			}
//			else if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod") ){
//				precio = new BigDecimal(11.0);
//			}
//			else{
//				precio = new BigDecimal(1);
//			}
//		}
		else if(elemento.equalsIgnoreCase("PLAN PRO 12 MESES")){
			sku = Premium.SKU_PREMIUM_ANUAL;
			
			if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("prod")){
				precio = new BigDecimal(599);
			}
			else if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("preprod") ){
				precio = new BigDecimal(12.0);
			}
			else{
				precio = new BigDecimal(1);
			}
			
		}
		else if(elemento.equalsIgnoreCase("DOMINIO TEL")){
			sku = Premium.SKU_DOMINIO_TEL;
			precio = new BigDecimal(199);
		}
		
		skuReturn = new String(sku);
		
		
		if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
			sku = ANDROID_TEST_PURCHASED;
		}
		
		
		try{
			Inventory inventory = billingHelper.queryInventory(true, null);
			
			if(inventory.hasPurchase(sku)){
				billingHelper.consumeAsync(inventory.getPurchase(sku), null);
			}
			else{
				purchaseItem(sku);
			}
			

		} catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public void consumirProducto(String sku){
		Inventory inventory;
		try {
			inventory = billingHelper.queryInventory(true, null);
			billingHelper.consumeAsync(inventory.getPurchase(sku), null);//Consumimos para mas compras
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * 
	 * @return si tiene algun item en la tienda
	 */
	public String getItemCompradoPendiente() {

		String resultItem  = "";

		Inventory inventory;
		try {

			inventory = billingHelper.queryInventory(true, null);
			
			if(inventory.hasPurchase(Premium.SKU_PREMIUM_MENSUAL)){
				resultItem = "PP_Mensual";
				
				if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
					consumirProducto(ANDROID_TEST_PURCHASED);
				}
				else{
					consumirProducto(Premium.SKU_PREMIUM_MENSUAL);
				}
				
			}
			else if(inventory.hasPurchase(Premium.SKU_PREMIUM_ANUAL)){
				resultItem = "PP_Anual";
				
				if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
					consumirProducto(ANDROID_TEST_PURCHASED);
				}
				else{
					consumirProducto(Premium.SKU_PREMIUM_ANUAL);
				}
			}
			else if(inventory.hasPurchase(Premium.SKU_DOMINIO_TEL)){
				resultItem = "tel";
				
				if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){
					consumirProducto(ANDROID_TEST_PURCHASED);
				}
				else{
					consumirProducto(Premium.SKU_DOMINIO_TEL);
				}
			}
			else{
				
				if(InfomovilApp.perfilInfomovil.equalsIgnoreCase("dev") || InfomovilApp.perfilInfomovil.equalsIgnoreCase("qa")){ //Para las pruebas en caso de error siempre dara plan mensual
					if(inventory.hasPurchase(ANDROID_TEST_PURCHASED)){
						resultItem = "tel";
						consumirProducto(ANDROID_TEST_PURCHASED);
					}
				}
				
			}
		

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return resultItem;
	}
	
	protected void purchaseItem(String sku) {
		billingHelper.launchPurchaseFlow(activity, sku, 10001, this);
	}

	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {
		if (result.isSuccess()) {
			purchasable.compraCorrecta(result, info, skuReturn, precio);
		} else{ 
			purchasable.compraFallida(skuReturn);
		}

	}

//	public void disposeBillingHelper() {
//		if (billingHelper != null) {
//			billingHelper.dispose();
//		}
//		billingHelper = null;
//	}

	

	public static IabHelper getBillingHelper() {
		return billingHelper;
	}


	public static void setBillingHelper(IabHelper billingHelper) {
		CompraInfomovil.billingHelper = billingHelper;
	}


}



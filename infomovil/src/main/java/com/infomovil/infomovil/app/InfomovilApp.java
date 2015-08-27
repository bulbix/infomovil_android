package com.infomovil.infomovil.app;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewConfiguration;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.server.ItemsDominioParse;


//@ReportsCrashes(formKey = "", // will not be used
//mailTo = "lfpradof@gmail.com",
//mode = ReportingInteractionMode.TOAST,
//resToastText = R.string.crash_toast_text)
public class InfomovilApp extends Application {
	
	//Variables de configuracion global
	//Habilita el ambiente de despliegue
	public final static String perfilInfomovil = "qa";
	public static String tipoInfomovil = "recurso";

	private static boolean enTramite;
	private static InfomovilActivity ultimoActivity;
	private static Context ctx;
	public static String urlInfomovil;
	public static boolean usuarioNuevo = false;

	@Override
	public void onCreate() {
		
		//ACRA.init(this);
		super.onCreate();
		ctx = getApplicationContext();
		
		if(perfilInfomovil.equals("qa")){
			urlInfomovil="qa.mobileinfo.io:8080/";
		}
		else if(perfilInfomovil.equals("dev")){
			urlInfomovil="localhost:8080/";
		}
		else{//prod preprod
			urlInfomovil="www.infomovil.com/";
		}
		
		try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }
		
	}
	

	public static Context getApp() {
		return ctx;
	}

	public static boolean isConnected(Context context) {
		
		ConnectivityManager connectivityManager;
	    NetworkInfo         wifiInfo;
	    NetworkInfo mobileInfo;
	    try
	    {
	        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

	        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
	        boolean serviceAvailable = (netInfo!=null && netInfo.isConnected())? true:false;

	        wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        if (wifiInfo.isConnected() || mobileInfo.isConnected() || serviceAvailable) 
	        { 
	            Log.i("Conexion Infomovil", "Conexion Establecida" + " : " + true);
	            return true; 
	        }
	    }
	    catch (Exception e)
	    {
	    	 Log.i("Conexion Infomovil", ": hasConnectivity: Exception: " + e.getMessage());
	    }
	    Log.i("Conexion Infomovil", "Conexion Establecida" + " : " + false);
	    return false;
	}
	
	public static boolean doesContainGsfPackage(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> list = pm.getInstalledPackages(0);

		for (PackageInfo pi : list) {
			if (pi.packageName.equals("com.google.android.gsf")) {
				return true; // ACCUWX.GSF_PACKAGE = com.google.android.gsf
			}

		}

		return false;
	}

	public static boolean servicesOK(Context context) {
		int isAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		} 
		else {
			return false;
		}
	}

	public static boolean isEnTramite() {
		return enTramite;
	}

	public static void setEnTramite(boolean enTramite) {
		InfomovilApp.enTramite = enTramite;
	}

	public static InfomovilActivity getUltimoActivity() {
		return ultimoActivity;
	}

	public static void setUltimoActivity(InfomovilActivity ultimoActivity) {
		InfomovilApp.ultimoActivity = ultimoActivity;
	}

	public static void mostrarOcultarMenu(InfomovilActivity currentActivity, Class<?> nextActivity) {
		currentActivity.finish();
		Intent intent = new Intent(InfomovilApp.ctx, nextActivity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		InfomovilApp.ctx.startActivity(intent);
		currentActivity.overridePendingTransition(R.anim.up_in, R.anim.up_out);
	}
}

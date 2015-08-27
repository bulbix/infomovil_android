package com.infomovil.infomovil.gui.fragment.principal;

import com.facebook.widget.FacebookDialog;
import com.google.android.gms.plus.PlusShare;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.view.View;

public class CompartirActivity extends InfomovilActivity  {
	
	protected String nombreDominio;
	
	public void compartirFacebook(View v){
		
		datosUsuario = DatosUsuario.getInstance();
		String mensaje = getResources().getString(R.string.txtComparte) + nombreDominio;
		
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
	        .setLink(nombreDominio)
	        .setName(getString(R.string.app_name))
	        .setDescription(mensaje)
	        .setPicture("http://infomovil.com/templates/Index/images/icn_infomovil_200.png")
	        .build();
			
			shareDialog.present();
			
		} else {
		    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=http://" + nombreDominio;
		    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		    this.startActivity(intent);
		}
	}
	
	public void compartirTwitter(View v){
		datosUsuario = DatosUsuario.getInstance();
		String mensaje = getResources().getString(R.string.txtComparte) + nombreDominio;
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/intent/tweet?text="+ mensaje));
		startActivity(browserIntent);
	}

	public void compartirGoogle(View v){
		datosUsuario = DatosUsuario.getInstance();
		String mensaje = getResources().getString(R.string.txtComparte);
		Intent shareIntent = new PlusShare.Builder(this)
        .setType("text/plain")
        .setText(mensaje+" "+nombreDominio)
        .setContentUrl(Uri.parse("http://"+nombreDominio))
        .getIntent();

		startActivityForResult(shareIntent, 0);
	}
	
	public void compartirEmail(View v){
		String mensaje = getResources().getString(R.string.txtComparte);
		datosUsuario = DatosUsuario.getInstance();
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		//i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.txtChecaSitio));
		i.putExtra(Intent.EXTRA_TEXT   , mensaje+" "+nombreDominio);
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			AlertView alertaError = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.configuracionCorreo));
			alertaError.setDelegado(this);
			alertaError.show();
		}
		
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void compartirSms(View v) {
		datosUsuario = DatosUsuario.getInstance();
		String mensaje = getResources().getString(R.string.txtComparte);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
	    {
	        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); //Need to change the build to API 19

	        Intent sendIntent = new Intent(Intent.ACTION_SEND);
	        sendIntent.setType("text/plain");
	        sendIntent.putExtra(Intent.EXTRA_TEXT,mensaje+" "+nombreDominio);

	        if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
	        {
	            sendIntent.setPackage(defaultSmsPackageName);
	        }
	       startActivity(sendIntent);

	    }
	    else //For early versions, do what worked for you before.
	    {
	        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
	        sendIntent.setData(Uri.parse("sms:"));
	        sendIntent.putExtra("sms_body", mensaje+" " + nombreDominio);
	        startActivity(sendIntent);
	    }

	}

	@SuppressWarnings("unused")
	public void compartirWhatsapp(View v){
		datosUsuario = DatosUsuario.getInstance();
		String mensaje = getResources().getString(R.string.txtComparte);
		Intent waIntent = new Intent(Intent.ACTION_SEND);
		waIntent.setType("text/plain");
		String text = mensaje+ nombreDominio;
		waIntent.setPackage("com.whatsapp");
		
		if (waIntent != null) {
			waIntent.putExtra(Intent.EXTRA_TEXT, text);//
		    startActivity(Intent.createChooser(waIntent, "Share with"));
		} else {
		   // Toast.makeText(this, "WhatsApp no instalado", Toast.LENGTH_SHORT).show();
			AlertView alertaError = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.noWhatsapp));
			alertaError.setDelegado(this);
			alertaError.show();
		}
	}
	
	
	

	@Override
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBotonEliminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initCreate() {
		
		
	}

	@Override
	public void initResume() {
		datosUsuario = DatosUsuario.getInstance();
		
		 if(InfomovilApp.tipoInfomovil.equals("tel")){
			nombreDominio = InfomovilApp.urlInfomovil;
		 }
		 else{
			 nombreDominio= InfomovilApp.urlInfomovil;
		 }
		
	}

	@Override
	public void guardarInformacion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub
		
	}

	
	

}

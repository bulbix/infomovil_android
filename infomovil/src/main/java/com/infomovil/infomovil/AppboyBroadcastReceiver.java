package com.infomovil.infomovil;

import android.support.v4.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.appboy.AppboyGcmReceiver;
import com.appboy.Constants;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.fragment.gui.nombrar.NombrarActivity;
import com.infomovil.infomovil.gui.fragment.background.ElegirTemplateActivity;
import com.infomovil.infomovil.gui.fragment.principal.MainActivity;
import com.infomovil.infomovil.gui.principal.InfomovilActivityFragment;
import com.infomovil.infomovil.menu.CuentaActivity;
import com.infomovil.infomovil.menu.EstadisticasActivity;
import com.infomovil.infomovil.menu.MenuCompartirActivity;
import com.infomovil.infomovil.menu.NoticiasActivity;

public class AppboyBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = String.format("%s.%s", Constants.APPBOY_LOG_TAG_PREFIX, AppboyBroadcastReceiver.class.getName());
	public static final String SOURCE_KEY = "source";
	public static final String DESTINATION_VIEW = "destination";
	public static final String HOME = "home";
	public static final String FEED = "feed";
	public static final String FEEDBACK = "feedback";

	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = context.getPackageName();
		String pushReceivedAction = packageName + ".intent.APPBOY_PUSH_RECEIVED";
		String notificationOpenedAction = packageName + ".intent.APPBOY_NOTIFICATION_OPENED";
		String action = intent.getAction();
		Log.d(TAG, String.format("Received intent with action %s", action));

		if (pushReceivedAction.equals(action)) {
			Log.d(TAG, "Received push notification.");
		} else if (notificationOpenedAction.equals(action)) {
			Bundle extras = new Bundle();
			extras.putString(DESTINATION_VIEW, FEED);
			extras.putString(AppboyGcmReceiver.CAMPAIGN_ID_KEY, intent.getStringExtra(AppboyGcmReceiver.CAMPAIGN_ID_KEY));

			// If a custom URI is defined, start an ACTION_VIEW intent pointing at the custom URI.
			// The intent returned from getStartActivityIntent() is placed on the back stack.
			// Otherwise, start the intent defined in getStartActivityIntent().
			if (intent.getStringExtra(Constants.APPBOY_PUSH_DEEP_LINK_KEY) != null) {
				Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra(Constants.APPBOY_PUSH_DEEP_LINK_KEY)));
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
				stackBuilder.addNextIntent(getStartActivityIntent(context, intent.getExtras()));
				stackBuilder.addNextIntent(uriIntent);
				stackBuilder.startActivities(extras);
			} else {
				context.startActivity(getStartActivityIntent(context, intent.getExtras()));
			}
		} else {
			Log.d(TAG, String.format("Ignoring intent with unsupported action %s", action));
		}
	}

	private Intent getStartActivityIntent(Context context, Bundle extras) {

		Intent activityIntent;
		if (extras != null && getBooleanExtra(extras.getBundle("extra"), "Noticias")) {
			activityIntent = new Intent(context, NoticiasActivity.class);
		} 
		else if (extras != null && getBooleanExtra(extras.getBundle("extra"), "Nombrar_Sitio")) {
			activityIntent = new Intent(context, NombrarActivity.class);
		}
		else if (extras != null && getBooleanExtra(extras.getBundle("extra"), "Cuenta")) {
			activityIntent = new Intent(context, CuentaActivity.class);
		}
		else if (extras != null && getBooleanExtra(extras.getBundle("extra"), "Compartir")) {
			activityIntent = new Intent(context, MenuCompartirActivity.class);
		}
		else if (extras != null && getBooleanExtra(extras.getBundle("extra"), "CrearEditar")) {
			activityIntent = new Intent(context, InfomovilActivityFragment.class);
			activityIntent.putExtra("tipoMenuCrear", "menuCrear");
		}
		else if (extras != null && getBooleanExtra(extras.getBundle("extra"), "Reportes")) {
			activityIntent = new Intent(context, EstadisticasActivity.class);
		}
		else {
			activityIntent = new Intent(context, MainActivity.class);
		}

		activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		activityIntent.putExtra(SOURCE_KEY, Constants.APPBOY);
		if (extras != null) {
			activityIntent.putExtras(extras);
		}
		return activityIntent;

	}

	public boolean getBooleanExtra(Bundle extras, String key) {
		boolean result = extras != null && Boolean.parseBoolean(extras.getString(key));
		Log.d("respuesta " + key, result+"");
		return result;
	}
}


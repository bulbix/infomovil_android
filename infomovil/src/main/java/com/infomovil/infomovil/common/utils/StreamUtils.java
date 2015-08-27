package com.infomovil.infomovil.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.gui.common.interfaces.Executable;
import com.infomovil.infomovil.gui.fragment.editar.GaleriaPaso2Activity;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class StreamUtils {

	public static String convertToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public static String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.NO_CLOSE);

		Log.e("infoLog", imageEncoded);
		return imageEncoded;
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	
	public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		Context context;
		WS_ImagenVO imagenActual;
		Executable executable;

		public DownloadImageTask(ImageView bmImage, Context context, WS_ImagenVO imagenActual, Executable... executable) {
			this.bmImage = bmImage;
			this.context = context;
			this.imagenActual = imagenActual;
			if(executable.length == 1)
			this.executable = executable[0];
		}
		
		@Override
		protected void onPreExecute() {
			bmImage.setImageResource(R.drawable.loader150);
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			imagenActual.setImagenPath(GaleriaPaso2Activity.saveToInternalSorage(context, result));
			if(executable != null){
				executable.execute();
			}
		}
	}	

}

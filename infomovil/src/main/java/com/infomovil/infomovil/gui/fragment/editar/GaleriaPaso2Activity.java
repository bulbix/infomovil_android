package com.infomovil.infomovil.gui.fragment.editar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import mx.com.infomovil.commons.CommonUtils;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.Session;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.android.lib.toolbox.ToolBox;
import com.infomovil.infomovil.android.lib.toolbox.media.MediaScannerNotifier;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.FacebookImage;
import com.infomovil.infomovil.common.utils.ScalingUtilities;
import com.infomovil.infomovil.common.utils.ScalingUtilities.ScalingLogic;
import com.infomovil.infomovil.common.utils.StreamUtils;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.AlertViewCloseDialog;
import com.infomovil.infomovil.gui.common.EditTextWithFont;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.fragment.InfomovilFragment;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WSInfomovilMethods;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilCall;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilDelgate;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.WsInfomovilProcessStatus;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DeleteItem;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;

import eu.janmuller.android.simplecropimage.CropImage;

public class GaleriaPaso2Activity extends InfomovilFragment implements WsInfomovilDelgate {

	boolean estaEditando, estaBorrando;
	private ImageView imagenElegida;
	private EditTextWithFont txtDescripcionImagen;
	private TextWithFont labelDescripcionImagen;
	private WS_ImagenVO imagenActual;
	private Vector<WS_ImagenVO> arrayImagenes;
	private File outFile = null;
	private boolean storeImage = false;
	private Bitmap imageBitmap;
//	private Button btnBorrarImagen;
	private boolean actualizacionCorrecta;
	Context context;
	private int banderaModifico;
	private File mFileTemp;
	private PhotoGaleryType galeryType;
	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	String galeryImage;
	Button botonBorrar;
	
	private boolean validarKeydown;
	

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.galeria_paso2_layout, container, false);
		imagenElegida = (ImageView) view.findViewById(R.id.imagenGaleriaPreview);
		txtDescripcionImagen = (EditTextWithFont) view.findViewById(R.id.textTitutloFoto);
		labelDescripcionImagen = (TextWithFont) view.findViewById(R.id.labelTituloFoto);
//		btnBorrarImagen = (Button) view.findViewById(R.id.btnBorrarImagen);
		botonBorrar = (Button) view.findViewById(R.id.btnBorrarImagen);
		Button botonTomarFoto = (Button) view.findViewById(R.id.botonGaleriaTomarFoto);
		botonTomarFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tomarFoto(v);
			}
		});
		
		Button botonUsarFoto = (Button) view.findViewById(R.id.botonGaleriaUsarFoto);
		botonUsarFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usarFoto(v);
			}
		});
		
		Button botonGaleriaFb = (Button) view.findViewById(R.id.botonGaleriaFacebook);
		botonGaleriaFb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (estaEditando) {
					final AlertView alertaEdicion = new AlertView(getActivity(), AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.msgEliminarImagen));
					alertaEdicion.setDelegado(new AlertViewCloseDialog(alertaEdicion));
					alertaEdicion.show();
				}
				else{
					new FacebookImage().getAlbums(GaleriaPaso2Activity.this, infomovilInterface);
				}
				
			}
		});
		
		
		return view;
	}

	@Override
	public void initCreate() {
		acomodarVistaConTitulo(getArguments().getInt("tituloVista"), R.drawable.plecaverde);
		modifico = false;
		context = activity;
		txtDescripcionImagen.addTextChangedListener(new EditTextWithFontWatcher());
		estaEditando = getArguments().getBoolean("editando", false);
		galeryType = (PhotoGaleryType) getArguments().getSerializable("galeryType");
		mensajeActualizacion = R.string.msgGuardarImagen;
		validarKeydown  = getArguments().getBoolean("validarKeyDown", true);
		
		actualizacionCorrecta = false;
		
		
	}

	private void acomodarVista() {
		if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
			acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
			txtDescripcionImagen.setVisibility(View.VISIBLE);
			labelDescripcionImagen.setVisibility(View.VISIBLE);
		} else {
			if (galeryType == PhotoGaleryType.PhotoGaleryTypeLogo) {
				acomodarBotones(ButtonStyleShow.ButtonStyleShowSave);
			} else {
				acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
			}
			txtDescripcionImagen.setVisibility(View.GONE);
			labelDescripcionImagen.setVisibility(View.GONE);
		}
	}

	private void mostrarImagen() {
		if (galeryType == PhotoGaleryType.PhotoGaleryTypeOffer) {
			if (estaEditando) {
				
				if (!StringUtils.isEmpty(imagenActual.getImagenPath())) {
					File f = new File(imagenActual.getImagenPath());
					Bitmap b;
					try {
						b = BitmapFactory.decodeStream(new FileInputStream(f));
						imageBitmap = b;
						imagenElegida.setImageBitmap(b);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					
					if (!StringUtils.isEmpty(imagenActual.getUrl())) {
						//imageBitmap = StreamUtils.decodeBase64(imagenActual.getImagenClobGaleria());
						//imagenElegida.setImageBitmap(imageBitmap);
						new StreamUtils.DownloadImageTask(imagenElegida,getActivity(),imagenActual).execute(imagenActual.getUrl());

					} 
				}
			}
		}
		else {
			
			if (!StringUtils.isEmpty(imagenActual.getImagenPath())) {
				try {
					File f = new File(imagenActual.getImagenPath());
					Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
					imageBitmap = b;
					imagenElegida.setImageBitmap(b);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				txtDescripcionImagen.setText(imagenActual.getDescripcionImagen());
				modifico = false;
			}
			else{
				if (!StringUtils.isEmpty(imagenActual.getUrl())) {
					//imageBitmap = StreamUtils.decodeBase64(imagenActual.getImagenClobGaleria());
					new StreamUtils.DownloadImageTask(imagenElegida, getActivity(),imagenActual).execute(imagenActual.getUrl());
					//imageBitmap = StreamU	tils.getBitmapFromURL(imagenActual.getUrl());
					//imagenElegida.setImageBitmap(imageBitmap);
					txtDescripcionImagen.setText(imagenActual.getDescripcionImagen());
					modifico = false;
				}
				
			}
		}

	}

	@Override
	public void guardarInformacion() {
		if (modifico && imageBitmap != null) {
			datosUsuario = DatosUsuario.getInstance();
			arrayImagenes = datosUsuario.getListaImagenes();
			if (estaEditando) {
				if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
					imagenActual = arrayImagenes.get(indiceSeleccionado);
				}
				else{
					imagenActual = new WS_ImagenVO();
				}

			} else {
				imagenActual = new WS_ImagenVO();
			}
			imagenActual.setDescripcionImagen(txtDescripcionImagen.getText()
					.toString());
			imagenActual.setImagenPath(saveToInternalSorage(getActivity(),imageBitmap));
			if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
				imagenActual.setTypeImage("IMAGEN");
			} else {
				imagenActual.setTypeImage("LOGO");
			}

			Log.d("infoLog", "djhcid hvid");
			Vector<WS_ImagenVO> imagenAux = new Vector<WS_ImagenVO>();

			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
			
			if (estaEditando && galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
				imagenAux = arrayImagenes;
				imagenAux.remove(indiceSeleccionado);
				imagenAux.add(indiceSeleccionado, imagenActual);
				wsCall.setActividad("Edito Galeria de Imagenes");
				wsCall.setLstImagen(imagenAux);
				wsCall.execute(WSInfomovilMethods.UPDATE_IMAGE);
			} else {
				imagenAux.add(imagenActual);
				wsCall.setActividad("Edito Logo");
				wsCall.setLstImagen(imagenAux);
				wsCall.execute(WSInfomovilMethods.INSERT_IMAGE);
			}
		} else {
			modifico = false;
			final AlertView alertModif = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtMensajeErrorImagen));
			alertModif.setDelegado(new AlertViewCloseDialog(alertModif));
			alertModif.show();
		}
	}

	public static String saveToInternalSorage(Context activity, Bitmap bitmapImage) {
		ContextWrapper cw = new ContextWrapper(activity);
		String nombreImagen = CommonUtils.generarRandomString(10) + ".jpg";
		// String nombreImagen = "imagen1.jpg";
		// path to /data/data/yourapp/app_data/imageDir
		File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, nombreImagen);

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("infoLog", "La ruta es " + directory.getAbsolutePath() + "/"
				+ nombreImagen);
		
//		if (storeImage) {
//			// We add the taken picture file to the gallery so user can see
//			// the image directly
//			new MediaScannerNotifier(context, directory.getAbsolutePath() + "/" + nombreImagen,
//					"image/*", false);
//		}
		
		return directory.getAbsolutePath() + "/" + nombreImagen;
	}

	public void tomarFoto(View v) {
		if (estaEditando) {
			final AlertView alertaEdicion = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.msgEliminarImagen));
			alertaEdicion.setDelegado(new AlertViewInterface() {
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
					alertaEdicion.dismiss();
				}
			});
			alertaEdicion.show();
		} else {
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
			} else {
				mFileTemp = new File(activity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
			}

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri mImageCaptureUri = null;
			mImageCaptureUri = Uri.fromFile(mFileTemp);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
//			start(intent, InfomovilConstants.REQUEST_CAMERA);
			activity.startActivityFromFragment(this, intent, InfomovilConstants.REQUEST_CAMERA);
		}

	}

	public void usarFoto(View v) {
		if (estaEditando) {
			final AlertView alertaEdicion = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.msgEliminarImagen));
			alertaEdicion.setDelegado(new AlertViewInterface() {
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
					alertaEdicion.dismiss();
				}
			});
			alertaEdicion.show();
		} else {
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
			} else {
				mFileTemp = new File(activity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
			}
			Intent gallerypickerIntent = new Intent(Intent.ACTION_PICK);
			gallerypickerIntent.setType("image/*");
			startActivityForResult(gallerypickerIntent,
					InfomovilConstants.REQUEST_OTHER);
		}
	}

	
	@Override
	public void checkBotonEliminar(){		

		if(estaEditando){
			botonBorrar.setVisibility(View.VISIBLE);
		}	
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		
		Session.getActiveSession().onActivityResult(this.getActivity(), requestCode, resultCode, data);
		 
		switch (requestCode) {
		case InfomovilConstants.REQUEST_CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				storeImage = true;
				startCropImage();
			}
			else {
				AlertView alertaAux = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorImagen));
				alertaAux.setDelegado(new AlertViewCloseDialog(alertaAux));
				alertaAux.show();
			}
			break;
		case InfomovilConstants.REQUEST_OTHER:
			if (resultCode == Activity.RESULT_OK) {
				storeImage = false;
				try {
					InputStream inputStream = activity.getContentResolver().openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();
					startCropImage();
				} catch (Exception e) {
					Log.e("infoLog", "Error while creating temp file", e);
				}
			}
			else {
				AlertView alertaAux = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorImagen));
				alertaAux.setDelegado(new AlertViewCloseDialog(alertaAux));
				alertaAux.show();
			}
			break;
		case InfomovilConstants.REQUEST_CROP:
			if (data == null) {
				if (resultCode == Activity.RESULT_CANCELED) {
					imagenElegida.setImageDrawable(getResources().getDrawable(R.drawable.previsualizador));
					modifico = false;
				}
				else {
					AlertView alertaAux = new AlertView(activity, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.txtErrorImagen));
					alertaAux.setDelegado(new AlertViewCloseDialog(alertaAux));
					alertaAux.show();
				}
			}
			else {
				if (resultCode == Activity.RESULT_OK) {
					String path = data.getStringExtra(CropImage.IMAGE_PATH);
					if (path == null) {
						return;
					}
					if (galeryType == PhotoGaleryType.PhotoGaleryTypeOffer) {
						datosUsuario.setAgregoImagenPromocion(true);
					}
					//System.gc();
					Bitmap bitmap = BitmapFactory.decodeFile(path);

					switch(galeryType){
						case PhotoGaleryTypeImage:
							infomovilInterface.loadFragment(this, null, "GaleriaPaso2");
							break;
						case PhotoGaleryTypeOffer:
							infomovilInterface.loadFragment(this, null, "PromocionesImagen");
							break;
						case PhotoGaleryTypeLogo:
							infomovilInterface.loadFragment(this, null, "ImagenLogo");
					}




					
					SaveImageFromCamera saveImage = new SaveImageFromCamera();
					saveImage.execute(bitmap);
				}
			}
			
			break;
		}

	}

	@SuppressWarnings("deprecation")
	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void startCropImage() {
		Log.d("infoLog", "el valor del path es " + mFileTemp.getPath());
		Intent intent = new Intent(activity, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.BITMAP_DATA, mFileTemp);
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 2);
		intent.putExtra(CropImage.ASPECT_Y, 3);

//		startActivityForResult(intent, InfomovilConstants.REQUEST_CROP);
		activity.startActivityFromFragment(this, intent, InfomovilConstants.REQUEST_CROP);
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public void accionAceptar() {
		super.accionAceptar();
		if (actualizacionCorrecta) {
			datosUsuario = DatosUsuario.getInstance();
			if (estaBorrando) {
				if (galeryType == PhotoGaleryType.PhotoGaleryTypeLogo) {
					datosUsuario.getEstatusEdicion()[1] = false;
					banderaModifico = 1;
					modifico = false;
					estaBorrando = false;
				} else if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
					txtDescripcionImagen.setText("");
					arrayImagenes = datosUsuario.getListaImagenes();
				}
				imagenElegida.setImageDrawable(getResources().getDrawable(R.drawable.previsualizador));
				imagenActual = null;
				modifico = false;
				estaBorrando = false;
				estaEditando = false;
				botonBorrar.setEnabled(false);
			} else {
				if (galeryType == PhotoGaleryType.PhotoGaleryTypeLogo) {
					datosUsuario.getEstatusEdicion()[1] = true;
//					Intent intent = new Intent();
//					intent.putExtra("banderaModifico", 1);
//					activity.setResult(Activity.RESULT_OK, intent);
					infomovilInterface.returnFragment("MenuCrear");
				}
				else {
					infomovilInterface.returnFragment("GaleriaPaso1");
				}
				
			}

		}
	}

	public void accionSi() {
		super.accionSi();
		if (estaBorrando) {
			
		} else {
			if(alerta != null)
				alerta.dismiss();

			guardarInformacion();
		}

	}

	public void accionNo() {
		super.accionNo();
		infomovilInterface.returnFragment("");
	}

	@Override
	public void keyDownAction() {
		super.keyDownAction();
		Log.d("infomovilLog", "Presionando back button");
		if (galeryType == PhotoGaleryType.PhotoGaleryTypeOffer) {
			if (imageBitmap != null) {
				datosUsuario.setRutaPromocion(saveToInternalSorage(getActivity(),imageBitmap));
			
			} else {
				datosUsuario.setRutaPromocion(imagenActual.getImagenPath());
			}
			
			Intent intent = new Intent();
			intent.putExtra("pathImage", galeryImage);
			activity.setResult(Activity.RESULT_OK, intent);
		}
//		else {
//			if (modifico) {
//				alerta = new AlertView(activity,
//						AlertViewType.AlertViewTypeQuestion, getResources()
//						.getString(R.string.txtPreguntaSalir));
//				alerta.setDelegado(this);
//				alerta.show();
//			} else {
//				if (galeryType == PhotoGaleryType.PhotoGaleryTypeLogo) {
//					Intent intent = new Intent();
//					intent.putExtra("banderaModifico", banderaModifico);
//					activity.setResult(Activity.RESULT_OK, intent);
//				}
//			}
//		}
	}

	@Override
	public void respuestaCompletada(WSInfomovilMethods metodo,
			long milisegundo, WsInfomovilProcessStatus status) {
		if(alerta != null)
			alerta.dismiss();

		if (status == WsInfomovilProcessStatus.EXITO) {
			Log.d("infoLog",
					"Imagen actualizada correctamente ****************");
			actualizacionCorrecta = true;
			modifico = false;
			imageBitmap = null;
			AlertView alertBien = new AlertView(activity,
					AlertViewType.AlertViewTypeInfo, getResources().getString(
							R.string.txtActualizacionCorrecta));
			alertBien.setDelegado(this);
			alertBien.show();
		} else {
			Log.d("infoLog",
					"Problema al actualizar la imagen *******************");
			actualizacionCorrecta = false;
			AlertView alertError = new AlertView(activity,
					AlertViewType.AlertViewTypeInfo, getResources().getString(
							R.string.txtErrorActualizacion));
			alertError.setDelegado(this);
			alertError.show();
		}

	}

	@Override
	public void respuestaObj(WSInfomovilMethods metodo,
			WsInfomovilProcessStatus status, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e) {
		// TODO Auto-generated method stub
		Log.d("infoLog", "Entrando al error");
		if(alerta != null)
			alerta.dismiss();

		AlertView mensajeError = new AlertView(activity,
				AlertViewType.AlertViewTypeInfo, getResources().getString(
						R.string.txtErrorGenerico));
		mensajeError.setDelegado(this);
		mensajeError.show();
	}

	private class SaveImageFromCamera extends
	AsyncTask<Bitmap, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(Bitmap... params) {
			Bitmap takenPictureData = params[0];
			// Intent data = params[0];

			if (takenPictureData != null) {
				// Android sets the picture in extra data.
				Bitmap bitAux = takenPictureData;
				Log.d("infoLog", "Redimensionando imagen " + sizeOf(bitAux));
				bitAux = ScalingUtilities.createScaledBitmap(bitAux, 450, bitAux.getHeight(), ScalingLogic.FIT);
				takenPictureData = bitAux;

			} else {
				// If we used EXTRA_OUTPUT we do not have the data so we get the
				// image
				// from the output.
				try {
					Bitmap bitAux = ToolBox.media_getBitmapFromFile(outFile);
					bitAux = ToolBox.media_correctImageOrientation(outFile
							.getAbsolutePath());
					bitAux = ScalingUtilities.createScaledBitmap(bitAux, 450, bitAux.getHeight(), ScalingLogic.FIT);
					takenPictureData = bitAux;
					
				} catch (Exception e) {
					ToolBox.dialog_showToastAlert(context,
							"Error getting saved taken picture.", false);
				}
			}

			if (storeImage) {
				//				// We add the taken picture file to the gallery so user can see
				//				// the image directly
				//				new MediaScannerNotifier(context, outFile.getAbsolutePath(),
				//						"image/*", false);


				MediaStore.Images.Media.insertImage(activity.getContentResolver(), takenPictureData, "" , "");


			}

			return takenPictureData;
		}

		protected void onPostExecute(Bitmap imageBitmapResult) {
			imageBitmap = imageBitmapResult;
			if (imageBitmap != null) {
				modifico = true;
				imagenElegida.setImageBitmap(imageBitmap);
			}
		}
	}

	protected int sizeOf(Bitmap data) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			return data.getRowBytes() * data.getHeight();
		} else {
			return data.getByteCount() / 1024;
		}
	}

	private class EditTextWithFontWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (imageBitmap != null) {
				modifico = true;
			}
			else {
				modifico = false;
			}
		}

	}

	@Override
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
//		if(alerta != null)
//			alerta.dismiss();

		if (galeryType == PhotoGaleryType.PhotoGaleryTypeOffer) {
			if(alerta != null)
				alerta.dismiss();
			imagenActual = new WS_ImagenVO();
			imagenElegida.setImageDrawable(getResources().getDrawable(R.drawable.previsualizador));
			modifico = false;
			datosUsuario.setAgregoImagenPromocion(false);
			datosUsuario.setRutaPromocion("");
			estaBorrando = false;
			estaEditando = false;
			//imageBitmap = null;
			botonBorrar.setEnabled(false);
		} else {
			WsInfomovilCall wsCall = new WsInfomovilCall(this,getActivity());
			if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
				wsCall.setActividad("Borro Galeria de Imagenes");
				imagenActual = datosUsuario.getListaImagenes().get(indiceSeleccionado);
			} else {
				wsCall.setActividad("Borro Logo");
				imagenActual = datosUsuario.getImagenLogo();
			}
			estaBorrando = true;
			WS_DeleteItem deleteItem = new WS_DeleteItem(datosUsuario.getDomainid(), imagenActual.getIdImagen());
			wsCall.setDeleteItem(deleteItem);
			wsCall.execute(WSInfomovilMethods.DELETE_IMAGE);
		}

	}


	@Override
	public void initResume() {
		
		datosUsuario = DatosUsuario.getInstance();
		arrayImagenes = datosUsuario.getListaImagenes();
		
		if (estaEditando) {
			modifico = false;
			indiceSeleccionado = getArguments().getInt("indiceTabla", 0);
			botonBorrar.setEnabled(true);
			if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
				imagenActual = arrayImagenes.get(indiceSeleccionado);
			} else if (galeryType == PhotoGaleryType.PhotoGaleryTypeLogo) {
				imagenActual = new WS_ImagenVO();

				if(datosUsuario.getImagenLogo() != null)
					imagenActual = datosUsuario.getImagenLogo();
			} else {
				imagenActual = new WS_ImagenVO();
				
				if(datosUsuario.getPromocionDominio() != null){//Solo cuando ya existe
					imagenActual.setUrl(datosUsuario.getPromocionDominio().getUrlImage());
				}
				
				imagenActual.setImagenPath(datosUsuario.getRutaPromocion());
			}

			mostrarImagen();
		} else {
			botonBorrar.setEnabled(false);
			if (galeryType == PhotoGaleryType.PhotoGaleryTypeOffer) {
				imagenActual = new WS_ImagenVO();
			}
		}
		if (galeryType == PhotoGaleryType.PhotoGaleryTypeImage) {
			if (arrayImagenes == null) {
				arrayImagenes = new Vector<WS_ImagenVO>();
				datosUsuario.setListaImagenes(arrayImagenes);
			}
		}

		// this.deleteFile(TEMP_PHOTO_FILE_NAME);
		mFileTemp = new File(activity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
		acomodarVista();

	}


	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub

	}

	@Override
	public void agregarNuevoItem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void organizarTabla() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public String validarCampos() {
		return "Correcto";
	}

	public boolean isValidarKeydown() {
		return validarKeydown;
	}

	public void setValidarKeydown(boolean validarKeydown) {
		this.validarKeydown = validarKeydown;
	}

}

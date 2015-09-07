package com.infomovil.infomovil.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infomovil.infomovil.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.infomovil.infomovil.constants.InfomovilConstants;
import com.infomovil.infomovil.gui.common.interfaces.Executable;
import com.infomovil.infomovil.gui.fragment.InfomovilFragmentInterface;
import com.infomovil.infomovil.gui.fragment.editar.SeleccionarImagenFragment;
import com.infomovil.infomovil.gui.fragment.editar.SeleccionarVideoActivity;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;

import eu.janmuller.android.simplecropimage.CropImage;

public class FacebookImage {

	public void getAlbums(final Fragment fragment, final InfomovilFragmentInterface infomovilInterface){

		Activity activity = fragment.getActivity();
		final ProgressDialog progressDialog = ProgressDialog.show(activity, activity.getString(R.string.app_name),
				activity.getResources().getString(R.string.txtCargandoDefault), true, false);

		final Session session = Session.getActiveSession();

		Bundle params = new Bundle();
		params.putString("fields", "count,name,id,picture");

		if (session!= null && session.isOpened()) {
			new Request(
					session,
					"/me/albums",
					params,
					HttpMethod.GET,
					new Request.Callback() {
						public void onCompleted(Response response) {

							progressDialog.dismiss();

							Log.i("Albums", response.toString());
							try {
								JSONArray albums =  response.getGraphObject().getInnerJSONObject().getJSONArray("data");
								List<ItemSelectModel> albumsList = new ArrayList<ItemSelectModel>();
								for (int i = 0; i < albums.length(); i++) {
									JSONObject album = albums.getJSONObject(i);
									String idAlbum = album.getString("id");
									String nameAlbum = album.getString("name");
									String urlAlbum = album.getJSONObject("picture").getJSONObject("data").getString("url");
									int countAlbum = album.getInt("count");
									ItemSelectModel albumItem = new ItemSelectModel();
									albumItem.setId(idAlbum);
									albumItem.setLinkImagen(urlAlbum);
									albumItem.setTitulo(nameAlbum + "--" + countAlbum + " imágenes");
									albumsList.add(albumItem);
									Log.i("album", album.toString());
								}

								if(albumsList.size() > 0){
									SeleccionarImagenFragment selectImagen = new SeleccionarImagenFragment(albumsList, new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											ItemSelectModel item = (ItemSelectModel)parent.getItemAtPosition(position);
											getPhotos(fragment, item.getId(), infomovilInterface);

										}
									},  R.string.txtAlbumFacebook);

									infomovilInterface.loadFragment(selectImagen, null, "SelectAlbumFb");
								}


							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					).executeAsync();
		}
		else{
			progressDialog.dismiss();
			//session.closeAndClearTokenInformation();
			Session.openActiveSession(activity, true, Arrays.asList("email","user_photos"), new Session.StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					Log.i("State Facebook", state.toString());
					if(state.isOpened()){
						getAlbums(fragment, infomovilInterface);
					}	
				}
			});
		}
	}

	public  void getPhotos(final Fragment fragment, final  String idAlbum, final InfomovilFragmentInterface infomovilInterface){
		
		final Activity activity = fragment.getActivity();
		
		final ProgressDialog progressDialog = ProgressDialog.show(activity, activity.getString(R.string.app_name),
				activity.getResources().getString(R.string.txtCargandoDefault), true, false);
		
		final Session session = Session.getActiveSession();
		if (session!= null && session.isOpened()) {
			new Request(
					session,
					String.format("/%s/photos",idAlbum),
					null,
					HttpMethod.GET,
					new Request.Callback() {
						public void onCompleted(Response response) {
							
							progressDialog.dismiss();
							
							Log.i("Photos", response.toString());
							try {
								List<ItemSelectModel> photosList = new ArrayList<ItemSelectModel>();
								JSONArray photos =  response.getGraphObject().getInnerJSONObject().getJSONArray("data");

								for(int k=0; k < photos.length(); k++){
									JSONArray images  = photos.getJSONObject(k).getJSONArray("images");
									Log.i("images", images.toString());
									JSONObject photo = images.getJSONObject(images.length()-1);
									//int idPhoto = photo.getInt("id");
									//String namePhoto = photo.getString("caption");
									String urlPhoto = photo.getString("source");
									ItemSelectModel photoItem = new ItemSelectModel();
									//photoItem.setId(idPhoto);
									photoItem.setLinkImagen(urlPhoto);
									photoItem.setLinkImagenFull(images.getJSONObject(0).getString("source"));
									photoItem.setTitulo("");
									photosList.add(photoItem);
									Log.i("photo", photo.toString());
								}

								if(photosList.size() > 0){
									SeleccionarImagenFragment selectImagen = new SeleccionarImagenFragment(photosList, new AdapterView.OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
										
											ItemSelectModel item = (ItemSelectModel)parent.getItemAtPosition(position);
											
											if (!StringUtils.isEmpty(item.getLinkImagenFull())) {
												final WS_ImagenVO imagenActual = new WS_ImagenVO();
												imagenActual.setUrl(item.getLinkImagenFull());
												
												final ProgressDialog progressDialogImage = ProgressDialog.show(activity, activity.getString(R.string.app_name),
														activity.getResources().getString(R.string.txtCargandoDefault), true, false);
												
												new StreamUtils.DownloadImageTask(new ImageView(activity), activity,imagenActual, new Executable(){

													@Override
													public void execute() {
														progressDialogImage.dismiss();
														Intent intent = new Intent(activity, CropImage.class);
														intent.putExtra(CropImage.IMAGE_PATH, imagenActual.getImagenPath());
														intent.putExtra(CropImage.BITMAP_DATA, new File(imagenActual.getImagenPath()));
														intent.putExtra(CropImage.SCALE, true);

														intent.putExtra(CropImage.ASPECT_X, 2);
														intent.putExtra(CropImage.ASPECT_Y, 3);
														
														activity.startActivityFromFragment(fragment, intent, InfomovilConstants.REQUEST_CROP);
														
													}
													
												}).execute(item.getLinkImagenFull());

											} 
											
											
											
											

										}
									},R.string.txtAddImage);


									infomovilInterface.loadFragment(selectImagen, null, "SelectPhotoFb");
								}


							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					).executeAsync();

		}
		else{
			progressDialog.dismiss();
		}
	}

}

package com.infomovil.infomovil.server;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.infomovil.infomovil.gui.common.interfaces.Progressable;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.infomovil.infomovil.server.common.JSONManager;

public class JSONParserVideos {
	
	private Progressable progress;	
	private String criterioBusqueda;
	private List<ItemSelectModel> listaVideos;
	private Integer maxResults;
	private boolean search;
	
	public JSONParserVideos(Progressable progress, String urlVideo, Integer maxResults, boolean search){
		this.progress = progress;
		this.criterioBusqueda = urlVideo.replace(" ", "%20");
		this.maxResults = maxResults;
		this.search = search;	
	}	
	
	class VideosTask extends AsyncTask<Void, Void, Void> {
		Progressable progress;

		public VideosTask(Progressable progress) {
			this.progress = progress;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				listaVideos = readJSONVideos(criterioBusqueda, maxResults, search);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progress.execute(listaVideos);
			progress.hideDialog();
		}
	}
	

	public void readAndParseJSONVideos() throws JSONException {		
		progress.showDialog();		
		new VideosTask(progress).execute();		
	}
	
	public static void main(String[] args) {
		JSONObject jObject = JSONManager
				.getJSONfromURL(String.format("https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&maxResults=%s&key=%s",
						"hola","20","AIzaSyBfyUsYuAxuiHu1IeOW-L6dbfkNfEIEIEU"));
		
		System.out.println(jObject);
		
	}
	

	private List<ItemSelectModel> readJSONVideos(String criterioBusqueda, Integer maxResults, boolean search) throws JSONException {
				
		List<ItemSelectModel> listaVideos = new ArrayList<ItemSelectModel>();
		
		JSONObject jObject = null;		
		
		if(search){
			jObject = JSONManager
					.getJSONfromURL(String.format("https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=video&maxResults=%s&key=%s",
							criterioBusqueda,maxResults,"AIzaSyBfyUsYuAxuiHu1IeOW-L6dbfkNfEIEIEU"));
			
			if (jObject != null) {		
				listaVideos = parseJSONVideos(jObject.getJSONArray("items"));
			}
			
		}
		else{			
			//Uri uri=Uri.parse(criterioBusqueda);
			String idVideo = getYoutubeVideoId(criterioBusqueda);		
			
			if(idVideo != null){
				jObject = JSONManager
						.getJSONfromURL("http://gdata.youtube.com/feeds/api/videos/"
								+ idVideo + "?alt=json");				
				if (jObject != null) {		
					listaVideos = parseJSONVideos(jObject.getJSONObject("entry"));
				}				
			}
			else{
				listaVideos = new ArrayList<ItemSelectModel>();
			}
			
		}

		return listaVideos;

	}
	
	public static String getYoutubeVideoId(String youtubeUrl){
		String video_id="";
		if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
		{

			String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
			CharSequence input = youtubeUrl;
			Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches())
			{
				String groupIndex1 = matcher.group(7);
				if(groupIndex1!=null && groupIndex1.length()==11)
					video_id = groupIndex1;
			}
		}
		return video_id;
	}

	private List<ItemSelectModel> parseJSONVideos(JSONArray videoArray) throws JSONException {
		
		List<ItemSelectModel> listaVideos = new ArrayList<ItemSelectModel>();		
		
		for (int i = 0; i < videoArray.length(); i++) {
			JSONObject json = videoArray.getJSONObject(i);
			listaVideos.addAll(parseJSONVideos(json));
		}
		
		return listaVideos;
	}
	
	private List<ItemSelectModel> parseJSONVideos(JSONObject json) throws JSONException {		
		List<ItemSelectModel> listaVideos = new ArrayList<ItemSelectModel>();		
		ItemSelectModel videoModel = new ItemSelectModel();		
		//videoModel.setAutor(json.getJSONArray("author").getJSONObject(0).getJSONObject("name").getString("$t"));
		videoModel.setTitulo(json.getJSONObject("snippet").getString("title"));
		videoModel.setDescripcion(json.getJSONObject("snippet").getString("description"));
		//videoModel.setLinkSolo(json.getJSONArray("link").getJSONObject(0).getString("href"));
		String video = json.getJSONObject("id").getString("videoId");
		videoModel.setLinkVideo("http://www.youtube.com/embed/"+ video);
		Log.d("Video", "http://www.youtube.com/embed/"+video);
		videoModel.setLinkImagen(json.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"));			
		listaVideos.add(videoModel);	
		return listaVideos;
	}

	public List<ItemSelectModel> getListaVideos() {
		return listaVideos;
	}

	public void setListaVideos(List<ItemSelectModel> listaVideos) {
		this.listaVideos = listaVideos;
	}

}

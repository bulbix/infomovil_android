package com.infomovil.infomovil.server.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONManager {
	public static JSONObject getJSONfromURL(String url) {
		String result = "";
		JSONObject json = null;
		try {
			HttpClient client = new DefaultHttpClient();
			// Perform a GET request to YouTube for a JSON list of all the
			// videos by a specific user
			HttpUriRequest request = new HttpGet(url);
			// Get the response that YouTube sends back
			HttpResponse response = client.execute(request);
			// Convert this response into a readable string
			result = com.infomovil.infomovil.common.utils.StreamUtils
					.convertToString(response.getEntity().getContent());
			json = new JSONObject(result);
		} catch (ClientProtocolException e) {
			Log.d("Feck", e.toString());
		} catch (IOException e) {
			Log.d("Feck", e.toString());
		} catch (JSONException e) {
			Log.d("Feck", e.toString());
		}

		return json;
	}
}

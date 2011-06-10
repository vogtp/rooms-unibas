package ch.almana.unibas.rooms.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.almana.unibas.rooms.helper.Logger;

public class RoomAccess {

	public static List<JSONObject> getRooms(long date, boolean pastEntries, String encoding) {
		String uri = getUri("http://rooms.medizin.unibas.ch/lib/json.php", date, pastEntries, encoding);
		try {
			return parseJson(uri);
		} catch (JSONException e) {
			Logger.e("Cannot get rooms", e);
		}
		return new ArrayList<JSONObject>();
	}

	private static String getUri(String uri, long time, boolean all, String encoding) {
		Logger.v("Loading >" + uri + "<");
		long start = System.currentTimeMillis();
		final DefaultHttpClient httpClient = new DefaultHttpClient();

//		if (all) {
//			uri = uri = "?alle=true";
//		} else {
//			uri = uri = "?alle=false";
//		}
		if (time > 1) {
			int t = (int) (time / 1000);
			uri = uri + "?datum=" + t;
		}

		HttpUriRequest request = new HttpGet(uri);
		BufferedHttpEntity bhe = null;
		BufferedReader content = null;
		try {
			HttpResponse response = httpClient.execute(request);
			bhe = new BufferedHttpEntity(response.getEntity());
			content = new BufferedReader(new InputStreamReader(bhe.getContent(), encoding));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = content.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();

		} catch (Exception e) {
			Logger.e("Error", e);
			return "";
		} finally {

			if (bhe != null) {
				try {
					bhe.consumeContent();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}

			long duration = System.currentTimeMillis() - start;
			duration /= 1000l;
			Logger.d(" search " + duration + " s");
		}
	}

	private static List<JSONObject> parseJson(String payload) throws JSONException {
		JSONArray jsonArray = new JSONArray(payload);
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			list.add(object);
		}
		return list;
	}
}

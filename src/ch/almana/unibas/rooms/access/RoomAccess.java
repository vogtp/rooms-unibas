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

import android.os.AsyncTask;
import ch.almana.unibas.rooms.helper.Logger;

public class RoomAccess extends AsyncTask<Long, Integer, List<JSONObject>> {

	public static final ArrayList<JSONObject> NO_ROOMS = new ArrayList<JSONObject>();

	public interface RoomAccessCallback {

		void updateProgress();

		void loadingFinished(List<JSONObject> result);

	}

	private RoomAccessCallback callback;

	public RoomAccess(RoomAccessCallback callback) {
		super();
		this.callback = callback;
	}

	@Override
	protected List<JSONObject> doInBackground(Long... params) {
		if (params.length < 1) {
			return NO_ROOMS;
		}
		String uri = getUri("http://rooms.medizin.unibas.ch/lib/json.php", params[0], "ISO-8859-1");
		publishProgress((Integer[]) null);
		try {
			return parseJson(uri);
		} catch (JSONException e) {
			Logger.e("Cannot get rooms", e);
		}
		return new ArrayList<JSONObject>();
	}

	@Override
	protected void onPostExecute(List<JSONObject> result) {
		super.onPostExecute(result);
		callback.loadingFinished(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		callback.updateProgress();
	}

	private String getUri(String uri, long time, String encoding) {
		Logger.v("Loading >" + uri + "<");
		long start = System.currentTimeMillis();
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		if (time > 1) {
			int t = (int) (time / 1000);
			uri = uri + "?datum=" + t;
		}

		HttpUriRequest request = new HttpGet(uri);
		BufferedHttpEntity bhe = null;
		BufferedReader content = null;
		try {
			HttpResponse response = httpClient.execute(request);
			publishProgress((Integer[]) null);
			bhe = new BufferedHttpEntity(response.getEntity());
			content = new BufferedReader(new InputStreamReader(bhe.getContent(), encoding));
			publishProgress((Integer[]) null);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = content.readLine()) != null) {
				sb.append(line);
				publishProgress((Integer[]) null);
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

	private List<JSONObject> parseJson(String payload) throws JSONException {
		JSONArray jsonArray = new JSONArray(payload);
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			publishProgress((Integer[]) null);
			JSONObject object = (JSONObject) jsonArray.get(i);
			list.add(object);
		}
		return list;
	}

}

package ch.almana.unibas.rooms.view.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.view.adapter.RoomAdapter;

public class RoomUnibasActivity extends ListActivity implements OnDateChangedListener {
	private DatePicker dpDate;
	private CheckBox cbShowAll;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_list);

		dpDate = (DatePicker) findViewById(R.id.dpDate);
		// cbShowAll = (CheckBox) findViewById(R.id.cbShowAll);
		//
		// cbShowAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// updateView();
		// }
		// });

		Calendar cal = Calendar.getInstance();
		dpDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), this);
		
		
	}

	@Override
	protected void onResume() {

		updateView();
		super.onResume();
	}

	private void updateView() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dpDate.getDayOfMonth());
		cal.set(Calendar.MONTH, dpDate.getMonth());
		cal.set(Calendar.YEAR, dpDate.getYear());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		boolean checked = false;// cbShowAll.isChecked();
		String uri = getUri("http://rooms.medizin.unibas.ch/lib/json.php", cal.getTimeInMillis(), checked);
		List<JSONObject> parseJson;
		try {
			parseJson = parseJson(uri);
			getListView().setAdapter(new RoomAdapter(this, parseJson));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getUri(String uri, long time, boolean all) {
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
			content = new BufferedReader(new InputStreamReader(bhe.getContent()));
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

	private List<JSONObject> parseJson(String payload) throws JSONException {
		JSONArray jsonArray = new JSONArray(payload);
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			list.add(object);
		}
		return list;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		updateView();
	}
}
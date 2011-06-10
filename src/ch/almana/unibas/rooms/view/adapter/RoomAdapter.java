package ch.almana.unibas.rooms.view.adapter;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.helper.Settings;

public class RoomAdapter extends BaseAdapter {


	private LayoutInflater layoutInflator;
	private List<JSONObject> entries;

	public RoomAdapter(Context ctx, List<JSONObject> entries) {
		super();
		layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.entries = entries;
	}

	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Object getItem(int position) {
		return entries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * {"start_date":"1307656800",""starttime":"1307685600","
	 * "room":"612 - Gynäkologie / Geburtsmedizin / Pädiatrie","
	 * "reservation_name":"Selbststudium",""teacher":"Laila
	 * Bürgin",""study_year":"""}
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = (convertView != null) ? convertView : createView(parent);
		JSONObject room = entries.get(position);
		Settings settings = Settings.getInstance();
		try {
			Date date = new Date(room.getLong("starttime") * 1000);
			((TextView) view.findViewById(R.id.tvTime)).setText(settings.getTimeFormat().format(date));
			// ((TextView)
			// view.findViewById(R.id.tvDate)).setText(settings.getDateFormat().format(date));
			((TextView) view.findViewById(R.id.tvRoom)).setText(room.getString("room"));
			((TextView) view.findViewById(R.id.tvLecturer)).setText(room.getString("teacher"));
			((TextView) view.findViewById(R.id.tvTitle)).setText(room.getString("reservation_name"));
		} catch (JSONException e) {
			Logger.e("Cannot access JSON", e);
		}
		return view;
	}

	private View createView(ViewGroup parent) {
		View item = (View) layoutInflator.inflate(R.layout.room_item, parent, false);
		return item;
	}
}

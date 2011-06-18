package ch.almana.unibas.rooms.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.access.RoomAccess;
import ch.almana.unibas.rooms.helper.Settings;
import ch.almana.unibas.rooms.model.IRoomModel;

public class RoomAdapter extends BaseAdapter {

	private LayoutInflater layoutInflator;
	private List<IRoomModel> entries;

	public RoomAdapter(Context ctx) {
		super();
		layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.entries = RoomAccess.NO_ROOMS;
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
		if (entries == RoomAccess.LOADING_ROOMS) {
			return (View) layoutInflator.inflate(R.layout.room_item_loading, parent, false);
		}
		if (entries == RoomAccess.NO_ROOMS) {
			return (View) layoutInflator.inflate(R.layout.room_item_noevents, parent, false);
		}
		View view = (convertView != null) ? convertView : createView(parent);
		IRoomModel room = entries.get(position);
		Settings settings = Settings.getInstance();
		((TextView) view.findViewById(R.id.tvTime)).setText(room.getStarttimeString());
		// ((TextView)
		// view.findViewById(R.id.tvDate)).setText(settings.getDateFormat().format(date));
		((TextView) view.findViewById(R.id.tvRoom)).setText(room.getRoom());
		((TextView) view.findViewById(R.id.tvLecturer)).setText(room.getLecturer());
		((TextView) view.findViewById(R.id.tvTitle)).setText(room.getTitle());
		return view;
	}

	private View createView(ViewGroup parent) {
		View item = (View) layoutInflator.inflate(R.layout.room_item, parent, false);
		return item;
	}

	public void setData(List<IRoomModel> rooms) {
		if (rooms == null) {
			entries = RoomAccess.NO_ROOMS;
		}
		entries = rooms;
	}
}

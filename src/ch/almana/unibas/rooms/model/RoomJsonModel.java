package ch.almana.unibas.rooms.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.helper.Settings;

public class RoomJsonModel extends IRoomModel {

	private final JSONObject room;

	public RoomJsonModel() {
		super();
		this.room = new JSONObject();
	}

	public RoomJsonModel(JSONObject jsonObject) {
		super();
		this.room = jsonObject;
	}

	@Override
	public CharSequence getRoom() {
		try {
			return room.getString("room");
		} catch (JSONException e) {
			Logger.e("Cannot access room", e);
			return NO_VALUE;
		}
	}

	public long getStarttime() {
		try {
			return room.getLong("starttime") * 1000;
		} catch (JSONException e) {
			Logger.e("Cannot access starttime ", e);
			return -1;
		}
	}

	@Override
	public CharSequence getStarttimeString() {
		long starttime = getStarttime();
		if (starttime < 0) {
			return NO_VALUE;
		}
		Date date = new Date(starttime);
		return Settings.getInstance().getTimeFormat().format(date);
	}

	@Override
	public CharSequence getLecturer() {
		try {
			return room.getString("teacher");
		} catch (JSONException e) {
			Logger.e("Cannot access teacher", e);
			return NO_VALUE;
		}
	}

	@Override
	public CharSequence getTitle() {
		try {
			return room.getString("reservation_name");
		} catch (JSONException e) {
			Logger.e("Cannot access reservation_name", e);
			return NO_VALUE;
		}
	}

	@Override
	public CharSequence getBuilding() {
		return "LZM";
	}

}

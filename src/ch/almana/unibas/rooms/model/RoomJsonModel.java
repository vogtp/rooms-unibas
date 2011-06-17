package ch.almana.unibas.rooms.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.helper.Settings;

public class RoomJsonModel implements IRoomModel {

	private static final CharSequence NO_VALUE = "n.a.";
	private JSONObject room;

	public RoomJsonModel() {
		super();
		this.room = new JSONObject();
	}

	public RoomJsonModel(JSONObject jsonObject) {
		super();
		this.room = jsonObject;
	}

	/* (non-Javadoc)
	 * @see ch.almana.unibas.rooms.model.IRoomModel#getRoom()
	 */
	@Override
	public CharSequence getRoom() {
		try {
			return room.getString("room");
		} catch (JSONException e) {
			Logger.e("Cannot access room", e);
			return NO_VALUE;
		}
	}

	/* (non-Javadoc)
	 * @see ch.almana.unibas.rooms.model.IRoomModel#getStarttime()
	 */
	@Override
	public CharSequence getStarttime() {
		try {
			Date date = new Date(room.getLong("starttime") * 1000);
			return Settings.getInstance().getTimeFormat().format(date);
		} catch (JSONException e) {
			Logger.e("Cannot access starttime", e);
			return NO_VALUE;
		}
	}

	/* (non-Javadoc)
	 * @see ch.almana.unibas.rooms.model.IRoomModel#getLecturer()
	 */
	@Override
	public CharSequence getLecturer() {
		try {
			return room.getString("teacher");
		} catch (JSONException e) {
			Logger.e("Cannot access teacher", e);
			return NO_VALUE;
		}
	}

	/* (non-Javadoc)
	 * @see ch.almana.unibas.rooms.model.IRoomModel#getTitle()
	 */
	@Override
	public CharSequence getTitle() {
		try {
			return room.getString("reservation_name");
		} catch (JSONException e) {
			Logger.e("Cannot access reservation_name", e);
			return NO_VALUE;
		}
	}

}

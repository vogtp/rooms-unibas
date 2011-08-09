package ch.unibas.urz.android.rooms.model;

import android.os.Bundle;

public class RoomBundleModel extends IRoomModel {

	private final Bundle bundle;

	public RoomBundleModel(Bundle bundleExtra) {
		super();
		this.bundle = bundleExtra;
	}

	private CharSequence getValue(String key) {
		CharSequence val = bundle.getCharSequence(key);
		if (val == null) {
			val = NO_VALUE;
		}
		return val;
	}

	@Override
	public CharSequence getRoom() {
		return getValue(KEY_ROOM);
	}

	@Override
	public CharSequence getBuilding() {
		return getValue(KEY_BUILDING);
	}

	@Override
	public CharSequence getStarttimeString() {
		return getValue(KEY_STARTTIMESTRING);
	}

	@Override
	public CharSequence getLecturer() {
		return getValue(KEY_LECTURER);
	}

	@Override
	public CharSequence getTitle() {
		return getValue(KEY_TITLE);
	}

}

package ch.unibas.urz.android.rooms.model;

import android.os.Bundle;

public abstract class IRoomModel {

	public static final CharSequence NO_VALUE = "n.a.";
	public static final String EXTRA_KEY = "roomModelExtras";

	protected static final String KEY_ROOM = "room";
	protected static final String KEY_BUILDING = "building";
	protected static final String KEY_STARTTIMESTRING = "starttimeString";
	protected static final String KEY_LECTURER = "lecturer";
	protected static final String KEY_TITLE = "title";

	public abstract CharSequence getRoom();

	public abstract CharSequence getBuilding();

	public abstract CharSequence getStarttimeString();
	
	public abstract CharSequence getLecturer();

	public abstract CharSequence getTitle();

	public Bundle getBundle() {
		Bundle b = new Bundle();
		b.putCharSequence(KEY_ROOM, getRoom());
		b.putCharSequence(KEY_BUILDING, getBuilding());
		b.putCharSequence(KEY_LECTURER, getLecturer());
		b.putCharSequence(KEY_TITLE, getTitle());
		b.putCharSequence(KEY_STARTTIMESTRING, getStarttimeString());
		return b;
	}

}
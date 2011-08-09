package ch.unibas.urz.android.rooms.helper;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	public static final String PREF_KEY_DEFAULT_BUILDING = "prefKeyDefaultBuilding";
	public static final String PREF_KEY_STEM_IS_SHOW_BUILDING = "showBuilding_";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE dd.MM.yyyy");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

	private static Settings instance;

	private Context context;

	public static void initInstance(Context ctx) {
		if (instance == null) {
			instance = new Settings(ctx);
		}
	}

	public Settings(Context ctx) {
		super();
		context = ctx;
	}

	protected SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static Settings getInstance() {
		return instance;
	}

	public SimpleDateFormat getDateFormat() {
		return DATE_FORMAT;
	}

	public SimpleDateFormat getTimeFormat() {
		return TIME_FORMAT;
	}

	public boolean isOnlyUseHours() {
		return getPreferences().getBoolean("prefKeyUseOnlyHours", true);
	}

	public boolean isEnableBetafeatures() {
		return "RaumDispo".equals(getPreferences().getString("prefKeyBetaKey", ""));
	}

	public boolean isShowBuilding(int building) {
		return getPreferences().getBoolean(PREF_KEY_STEM_IS_SHOW_BUILDING + building, true);
	}

	public int getDefaultBuilding() {
		try {
			return Integer.parseInt(getPreferences().getString(PREF_KEY_DEFAULT_BUILDING, "-1"));
		} catch (NumberFormatException e) {
			Logger.w("Cannot parse " + PREF_KEY_DEFAULT_BUILDING + " as int", e);
			return -1;
		}
	}

}

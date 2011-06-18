package ch.almana.unibas.rooms.helper;

import java.text.SimpleDateFormat;

public class Settings {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE dd.MM.yyyy");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

	private static Settings instance;

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public SimpleDateFormat getDateFormat() {
		return DATE_FORMAT;
	}

	public SimpleDateFormat getTimeFormat() {
		return TIME_FORMAT;
	}

}

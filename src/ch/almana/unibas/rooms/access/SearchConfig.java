package ch.almana.unibas.rooms.access;

import java.util.Calendar;

import ch.almana.unibas.rooms.helper.Settings;

public class SearchConfig {

	public final static int BUILDING_KOLLEGIENHAUS = 1;
	public final static int BUILDING_BIOZ = 12;
	public final static int BUILDING_LZM = -10;

	public enum RoomAccessType {
		RaumDispo, MedRooms
	}

	private int building;
	private Calendar cal;

	@SuppressWarnings("unused")
	private SearchConfig() {
	}

	public SearchConfig(int building) {
		super();
		this.setBuilding(building);
		cal = Calendar.getInstance();
	}
	
	public RoomAccessType getRoomAccessType() {
		if (building == BUILDING_LZM) {
			return RoomAccessType.MedRooms;
		}
		return RoomAccessType.RaumDispo;
	}

	public void setTimeInMillis(long time) {
		cal.setTimeInMillis(time);
	}

	public long getTimeInMillis() {
		return cal.getTimeInMillis();
	}

	public void set(int field, int value) {
		cal.set(field, value);
	}

	public int get(int field) {
		return cal.get(field);
	}

	public CharSequence getDateTimeFormated() {
		switch (getRoomAccessType()) {
		case MedRooms:
			return Settings.getInstance().getDateFormat().format(getTimeInMillis());
		case RaumDispo:
			StringBuilder sb = new StringBuilder(Settings.getInstance().getDateFormat().format(getTimeInMillis()));
			sb.append("\n");
			sb.append(Settings.getInstance().getTimeFormat().format(getTimeInMillis()));
			return sb.toString();
		default:
			return Settings.getInstance().getDateFormat().format(getTimeInMillis());
		}

	}

	private void move(int direction) {
		switch (getRoomAccessType()) {
		case MedRooms:
			cal.add(Calendar.DAY_OF_YEAR, direction);
			break;
		case RaumDispo:
			cal.add(Calendar.HOUR, direction);
			break;
		default:
			cal.add(Calendar.DAY_OF_YEAR, direction);
			break;
		}
	}

	public void moveNext() {
		move(1);
	}

	public void movePref() {
		move(-1);
	}

	public Calendar getCalendar() {
		return cal;
	}

	public void setBuilding(int building) {
		this.building = building;
	}

	public int getBuilding() {
		return building;
	}

}

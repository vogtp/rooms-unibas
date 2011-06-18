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
		return Settings.getInstance().getDateFormat().format(getTimeInMillis());
	}

	public void moveNext() {
		cal.add(Calendar.DAY_OF_YEAR, 1);
	}

	public void movePref() {
		cal.add(Calendar.DAY_OF_YEAR, -1);
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

package ch.almana.unibas.rooms.access;

import java.util.Calendar;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import ch.almana.unibas.rooms.R;
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
	private String buildingName;

	@SuppressWarnings("unused")
	private SearchConfig() {
	}

	public SearchConfig(String buildingName, int building) {
		super();
		this.buildingName = buildingName;
		this.building = building;
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
		return getCalendar().getTimeInMillis();
	}

	public void set(int field, int value) {
		cal.set(field, value);
	}

	public int get(int field) {
		return getCalendar().get(field);
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

	public boolean isSetTime() {
		switch (getRoomAccessType()) {
		case MedRooms:
			return false;
		case RaumDispo:
			return true;
		default:
			return false;
		}
	}

	public void moveNext() {
		move(1);
	}

	public void movePref() {
		move(-1);
	}

	public Calendar getCalendar() {
		if (Settings.getInstance().isOnlyUseHours()) {
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		return cal;
	}

	public void setBuilding(int building) {
		this.building = building;
	}

	public int getBuilding() {
		return building;
	}

	public String getBuildingName() {
		return buildingName;
	}

	@Override
	public String toString() {
		return buildingName;
	}


	public static SpinnerAdapter getBuildingAdapter(Context ctx) {
		ArrayAdapter<SearchConfig> buildingAdapter = new ArrayAdapter<SearchConfig>(ctx, android.R.layout.simple_spinner_item);
		buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingAdapter.add(new SearchConfig(ctx.getString(R.string.building_lzm), SearchConfig.BUILDING_LZM));
		if (Settings.getInstance().isEnableBetafeatures()) {
			buildingAdapter.add(new SearchConfig(ctx.getString(R.string.building_kollegienhaus), SearchConfig.BUILDING_KOLLEGIENHAUS));
		}
		return buildingAdapter;
	}

}

package ch.unibas.urz.android.rooms.access;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import ch.unibas.urz.android.rooms.R;
import ch.unibas.urz.android.rooms.helper.Settings;

public class SearchConfig {

	public final static int BUILDING_KOLLEGIENHAUS = 1;
	public final static int BUILDING_BIOPHARMAZENTRUM = 12;
	public final static int BUILDING_LZM = -10;

	public enum RoomAccessType {
		RaumDispo, MedRooms
	}

	private int buildingId;
	private Calendar cal;
	private String buildingName;

	@SuppressWarnings("unused")
	private SearchConfig() {
	}

	public SearchConfig(String buildingName, int building) {
		super();
		this.buildingName = buildingName;
		this.buildingId = building;
		cal = Calendar.getInstance();
	}
	
	public RoomAccessType getRoomAccessType() {
		if (buildingId == BUILDING_LZM) {
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
			sb.append(" ");
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

	public void setBuildingId(int buildingID) {
		this.buildingId = buildingID;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	@Override
	public String toString() {
		return buildingName;
	}

	public static List<SearchConfig> getBuildings(Context ctx) {
		List<SearchConfig> buildingsList = new ArrayList<SearchConfig>();
		buildingsList.add(new SearchConfig(ctx.getString(R.string.building_lzm), SearchConfig.BUILDING_LZM));
		buildingsList.add(new SearchConfig(ctx.getString(R.string.building_kollegienhaus), SearchConfig.BUILDING_KOLLEGIENHAUS));
		buildingsList.add(new SearchConfig(ctx.getString(R.string.building_biopharmazentrum), SearchConfig.BUILDING_BIOPHARMAZENTRUM));
		// buildingsList.add(new SearchConfig("", ));
		// buildingsList.add(new SearchConfig("", ));
		// buildingsList.add(new SearchConfig("", ));
		// buildingsList.add(new SearchConfig("", ));
		
		Comparator<SearchConfig> comparator = new Comparator<SearchConfig>() {

			@Override
			public int compare(SearchConfig s1, SearchConfig s2) {
				return s1.getBuildingName().compareTo(s2.getBuildingName());
			}

		};
		Collections.sort(buildingsList, comparator);
		return buildingsList;
	}

}

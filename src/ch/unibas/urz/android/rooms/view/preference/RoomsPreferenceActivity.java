package ch.unibas.urz.android.rooms.view.preference;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import ch.unibas.urz.android.rooms.R;
import ch.unibas.urz.android.rooms.access.SearchConfig;
import ch.unibas.urz.android.rooms.helper.Settings;

public class RoomsPreferenceActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}

	@Override
	protected void onResume() {
		
		Settings settings = Settings.getInstance();
		
		PreferenceCategory pc = (PreferenceCategory) findPreference("prefKeyManageBuildingsCat");
		List<SearchConfig> buildings = SearchConfig.getBuildings(this);
		ListPreference defaultBuilding = (ListPreference) findPreference(Settings.PREF_KEY_DEFAULT_BUILDING);
		ArrayList<String> entries = new ArrayList<String>();
		ArrayList<String> entryValues = new ArrayList<String>();
		for (SearchConfig searchConfig : buildings) {
			CheckBoxPreference preference = new CheckBoxPreference(this);
			int id = searchConfig.getBuildingId();
			preference.setKey(Settings.PREF_KEY_STEM_IS_SHOW_BUILDING + id);
			preference.setChecked(settings.isShowBuilding(id));
			preference.setTitle(searchConfig.getBuildingName());
			pc.addPreference(preference);
			if (settings.isShowBuilding(id)) {
				entries.add(searchConfig.getBuildingName());
				entryValues.add(Integer.toString(id));
			}
		}
		int size = entries.size();
		if (size>0) {
			String[] e = new String[size];
			String[] ev = new String[size];
			for (int i = 0; i < size; i++) {
				e[i] = entries.get(i);
				ev[i] = entryValues.get(i);
			}
			defaultBuilding.setEntries(e);
			defaultBuilding.setEntryValues(ev);
		}
		super.onResume();
	}

}

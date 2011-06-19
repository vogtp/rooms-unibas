package ch.almana.unibas.rooms.view.preference;

import java.util.List;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.access.SearchConfig;
import ch.almana.unibas.rooms.helper.Settings;

public class RoomsPreferenceActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		PreferenceCategory pc = (PreferenceCategory) findPreference("prefKeyManageBuildingsCat");
		List<SearchConfig> buildings = SearchConfig.getBuildings(this);
		for (SearchConfig searchConfig : buildings) {
			CheckBoxPreference preference = new CheckBoxPreference(this);
			preference.setKey(Settings.PREF_KEY_STEM_IS_SHOW_BUILDING + searchConfig.getBuildingId());
			preference.setTitle(searchConfig.getBuildingName());
			pc.addPreference(preference);
		}

	}

}

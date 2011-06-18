package ch.almana.unibas.rooms.preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import ch.almana.unibas.rooms.R;

public class RoomsPreferenceActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}

}

package ch.unibas.urz.android.rooms.application;

import android.app.Application;
import ch.unibas.urz.android.rooms.helper.Settings;

public class RoomsApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Settings.initInstance(getApplicationContext());
	}

}

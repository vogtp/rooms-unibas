package ch.almana.unibas.rooms.application;

import android.app.Application;
import ch.almana.unibas.rooms.helper.Settings;

public class RoomsApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Settings.initInstance(getApplicationContext());
	}

}

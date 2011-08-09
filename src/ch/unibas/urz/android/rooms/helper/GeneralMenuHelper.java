package ch.unibas.urz.android.rooms.helper;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import ch.unibas.urz.android.rooms.view.preference.RoomsPreferenceActivity;

public class GeneralMenuHelper {

	public static boolean onOptionsItemSelected(Context ctx, MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case ch.unibas.urz.android.rooms.R.id.itemSettings:
			i = new Intent(ctx, RoomsPreferenceActivity.class);
			ctx.startActivity(i);
			return true;


		default:
			return false;

		}

	}

}

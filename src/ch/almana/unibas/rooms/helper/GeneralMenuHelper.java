package ch.almana.unibas.rooms.helper;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.preference.RoomsPreferenceActivity;

public class GeneralMenuHelper {

	public static boolean onOptionsItemSelected(Context ctx, MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.itemSettings:
			i = new Intent(ctx, RoomsPreferenceActivity.class);
			ctx.startActivity(i);
			return true;


		default:
			return false;

		}

	}

}

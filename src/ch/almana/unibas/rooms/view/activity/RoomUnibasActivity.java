package ch.almana.unibas.rooms.view.activity;

import java.util.Calendar;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.access.RoomAccess;
import ch.almana.unibas.rooms.view.adapter.RoomAdapter;

public class RoomUnibasActivity extends ListActivity implements OnDateChangedListener {
	private DatePicker dpDate;
	private CheckBox cbShowAll;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_list);

		dpDate = (DatePicker) findViewById(R.id.dpDate);
		// cbShowAll = (CheckBox) findViewById(R.id.cbShowAll);
		//
		// cbShowAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// updateView();
		// }
		// });

		Calendar cal = Calendar.getInstance();
		dpDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), this);
		
		
	}

	@Override
	protected void onResume() {

		updateView();
		super.onResume();
	}

	private void updateView() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dpDate.getDayOfMonth());
		cal.set(Calendar.MONTH, dpDate.getMonth());
		cal.set(Calendar.YEAR, dpDate.getYear());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		boolean checked = false;// cbShowAll.isChecked();

		getListView().setAdapter(new RoomAdapter(this, RoomAccess.getRooms(cal.getTimeInMillis(), checked, "ISO-8859-1")));
	}



	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		updateView();
	}
}
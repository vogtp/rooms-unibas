package ch.almana.unibas.rooms.view.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.access.RoomAccess;
import ch.almana.unibas.rooms.view.adapter.RoomAdapter;
import ch.almana.unibas.rooms.view.widget.DateButton;
import ch.almana.unibas.rooms.view.widget.DateButton.OnDateChangedListener;

public class RoomUnibasActivity extends ListActivity implements OnDateChangedListener {
	private CheckBox cbShowAll;
	private DateButton dateButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_list);

		dateButton = (DateButton) findViewById(R.id.dateButton1);
		dateButton.setOnDateChangedListener(this);

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

	}

	@Override
	protected void onResume() {

		updateView();
		super.onResume();
	}

	private void updateView() {
		boolean checked = false;// cbShowAll.isChecked();
		getListView().setAdapter(new RoomAdapter(this, RoomAccess.getRooms(dateButton.getDate(), checked, "ISO-8859-1")));
	}

	@Override
	public void dateChanged(long date) {
		updateView();
	}

}
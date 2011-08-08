package ch.almana.unibas.rooms.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.model.IRoomModel;
import ch.almana.unibas.rooms.model.RoomBundleModel;

public class RoomDetailActivity extends Activity {

	private IRoomModel roomModel;
	private TextView tvTime;
	private TextView tvTitle;
	private TextView tvLecturer;
	private TextView tvRoom;
	private TextView tvBuilding;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.room_detail);

		tvTime = (TextView)findViewById(R.id.tvTime);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvLecturer = (TextView)findViewById(R.id.tvLecturer);
		tvBuilding = (TextView) findViewById(R.id.tvBuilding);
		tvRoom = (TextView) findViewById(R.id.tvRoom);

		roomModel = new RoomBundleModel(getIntent().getBundleExtra(IRoomModel.EXTRA_KEY));
	}

	@Override
	protected void onResume() {
		super.onResume();
		tvTime.setText(roomModel.getStarttimeString());
		tvTitle.setText(roomModel.getTitle());
		tvLecturer.setText(roomModel.getLecturer());
		tvBuilding.setText(roomModel.getBuilding());
		tvRoom.setText(roomModel.getRoom());
	}

}

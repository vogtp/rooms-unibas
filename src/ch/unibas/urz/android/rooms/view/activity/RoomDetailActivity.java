package ch.unibas.urz.android.rooms.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import ch.unibas.urz.android.rooms.R;
import ch.unibas.urz.android.rooms.model.IRoomModel;
import ch.unibas.urz.android.rooms.model.RoomBundleModel;

import com.markupartist.android.widget.ActionBar;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.room_detail);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar1);
		actionBar.setTitle(R.string.app_name);

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

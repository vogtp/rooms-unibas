package ch.almana.unibas.rooms.view.activity;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.access.RoomAccess;
import ch.almana.unibas.rooms.access.RoomLoaderTask;
import ch.almana.unibas.rooms.access.RoomLoaderTask.RoomAccessCallback;
import ch.almana.unibas.rooms.access.SearchConfig;
import ch.almana.unibas.rooms.model.IRoomModel;
import ch.almana.unibas.rooms.view.adapter.RoomAdapter;
import ch.almana.unibas.rooms.view.gestures.IGestureReceiver;
import ch.almana.unibas.rooms.view.gestures.LeftRightGestureListener;
import ch.almana.unibas.rooms.view.widget.DateButton;
import ch.almana.unibas.rooms.view.widget.DateButton.OnDateChangedListener;

public class RoomUnibasActivity extends ListActivity implements OnDateChangedListener, IGestureReceiver, RoomAccessCallback {

	private DateButton dateButton;
	private LeftRightGestureListener leftRightGestureListener;
	private RoomAdapter roomAdapter;
	private int progress = 0;
	private RoomLoaderTask roomLoaderTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.room_list);

		dateButton = (DateButton) findViewById(R.id.dateButton1);
		dateButton.setOnDateChangedListener(this);

		((Button)findViewById(R.id.buLeft)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveLeft();
			}
		});
		((Button)findViewById(R.id.buRight)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveRight();
			}
		});
		
		leftRightGestureListener = new LeftRightGestureListener(this);
		OnTouchListener gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return leftRightGestureListener.getGestureDetector().onTouchEvent(event);
			}
		};
		getListView().setOnTouchListener(gestureListener);
		roomAdapter = new RoomAdapter(this);
		getListView().setAdapter(roomAdapter);
		dateButton.setSearchConfig(new SearchConfig(SearchConfig.BUILDING_LZM));
	}

	@Override
	protected void onResume() {
		loadData();
		super.onResume();
	}

	private void loadData() {
		if (progress > 0) {
			roomLoaderTask.cancel(true);
		}
		updateAdapter(RoomAccess.LOADING_ROOMS);
		roomLoaderTask = new RoomLoaderTask(this);
		setProgressBarVisibility(true);
		progress = 1000;
		updateProgress();
		roomLoaderTask.execute(dateButton.getSearchConfig());
	}

	@Override
	public void updateProgress() {
		progress += 1000;
		setProgress(progress);
	}

	@Override
	public void loadingFinished(List<IRoomModel> result) {
		if (result.size() > 0) {
			updateAdapter(result);
		} else {
			updateAdapter(RoomAccess.NO_ROOMS);
		}
		setProgressBarVisibility(false);
		progress = 0;
	}

	private void updateAdapter(List<IRoomModel> result) {
		roomAdapter.setData(result);
		getListView().setAdapter(roomAdapter);
	}

	@Override
	public void moveRight() {
		dateButton.moveNext();
		loadData();
	}

	@Override
	public void moveLeft() {
		dateButton.movePref();
		loadData();
	}

	@Override
	public void searchConfigChanged(SearchConfig searchConfig) {
		loadData();
	}

}
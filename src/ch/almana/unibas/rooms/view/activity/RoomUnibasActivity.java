package ch.almana.unibas.rooms.view.activity;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import ch.almana.unibas.rooms.R;
import ch.almana.unibas.rooms.access.RoomAccess;
import ch.almana.unibas.rooms.access.RoomLoaderTask;
import ch.almana.unibas.rooms.access.RoomLoaderTask.RoomAccessCallback;
import ch.almana.unibas.rooms.access.SearchConfig;
import ch.almana.unibas.rooms.helper.GeneralMenuHelper;
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
	private Spinner spBuilding;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.room_list);

		spBuilding = (Spinner) findViewById(R.id.spBuilding);
		spBuilding.setAdapter(SearchConfig.getBuildingAdapter(this));
		spBuilding.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SearchConfig sc = (SearchConfig) spBuilding.getAdapter().getItem(position);
				sc.setTimeInMillis(dateButton.getSearchConfig().getTimeInMillis());
				dateButton.setSearchConfig(sc);
				loadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
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

	}

	@Override
	protected void onResume() {
		dateButton.setSearchConfig((SearchConfig) spBuilding.getSelectedItem());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// getMenuInflater().inflate(R.menu.gerneral_help_menu, menu);
		getMenuInflater().inflate(R.menu.gerneral_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (GeneralMenuHelper.onOptionsItemSelected(this, item)) {
			return true;
		}
		return false;
	}

}
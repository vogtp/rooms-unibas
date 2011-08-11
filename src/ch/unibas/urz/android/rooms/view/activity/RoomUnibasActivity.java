package ch.unibas.urz.android.rooms.view.activity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import ch.unibas.urz.android.rooms.R;
import ch.unibas.urz.android.rooms.access.RoomAccess;
import ch.unibas.urz.android.rooms.access.RoomLoaderTask;
import ch.unibas.urz.android.rooms.access.RoomLoaderTask.RoomAccessCallback;
import ch.unibas.urz.android.rooms.access.SearchConfig;
import ch.unibas.urz.android.rooms.helper.GeneralMenuHelper;
import ch.unibas.urz.android.rooms.helper.Settings;
import ch.unibas.urz.android.rooms.model.IRoomModel;
import ch.unibas.urz.android.rooms.view.adapter.RoomAdapter;
import ch.unibas.urz.android.rooms.view.gestures.IGestureReceiver;
import ch.unibas.urz.android.rooms.view.gestures.LeftRightGestureListener;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class RoomUnibasActivity extends ListActivity implements IGestureReceiver, RoomAccessCallback {

	// private DateButton dateButton2;
	private LeftRightGestureListener leftRightGestureListener;
	private RoomAdapter roomAdapter;
	private int progress = 0;
	private RoomLoaderTask roomLoaderTask;
	private Spinner spBuilding;
	private int buildingId;
	private ActionBar actionBar;
	private TextView tvDate;
	private SearchConfig searchConfig;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.room_list);

		actionBar = (ActionBar) findViewById(R.id.actionBar1);
		actionBar.setTitle(R.string.app_name);

		buildingId = Settings.getInstance().getDefaultBuilding();
		spBuilding = new Spinner(this);// (Spinner)
										// findViewById(R.id.spBuilding);
		spBuilding.setAdapter(getBuildingAdapter());

		tvDate = (TextView) findViewById(R.id.tvDate);

		spBuilding.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SearchConfig sc = (SearchConfig) spBuilding.getAdapter().getItem(position);
				sc.setTimeInMillis(searchConfig.getTimeInMillis());
				searchConfig = sc;
				loadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		//
		// dateButton = (DateButton) findViewById(R.id.dateButton1);
		// dateButton.setOnDateChangedListener(this);
		//
		// ((Button) findViewById(R.id.buLeft)).setOnClickListener(new
		// OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// moveLeft();
		// }
		// });
		// ((Button) findViewById(R.id.buRight)).setOnClickListener(new
		// OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// moveRight();
		// }
		// });

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
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				IRoomModel room = roomAdapter.getItem(position);
				Intent i = new Intent(RoomUnibasActivity.this, RoomDetailActivity.class);
				i.putExtra(IRoomModel.EXTRA_KEY, room.getBundle());
				startActivity(i);
			}
		});
		Action buildingAction = new ActionBar.Action() {
			@Override
			public void performAction(View view) {
				spBuilding.performClick();
			}

			@Override
			public int getDrawable() {
				return android.R.drawable.ic_dialog_map;
			}
		};
		Action leftAction = new ActionBar.Action() {
			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return android.R.drawable.ic_media_previous;
			}
			@Override
			public void performAction(View view) {
				moveLeft();
			}
		};
		Action rightAction = new ActionBar.Action() {
			@Override
			public int getDrawable() {
				return android.R.drawable.ic_media_next;
			}
			@Override
			public void performAction(View view) {
				moveRight();
			}
		};
		Action newDateAction = new ActionBar.Action() {
			@Override
			public int getDrawable() {
				return android.R.drawable.ic_dialog_info;
			}
			@Override
			public void performAction(View view) {
				showDatePickerDialog();
			}
		};

		actionBar.addAction(buildingAction);
		actionBar.addAction(leftAction);
		actionBar.addAction(newDateAction);
		actionBar.addAction(rightAction);
	}

	@Override
	protected void onResume() {
		// spBuilding.setAdapter(getBuildingAdapter());
		if (buildingId != -1) {
			SpinnerAdapter adapter = spBuilding.getAdapter();
			for (int i = 0; i < adapter.getCount(); i++) {
				SearchConfig item = (SearchConfig) adapter.getItem(i);
				if (buildingId == item.getBuildingId()) {
					spBuilding.setSelection(i);
				}
			}
		}
		searchConfig = (SearchConfig) spBuilding.getSelectedItem();
		searchConfigChanged();
		loadData();
		super.onResume();
	}

	private void loadData() {
		if (progress > 0) {
			roomLoaderTask.cancel(true);
		}
		showRoomInfo();
		updateAdapter(RoomAccess.LOADING_ROOMS);
		roomLoaderTask = new RoomLoaderTask(this);
		actionBar.setProgressBarVisibility(View.VISIBLE);
		roomLoaderTask.execute(searchConfig);
	}

	// FIXME remove
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
		actionBar.setProgressBarVisibility(View.GONE);
		showRoomInfo();
	}

	private void updateAdapter(List<IRoomModel> result) {
		roomAdapter.setData(result);
		getListView().setAdapter(roomAdapter);
	}

	@Override
	public void moveRight() {
		searchConfig.moveNext();
		loadData();
	}

	@Override
	public void moveLeft() {
		searchConfig.movePref();
		loadData();
	}

	// FIXME do we really need this?
	public void searchConfigChanged() {
		loadData();
		showRoomInfo();
	}

	private void showRoomInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(searchConfig.getBuildingName()).append(" ");
		sb.append(searchConfig.getDateTimeFormated());
		tvDate.setText(sb.toString());
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

	private SpinnerAdapter getBuildingAdapter() {
		ArrayAdapter<SearchConfig> buildingAdapter = new ArrayAdapter<SearchConfig>(this, android.R.layout.simple_spinner_item);
		buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		List<SearchConfig> buildings = SearchConfig.getBuildings(this);
		Settings settings = Settings.getInstance();
		for (Iterator<SearchConfig> iterator = buildings.iterator(); iterator.hasNext();) {
			SearchConfig building = iterator.next();
			if (settings.isShowBuilding(building.getBuildingId())) {
				buildingAdapter.add(building);
			}
		}
		return buildingAdapter;
	}

	public void showDatePickerDialog() {
		OnDateSetListener callBack = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				searchConfig.set(Calendar.YEAR, year);
				searchConfig.set(Calendar.MONTH, monthOfYear);
				searchConfig.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				if (searchConfig.isSetTime()) {
					showTimePickerDialog();
				} else {
					searchConfigChanged();
				}
			}
		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(this, callBack, searchConfig.get(Calendar.YEAR), searchConfig.get(Calendar.MONTH),
				searchConfig.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	private void showTimePickerDialog() {
		OnTimeSetListener timeCallBack = new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				searchConfig.set(Calendar.HOUR_OF_DAY, hourOfDay);
				searchConfig.set(Calendar.MINUTE, minute);
				searchConfigChanged();
			}
		};
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeCallBack, searchConfig.get(Calendar.HOUR_OF_DAY), searchConfig.get(Calendar.MINUTE), true);
		timePickerDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				searchConfigChanged();
			}
		});
		timePickerDialog.show();
	}

}
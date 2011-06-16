package ch.almana.unibas.rooms.view.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import ch.almana.unibas.rooms.helper.Settings;

public class DateButton extends Button {

	private Calendar cal;
	private List<OnDateChangedListener> dateChangedListeners;

	public interface OnDateChangedListener {

		void dateChanged(long date);

	}

	public DateButton(Context context) {
		super(context);
		init();
	}

	public DateButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setDate(System.currentTimeMillis());

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OnDateSetListener callBack = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.MONTH, monthOfYear);
						cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						fireDateChanged();
					}
				};
				DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), callBack, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
	}

	public long getDate() {
		return cal.getTimeInMillis();
	}

	public void setDate(long time) {

		cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		updateText();
	}

	private void updateText() {
		setText(Settings.getInstance().getDateFormat().format(getDate()));
	}

	public void setOnDateChangedListener(OnDateChangedListener listener) {
		if (dateChangedListeners == null) {
			dateChangedListeners = new ArrayList<DateButton.OnDateChangedListener>();
		}
		dateChangedListeners.add(listener);
	}
	
	private void fireDateChanged() {
		updateText();
		for (OnDateChangedListener listener : dateChangedListeners) {
			listener.dateChanged(getDate());
		}
	}

	public void nextDay() {
		cal.add(Calendar.DAY_OF_YEAR, 1);
		updateText();
	}

	public void prevDay() {
		cal.add(Calendar.DAY_OF_YEAR, -1);
		updateText();
	}

}

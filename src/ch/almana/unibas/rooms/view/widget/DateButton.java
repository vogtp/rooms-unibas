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
import ch.almana.unibas.rooms.access.SearchConfig;

public class DateButton extends Button {

	private SearchConfig searchConfig;
	private List<OnDateChangedListener> dateChangedListeners;

	public interface OnDateChangedListener {

		void searchConfigChanged(SearchConfig searchConfig);

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
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OnDateSetListener callBack = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						searchConfig.set(Calendar.YEAR, year);
						searchConfig.set(Calendar.MONTH, monthOfYear);
						searchConfig.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						fireDateChanged();
					}
				};
				DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), callBack, searchConfig.get(Calendar.YEAR), searchConfig.get(Calendar.MONTH),
						searchConfig.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
	}

	public SearchConfig getSearchConfig() {
		return searchConfig;
	}

	public void setSearchConfig(SearchConfig sc) {
		searchConfig = sc;
		updateText();
	}

	private void updateText() {
		setText(searchConfig.getDateTimeFormated());
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
			listener.searchConfigChanged(searchConfig);
		}
	}

	public void moveNext() {
		searchConfig.moveNext();
		updateText();
	}

	public void movePref() {
		searchConfig.movePref();
		updateText();
	}

}

package ch.unibas.urz.android.rooms.view.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import ch.unibas.urz.android.rooms.access.SearchConfig;

public class DateButton extends ImageButton {

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
				// showDatePickerDialog();
			}
		});
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				searchConfig.setTimeInMillis(System.currentTimeMillis());
				fireDateChanged();
				return true;
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
		// setText(searchConfig.getDateTimeFormated());
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

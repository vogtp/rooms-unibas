package ch.unibas.urz.android.rooms.access;

import java.util.List;

import android.os.AsyncTask;
import ch.unibas.urz.android.rooms.model.IRoomModel;

public class RoomLoaderTask extends AsyncTask<SearchConfig, Integer, List<IRoomModel>> {
	public interface RoomAccessCallback {
		void updateProgress();

		void loadingFinished(List<IRoomModel> result);
	}

	private RoomAccessCallback callback;

	public RoomLoaderTask(RoomAccessCallback callback) {
		super();
		this.callback = callback;
	}

	@Override
	protected List<IRoomModel> doInBackground(SearchConfig... params) {
		if (params.length < 1) {
			return RoomAccess.NO_ROOMS;
		}
		RoomAccess roomAccess;
		SearchConfig searchConfig = params[0];
		
		switch (searchConfig.getRoomAccessType()) {
		case MedRooms:
			roomAccess = new RoomAccessMedRooms(this);
			break;
		case RaumDispo:
			roomAccess = new RoomAccessRaumDispo(this);
			break;
		default:
			return RoomAccess.UNKNOWN_BUILDING;
		}

		return roomAccess.getRoomModels(searchConfig);
	}

	@Override
	protected void onPostExecute(List<IRoomModel> result) {
		super.onPostExecute(result);
		callback.loadingFinished(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		callback.updateProgress();
	}

	public void updateProgress() {
		publishProgress((Integer[]) null);
	}
}

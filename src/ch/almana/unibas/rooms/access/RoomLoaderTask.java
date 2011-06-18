package ch.almana.unibas.rooms.access;

import java.util.List;

import android.os.AsyncTask;
import ch.almana.unibas.rooms.model.IRoomModel;

public class RoomLoaderTask extends AsyncTask<Long, Integer, List<IRoomModel>> {
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
	protected List<IRoomModel> doInBackground(Long... params) {
		if (params.length < 1) {
			return RoomAccess.NO_ROOMS;
		}
		RoomAccess roomAccess;
		if (true) {
			roomAccess = new RoomXmlAccess(this);
		} else {
			roomAccess = new RoomJsonAccess(this);
		}
		return roomAccess.getRoomModels(params[0]);
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

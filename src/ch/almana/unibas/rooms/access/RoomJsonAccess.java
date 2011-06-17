package ch.almana.unibas.rooms.access;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.model.IRoomModel;
import ch.almana.unibas.rooms.model.RoomJsonModel;

public class RoomJsonAccess extends RoomAccess {

	public RoomJsonAccess(RoomLoaderTask roomLoaderTask) {
		super(roomLoaderTask);
	}

	@Override
	protected String getEncoding() {
		return "ISO-8859-1";
	}

	@Override
	protected String buildUrl(long time) {
		String uri = "http://rooms.medizin.unibas.ch/lib/json.php";

		if (time > 1) {
			int t = (int) (time / 1000);
			uri = uri + "?datum=" + t;
		}
		return uri;
	}

	@Override
	protected List<IRoomModel> parse(String payload) {
		try {
			JSONArray jsonArray = new JSONArray(payload);
			if (jsonArray.length() < 1) {
				return RoomAccess.NO_ROOMS;
			}
			List<IRoomModel> list = new ArrayList<IRoomModel>();
			for (int i = 0; i < jsonArray.length(); i++) {
				loaderTask.updateProgress();
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				list.add(new RoomJsonModel(jsonObject));
			}
			return list;

		} catch (JSONException e) {
			Logger.e("Cannot get rooms", e);
		}
		return new ArrayList<IRoomModel>();
	}

}

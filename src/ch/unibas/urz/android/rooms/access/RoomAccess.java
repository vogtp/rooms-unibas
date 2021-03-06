package ch.unibas.urz.android.rooms.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ch.unibas.urz.android.rooms.helper.Logger;
import ch.unibas.urz.android.rooms.model.IRoomModel;
import ch.unibas.urz.android.rooms.model.RoomJsonModel;

public abstract class RoomAccess {

	public static final List<IRoomModel> UNKNOWN_BUILDING = new ArrayList<IRoomModel>(1);
	public static final List<IRoomModel> NO_ROOMS = new ArrayList<IRoomModel>(1);
	public static final List<IRoomModel> LOADING_ROOMS = new ArrayList<IRoomModel>(1);

	static {
		RoomJsonModel dummyModel = new RoomJsonModel();
		NO_ROOMS.add(dummyModel);
		LOADING_ROOMS.add(dummyModel);
		UNKNOWN_BUILDING.add(dummyModel);
	}

	protected RoomLoaderTask loaderTask;
	
	public RoomAccess(RoomLoaderTask roomLoaderTask) {
		super();
		this.loaderTask = roomLoaderTask;
	}

	protected abstract String getEncoding();

	protected abstract String buildUrl(SearchConfig searchConfig);

	public abstract List<IRoomModel> getRoomModels(SearchConfig searchConfig);

	protected InputStream getDataAsStream(SearchConfig searchConfig) throws IOException {
		String uri = buildUrl(searchConfig);
		Logger.v("Loading >" + uri + "<");
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet(uri);
		BufferedHttpEntity bhe = null;
		HttpResponse response = httpClient.execute(request);
		loaderTask.updateProgress();
		bhe = new BufferedHttpEntity(response.getEntity());

		// TODO is this needed
		// if (bhe != null) {
		// try {
		// bhe.consumeContent();
		// } catch (IOException e) {
		// Logger.e("Error closing");
		// }
		// }
		return bhe.getContent();
	}

	protected String getDataAsString(SearchConfig searchConfig) throws IOException {
		BufferedReader content = null;
		try {
			content = new BufferedReader(new InputStreamReader(getDataAsStream(searchConfig), getEncoding()));
			loaderTask.updateProgress();
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = content.readLine()) != null) {
				sb.append(line);
				loaderTask.updateProgress();
			}
			return sb.toString();

		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}

		}
	}

}

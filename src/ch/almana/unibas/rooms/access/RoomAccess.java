package ch.almana.unibas.rooms.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.model.IRoomModel;
import ch.almana.unibas.rooms.model.RoomJsonModel;

public abstract class RoomAccess {

	public static List<IRoomModel> NO_ROOMS = new ArrayList<IRoomModel>(1);
	public static List<IRoomModel> LOADING_ROOMS = new ArrayList<IRoomModel>(1);

	static {
		NO_ROOMS.add(new RoomJsonModel());
		LOADING_ROOMS.add(new RoomJsonModel());
	}

	protected RoomLoaderTask loaderTask;

	
	public RoomAccess(RoomLoaderTask roomLoaderTask) {
		super();
		this.loaderTask = roomLoaderTask;
	}

	protected abstract String getEncoding();

	protected abstract String buildUrl(long time);

	protected abstract List<IRoomModel> parse(String payLoad);

	public List<IRoomModel> getData(long time) {
			String payLoad = getUri(time);
			return parse(payLoad);
	}


	private String getUri(long time) {
		String uri = buildUrl(time);
		Logger.v("Loading >" + uri + "<");
		long start = System.currentTimeMillis();
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet(uri);
		BufferedHttpEntity bhe = null;
		BufferedReader content = null;
		try {
			HttpResponse response = httpClient.execute(request);
			loaderTask.updateProgress();
			bhe = new BufferedHttpEntity(response.getEntity());
			content = new BufferedReader(new InputStreamReader(bhe.getContent(), getEncoding()));
			loaderTask.updateProgress();
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = content.readLine()) != null) {
				sb.append(line);
				loaderTask.updateProgress();
			}
			return sb.toString();

		} catch (Exception e) {
			Logger.e("Error", e);
			return "";
		} finally {

			if (bhe != null) {
				try {
					bhe.consumeContent();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}

			long duration = System.currentTimeMillis() - start;
			duration /= 1000l;
			Logger.d(" search " + duration + " s");
		}
	}

}

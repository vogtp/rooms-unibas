package ch.almana.unibas.rooms.access;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.net.Uri;
import ch.almana.unibas.rooms.helper.Logger;
import ch.almana.unibas.rooms.model.IRoomModel;
import ch.almana.unibas.rooms.model.RoomXmlModel;

public class RoomXmlAccess extends RoomAccess {

	// <event><time>10:00</time><title>Funktionelle
	// Neuroanatomie</title><lecturer>Pasquale Calabrese, Iris-Katharina
	// Penner</lecturer><building>Kollegienhaus</building><room>Hörsaal
	// 114</room><code>11475-01</code></event>

	private static final String TAG_EVENTS = "events";
	private static final String TAG_EVENT = "event";
	public static final String TAG_ROOM = "room";
	public static final String TAG_TIME = "time";
	public static final String TAG_LECTURER = "lecturer";
	public static final String TAG_TITLE = "title";
	public static final String TAG_BUILDING = "building";

	public RoomXmlAccess(RoomLoaderTask roomLoaderTask) {
		super(roomLoaderTask);
	}

	@Override
	protected String getEncoding() {
		return "UTF-8";
	}

	@Override
	protected String buildUrl(long time) {
		String date = "2008-12-19 10:00:00";
		String url = "http://beta2.nextron.ch/unibas_raumdispo/api/?pid=1&ct=";
		return url + Uri.encode(date);
	}


	@Override
	public List<IRoomModel> getRoomModels(long time) {
		try {
			Document document = getDocument(getDataAsStream(time));
			NodeList eventNodesList = document.getElementsByTagName(TAG_EVENT);
			int length = eventNodesList.getLength();
			if (length < 1) {
				return RoomAccess.NO_ROOMS;
			}
			List<IRoomModel> list = new ArrayList<IRoomModel>(length);
			for (int i = 0; i < length; i++) {
				loaderTask.updateProgress();
				Node node = eventNodesList.item(i);
				String nodeName = node.getNodeName();
				if (TAG_EVENT.equals(nodeName)) {
					list.add(new RoomXmlModel(node));
				}
			}
			return list;
		} catch (Exception e) {
			Logger.e("Cannot get rooms from xml", e);
			return NO_ROOMS;
		}
	}

	private Document getDocument(InputStream documentAsStream) throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return documentBuilder.parse(documentAsStream);
	}

}

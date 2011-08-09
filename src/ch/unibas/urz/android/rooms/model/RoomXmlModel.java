package ch.unibas.urz.android.rooms.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.unibas.urz.android.rooms.access.RoomAccessRaumDispo;
import ch.unibas.urz.android.rooms.helper.Logger;

public class RoomXmlModel extends IRoomModel {

	private CharSequence room;
	private CharSequence starttimeString;
	private CharSequence lecturer;
	private CharSequence title;
	// private long starttime;
	private CharSequence building;

	public RoomXmlModel(Node node) {
		super();
		NodeList valueNodes = node.getChildNodes();
		for (int i = 0; i < valueNodes.getLength(); i++) {
			Node item = valueNodes.item(i);
			String name = item.getNodeName();
			if (RoomAccessRaumDispo.TAG_ROOM.equals(name)) {
				room = getValue(item);
			}
			if (RoomAccessRaumDispo.TAG_TIME.equals(name)) {
				starttimeString = getValue(item);
			}
			if (RoomAccessRaumDispo.TAG_LECTURER.equals(name)) {
				lecturer = getValue(item);
			}
			if (RoomAccessRaumDispo.TAG_TITLE.equals(name)) {
				title = getValue(item);
			}
			if (RoomAccessRaumDispo.TAG_BUILDING.equals(name)) {
				building = getValue(item);
			}
		}
		// FIXME calculate starttime
	}

	private CharSequence getValue(Node item) {
		try {
		NodeList childNodes = item.getChildNodes();
		Node valueNode = childNodes.item(0);
		return valueNode.getNodeValue();
		}catch (Exception e) {
			Logger.e("No value found in xml ",e);
			return IRoomModel.NO_VALUE;
		}
	}

	@Override
	public CharSequence getRoom() {
		return room;
	}

	// @Override
	// public long getStarttime() {
	// return starttime;
	// }

	@Override
	public CharSequence getStarttimeString() {
		return starttimeString;
	}

	@Override
	public CharSequence getLecturer() {
		return lecturer;
	}

	@Override
	public CharSequence getTitle() {
		return title;
	}

	@Override
	public CharSequence getBuilding() {
		return building;
	}


}

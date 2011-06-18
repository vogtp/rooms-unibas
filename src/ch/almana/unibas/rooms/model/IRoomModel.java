package ch.almana.unibas.rooms.model;

public interface IRoomModel {

	public static final CharSequence NO_VALUE = "n.a.";

	public abstract CharSequence getRoom();

	public abstract CharSequence getBuilding();

	public abstract long getStarttime();

	public abstract CharSequence getStarttimeString();

	public abstract CharSequence getLecturer();

	public abstract CharSequence getTitle();

}
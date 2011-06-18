package ch.almana.unibas.rooms.model;

public interface IRoomModel {

	public static final CharSequence NO_VALUE = "n.a.";

	public abstract CharSequence getRoom();

	public abstract CharSequence getStarttime();

	public abstract CharSequence getLecturer();

	public abstract CharSequence getTitle();

}
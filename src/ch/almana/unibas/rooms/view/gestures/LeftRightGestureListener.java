package ch.almana.unibas.rooms.view.gestures;import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
;

public class LeftRightGestureListener extends SimpleOnGestureListener {
	private static final float MAJOR_MOVE = 100f;
	private IGestureReceiver receiver;
	private GestureDetector gestureDetector;

	public LeftRightGestureListener(IGestureReceiver receiver) {
		super();
		this.receiver = receiver;
		gestureDetector = new GestureDetector(this);

	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		if (e2 == null || e1 == null)
			return true;

		float dx = (e2.getX() - e1.getX());
		float dy = e2.getY() - e1.getY();

		boolean movedAcross = (Math.abs(dx) > Math.abs(dy * 4));
		boolean steadyHand = (Math.abs(dx / dy) > 2);

		if (Math.abs(dx) > MAJOR_MOVE && Math.abs(velocityX) > Math.abs(velocityY) && movedAcross && steadyHand) {
			if (velocityX > 0) {
				receiver.moveRight();
			} else {
				receiver.moveLeft();
			}
			return true;
		} else {
			return false;
		}
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

}

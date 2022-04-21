package daw.engine;

public interface RPClipPlayerNotifier {
	
	/**
	 * Read the time from the clock and notify all the players registered at that time.
	 */
	void update();

}

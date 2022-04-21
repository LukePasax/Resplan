package daw.core.clip;

/**
 * @author alessandro
 * This is a player for an RPClip.
 */
public interface RPClipPlayer {
	
	/**
	 * 
	 */
	void play();
	
	/**
	 * 
	 */
	void pause();
	
	/**
	 * 
	 */
	void stop();
	
	/**
	 * @param milliseconds
	 */
	void setPlaybackPosition(double milliseconds);
	
	/**
	 * @return
	 */
	double getPlaybackPosition();
	
	void setCut(double time);
	
	void disableCut();
	
	
}

package daw.core.clip;

import net.beadsproject.beads.core.UGen;

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
	
	UGen getUGen();
	
	
}

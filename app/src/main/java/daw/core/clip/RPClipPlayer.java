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
	
	void setCut(double time);
	
	void disableCut();
	/**
	 * @return
	 */
	double getPlaybackPosition();
	
	boolean isPlaused();
	
	UGen getUGen();
	
	
}

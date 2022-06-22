package daw.engine;

import daw.core.clip.RPClip;

/**
 * The engine which sets and starts playback of each playable {@link RPClip}.
 * <p>The engine could be started, paused and stopped. 
 * <br>The playback starts from the actual playback time or from time zero 
 * if the engine was previously stopped.
 */
public interface RPEngine {
	
	/**
	 * Start the playback from the current playback time.
	 */
	void start();
	
	/**
	 * Stop the playback.
	 */
	void pause();
	
	/**
	 * Stop the playback and reset the playback time to zero.
	 */
	void stop();
	
	/**
	 * Set the playback time.
	 * @param  time  The time to set the playback to.
	 */
	void setPlaybackTime(Double time);
	
	/**
	 * Get the current playback time.
	 * 
	 * @return  The current playback time.
	 */
	Double getPlaybackTime();
	
	/**
	 * Check if the engine is playing back.
	 * 
	 * @return  {@code false} if and only if the engine is playing back
	 * 			{@code true} otherwise.
	 */
	boolean isPaused();

}

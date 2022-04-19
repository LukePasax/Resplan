package daw.core.clip;

import java.io.File;

/**
 * @author alessandro
 * This interface models a clip, that is a container for each audio/MIDI file you want to place in the timeline of some tape channel.
 * A clip let you specify the audio content play-back starting point and its duration.
 */
public interface RPClip {
	
	public static final double DEFAULT_DURATION = 1000;
	
	/**
	 * @param milliseconds
	 */
	void setDuration(double milliseconds);

	/**
	 * @param milliseconds
	 */
	void setContentPosition(double milliseconds);
	
	/**
	 * @return
	 */
	double getDuration();

	/**
	 * @return
	 */
	double getContentPosition();
	
	/**
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * @return
	 */
	File getContent();
	
	/**
	 * Return a duplicate object of this RPClip.
	 * @return
	 */
	RPClip duplicate();
}

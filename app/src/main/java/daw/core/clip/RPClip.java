package daw.core.clip;

import java.io.File;

/**
 * An {@code RPClip} is a container for each content (such as an audio/MIDI file) you want to 
 * add to any {@link RPTapeChannel}.
 * <p>For every {@code RPClip} you can specify it's duration, it's content and the content position.
 * <br>The duration of an {@code RPClip} could be different than it's content duration, so you can select 
 * the section of the content you want to fill the clip adjusting the duration with {@link #setDuration} 
 * and the content starting position with {@link #setContentPosition}.
 * <p>An {@code RPClip} could also be empty. You can verify it calling {@link #isEmpty} method.
 * An empty {@code RPClip} is for reserving space in any {@link RPTapeChannel}, so you can fill it later.
 * <p>You can get an exact copy of any clip with it's content calling the {@link #duplicate} method.
 */
public interface RPClip {
	
	/**
	 * This should be the default duration for an {@code RPClip} if it has not been specified or is not yet known.
	 */
	public static final double DEFAULT_DURATION = 5000;
	
	/** 
	 * Set the {@code RPClip} duration.
	 * 
	 * @param  milliseconds The duration of the clip in milliseconds.
	 * 
	 * @throws  IllegalArgumentException If the specified duration is negative.
	 */
	void setDuration(double milliseconds);

	/**
	 * Set the content starting point.
	 * 
	 * @param  milliseconds The content starting time in milliseconds. 
	 * 
	 * @throws  IllegalStateException If the {@code RPClip} is Empty
	 * 
	 * @throws  IllegalArgumentException If the specified time is not within the content duration.
	 */
	void setContentPosition(double milliseconds);
	
	/**
	 * Get the duration of the {@code RPClip}.
	 * 
	 * @return  The duration in milliseconds.
	 */
	double getDuration();

	/**
	 * Get the content starting point.
	 * 
	 * @return  The content starting position in milliseconds.
	 */
	double getContentPosition();
	
	/**
	 * Check if the {@code RPClip} is empty.
	 * 
	 * @return  {@code true} if and only if the clip has no content;
	 *  		{@code false} otherwise. 
	 */
	boolean isEmpty();
	
	/**
	 * Get a {@link File} object with the content of the {@code RPClip}.
	 * 
	 * @return The clip content.
	 */
	File getContent();
	
	/**
	 * Duplicate this clip with it's content.
	 * 
	 * @return a duplicate of this {@code RPClip}.
	 */
	RPClip duplicate() throws Exception;
}

package daw.core.clip;

import java.io.File;

/**
 * An RPClip is a container for each content (such as an audio/MIDI file) you want to 
 * add to any {@link RPTapeChannel}.
 * <p>For every clip you can specify it's duration, it's content and the content position.
 * <br>The duration of a clip could be different than it's content duration, so you can select 
 * the section of the content you want to fill the clip adjusting the duration with {@link #setDuration} 
 * and the content starting position with {@link #setContentPosition}.
 * <p>A clip could also be empty. You can verify it calling {@link #isEmpty} method.
 * An empty RPclip is for reserving space in any {@link RPTapeChannel}, so you can fill it later.
 * <p>You can get an exact copy of any clip with it's content calling the {@link #duplicate} method.
 */
public interface RPClip {
	
	/**
	 * This should be the default duration for an RPClip if it has not been specified or is not yet known.
	 */
	public static final double DEFAULT_DURATION = 5000;
	
	/** 
	 * Set the RPClip duration.
	 * 
	 * @param milliseconds The duration of the clip in milliseconds.
	 * 
	 * @throws IllegalArgumentException If the specified duration is negative.
	 */
	void setDuration(double milliseconds);

	/**
	 * Set the content starting point.
	 * @param milliseconds The content starting time in milliseconds. The specified time must be within the duration of the content or 
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
	RPClip duplicate() throws Exception;
}

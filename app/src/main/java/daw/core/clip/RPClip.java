package daw.core.clip;

import java.io.File;

/**
 * A container for each content (such as an audio/MIDI file) you want to 
 * add to any {@link RPTapeChannel}.
 * <p>Every RPClip stores it's duration, it's content and it's content position.
 * <br>The duration of an RPClip could be different than it's content duration, so the section 
 * of the content you want to fill the clip is setted adjusting the duration through {@link #setDuration} 
 * and the content starting position through {@link #setContentPosition}.
 * <p>An RPClip could also be empty.
 * An empty RPClip is used for reserving space in any {@link RPTapeChannel}, so it can be filled later.
 * <p>Calling the {@link #duplicate} method generate an exact copy of a clip.
 */
public interface RPClip {
	
	/**
	 * This should be the default duration for an RPClip if it has not been specified or is not yet known.
	 */
	public static final double DEFAULT_DURATION = 5000;
	
	/** 
	 * Set this RPClip duration.
	 * 
	 * @param  milliseconds  The duration of this clip in milliseconds.
	 * 
	 * @throws  IllegalArgumentException  If the specified duration is zero or a negative value.
	 */
	void setDuration(double milliseconds);

	/**
	 * Set this content starting point.
	 * 
	 * @param  milliseconds  The content starting time in milliseconds. 
	 * 
	 * @throws  UnsupportedOperationException  If this RPClip is Empty.
	 * 
	 * @throws  IllegalArgumentException  If the specified time is not within this content duration.
	 */
	void setContentPosition(double milliseconds);
	
	/**
	 * Get the duration of this RPClip.
	 * 
	 * @return  The duration in milliseconds.
	 */
	double getDuration();

	/**
	 * Get this content starting point.
	 * 
	 * @return  The content starting position in milliseconds.
	 * 
	 * @throws  UnsupportedOperationException  If this RPClip is Empty.
	 */
	double getContentPosition();
	
	/**
	 * Check if this RPClip is empty.
	 * 
	 * @return  {@code true} if and only if the clip has no content;
	 *  		{@code false} otherwise. 
	 */
	boolean isEmpty();
	
	/**
	 * Get a {@link File} object with the content of this RPClip.
	 * 
	 * @return  The clip content.
	 *
	 * @throws  UnsupportedOperationException  If this RPClip is Empty. 
	 */
	File getContent();
	
	/**
	 * Generate an exact copy of this RPClip.
	 * 
	 * @return  a duplicate of this RPClip.
	 */
	RPClip duplicate() throws Exception;
}

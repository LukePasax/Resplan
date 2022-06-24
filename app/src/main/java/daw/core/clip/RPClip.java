package daw.core.clip;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

import java.io.IOException;

/**
 * A container for each content (such as an audio/MIDI file) you want to 
 * add to any {@link RPTapeChannel}.
 * <p>Every RPClip stores its duration, its content and its content position.
 * <br>The duration of an RPClip could be different from its content duration, so the section
 * of the content you want to fill the clip is set adjusting the duration through {@link #setDuration}
 * and the content starting position through {@link #setContentPosition}.
 * <p>An RPClip could also be empty.
 * An empty RPClip is used for reserving space in any {@link RPTapeChannel}, so it can be filled later.
 * <p>Calling the {@link #duplicate} method generate an exact copy of a clip.
 * 
 * @param <X>  The content Type.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = EmptyClip.class, name = "empty"),
		@JsonSubTypes.Type(value = SampleClip.class, name = "sample")
})
public interface RPClip<X> {
	
	/**
	 * This should be the default duration for an RPClip if it has not been specified or is not yet known.
	 */
	double DEFAULT_DURATION = 5000;
	
	/**
	 * Title of this clip.
	 * 
	 * @return the title of this clip.
	 */
	String getTitle();
	
	/** 
	 * Set this RPClip duration.
	 * 
	 * @param  milliseconds  The duration of this clip in milliseconds.
	 * 
	 * @throws  IllegalArgumentException  If the specified duration is zero, a negative value or if the specified duration is bigger than the content duration. 
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
	
	/**Get this content duration.
	 * 
	 * @return The content duration.
	 * 
	 * @throws  UnsupportedOperationException  If this RPClip is Empty or content type has no duration.
	 */
	double getContentDuration();
	
	/**
	 * Check if this RPClip is empty.
	 * 
	 * @return  {@code true} if and only if the clip has no content;
	 *  		{@code false} otherwise. 
	 */
	boolean isEmpty();
	
	/**
	 * Get a {@link X} object with the content of this RPClip.
	 * 
	 * @return  The clip content.
	 *
	 * @throws  UnsupportedOperationException  If this RPClip is Empty. 
	 */
	X getContent();
	
	/**
	 * Generate an exact copy of this RPClip.
	 * 
	 * @param  title  The title of the new clip.
	 * 
	 * @return  a duplicate of this RPClip.
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	RPClip<X> duplicate(String title) throws IOException, OperationUnsupportedException, FileFormatException;

	
}

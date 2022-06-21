package daw.core.clip;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.util.Pair;

/**
 * Place and Manage {@link RPClip} in a Timeline.
 * <p>The tape rapresent the physical support where you can record your audio.
 * <br>For every channel of the Tape is supported one source at time so clips of one RPTapeChannel can't overlap in the timeline.
 * <p>The time in and time out refers to the starting and ending position of a clip in the timeline.
 * Each clip is identified from his time in.
 */
@JsonDeserialize(as = TapeChannel.class)
public interface RPTapeChannel {
	
	/**
	 * Add an {@link RPClip} in the timeline of this tape channel. 
	 * <p>If another clip with a different time in is already present at the specified time the old one will be cutted 
	 * and the new one will be placed at the specified time.
	 * 
	 * @param  clip  The clip to insert.
	 * 
	 * @param  time  The time where to insert the clip.
	 * 
	 * @throws  IllegalStateException  If another clip with the same time in already exists or if 
	 * 						the same clip already exists in this tape channel.
	 *
	 * @throws  IllegalArgumentException  If the given time is a negative value.
	 */
	void insertRPClip(RPClip<?> clip, double time);
	
	/**
	 * Remove the specified {@link RPClip} from the timeline of this tape channel.
	 * 
	 * @param  clipTimeIn  The time in of the clip to remove.
	 * 
	 * @throws ClipNotFoundException  If there's no clip with the specified time in.
	 */
	void removeClip(double clipTimeIn) throws ClipNotFoundException;
	
	/**
	 * Remove all clips from the timeline of this tape channel.
	 */
	void clearTape();
	
	/**
	 * Check if this tape channel is empty.
	 * 
	 * @return  {@code true} if and only if there aren't clips in the tape channel
	 * 			{@code false} otherwise.
	 */
	boolean isEmpty();
	
	/**
	 * If present get an {@link Optional} of {@link Pair} of the clip intersected at the specified time and it's time in.
	 * 
	 * @param  time  The timeline position where to search the clip.
	 * 
	 * @return  {@code Optional<Entry<Double, RPClip>>} of clip intersected or 
	 * 			{@code  Optional.empty()} if no clip is present at the specified time.
	 */
	Optional<Pair<Double, RPClip<?>>> getClipAt(double time);
	
	/**
	 * Get an {@link Iterator} which iterate all the clips of the tape channel ordered by time with their time in.
	 * 
	 * @return	An {@code Iterator} of all {@code Pair<Double, RPClip>} of this tape channel.
	 */
	Iterator<Pair<Double, RPClip<?>>> getClipWithTimeIterator();
	
	/**
	 * Get an {@link Iterator} which iterate all the clips of the tape channel that match the {@link Predicate}. All the items are ordered by time.
	 * 
	 * @param  predicate  The predicate to filter the clips.
	 * 
	 * @return An {@code Iterator} of all {@code Pair<Double, RPClip>} of this tape channel matching the {@code Predicate}.
	 */
	Iterator<Pair<Double, RPClip<?>>> getClipWithTimeIteratorFiltered(Predicate<? super Entry<Double, RPClip<?>>> predicate);
	
	/**
	 * Get the time out of the specified clip.
	 * 
	 * @param  clipTimeIn  The time in of the clip.
	 * 
	 * @return  The time out of the specified clip.
	 * 
	 * @throws ClipNotFoundException  If there's no clip with the specified time in.
	 */
	double getClipTimeOut(double clipTimeIn) throws ClipNotFoundException;
	
	/**
	 * Move the specified clip in a new position of the timeline of this tape channel.
	 * <p>If another clip is already present at the specified time the old one will be cutted 
	 * and the new one will be placed at the specified time.
	 * 
	 * @param  initialClipTimeIn  The time in of the clip to move.
	 * 
	 * @param  finalClipTimeIn  The final position where to move the clip.
	 * 
	 * @throws  ClipNotFoundException  If there's no clip with the specified time in.
	 */
	void move(double initialClipTimeIn, double finalClipTimeIn) throws ClipNotFoundException;
	
	/**
	 * Set the duration of the specified clip.
	 * <p>If you extend the clip over another, the secondone will be cutted.
	 * 
	 * @param  clipTimeIn  The time in of the clip to modify.
	 * 
	 * @param  finalClipTimeOut  The final time out of the clip.
	 * 
	 * @throws  IllegalArgumentException  If the specified time out is before the clip time in or 
	 * 					if the new clip duration exceeds the remaining content at the end of the clip.
	 * 
	 * @throws  ClipNotFoundException  If there's no clip with the specified time in.
	 */
	void setTimeOut(double clipTimeIn, double finalClipTimeOut) throws ClipNotFoundException;
	
	/**
	 * Move the time in over the timeline without changing the position of the clip content.
	 * <p>The duration of the clip and the time in will change. The clip content position will be modified for compensate the shift of the clip time in.
	 * 
	 * @param  initialClipTimeIn  The time in of the clip to modify.
	 * 
	 * @param  finalClipTimeIn  The final time in of the clip.
	 * 
	 * @throws ClipNotFoundException  If there's no clip with the specified time in.
	 * 
	 * @throws  IllegalArgumentException  If the specified time in is after the clip time out or 
	 * 					if the new clip duration exceeds the remaining content at the beginning of the clip.
	 */
	void setTimeIn(double initialClipTimeIn, double finalClipTimeIn) throws ClipNotFoundException;
	
	/**
	 * Split a clip in two different clips.
	 * <p>The splitting time does not refere to the timeline, but to the clip duration.
	 * So the time where to split the clip it's calculated considering the zero at the beginning of the clip.
	 * 
	 * @param  initialClipTimeIn  The time in of the clip to modify.
	 *
	 * @param  splittingTime  The time where to split the clip.
	 * 
	 * @throws ClipNotFoundException  If there's no clip with the specified time in.
	 * 
	 * @throws IllegalArgumentException  If the specified splitting time is bigger than the clip duration.
	 */
	void split(double initialClipTimeIn, double splittingTime) throws ClipNotFoundException;

	/**
	 * Calculate the time out of an ipotetic clip from a time in and a duration.
	 * <p>There might not be a clip associated with the given time in.
	 * 
	 * @param  timeIn  The time in of the ipotetic clip.
	 * 
	 * @param  duration  The duration of the ipotetic clip.
	 * 
	 * @return  The time out of the ipotetic clip.
	 */
	double calculateTimeOut(double timeIn, double duration);

}

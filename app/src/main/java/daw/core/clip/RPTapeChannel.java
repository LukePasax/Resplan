package daw.core.clip;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.Map.Entry;
import javafx.util.Pair;

/**
 * Place and Manage RSClips in the Timeline of a tape channel.
 * The tape rapresent the physical support where you can record your audio.
 * For every channel of the Tape can record one source at time so clips of one channel can't overlap in the timeline.
 *
 * @author alessandro
 */
public interface RPTapeChannel {
	
	/**
	 * Add an RPClip in the timeline of this tape channel. 
	 * If another RPClip is present at the specified time the new clip will be placed over the already present RPClip.
	 * @param clip
	 * @param time
	 */
	void insertRPClip(RPClip clip, double time);
	
	/**
	 * Remove the specified RPClip in the timeline of this tape channel.
	 * @param clipTimeIn
	 */
	void removeRPClip(double clipTimeIn);
	
	/**
	 * Return true if there's no clip in the tape channel
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * Get the Optional<Entry<Double, RPClip>> placed at a specified time in the timeline of this tape channel.
	 * If there's no RPClip placed in the specified time this method must return an Optional.empty().
	 * @param time
	 * @return
	 */
	Optional<Pair<Double, RPClip>> getClipAt(double time);
	
	/**
	 * Get an Optional of Iterator which iterate all the Pair<Double, RPClip> of the tape channel ordered by time.
	 * Return an Optional.empty() if the Tape Channel contains no clip.
	 * @return
	 */
	Optional<Iterator<Pair<Double, RPClip>>> getClipWithTimeIterator();
	
	/**
	 * Get an Optional of Iterator which iterate all the Pair<Double, RPClip> of the tape channel that match the predicate. All the items are ordered by time).
	 * Return an Optional.empty() if the Tape Channel contains no clip matching the predicate.
	 * @return
	 */
	Optional<Iterator<Pair<Double, RPClip>>> getClipWithTimeIteratorFiltered(Predicate<? super Entry<Double, RPClip>> predicate);
	
	/**
	 * @param clip
	 * @return
	 */
	public double getClipTimeOut(Pair<Double, RPClip> clip);
	
	/**
	 * Move the specified clip at the specified time in the timeline. 
	 * If another RPClip is present at the specified time the moved clip will be placed over the other RPClip.
	 * @param clip
	 * @param time
	 */
	void move(double initialClipTimeIn, double finalClipTimeIn);
	
	/**
	 * Set the duration of the specified clip.
	 * If you extend the clip over another, the secondone will be cutted.
	 * If the specified duration is bigger than the Clip Content Size then an exception could be lunched.
	 * @param clip
	 * @param duration
	 */
	void setTimeOut(double initialClipTimeIn, double finalClipTimeOut);
	
	/**
	 * Move the clip starting point over the timeline without move the clip.
	 * The duration and the RPClip content starting position will be modified.
	 * @param clip
	 * @param time
	 */
	void setTimeIn(double initialClipTimeIn, double finalClipTimeIn);
	
	/**
	 * Split a clip in two different RPClips.
	 * The time specified does not refere to the timeline but to the position where to split the clip starting from the beginning of the clip.
	 * @param clip
	 * @param time
	 */
	void split(double initialClipTimeIn, double SplittingTime);

}

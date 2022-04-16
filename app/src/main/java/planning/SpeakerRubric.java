package planning;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This interface models a rubric that contains all the speakers saved before
 */
public interface SpeakerRubric {
	List<Speaker> getSpeakers();
	boolean addSpeaker(Speaker speaker);
	boolean removeSpeaker(Speaker speaker);
	Optional<Speaker> searchSpeaker(int speakerCode);
	List<Speaker> getFilteredSpeakers(Predicate<? super Speaker> filter);
}

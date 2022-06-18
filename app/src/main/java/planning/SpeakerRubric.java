package planning;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This interface models a rubric that contains all the speakers saved before
 */
@JsonDeserialize(as = SimpleSpeakerRubric.class)
public interface SpeakerRubric {
	
	/**
	 * Returns all the speakers in the rubric
	 * 
	 * @return the list of the speakers who make up the rubric
	 */
	List<Speaker> getSpeakers();
	
	/**
	 * Adds a speaker to the rubric
	 * 
	 * @param speaker
	 * the speaker to add
	 * 
	 * @return true if the addition is ok,
	 * false if the speaker code is already associated to someone
	 */
	boolean addSpeaker(Speaker speaker);
	
	/**
	 * Remove a speaker from the rubric
	 * 
	 * @param speaker
	 * the speaker to remove
	 * 
	 * @return true if the removal is ok,
	 * false if the speaker doesn't exists
	 */
	boolean removeSpeaker(Speaker speaker);
	
	/**
	 * Searches a speaker into the rubric
	 * 
	 * @param speakerCode
	 * the code of the speaker to search for
	 * 
	 * @return the optional speaker found,
	 * Optional.empty() if no matches are found
	 */
	Optional<Speaker> searchSpeaker(int speakerCode);
	
	/**
	 * Returns all the speakers in the rubric matching the filter
	 * 
	 * @param filter
	 * the filter to apply to the search
	 * 
	 * @return the filtered speaker list
	 */
	List<Speaker> getFilteredSpeakers(Predicate<? super Speaker> filter);
}

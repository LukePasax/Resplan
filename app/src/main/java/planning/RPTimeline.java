package planning;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This interface represents the timeline at high level
 */
@JsonDeserialize(as = TimelineImpl.class)
public interface RPTimeline {
	
	/**
	 * Allows to add a section to the timeline.
	 * Returns true if the addition is successful
	 * 
	 * @param initialTime
	 * the time at which the section starts
	 * 
	 * @param section
	 * the section to add
	 * 
	 * @return true if the addition is successful,
	 * false if something goes wrong
	 */
	boolean addSection(double initialTime, RPSection section);
	
	/**
	 * Allows to remove a section from the timeline
	 * 
	 * @param section
	 * the section to remove
	 */
	void removeSection(RPSection section);
	
	/**
	 * Allows to get a specific section
	 * 
	 * @param initialTime
	 * the starting time of the section to be obtained
	 * 
	 * @return the optional section, Optional.empty()
	 * if the section doesn't exist
	 */
	Optional<RPSection> getSection(double initialTime);
	
	/**
	 * Gets the total duration of the sections that make up the timeline
	 * 
	 * @return the overall duration of the timeline
	 */
	double getOverallDuration();

	/**
	 * Gets a set containing all the sections in the timeline
	 *
	 * @return A {@link Set} of {@link java.util.Map.Entry} of time and section
	 */
	Set<Map.Entry<Double,RPSection>> getAllSections();
}

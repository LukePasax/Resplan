package planning;

import java.util.Optional;

/**
 * This interface represents the timeline at high level
 */
public interface RPTimeline {
	boolean addSection(int initialTime, RPSection section);
	void removeSection(RPSection section);
	Optional<RPSection> getSection(int initialTime);
	int getOverallDuration();
}

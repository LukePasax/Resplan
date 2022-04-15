package planning;

/**
 * This interface represents the timeline at high level
 */
public interface RPTimeline {
	void addSection(int initialTime, RPSection section);
	void removeSection(RPSection section);
	RPSection getSection(int initialTime);
	int getOverallDuration();
}

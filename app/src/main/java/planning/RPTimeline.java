package planning;

/**
 * This interface represents the timeline at high level
 */
public interface RPTimeline {
	Double getDuration(RPSection section);
	Double getOverallDuration();
}

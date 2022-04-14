package planning;

/**
 * It's an interface that represents the master channel at high level
 */

public interface RPProject extends Element {
	
	public enum ProjectType{
		PODCAST, AUDIOBOOK, LESSON
	}
	
	ProjectType getType();

}

package planning;

/**
 * It's an interface that represents the master channel at high level
 */

public interface RPProject extends Element {
	
	/**
	 * Represents the three possible types of a RPProjects
	 */
	public enum ProjectType{
		PODCAST, AUDIOBOOK, LESSON
	}
	
	/**
	 * Returns the type of the project
	 * 
	 * @return the chosen type of the project
	 */
	ProjectType getType();

}

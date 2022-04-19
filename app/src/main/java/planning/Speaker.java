package planning;

/**
 * This interface represents a speaker who can participate to a project
 */
public interface Speaker {
	
	/**
	 * Gets the code representing a speaker
	 * 
	 * @return the code associated to the speaker
	 */
	int getSpeakerCode();
	
	/**
	 * Gets the first name of a speaker
	 * 
	 * @return the first name of the speaker
	 */
	String getFirstName();
	
	/**
	 * Gets the last name of a speaker
	 * 
	 * @return the last name of the speaker
	 */
	String getLastName();
}

package planning;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This interface represents a speaker who can participate in a project
 */
@JsonDeserialize(as = SimpleSpeaker.class)
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

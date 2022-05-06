package planning;

import java.util.Optional;

/**
 * It's an interface that represents a generic element of high level DAW
 */
public interface Element {
	
	/**
	 * Returns the title of an element
	 * 
	 * @return the string representing the string of an element
	 */
	String getTitle();
	
	/**
	 * Adds the description
	 * 
	 * @param description
	 * the description to add
	 */
	void addDescription(String description);
	
	/**
	 * Returns the description of an element
	 * 
	 * @return the optional string representing the description of an element,
	 * Optional.empty() if the description isn't present
	 */
	Optional<String> getDescription();
}

package planning;

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
	 * Returns the description of an element
	 * 
	 * @return the string representing the description of an element
	 */
	String getDescription();
}

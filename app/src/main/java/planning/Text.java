package planning;

/**
 *It's an interface modelling a text that could be used in a RPRole of type speech
 */
public interface Text {
	
	/**
	 * Gets the string content of the text
	 * 
	 * @return the string representing the content of the text
	 * 
	 * @throws Exception if the text file string is invalid or
	 * if an I/O problem arises
	 */
	String getContent() throws Exception;
}

package planning;

/**
 * This interface represents the Factory Method to create a {@link planning.Text}
 */
public interface TextFactory {
	
	/**
	 * Creates a text from a string
	 * 
	 * @param content
	 * the string with the content of the text
	 * 
	 * @return the text created
	 */
	Text createFromString(String content);
	
	/**
	 * Creates a text from a text file
	 * 
	 * @param fileName
	 * the string file in which the content is present
	 * 
	 * @return the text created
	 */
	Text createFromFile(String fileName);
}

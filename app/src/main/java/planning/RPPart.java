package planning;

import java.util.List;
import java.util.Optional;

/**
 * It's an interface that represents a clip in high level which could belong to a guest or a soundtrack
 */
public interface RPPart extends Element{
	
	/**
	 * Represents the three possible types of a RPPart
	 */
	public enum PartType{
		SPEECH, SOUNDTRACK, EFFECTS
	}
	
	/**
	 * Allows to add notes in a part
	 * 
	 * @param note
	 * 			the note to add
	 */
	void addNote(String note);
	
	/**
	 * Returns a list of notes
	 * 
	 * @return the list of notes belonging to a part
	 */
	List<String> getNotes();
	
	/**
	 * Returns the type of a part
	 * 
	 * @return the chosen type of the part
	 */
	PartType getType();
	
	/**
	 * Allows to add text to a part of type "SPEECH": a part can have at most one text
	 * 
	 * @param text
	 * 			the text to add to a part
	 */
	void addText(final Text text);
	
	/**
	 * Returns true if the text is present
	 * 
	 * @return true if a text has been added to a part,
	 * 			always false if the part type is not "SPEECH"
	 */
	boolean isTextPresent();
	
	/**
	 * Returns the text belonging to a part
	 * 
	 * @return the optional text of a part, always 
	 * 			Optional.empty() if type part isn't
	 * 			"SPEECH"
	 */
	Optional<Text> getText();
}

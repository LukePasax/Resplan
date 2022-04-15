package planning;

import java.util.List;

/**
 * It's an interface that represents a clip in high level which could belong to a guest or a soundtrack
 */
public interface RPPart extends Element{
	
	public enum PartType{
		SPEECH, SOUNDTRACK, EFFECTS
	}
	
	void addNote(String note);
	List<String> getNotes();
	PartType getType();
}

package planning;

import java.util.List;
import java.util.Optional;

/**
 * It's an interface that represents a guest or a soundtrack in high level
 */
public interface RPRole extends Element {
	
	public enum RoleType{
		SPEECH, SOUNDTRACK, EFFECTS
	}
	
	RoleType getType();
	void addNote(String note);
	List<String> getNotes();
	void addSpeaker(Speaker speaker);
	Optional<Speaker> getSpeaker();
}

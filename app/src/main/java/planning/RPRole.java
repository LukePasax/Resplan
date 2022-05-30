package planning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import java.util.Optional;

/**
 * It's an interface that represents a guest, a soundtrack, or effects in high level
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		property = "type name")
@JsonSubTypes({
		@JsonSubTypes.Type(value = EffectsRole.class, name = "effects"),
		@JsonSubTypes.Type(value = SoundtrackRole.class, name = "soundtrack"),
		@JsonSubTypes.Type(value = SpeechRole.class, name = "speech")
})
public interface RPRole extends Element {
	
	/**
	 * Represents the three possible types of a RPRole
	 */
	enum RoleType{
		SPEECH, SOUNDTRACK, EFFECTS
	}
	
	/**
	 * Returns the type of a role
	 * 
	 * @return the chosen type of the role
	 */
	@JsonIgnore
	RoleType getType();
	
	/**
	 * Allows to add notes in a role
	 * 
	 * @param note
	 * the note to add
	 */
	void addNote(String note);
	
	/**
	 * Returns a list of notes
	 * 
	 * @return the list of notes belonging to a role
	 */
	List<String> getNotes();
	
	/**
	 * Allows to add a speaker to a role of type "SPEECH": a role can have at most one speaker
	 * 
	 * @param speaker
	 * the speaker to add to a role
	 */
	void addSpeaker(Speaker speaker);
	
	/**
	 * Returns true if the speaker is present
	 * 
	 * @return true if a speaker has been added to a role,
	 * always false if the role type is not "SPEECH"
	 */
	boolean isSpeakerPresent();
	
	/**
	 * Returns the speaker owner of a role
	 * 
	 * @return the optional speaker of a role, always 
	 * Optional.empty() if role type isn't
	 * "SPEECH"
	 */
	Optional<Speaker> getSpeaker();
}

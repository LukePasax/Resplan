package planning;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * It's an abstract implementation of a {@link planning.RPRole}
 */
public abstract class RoleImpl implements RPRole {
	
	private String title;
	private Optional<String> description = Optional.empty();
	private RoleType type;
	private final List<String> notesList = new ArrayList<>();
	
	/**
	 * It is used to build a new Object of type RPRole, with description, but it can't be istantieted
	 * 
	 * @param title
	 * the title of the role
	 * 
	 * @param description
	 * the description of the role
	 * 
	 * @param type
	 * the type of the role
	 */
	public RoleImpl(@JsonProperty("title") final String title, @JsonProperty("description") String description,
					@JsonProperty("type") final RoleType type) {
		this.title = title;
		this.description = Optional.of(description);
		this.type = type;
	}
	
	/**
	 * It is used to build a new Object of type RPRole, without description, but it can't be istantieted
	 * 
	 * @param title
	 * the title of the role
	 * 
	 * @param type
	 * the type of the role
	 */
	public RoleImpl(final String title, final RoleType type) {
		this.title = title;
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDescription(final String description) {
		this.description = Optional.of(description);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RoleType getType() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNote(String note) {
		this.notesList.add(note);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean isSpeakerPresent();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getNotes() {
		return this.notesList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void addSpeaker(Speaker speaker);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Optional<Speaker> getSpeaker();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title, type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleImpl other = (RoleImpl) obj;
		return Objects.equals(title, other.title) && type == other.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RoleImpl [title=" + title + ", description=" + description + ", type=" + type + "]";
	}
}

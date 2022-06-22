package planning;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * It's an abstract implementation of a {@link planning.RPRole}
 */
public abstract class RoleImpl implements RPRole {
	
	private final String title;
	private Optional<String> description = Optional.empty();
	private final RoleType type;
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
	public RoleImpl(@JsonProperty("title") final String title,
					@JsonProperty("description") final String description,
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
	public final String getTitle() {
		return this.title;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addDescription(final String description) {
		this.description = Optional.of(description);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Optional<String> getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final RoleType getType() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addNote(String note) {
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
	public final List<String> getNotes() {
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
	public final int hashCode() {
		return Objects.hash(title, type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object obj) {
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
	public final String toString() {
		return "RoleImpl [title=" + title + ", description=" + description + ", type=" + type + "]";
	}
}

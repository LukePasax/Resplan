package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * It's an abstract implementation of a {@link planning.RPPart}
 */
public abstract class PartImpl implements RPPart {
	
	private final String title;
	private Optional<String> description = Optional.empty();
	private final PartType type;
	private final List<String> notes = new ArrayList<>();
	
	/**
	 * It is used to build a new Object of type RPPart, with the description, but it can't be instantiated
	 * 
	 * @param title
	 * the title of the part
	 * 
	 * @param description
	 * the description of the part
	 * 
	 * @param type
	 * the type of the part
	 */
	public PartImpl(final String title, final String description, final PartType type) {
		this.title = title;
		this.description = Optional.of(description);
		this.type = type;
	}
	
	/**
	 * It is used to build a new Object of type RPPart, without the description, but it can't be instantiated
	 * 
	 * @param title
	 * the title of the part
	 * 
	 * @param type
	 * the type of the part
	 */
	public PartImpl(final String title, final PartType type) {
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
	public final void addNote(String note) {
		this.notes.add(note);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getNotes() {
		return this.notes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final PartType getType() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void addText(final Text text);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean isTextPresent();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Optional<Text> getText();
	
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
		PartImpl other = (PartImpl) obj;
		return Objects.equals(title, other.title) && type == other.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return "PartImpl [title=" + title + ", description=" + description + ", type=" + type + "]";
	}
}

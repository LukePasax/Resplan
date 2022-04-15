package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * It's an abstract implementation of a {@link planning.RPPart}
 */
public abstract class PartImpl implements RPPart {
	
	private String title;
	private String description;
	private PartType type;
	private List<String> notes = new ArrayList<>();
	
	/**
	 * It is used to build a new Object of type RPPart, but it can't be istantieted
	 * 
	 * @param title
	 * 			the title of the part
	 * @param description
	 * 			the description of the part
	 * @param type
	 * 			the type of the part
	 */
	public PartImpl(final String title, final String description, final PartType type) {
		this.title = title;
		this.description = description;
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
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNote(String note) {
		this.notes.add(note);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getNotes() {
		return this.notes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PartType getType() {
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
		PartImpl other = (PartImpl) obj;
		return Objects.equals(title, other.title) && type == other.type;
	}
}

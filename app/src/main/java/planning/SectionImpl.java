package planning;

import java.util.Objects;
import java.util.Optional;

/**
 * It's the implementation of a {@link planning.RPSection}
 */
public class SectionImpl implements RPSection {
	
	private String title;
	private Optional<String> description = Optional.empty();
	private double duration;
	
	/**
	 * It is used to build a new Object of type RPSection with a description
	 * 
	 * @param title
	 * the title of the section
	 * 
	 * @param description
	 * the description of the section
	 * 
	 * @param duration
	 * the duration of the section
	 */
	public SectionImpl(final String title, final String description, final double duration) {
		this.title = title;
		this.description = Optional.of(description);
		this.duration = duration;
	}
	
	/**
	 * It is used to build a new Object of type RPSection without a description
	 * 
	 * @param title
	 * the title of the section
	 * 
	 * @param duration
	 * the duration of the section
	 */
	public SectionImpl(final String title, final double duration) {
		this.title = title;
		this.duration = duration;
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
	public double getDuration() {
		return this.duration;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title);
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
		SectionImpl other = (SectionImpl) obj;
		return Objects.equals(title, other.title);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "SectionImpl [title=" + title + ", description=" + description + ", duration=" + duration + "]";
	}
}

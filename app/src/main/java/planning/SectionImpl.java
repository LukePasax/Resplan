package planning;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

/**
 * It's the implementation of a {@link planning.RPSection}
 */
public final class SectionImpl implements RPSection {
	
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
	@JsonCreator
	public SectionImpl(@JsonProperty("title") final String title,
					   @JsonProperty("description") final String description,
					   @JsonProperty("duration") final double duration) {
		this.title = title;
		if (description != null) {
			this.description = Optional.of(description);
		} else {
			this.description = Optional.empty();
		}
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
	public final double getDuration() {
		return this.duration;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		return Objects.hash(title);
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
		SectionImpl other = (SectionImpl) obj;
		return Objects.equals(title, other.title);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return "SectionImpl [title=" + title + ", description=" + description + ", duration=" + duration + "]";
	}
}

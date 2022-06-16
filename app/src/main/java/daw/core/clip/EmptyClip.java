package daw.core.clip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * An {@link RPClip} without a content.
 * <p>Since there's no content an EmptyClip stores only its duration.
 */
public class EmptyClip implements RPClip<NoContent> {

	/**
	 * The duration of this RPClip.
	 */
	private double duration;
	private final String title;

	/**
	 * Creates an EmptyClip with a duration of {@value RPClip#DEFAULT_DURATION}
	 */
	public EmptyClip(String title) {
		this.duration = RPClip.DEFAULT_DURATION;
		this.title = title;
	}

	/**
	 * Creates an EmptyClip with a specified duration.
	 *
	 * @param duration The duration of this clip in milliseconds.
	 */
	@JsonCreator
	public EmptyClip(@JsonProperty("duration") double duration, @JsonProperty("name") String title) {
		if (Double.compare(duration, 0.0) < 0) {
			throw new IllegalArgumentException("The duration of a clip must be a non-zero and positive value.");
		}
		this.duration = duration;
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 */
	@Override
	public void setContentPosition(double milliseconds) {
		throw new UnsupportedOperationException("Can't set Content Position in an Empty Clip. " +
				"Convert the clip into one with content then retry.");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 */
	@Override
	public double getContentPosition() {
		throw new UnsupportedOperationException("Can't get the Content Position. This is an Empty Clip. " +
				"Convert the clip into one with content then retry.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getContentDuration() {
		throw new UnsupportedOperationException("A file content has no duration");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 */
	@Override
	public NoContent getContent() {
		throw new UnsupportedOperationException("Can't get the Content of an Empty Clip. " +
				"Convert the clip into one with content then retry.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	@Override
	public void setDuration(double milliseconds) {
		if (duration <= 0) {
			throw new IllegalArgumentException("The duration of a clip must be a non-zero and positive value.");
		}
		this.duration = milliseconds;
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
	public RPClip<NoContent> duplicate() {
		return new EmptyClip(this.duration, this.title + "(Duplicate)");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmptyClip emptyClip = (EmptyClip) o;
		return title.equals(emptyClip.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

}

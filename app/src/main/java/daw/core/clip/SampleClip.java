package daw.core.clip;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

/**
 * A SampleClip object is an RPClip which accepts any Audio File as content.
 * <p>The default duration is the same as the audio content one.
 * A SampleClip wrap a FileClip object and add all the Audio content related features and controls.
 */
public final class SampleClip implements RPClip<Sample> {
	
	/**
	 * The FileClip that this object wrap.
	 */
	private final RPClip<File> clip;
	
	/**
	 * The audio {@link Sample} content.
	 */
	private final Sample sample;
	
	
	/**
	 * Private internal constructor used by other public/protected constructors.
	 * <p>Uses the proxy pattern for create a SampleClip from a FileClip 
	 * and be sure that the file is an Audio File.
	 * The SampleClip duration is the same as the supplied FileClip or the same as the content duration 
	 * if the duration of the supplied clip is bigger than the content one.
	 * 
	 * @param  fileClip  The File Clip to wrap.
	 * 
	 * @throws  IllegalArgumentException  If the supplied clip isn't a {@code FileClip.class} object
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	private SampleClip(final RPClip<File> fileClip) throws IOException, OperationUnsupportedException, FileFormatException {
		if (fileClip.getClass() != FileClip.class) {
			throw new IllegalArgumentException("The supplied fileClip must be a FileClip class object");
		}
		this.clip = fileClip;
		this.sample = new Sample(clip.getContent().getAbsolutePath());
		if (this.clip.getDuration() > this.sample.getLength()) {
			this.setDuration(this.sample.getLength());
		}
	}

	/**
	 * Creates a SampleClip from the supplied file content and an empty clip.
	 * <p>Constructor reserved for {@link RPClipConverter}. 
	 * It takes an empty clip and creates a sample clip adding an audio file.
	 * The SampleClip duration is the same as the supplied FileClip or the same as the content duration 
	 * if the duration of the supplied clip is bigger than the content one.
	 * 
	 * @param  file  The audio file content.
	 * 
	 * @param  emptyClip  The empty clip to wrap.
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	protected SampleClip(final File file, final RPClip<?> emptyClip) throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(file, emptyClip));
	}
	
	/**
	 * Creates a Sample Clip just specifying an audio file. The duration will be fitted to the audio file duration.
	 * 
	 * @param title The title of this clip.
	 * 
	 * @param  content  The audio file content.
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	public SampleClip(final String title, final File content)
			throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(title, content));
		this.setDuration(sample.getLength());
	}
	
	/**
	 * Creates a Sample Clip just specifying an audio file and a duration.
	 * The SampleClip duration will be the same as the content duration 
	 * if the specified duration is bigger than the content one.
	 * 
	 * @param title The title of this clip.
	 * 
	 * @param  duration  The duration of the clip in milliseconds.
	 * 
	 * @param  file  The audio file content.
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	@JsonCreator
	public SampleClip(@JsonProperty("name") final String title, @JsonProperty("duration") final double duration, 
			@JsonProperty("content name") final File file)
			throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(title, duration, file));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IllegalArgumentException  If the specified duration is zero, a negative value or if the
	 * specified duration is bigger than the content duration.
	 */
	@Override
	public void setDuration(final double milliseconds) {
		if ((sample.getLength() - this.getContentPosition()) < milliseconds) {
			throw new IllegalArgumentException("the new clip duration can't be bigger than the content duration");
		}
		this.clip.setDuration(milliseconds);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IllegalArgumentException  {@inheritDoc}
	 */
	@Override
	public void setContentPosition(final double milliseconds) {
		if (sample.getLength() <= milliseconds) {
			throw new IllegalArgumentException("the new content position can't be bigger or equal "
					+ "than the content duration");
		}
		this.clip.setContentPosition(milliseconds);
	}

	@Override
	public double getDuration() {
		return this.clip.getDuration();
	}

	@Override
	public double getContentPosition() {
		return this.clip.getContentPosition();
	}

	@Override
	public double getContentDuration() {
		return this.sample.getLength();
	}

	@Override
	public Sample getContent() {
		return this.sample;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	@Override
	public RPClip<Sample> duplicate(final String title) throws IOException, OperationUnsupportedException, FileFormatException {
			return new SampleClip(this.clip.duplicate(title));
	}

	@Override
	public String getTitle() {
		return clip.getTitle();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SampleClip that = (SampleClip) o;
		return Objects.equals(clip.getTitle(), that.clip.getTitle());
	}

	@Override
	public int hashCode() {
		return Objects.hash(clip);
	}
}

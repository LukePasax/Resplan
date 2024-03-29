package daw.core.clip;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

/**
 * An {@link RPClip} which accepts any File as content.
 * <p>
 * A FileClip is designed for store files that could be interpreted as an audio 
 * in some way or converted to it, so the content position is a time value in milliseconds.
 * A FileClip wrap an Empty RPClip object and add all the File content related features and controls.
 */
public final class FileClip implements RPClip<File> {
	
	/**
	 * The empty clip that this object wrap.
	 */
	private final RPClip<?> clip;
	
	/**
	 * The File content.
	 */
	private final File content;
	
	/**
	 * The content starting point in milliseconds.
	 */
	private double contentPosition;
	
	/**
	 * Creates a FileClip from a file content and an empty Clip.
	 * Constructor reserved for the {@link RPClipConverter} method 
	 * that fills an empty clip with a content.
	 * The duration is the same as the supplied empty clip.
	 * 
	 * @param  content  The File content of this RPClip.
	 * 
	 * @param  emptyClip  The empty RPClip to wrap.
	 * 
	 * @throws  IllegalArgumentException  If the supplied file does not exist
	 * 							 or if {@link #isEmpty} method of the supplied clip returns {@code false}.
	 */
	FileClip(final File content, final RPClip<?> emptyClip) {
		if (!content.exists()) {
			throw new IllegalArgumentException("The file does not exists");
		}
		this.content = content;
		if (!emptyClip.isEmpty()) {
			throw new IllegalArgumentException("The supplied clip must be empty");
		}
		this.clip = emptyClip;
	}
	
	/**
	 * Creates a FileClip with a duration of {@value RPClip#DEFAULT_DURATION} milliseconds 
	 * filled with the specified file.
	 * 
	 * @param title The title of this clip.
	 * 
	 * @param  content  The File content of this RPClip.
	 * 
	 * @throws  IllegalArgumentException  If the supplied file does not exist
	 * 							 or if {@link #isEmpty} method of the supplied clip returns {@code false}.
	 */
	public FileClip(final String title, final File content) {
		this(content, new EmptyClip(title));
	}
	
	/**
	 * Creates a FileClip with the specified duration filled with the specified file.
	 * 
	 * @param title The title of this clip.
	 * 
	 * @param  duration  The duration of this clip in milliseconds.
	 * 
	 * @param  content  The File content of this RPClip.
	 */
	public FileClip(final String title, final double duration, final File content) {
		this(content, new EmptyClip(title, duration));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IllegalArgumentException {@inheritDoc}
	 */
	@Override
	public void setDuration(final double milliseconds) {
		this.clip.setDuration(milliseconds);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IllegalArgumentException {@inheritDoc}
	 */
	@Override
	public void setContentPosition(final double milliseconds) {
		this.contentPosition = milliseconds;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDuration() {
		return this.clip.getDuration();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getContentPosition() {
		return this.contentPosition;
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public double getContentDuration() {
		throw new UnsupportedOperationException("A file content has no duration");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getContent() {
		return this.content;
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * 
	 * @throws IOException 
	 */
	@Override
	public RPClip<File> duplicate(final String title) throws IOException, OperationUnsupportedException, FileFormatException {
		RPClip<File> newClip = new FileClip(this.content, this.clip.duplicate(title));
		newClip.setContentPosition(this.contentPosition);
		return newClip;
	}

	@Override
	public String getTitle() {
		return clip.getTitle();
	}

	@Override
	// DO NOT DELETE!
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FileClip fileClip = (FileClip) o;
		return Objects.equals(clip.getTitle(), fileClip.clip.getTitle());
	}

	@Override
	public int hashCode() {
		return Objects.hash(clip);
	}
}

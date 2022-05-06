package daw.core.clip;

import java.io.File;

/**
 * An {@link RPClip} which accepts any File as content.
 * <p>
 * A FileClip is designed for store files that could be interpreted as an audio 
 * in some way or converted to it, so the content position is a time value in milliseconds.
 * A FileClip wrap an Empty RPClip object and add all the File content related features and controls.
 */
public class FileClip implements RPClip<File> {
	
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
	 * @throws  IllegalArgumentException  If the supplied file does not exists
	 * 							 or if {@link #isEmpty} method of the supplied clip returns {@code false}.
	 */
	protected FileClip(File content, RPClip<?> emptyClip) {
		if(!content.exists()) {
			throw new IllegalArgumentException("The file does not exists");
		}
		this.content = content;
		if(!emptyClip.isEmpty()) {
			throw new IllegalArgumentException("The supplied clip must be empty");
		}
		this.clip = emptyClip;
	}
	
	/**
	 * Creates a FileClip with a duration of {@value RPClip#DEFAULT_DURATION} milliseconds 
	 * filled with the specified file.
	 * 
	 * @param  content  The File content of this RPClip.
	 */
	public FileClip(File content) {
		this(content, new EmptyClip());
	}
	
	/**
	 * Creates a FileClip with the specified duration filled with the specified file.
	 * 
	 * @param  duration  The duration of this clip in milliseconds.
	 * 
	 * @param  content  The File content of this RPClip.
	 */
	public FileClip(double duration, File content) {
		this(content, new EmptyClip(duration));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IllegalArgumentException {@inheritDoc}
	 */
	@Override
	public void setDuration(double milliseconds) {
		this.clip.setDuration(milliseconds);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IllegalArgumentException {@inheritDoc}
	 */
	@Override
	public void setContentPosition(double milliseconds) {
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
	 */
	@Override
	public RPClip<File> duplicate() {
		RPClip<File> newClip = new FileClip(this.content, this.clip);
		newClip.setContentPosition(this.contentPosition);
		return newClip;
	}
}

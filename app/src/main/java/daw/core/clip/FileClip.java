package daw.core.clip;

import java.io.File;

/**
 * @author alessandro
 * A FileClip object is an RPClip which accepts any File as content.
 */
public class FileClip implements RPClip {
	
	private final RPClip clip;
	
	private final File content;
	
	private double contentPosition;
	
	/**
	 * Constructor reserved for the convertClip() utility method that fills an empty clip with a content.
	 * @param content
	 * @param emptyClip
	 */
	protected FileClip(File content, RPClip emptyClip) {
		if(!content.exists()) {
			throw new IllegalArgumentException("The file does not exists");
		}
		this.content = content;
		if(!emptyClip.isEmpty()) {
			throw new IllegalArgumentException("The supplied clip must be empty");
		}
		this.clip = emptyClip;
	}
	
	public FileClip(File content) {
		this(content, new EmptyClip());
	}
	
	@Override
	public void setDuration(double milliseconds) {
		this.clip.setDuration(milliseconds);
	}

	@Override
	public void setContentPosition(double milliseconds) {
		this.contentPosition = milliseconds;
	}
	
	@Override
	public double getDuration() {
		return this.clip.getDuration();
	}
	
	@Override
	public double getContentPosition() {
		return this.contentPosition;
	}

	@Override
	public File getContent() {
		return this.content;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}

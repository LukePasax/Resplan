package daw.core.clip;

import java.io.File;

/**
 * @author alessandro
 * A FileClip object is an RPClip which accepts any File as content.
 */
public class FileClip implements RPClip {
	
	private final File content;
	
	private double duration;
	
	private double contentPosition;
	
	public FileClip(File content) {
		if(!content.exists()) {
			throw new IllegalArgumentException("The file does not exists");
		}
		this.content = content;
	}

	@Override
	public void setDuration(double milliseconds) {
		this.duration = milliseconds;
	}

	@Override
	public void setContentPosition(double milliseconds) {
		this.contentPosition = milliseconds;
	}

	@Override
	public double getDuration() {
		return this.duration;
	}

	@Override
	public double getContentPosition() {
		return this.contentPosition;
	}

	@Override
	public File getContent() {
		return this.content;
	}

}

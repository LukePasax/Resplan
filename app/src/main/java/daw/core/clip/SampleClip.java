package daw.core.clip;

import java.io.File;
import java.io.IOException;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

/**
 * @author alessandro
 * A SampleClip object is an RPClip which accepts any Audio File as content.
 * The default duration is the same as the audio content one.
 */
public class SampleClip implements RPClip {
	
	private final RPClip clip;
	
	private final Sample sample;
	
	private SampleClip(FileClip fileClip) throws IOException, OperationUnsupportedException, FileFormatException {
		this.clip = fileClip;
		this.sample = new Sample(clip.getContent().getAbsolutePath());
		this.setDuration(sample.getLength());
	}

	protected SampleClip(File file, RPClip emptyClip) throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(file, emptyClip));
	}
	
	public SampleClip(File file) throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(file));
	}
	
	@Override
	public void setDuration(double milliseconds) {
		if(sample.getLength()<milliseconds) {
			throw new IllegalArgumentException("the new clip duration can't be bigger than the content duration");
		}
		this.clip.setDuration(milliseconds);
	}

	@Override
	public void setContentPosition(double milliseconds) {
		if(sample.getLength()<=milliseconds) {
			throw new IllegalArgumentException("the new content position can't be bigger or equal than the content duration");
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
	public File getContent() {
		return this.clip.getContent();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}

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
	
	private final RPClip fileClip;
	
	private final Sample sample;

	public SampleClip(File file) throws IOException, OperationUnsupportedException, FileFormatException {
		this.fileClip = new FileClip(file);
		this.sample = new Sample(fileClip.getContent().getAbsolutePath());
		this.setDuration(sample.getLength());
	}

	@Override
	public void setDuration(double milliseconds) {
		if(sample.getLength()<milliseconds) {
			throw new IllegalArgumentException("the new clip duration can't be bigger than the content duration");
		}
		this.fileClip.setDuration(milliseconds);
	}

	@Override
	public void setContentPosition(double milliseconds) {
		if(sample.getLength()<=milliseconds) {
			throw new IllegalArgumentException("the new content position can't be bigger or equal than the content duration");
		}
		this.fileClip.setContentPosition(milliseconds);
	}

	@Override
	public double getDuration() {
		return this.fileClip.getDuration();
	}

	@Override
	public double getContentPosition() {
		return this.fileClip.getContentPosition();
	}

	@Override
	public File getContent() {
		return this.fileClip.getContent();
	}

}

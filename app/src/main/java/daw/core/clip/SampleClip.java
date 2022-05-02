package daw.core.clip;

import java.io.File;
import java.io.IOException;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

/**
 * A SampleClip object is an RPClip which accepts any Audio File as content.
 * The default duration is the same as the audio content one.
 */
public class SampleClip implements RPClip {
	
	private final RPClip clip;
	
	private final Sample sample;
	
	/**
	 * Use the proxy for create a file clip from a sample clip and be shure that the file is a
	 * @param fileClip
	 * @throws IOException
	 * @throws OperationUnsupportedException
	 * @throws FileFormatException
	 */
	private SampleClip(RPClip fileClip) throws IOException, OperationUnsupportedException, FileFormatException {
		if(fileClip.getClass()!=FileClip.class) {
			throw new IllegalArgumentException("The supplied fileClip must be a FileClip class object");
		}
		this.clip = fileClip;
		this.sample = new Sample(clip.getContent().getAbsolutePath());
	}

	/**
	 * Constructor for converter. It takes an empty clip and want to create a sample clip adding an audio file
	 * @param file
	 * @param emptyClip
	 * @throws IOException
	 * @throws OperationUnsupportedException
	 * @throws FileFormatException
	 */
	protected SampleClip(File file, RPClip emptyClip) throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(file, emptyClip));
	}
	
	/**
	 * Constructor for client. Just specify an audio File. The duration will be fitted to the audio file duration.
	 * @param file
	 * @throws IOException
	 * @throws OperationUnsupportedException
	 * @throws FileFormatException
	 */
	public SampleClip(File content) throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(content));
		this.setDuration(sample.getLength());
	}
	
	/**
	 * Constructor for client. Just specify an audio File and a duration.
	 * @param duration
	 * @param content
	 * @throws IOException
	 * @throws OperationUnsupportedException
	 * @throws FileFormatException
	 */
	public SampleClip(double duration, File content) throws IOException, OperationUnsupportedException, FileFormatException {
		this(new FileClip(duration, content));
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

	@Override
	public RPClip duplicate() throws IOException, OperationUnsupportedException, FileFormatException {
			return new SampleClip(this.clip);
	}
}

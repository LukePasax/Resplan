package daw.core.clip;

import java.io.File;
import java.io.IOException;

import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

/**
 * {@link RPClipConverter} implementation.
 * <p>Converts a specific type of RPClip in a compatible type, 
 * for example to fill an {@link EmptyClip} with some kind of content.
 */
public class ClipConverter implements RPClipConverter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileClip fromEmptyToFileClip(EmptyClip emptyClip, File file) {
		return new FileClip(file, emptyClip);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IOException  {@inheritDoc}
	 * 
	 * @throws  OperationUnsupportedException  {@inheritDoc}
	 * 
	 * @throws  FileFormatException  {@inheritDoc}
	 */
	@Override
	public SampleClip fromEmptyToSampleClip(EmptyClip emptyClip, File file) throws IOException, OperationUnsupportedException, FileFormatException {
		return new SampleClip(file, emptyClip);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws  IOException  {@inheritDoc}
	 * 
	 * @throws  OperationUnsupportedException  {@inheritDoc}
	 * 
	 * @throws  FileFormatException  {@inheritDoc}
	 */
	@Override
	public SampleClip fromFileToSampleClip(FileClip fileClip) throws IOException, OperationUnsupportedException, FileFormatException {
		SampleClip clip = new SampleClip(fileClip.getContent(), this.fromFileToEmptyClip(fileClip));
		clip.setContentPosition(fileClip.getContentPosition());
		return clip;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EmptyClip fromFileToEmptyClip(FileClip fileClip) {
		return new EmptyClip(fileClip.getDuration());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EmptyClip fromSampleToEmptyClip(SampleClip sampleClip) {
		return new EmptyClip(sampleClip.getDuration());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileClip fromSampleToFileClip(SampleClip sampleClip) {
		FileClip clip = new FileClip(new File(sampleClip.getContent().getFileName()), new EmptyClip(sampleClip.getDuration()));
		clip.setContentPosition(sampleClip.getContentPosition());
		return clip;
	}

}

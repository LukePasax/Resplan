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
public final class ClipConverter implements RPClipConverter {

	@Override
	public FileClip fromEmptyToFileClip(final EmptyClip emptyClip, final File file) {
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
	public SampleClip fromEmptyToSampleClip(final EmptyClip emptyClip, final File file) throws IOException, OperationUnsupportedException, FileFormatException {
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
	public SampleClip fromFileToSampleClip(final FileClip fileClip) throws IOException, OperationUnsupportedException, FileFormatException {
		SampleClip clip = new SampleClip(fileClip.getContent(), this.fromFileToEmptyClip(fileClip));
		clip.setContentPosition(fileClip.getContentPosition());
		return clip;
	}

	@Override
	public EmptyClip fromFileToEmptyClip(final FileClip fileClip) {
		return new EmptyClip(fileClip.getTitle(), fileClip.getDuration());
	}

	@Override
	public EmptyClip fromSampleToEmptyClip(final SampleClip sampleClip) {
		return new EmptyClip(sampleClip.getTitle(), sampleClip.getDuration());
	}

	@Override
	public FileClip fromSampleToFileClip(final SampleClip sampleClip) {
		FileClip clip = new FileClip(new File(sampleClip.getContent().getFileName()), new EmptyClip(sampleClip.getTitle(), sampleClip.getDuration()));
		clip.setContentPosition(sampleClip.getContentPosition());
		return clip;
	}
}

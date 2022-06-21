package daw.core.clip;

import java.io.File;
import java.io.IOException;

import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

/**
 * Clip Types Converter.
 * <p>Converts a specific type of RPClip in a compatible type, 
 * for example to fill an {@link EmptyClip} with some kind of content.
 */
public interface RPClipConverter {
	
	/**
	 * Convert an {@link EmptyClip} to a {@link FileClip}.
	 * 
	 * @param  emptyClip  The EmptyClip to convert.
	 * 
	 * @param  file  The file to insert in the EmptyClip.
	 * 
	 * @return  The FileClip.
	 */
	FileClip fromEmptyToFileClip(EmptyClip emptyClip, File file);
	
	/**
	 * Convert an {@link EmptyClip} to a {@link SampleClip}.
	 * 
	 * @param  emptyClip  The EmptyClip to convert.
	 * 
	 * @param  file  The audio file to insert in the EmptyClip.
	 * 
	 * @return  The SampleClip.
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	SampleClip fromEmptyToSampleClip(EmptyClip emptyClip, File file) throws IOException, OperationUnsupportedException, FileFormatException;
	
	/**
	 * Convert a {@link FileClip} to a {@link SampleClip}.
	 * 
	 * @param  fileClip  The FileClip to convert.
	 * 
	 * @return  The SampleClip.
	 * 
	 * @throws  IOException  If some I/O exception has occurred.
	 * 
	 * @throws  OperationUnsupportedException  If some write/read operation is not supported for this file.
	 * 
	 * @throws  FileFormatException  If the file format isn't a supported audio format.
	 */
	SampleClip fromFileToSampleClip(FileClip fileClip) throws IOException, OperationUnsupportedException, FileFormatException;
	
	/**
	 * Convert a {@link FileClip} to an {@link EmptyClip}.
	 * 
	 * @param  fileClip  The FileClip to convert.
	 * 
	 * @return  The EmptyClip.
	 */
	EmptyClip fromFileToEmptyClip(FileClip fileClip);
	
	/**
	 * Convert a {@link SampleClip} to an {@link EmptyClip}.
	 * 
	 * @param  sampleClip  The SampleClip to convert.
	 * 
	 * @return  The EmptyClip.
	 */
	EmptyClip fromSampleToEmptyClip(SampleClip sampleClip);
	
	/**
	 * Convert an {@link SampleClip} to a {@link FileClip}.
	 * 
	 * @param  sampleClip  The SampleClip to convert.
	 * 
	 * @return  The FileClip.
	 */
	FileClip fromSampleToFileClip(SampleClip sampleClip);

}

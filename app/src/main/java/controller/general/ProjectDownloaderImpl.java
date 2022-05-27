package controller.general;

import controller.storing.WriteToFileImpl;
import controller.storing.serialization.ManagerSerializer;
import controller.storing.serialization.Serializer;
import daw.manager.Manager;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

/**
 * Implements {@link ProjectDownloader}.
 */
public class ProjectDownloaderImpl implements ProjectDownloader {

    private final Serializer<Manager> serializer;

    public ProjectDownloaderImpl() {
        this.serializer = new ManagerSerializer(true, false);
    }

    /**
     * {@inheritDoc}
     * @param file the file where to download.
     * @throws IOException if the writer fails to write onto the given file.
     * @throws FileFormatException if the given file is not compatible with the standard format.
     */
    @Override
    public void download(File file, final Manager manager) throws IOException, FileFormatException {
        if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
            throw new FileFormatException("Selected file's format is not supported. Choose only .json files.");
        }
        new WriteToFileImpl(file).write(this.serializer.serialize(manager));
    }

}

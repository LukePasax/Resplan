package controller.general;

import controller.storing.RPFileReader;
import controller.storing.deserialization.ManagerDeserializer;
import daw.manager.Manager;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;

/**
 * Implements {@link ProjectLoader}.
 */
public class ProjectLoaderImpl implements ProjectLoader {

    private final ManagerDeserializer deserializer;

    public ProjectLoaderImpl() {
        this.deserializer = new ManagerDeserializer();
    }

    /**
     * {@inheritDoc}
     * @param file the file where to read from.
     * @return the {@link Manager} corresponding to the loaded project.
     * @throws IOException if the reader fails to read from the given file.
     * @throws FileFormatException if the given file is not compatible with the standard format.
     */
    public Manager load(File file) throws IOException, FileFormatException {
        if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
            throw new FileFormatException("Selected file's format is not supported. Choose only .json files.");
        }
        return this.deserializer.deserialize(new RPFileReader(file).read());
    }

}

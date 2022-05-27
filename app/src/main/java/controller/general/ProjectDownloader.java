package controller.general;

import daw.manager.Manager;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import java.io.File;
import java.io.IOException;

/**
 * Interface for classes responsible for project downloading onto file. Said classes will somehow use
 * both a serialization system and a writer to save the current project on a file.
 */
public interface ProjectDownloader {

    /**
     * Downloads the current project onto the given file.
     * @param file the file where to download.
     * @param manager the {@link Manager} that represents the current project.
     * @throws IOException if the writer fails to write onto the given file.
     * @throws FileFormatException if the given file is not compatible with the standard format.
     */
    void download(File file, Manager manager) throws IOException, FileFormatException;

}

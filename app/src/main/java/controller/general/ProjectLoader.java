package controller.general;

import daw.manager.Manager;
import net.beadsproject.beads.data.audiofile.FileFormatException;

import java.io.File;
import java.io.IOException;

/**
 * Interface for classes responsible for project loading from file. Said classes will somehow use
 * both a deserialization system and a reader to load a project from a file.
 */
public interface ProjectLoader {

    /**
     * Loads the project that is saved on the given file.
     * @param file the file where to read from.
     * @return the {@link Manager} corresponding to the loaded project.
     * @throws IOException if the reader fails to read from the given file.
     * @throws FileFormatException if the given file is not compatible with the standard format.
     */
    Manager load(File file) throws IOException, FileFormatException;

}

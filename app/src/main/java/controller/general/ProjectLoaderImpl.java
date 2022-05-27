package controller.general;

import controller.storing.ReadFromFileImpl;
import controller.storing.deserialization.ManagerDeserializer;
import daw.manager.Manager;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;

public class ProjectLoaderImpl implements ProjectLoader {

    private final ManagerDeserializer deserializer;

    public ProjectLoaderImpl() {
        this.deserializer = new ManagerDeserializer();
    }

    public Manager load(File file) throws IOException, FileFormatException {
        if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
            throw new FileFormatException("Selected file's format is not supported. Choose only .json files.");
        }
        return this.deserializer.deserialize(new ReadFromFileImpl(file).read());
    }

}

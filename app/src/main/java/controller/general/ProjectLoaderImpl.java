package controller.general;

import controller.storing.ReadFromFile;
import controller.storing.ReadFromFileImpl;
import controller.storing.deserialization.ManagerDeserializer;
import daw.manager.Manager;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class ProjectLoaderImpl implements ProjectLoader {

    private final ManagerDeserializer deserializer;

    public ProjectLoaderImpl() {
        this.deserializer = new ManagerDeserializer();
    }

    public Manager load(File file) throws IOException {
        if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
            throw new IllegalArgumentException("Selected file's format is not supported. Choose only .json files.");
        }
        final ReadFromFile reader = new ReadFromFileImpl(file);
        return this.deserializer.deserialize(reader.read());
    }

}

package controller.general;

import controller.storing.ReadFromFile;
import controller.storing.ReadFromFileImpl;
import controller.storing.deserialization.ManagerDeserializer;
import daw.manager.Manager;
import java.io.File;
import java.io.IOException;

public class ProjectLoaderImpl implements ProjectLoader {

    private static final String SEP = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    private final ReadFromFile reader;
    private final ManagerDeserializer deserializer;

    public ProjectLoaderImpl() {
        this.reader = new ReadFromFileImpl(new File(WORKING_DIRECTORY + SEP + "save.json"));
        this.deserializer = new ManagerDeserializer();
    }

    public Manager load() throws IOException, IllegalStateException {
        try {
            return this.deserializer.deserialize(this.reader.read());
        } catch (RuntimeException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

}

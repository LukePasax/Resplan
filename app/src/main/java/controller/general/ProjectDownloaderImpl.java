package controller.general;

import controller.storing.WriteToFile;
import controller.storing.WriteToFileImpl;
import controller.storing.serialization.ManagerSerializer;
import controller.storing.serialization.Serializer;
import daw.manager.Manager;
import java.io.File;
import java.io.IOException;

public class ProjectDownloaderImpl implements ProjectDownloader {

    private static final String SEP = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    private final WriteToFile writer;
    private final Serializer<Manager> serializer;
    private final Manager manager;

    public ProjectDownloaderImpl(Manager manager) {
        this.manager = manager;
        this.writer = new WriteToFileImpl(new File(WORKING_DIRECTORY + SEP + "save.json"));
        this.serializer = new ManagerSerializer(true, false);
    }

    @Override
    public void save() throws IOException {
        this.writer.write(this.serializer.serialize(this.manager));
    }

}

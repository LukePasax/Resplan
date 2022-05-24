package controller.general;

import controller.storing.WriteToFile;
import controller.storing.WriteToFileImpl;
import controller.storing.serialization.ManagerSerializer;
import controller.storing.serialization.Serializer;
import daw.manager.Manager;
import java.io.File;
import java.io.IOException;

public class ProjectDownloaderImpl implements ProjectDownloader {

    private final WriteToFile writer;
    private final Serializer<Manager> serializer;
    private final Manager manager;

    public ProjectDownloaderImpl(Manager manager) {
        this.manager = manager;
        this.writer = new WriteToFileImpl(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "save.json"));
        this.serializer = new ManagerSerializer(true, false);
    }

    @Override
    public void download() throws IOException {
        this.writer.write(this.serializer.serialize(this.manager));
    }

}

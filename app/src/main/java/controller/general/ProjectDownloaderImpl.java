package controller.general;

import controller.storing.WriteToFileImpl;
import controller.storing.serialization.ManagerSerializer;
import controller.storing.serialization.Serializer;
import daw.manager.Manager;
import java.io.File;
import java.io.IOException;

public class ProjectDownloaderImpl implements ProjectDownloader {

    private final Serializer<Manager> serializer;
    private final Manager manager;

    public ProjectDownloaderImpl(Manager manager) {
        this.manager = manager;
        this.serializer = new ManagerSerializer(true, false);
    }

    @Override
    public void download(File file) throws IOException {
        new WriteToFileImpl(file).write(this.serializer.serialize(this.manager));
    }

}

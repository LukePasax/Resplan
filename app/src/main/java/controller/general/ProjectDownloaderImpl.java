package controller.general;

import controller.storing.WriteToFileImpl;
import controller.storing.serialization.ManagerSerializer;
import controller.storing.serialization.Serializer;
import daw.manager.Manager;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import org.apache.commons.io.FilenameUtils;

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
    public void download(File file) throws IOException, FileFormatException {
        if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
            throw new FileFormatException("Selected file's format is not supported. Choose only .json files.");
        }
        new WriteToFileImpl(file).write(this.serializer.serialize(this.manager));
    }

}

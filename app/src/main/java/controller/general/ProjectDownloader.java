package controller.general;

import net.beadsproject.beads.data.audiofile.FileFormatException;
import java.io.File;
import java.io.IOException;

public interface ProjectDownloader {

    void download(File file) throws IOException, FileFormatException;

}

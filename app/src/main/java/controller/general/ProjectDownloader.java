package controller.general;

import java.io.File;
import java.io.IOException;

public interface ProjectDownloader {

    void download(File file) throws IOException;

}

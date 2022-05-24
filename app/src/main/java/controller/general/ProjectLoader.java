package controller.general;

import daw.manager.Manager;
import java.io.File;
import java.io.IOException;

public interface ProjectLoader {

    Manager load(File file) throws IOException;

}

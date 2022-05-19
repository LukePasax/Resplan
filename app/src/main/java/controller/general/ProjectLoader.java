package controller.general;

import daw.manager.Manager;
import java.io.IOException;

public interface ProjectLoader {

    Manager load() throws IOException;

}

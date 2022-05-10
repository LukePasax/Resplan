package controller.general;

import controller.storing.WriteToFile;
import controller.storing.WriteToJsonFile;
import daw.manager.Manager;
import java.io.File;
import java.io.IOException;

public class ControllerImpl implements Controller {

    private static final String SEP = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    private final Manager manager;
    private final File managerInfo;
    private final WriteToFile<Manager> managerWriter;

    public ControllerImpl(Manager manager) {
        this.manager = manager;
        this.managerInfo = new File(WORKING_DIRECTORY + SEP + "/manager.json");
        this.managerWriter = new WriteToJsonFile<>(this.managerInfo);
    }

    @Override
    public void updateView() {

    }

    @Override
    public void saveProject() {
        try {
            this.managerWriter.write(this.manager);
        } catch (IOException ex) {
        }
    }

}

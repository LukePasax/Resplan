package controller.general;

import daw.manager.Manager;

public class SaveProjectImpl implements SaveProject {

    private static final String SEP = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    private final Manager manager;

    public SaveProjectImpl(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void save() {

    }

}

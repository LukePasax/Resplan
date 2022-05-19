package controller.general;

import daw.manager.Manager;

public class ControllerImpl implements Controller {

    private static final String SEP = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    private final SaveProject project;
    private final Manager manager;

    public ControllerImpl() {
        this.manager = new Manager();
        this.project = new SaveProjectImpl(this.manager);
    }

    @Override
    public void updateView() {

    }

    @Override
    public void saveProject() {
        this.project.save();
    }

    @Override
    public void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException {

    }

    @Override
    public void newPlanningClip() {

    }

}

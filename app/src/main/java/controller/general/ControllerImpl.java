package controller.general;

import daw.manager.Manager;
import java.io.IOException;

public class ControllerImpl implements Controller {

    private final SaveProject project;
    private final ProjectLoader loader;
    private final Manager manager;

    public ControllerImpl() {
        this.manager = new Manager();
        this.project = new SaveProjectImpl(this.manager);
        this.loader = new ProjectLoaderImpl();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void saveProject() {
        try {
            this.project.save();
        } catch (IOException e) {
            //TODO: make the view react so as to warn the user?
        }
    }

    @Override
    public Manager loadProject() {
        return this.loader.load();
    }


    @Override
    public void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException {

    }

    @Override
    public void newPlanningClip() {

    }

}

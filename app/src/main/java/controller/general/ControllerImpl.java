package controller.general;

import controller.view.planning.PlanningController;
import daw.manager.Manager;
import planning.RPRole;

import java.io.IOException;
import java.util.Optional;

public class ControllerImpl implements Controller {

    private final ProjectDownloader downloader;
    private final ProjectLoader loader = new ProjectLoaderImpl();
    private final Manager manager;
    private PlanningController planningController;

    public ControllerImpl() {
        this.manager = this.loadProject();
        this.downloader = new ProjectDownloaderImpl(this.manager);
    }

    @Override
    public void updateView() {

    }

    @Override
    public void downloadProject() {
        try {
            this.downloader.download();
        } catch (IOException e) {
            //TODO: make the view react so as to warn the user? All unsaved stuff would be lost...
        }
    }

    @Override
    public Manager loadProject() {
        try {
            return this.loader.load();
        } catch (IOException | IllegalStateException e) {
            //TODO: make the view react so as to warn the user? If information cannot be retrieved from file,
            // the user can still be provided with a new clean manager, just like the first time the app is used.
            return new Manager();
        }
    }

    @Override
    public void setPlanningController(PlanningController planningController) {
        this.planningController = planningController;
    }

    @Override
    public void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException {
        RPRole.RoleType roleType;
        if (type.equals("Speaker")) {
            roleType = RPRole.RoleType.SPEECH;
        } else if (type.equals("Effect")) {
            roleType = RPRole.RoleType.EFFECTS;
        } else {
            roleType = RPRole.RoleType.SOUNDTRACK;
        }
        if (description.equals("")) {
            this.manager.addChannel(roleType, title, Optional.empty());
        } else {
            this.manager.addChannel(roleType, title, Optional.of(description));
        }
        this.planningController.addChannel(type, title, description);
    }

    @Override
    public void newPlanningClip() {

    }

    // ONLY FOR TEMPORARY TESTING PURPOSES
    public Manager getManager() {
        return this.manager;
    }

}

package controller.general;

import daw.manager.Manager;
import java.io.IOException;

public class ControllerImpl implements Controller {

    private final ProjectDownloader downloader;
    private final ProjectLoader loader = new ProjectLoaderImpl();
    private final Manager manager;

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
    public void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException {

    }

    @Override
    public void newPlanningClip() {

    }

    // ONLY FOR TEMPORARY TESTING PURPOSES
    public Manager getManager() {
        return this.manager;
    }

}

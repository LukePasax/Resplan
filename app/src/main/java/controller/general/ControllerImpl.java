package controller.general;

import view.planning.PlanningController;
import daw.manager.ImportException;
import daw.manager.Manager;
import planning.Element;
import planning.RPPart;
import planning.RPRole;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<String> desc = description.equals("") ? Optional.empty() : Optional.of(description);
        this.manager.addChannel(roleType, title, desc);
        this.planningController.addChannel(type, title, description);
    }

    @Override
    public void newPlanningClip(String type, String title, String description, String channel, Double time, File content) throws IllegalArgumentException, ImportException {
        RPPart.PartType partType;
        if (type.equals("Speaker")) {
            partType = RPPart.PartType.SPEECH;
        } else if (type.equals("Effects")) {
            partType = RPPart.PartType.EFFECTS;
        } else {
            partType = RPPart.PartType.SOUNDTRACK;
        }
        Optional<String> desc = description.equals("") ? Optional.empty() : Optional.of(description);
        Optional<File> file = content == null ? Optional.empty() : Optional.of(content);
        this.manager.addClip(partType, title, desc, channel, time, file);
        this.planningController.addClip(title, description, channel, time);
    }


    @Override
    public List<String> getChannelList() {
        return this.manager.getChannelList().stream().map(Element::getTitle).collect(Collectors.toList());
    }

    // ONLY FOR TEMPORARY TESTING PURPOSES
    public Manager getManager() {
        return this.manager;
    }

}

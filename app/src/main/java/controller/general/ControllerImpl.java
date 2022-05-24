package controller.general;

import controller.storing.ReadFromFileImpl;
import controller.storing.WriteToFile;
import controller.storing.WriteToFileImpl;
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
    private Manager manager;
    private PlanningController planningController;
    private File currentProject;
    private final File appSettings;

    public ControllerImpl() {
        this.appSettings = new File(WORKING_DIRECTORY + SEP + "settings.json");
        try {
            this.currentProject = new File(new ReadFromFileImpl(this.appSettings).read());
            this.manager = this.loader.load(this.currentProject);
        } catch (IOException e) {
            this.manager = new Manager();
        }
        this.downloader = new ProjectDownloaderImpl(this.manager);
    }

    @Override
    public void updateView() {

    }

    @Override
    public void saveCurrentProject() throws DownloadingException {
        try {
            this.downloader.download(this.currentProject);
        } catch (IOException e) {
            throw new DownloadingException(e.getMessage());
        }
    }

    @Override
    public Manager openProject(File file) throws LoadingException {
        if (!this.currentProject.equals(file)) {
            try {
                final var man = this.loader.load(file);
                this.currentProject = file;
                return man;
            } catch (IOException | IllegalArgumentException e) {
                throw new LoadingException(e.getMessage());
            }
        }
        return this.manager;
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
    public void newPlanningClip(String type, String title, String description, String channel, Double time, File content)
            throws IllegalArgumentException, ImportException {
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

    @Override
    public void setTemplateProject() throws DownloadingException {
        final WriteToFile writer = new WriteToFileImpl(this.appSettings);
        try {
            writer.write(this.currentProject.getAbsolutePath());
        } catch (IOException e) {
            throw new DownloadingException("Unable to perform this operation. " +
                    "Retry to set this project as the template.");
        }
    }

    // ONLY FOR TEMPORARY TESTING PURPOSES
    public Manager getManager() {
        return this.manager;
    }

}

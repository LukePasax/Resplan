package controller.general;

import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import view.common.App;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Controller {

    String SEP = System.getProperty("file.separator");
    String WORKING_DIRECTORY = System.getProperty("user.dir");
    String APP_SETTINGS = "settings.json";

    void setApp(App app);

    /**
     * Creates a new project. The new project is the template project if there is one,
     * otherwise it is a blank project.
     */
    void newProject();

    void updateView();

    /**
     * Saves the current project on the file associated to it,
     * which means the current project has already been saved before.
     * @throws DownloadingException if an error has occurred when trying to write to file.
     * @throws IllegalStateException if the current project has never been saved before.
     */
    void save() throws DownloadingException, IllegalStateException;

    /**
     * Saves the current project on the given file.
     * @param file the file where to save.
     * @throws DownloadingException if an error has occurred when trying to write to file.
     */
    void saveWithName(File file) throws DownloadingException;

    /**
     * Opens the project associated to the given file. The project that is open when this method is called
     * is abruptly closed, meaning all unsaved work will be lost.
     * @param file the file where to read.
     * @throws LoadingException if an error has occurred when trying to read from file.
     */
    void openProject(File file) throws LoadingException;

    void newChannel(String type, String title, String description) throws IllegalArgumentException;

    void newClip(String type, String title, String description, String channel, Double startTime,
                 Double duration, File content) throws IllegalArgumentException, ImportException;

    void deleteChannel(String title);

    void deleteClip(String title, String channel, Double time);

    List<String> getChannelList();

    /**
     * Sets the current project as the template. If this call is successful,
     * then any time a user launches the application, the project that will be opened is the current one.
     * @throws DownloadingException if the writing to file is unsuccessful.
     * @throws IllegalStateException if the current project has never been saved before.
     */
    void setTemplateProject() throws DownloadingException, IllegalStateException;

    List<String> getClipList(String channel);

    Double getClipTime(String clip, String channel);

    Double getClipDuration(String clip);

    void start();

    void pause();

    void stop();

    void setPlaybackTime(Double time);

    Double getPlaybackTime();

    void updatePlaybackTime(Double time);

    boolean isPaused();

    double getProjectLength();

    void moveClip(String clip, String channel, Double finalTimeIn) throws ClipNotFoundException, ImportException;

    void setClipTimeIn(String clip, String channel, Double finalTimeIn) throws ClipNotFoundException, ImportException;

    void setClipTimeOut(String clip, String channel, Double finalTimeOut) throws ClipNotFoundException, ImportException;

    void splitClip(String clip, String channel, Double splittingTime) throws ClipNotFoundException, ImportException;

    void addContentToClip(String clip, File content) throws ImportException;

    void removeContentFromClip(String clip);

    void startRecording();

    void stopRecording(String text) throws ImportException;

    void exportAudio(File file) throws InterruptedException, IOException;
}

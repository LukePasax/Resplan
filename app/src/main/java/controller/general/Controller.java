package controller.general;

import daw.core.audioprocessing.RPEffect;
import daw.core.channel.RPChannel;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import view.common.App;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Controller {

    String SEP = System.getProperty("file.separator");
    String WORKING_DIRECTORY = System.getProperty("user.dir");
    String APP_SETTINGS = "settings.json";

    void setApp(App app);

    /**
     * Creates a new project. The new project is the template project if there is one,
     * otherwise it is a blank project.
     */
    void startApp();

    void newProject();

    void loadViewData();

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
                 Double duration, File content) throws IllegalArgumentException, ImportException, ClipNotFoundException;

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

    void addContentToClip(String clip, File content) throws ImportException, ClipNotFoundException;

    void removeContentFromClip(String clip) throws ClipNotFoundException;

    void newSection(String title, String description, Double initialTime, Double duration);

    void deleteSection(Double time);

    void startRecording();

    void stopRecording(String text, File file) throws ImportException, ClipNotFoundException, IOException;

    void startExport(Double startTime) throws InterruptedException;

    void stopExport(File file) throws IOException;

    /**
     * Inverts the muteness of the given channel. This means that if the channel is currently disabled (muted),
     * then this method enables it. On the other hand, if the channel is currently enabled (not muted), then
     * this method disables it. This method can also be effectively called when the project is in a solo
     * environment. However, if this is the case, then the muteness will be changed only after the project
     * returns to the non-solo environment.
     * @param channel the name of the {@link RPChannel} that has to be enabled or disabled.
     */
    void setMute(String channel);

    /**
     * Makes the given channel enabled in a solo environment. Calling this method has the effect of muting every
     * channel, except for the one in input and all the channels that were previously set as solo. Effectively,
     * repeated calls to this method with different channels will create a set of solo channels.
     * If the given channel is already solo, then this method does nothing.
     * @param channel The name of the {@link RPChannel} to be set as solo.
     * @throws IllegalStateException if the given channel is muted. In that case, first call
     * {@link #setMute(String)} and then this method.
     */
    void setSolo(String channel);

    void addEffectAtPosition(String channel, RPEffect e, int index);

    void removeEffectAtPosition(String channel, int index);

    void moveEffect(String channel, int index1, int index2);

    void replaceEffect(String channel, int index, RPEffect e);

    void setEffectParameters(String channel, int index, Map<String, Float> parameters);

    void setVolume(String channel, int value);

    void setPan(String channel, float value);

    Map<String, Float> getEffectParameters(String channel, int index);

}

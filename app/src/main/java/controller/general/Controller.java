package controller.general;

import daw.core.audioprocessing.ProcessingUnit;
import daw.core.channel.RPChannel;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import net.beadsproject.beads.data.Sample;
import planning.Speaker;
import view.common.App;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Controller of the application. In the context of the MVC architectural pattern, the controller connects the view
 * to the model. Users interact view the view and since the application has to potentially support multiple views,
 * action on the GUI cannot be directly linked to action on the model.
 */
public interface Controller {

    /**
     * Gets the system separator.
     */
    String SEP = System.getProperty("file.separator");

    /**
     * Gets the user's current directory.
     */
    String WORKING_DIRECTORY = System.getProperty("user.dir");

    /**
     * Defines a global settings file.
     */
    String APP_SETTINGS = "settings.json";

    /**
     * Specify the GUI that this controller has to manage.
     * @param app the {@link App}.
     */
    void setApp(App app);

    /**
     * Initialize all what is needed for the application to launch.
     */
    void startApp();

    /**
     * Creates a new project. The new project is the template project if there is one,
     * otherwise it is a blank project.
     */
    void newProject();

    /**
     * Loads the view.
     */
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

    /**
     * Sets the current project as the template. If this call is successful,
     * then any time a user launches the application, the project that will be opened is the current one.
     * @throws DownloadingException if the writing to file is unsuccessful.
     * @throws IllegalStateException if the current project has never been saved before.
     */
    void setTemplateProject() throws DownloadingException, IllegalStateException;

    /**
     * Resets the template project. The next time the user calls {@link #newProject()}, an empty project will be
     * created.
     * @throws DownloadingException if an error has occurred when trying to reset the template project.
     */
    void resetTemplateProject() throws DownloadingException;

    /**
     * Creates a new {@link planning.RPRole} and the corresponding {@link RPChannel} in the model, based on the data
     * coming from the view.
     * @param type the type of the new role.
     * @param title the title of the new role.
     * @param description the description of new the role. If the description is empty, then the
     *                    role will have no description in model.
     * @throws IllegalArgumentException if the given title is already in use.
     */
    void newChannel(String type, String title, String description) throws IllegalArgumentException;

    /**
     * Creates a new {@link planning.RPPart} and the corresponding {@link daw.core.clip.RPClip} in the model, based
     * on the data coming from the view.
     * @param type the type of the new part.
     * @param title the title of the new part.
     * @param description the description of new the part. If the description is empty, then the
     *                    part will have no description in model.
     * @param channel the channel this clip has to be put into.
     * @param startTime the position of the clip in the {@link planning.RPTimeline}.
     * @param duration the length of the clip.
     * @param content the content that has to be put into this clip. If the given content is null, then this clip
     *                will initially have no content. To later add content into a clip,
     *                use {@link #addContentToClip(String, File)}
     * @throws IllegalArgumentException if the given title is already in use.
     * @throws ImportException if there are problems importing the content file.
     */
    void newClip(String type, String title, String description, String channel, Double startTime,
                 Double duration, File content) throws IllegalArgumentException, ImportException;

    /**
     * Deletes the channel corresponding to the given title in the model.
     * @param title the name of the channel to be deleted.
     */
    void deleteChannel(String title);

    /**
     * Deletes the clip corresponding to the given title and channel, positioned at the given time.
     * @param title the name of a clip.
     * @param channel the name of a channel.
     * @param time a position in the timeline.
     */
    void deleteClip(String title, String channel, Double time);

    /**
     * Gets all the current channels' titles.
     * @return a {@link List} of names of channels.
     */
    List<String> getChannelList();

    /**
     * Gets all the clips contained in the given channel.
     * @param channel the name of a channel.
     * @return a {@link List} of names of clips.
     */
    List<String> getClipList(String channel);

    /**
     * Gets the sample connected to the given clip, if present, otherwise returns an empty {@link Optional}.
     * @param clip the name of a clip.
     * @return an {@link Optional}.
     */
    Optional<Sample> getClipSample(String clip);

    /**
     * Gets the position in timeline that is the starting point of the given clip.
     * @param clip the name of a clip.
     * @param channel the name of the channel that contains the given clip.
     * @return the starting time in milliseconds of the clip.
     */
    Double getClipTime(String clip, String channel);

    /**
     * Gets the duration of the given clip.
     * @param clip the name of a clip.
     * @return the duration in milliseconds of the given clip.
     */
    Double getClipDuration(String clip);

    /**
     * Makes the engine start.
     */
    void start();

    /**
     * Makes the engine pause.
     */
    void pause();

    /**
     * Makes the engine stop.
     */
    void stop();

    /**
     * Sets a playback time for the engine. Next time the engine is started, it will reproduce sound from the given
     * time in the timeline on.
     * @param time a position in the timeline.
     */
    void setPlaybackTime(Double time);

    /**
     * Gets the current playback time of the engine.
     * @return a position in the timeline.
     */
    Double getPlaybackTime();

    /**
     * Sets the playback time view representation to the given time.
     * @param time a position in the timeline.
     */
    void updatePlaybackTime(Double time);

    /**
     *
     * @return whether the engine is paused or not.
     */
    boolean isPaused();

    /**
     * Gets the current project length. Project length is defined as the highest value between the
     * furthest timeout position of a clip and the minimum length of a project.
     * @return the length of the projects in milliseconds.
     */
    double getProjectLength();

    /**
     * Gets the furthest clip timeout position of a clip in the timeline.
     * It is different from {@link #getProjectLength()} since the return value of this method does not depend on
     * a minimum project length.
     * @return the project timeout in milliseconds.
     */
    double getProjectTimeOut();

    /**
     * Moves a clip from a position to another in the timeline.
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param finalTimeIn the new starting position of the clip.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void moveClip(String clip, String channel, Double finalTimeIn) throws ClipNotFoundException;

    /**
     * Sets a clip starting position in the timeline.
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param finalTimeIn the starting position of the clip.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void setClipTimeIn(String clip, String channel, Double finalTimeIn) throws ClipNotFoundException;

    /**
     * Sets a clip timeout position in the timeline. This method basically resizes the clip, though leaving it
     * in the same starting position.
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param finalTimeOut the timeout position of the clip.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void setClipTimeOut(String clip, String channel, Double finalTimeOut) throws ClipNotFoundException;

    /**
     * Splits the given clip. Splitting a clip creates a duplicate of it  that can later be utilized and edited
     * as every other clip.
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param splittingTime the position in the timeline at which the clip has to be split.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void splitClip(String clip, String channel, Double splittingTime) throws ClipNotFoundException;

    /**
     * Adds a playable content to the given clip.
     * @param clip the name of a clip.
     * @param content the {@link File} representing the content to be added.
     * @throws ImportException if there are problems importing the content file.
     * @throws ClipNotFoundException if there is no clip with the given name.
     */
    void addContentToClip(String clip, File content) throws ImportException, ClipNotFoundException;

    /**
     * Removes the content from the given clip. After this operation is done, the clip will be empty.
     * @param clip the name of a clip.
     * @throws ClipNotFoundException if there is no clip with the given name.
     */
    void removeContentFromClip(String clip) throws ClipNotFoundException;

    /**
     * Creates a new {@link planning.RPSection} and adds it to the timeline.
     * @param title the name of the section.
     * @param description the description of the section. If the description is empty, then the
     *                    part will have no description in model.
     * @param initialTime the position of the section in the timeline.
     * @param duration the duration of the section.
     */
    void newSection(String title, String description, Double initialTime, Double duration);

    /**
     * Deletes the section at the given time.
     * @param time a position in the timeline.
     */
    void deleteSection(Double time);

    /**
     * Starts the recorder.
     */
    void startRecording();

    /**
     * Stops the recorder.
     */
    void stopRecording();

    /**
     * Stops the recorder.
     * @param file the file the recording has to be saved on.
     * @throws IOException if there are problems writing onto the given file.
     */
    void writeRecordingOnFile(File file) throws IOException;

    /**
     * Starts the export of the project.
     * @param startTime the position in the timeline from which export has to start.
     * @throws InterruptedException if there are problems while exporting.
     */
    void startExport(Double startTime) throws InterruptedException;

    /**
     * Stops the export.
     * @param file the file the audio content of the project has to be put into.
     * @throws IOException if there are problems writing onto the given file.
     */
    void stopExport(File file) throws IOException;

    /**
     * Inverts the muteness of the given channel. This means that if the channel is currently disabled (muted),
     * then this method enables it. On the other hand, if the channel is currently enabled (not muted), then
     * this method disables it. This method can also be effectively called when the project is in a solo
     * environment. However, if this is the case, then the muteness will be changed only after the project
     * returns to the non-solo environment.
     * @param channel the name of the channel that has to be enabled or disabled.
     */
    void setMute(String channel);

    /**
     * If this method is called while there are no solo channels, then this method makes the given channel enabled
     * and mutes all the others. Therefore, the project will enter the so-called solo environment.
     * When the project is in the solo environment, calling this method on a solo channel will un-solo it,
     * whereas calling it on a currently non solo channel will add it to the list of the solo channel.
     * Only the solo channels play when the project is in the solo environment. The end of this state is automatic
     * as it is triggered by the removal of the last solo channel.
     * @param channel The name of the channel to be set as solo.
     * @throws IllegalStateException if the given channel is muted. In that case, first call
     * {@link #setMute(String)} and then this method.
     */
    void setSolo(String channel);

    /**
     * Adds a new instance of the given effect to the processing unit of the given channel, at the given index.
     * @param channel the name of a channel.
     * @param effect the name of an effect.
     * @param index the position in the processing unit where to add the effect.
     */
    void addEffectAtPosition(String channel, String effect, int index);

    /**
     * Removes the effect at the given position from the processing unit of the given channel.
     * @param channel the name of a channel.
     * @param index the position in the processing unit where to remove the effect.
     */
    void removeEffectAtPosition(String channel, int index);

    /**
     * Removes the processing unit from the given channel.
     * @param channel the name of a channel.
     */
    void removeProcessingUnit(String channel);

    /**
     * Swaps the effects at the given indexes in the processing unit of the given channel.
     * @param channel the name of a channel.
     * @param index1 the position of the first effect to swap.
     * @param index2 the position of the second effect to swap.
     */
    void swapEffects(String channel, int index1, int index2);

    /**
     * Sets the parameters of the effect at the given position in the processing unit of the given channel.
     * @param channel the name of a channel.
     * @param index the position of the effect in the processing unit.
     * @param parameters a {@link Map} representing the new values of the effect.
     */
    void setEffectParameters(String channel, int index, Map<String, Float> parameters);

    /**
     * Gets the parameters of the effect at the given position in the processing unit of the given channel.
     * @param channel the name of a channel.
     * @param index the position of the effect in the processing unit.
     * @return a {@link Map} representing the current values of the effect.
     */
    Map<String, Float> getEffectParameters(String channel, int index);

    /**
     * Sets the volume of the given channel.
     * @param channel the name of a channel.
     * @param value the 0 - 100 integer representation of the volume.
     */
    void setVolume(String channel, int value);

    /**
     * Sets the pan of the given channel.
     * @param channel the name of a channel.
     * @param value the -1.0 - 1.0 float representation of the pan.
     */
    void setPan(String channel, float value);

    /**
     * Gets the processing unit of the given channel.
     * @param channel the name of a channel.
     * @return a {@link ProcessingUnit}.
     * @throws NoSuchElementException if the given channel does not possess a processing unit.
     */
    ProcessingUnit getProcessingUnit(String channel);

    /**
     * Adds text to the given clip.
     * @param clipTitle the name of a clip.
     * @param text the text that has to be associated to the given clip.
     */
    void setClipText(String clipTitle, String text);

    /**
     * Adds text read from file to the given clip.
     * @param clipTitle the name of a clip.
     * @param fileName the name of the file the text has to be read from.
     * @throws IOException if there are problems reading from the given file.
     */
    void setClipTextFromFile(String clipTitle, String fileName) throws IOException;

    /**
     * Gets the text associated to the given clip.
     * @param clipTitle the name of a clip.
     * @return an optional containing the text if the clip has any, otherwise an empty optional.
     */
    Optional<String> getClipText(String clipTitle);

    /**
     * Gets the string representation of the given clip's type.
     * @param clip the name of a clip.
     * @return a string representing the type of the given clip.
     */
    String getClipType(String clip);

    /**
     * Sets the description of the given clip. If there was already a description associated to it, that
     * description gets overwritten.
     * @param title the name of a clip.
     * @param text the new description.
     */
    void setClipDescription(String title, String text);

    /**
     * Gets the description of the given clip, if present, otherwise it returns an empty {@link Optional}.
     * @param clip the name of a clip.
     * @return an {@link Optional}.
     */
    Optional<String> getClipDescription(String clip);

    /**
     * Gets the string representation of the given channel's type.
     * @param channel the name of a channel.
     * @return a string representing the type of the given channel.
     */
    String getChannelType(String channel);

    /**
     * Sets the description of the given channel. If there was already a description associated to it, that
     * description gets overwritten.
     * @param title the name of a channel.
     * @param text the new description.
     */
    void setChannelDescription(String title, String text);

    /**
     * Gets the description of the given channel, if present, otherwise it returns an empty {@link Optional}.
     * @param title the name of a channel.
     * @return an {@link Optional}.
     */
    Optional<String> getChannelDescription(String title);

    /**
     * Adds a new speaker with the given code, first name and last name in the rubric.
     * @param code an integer value representing the speaker.
     * @param firstName the first name of the speaker.
     * @param lastName the last name of the speaker.
     */
    void addSpeakerToRubric(int code, String firstName, String lastName);

    /**
     * Removes the speaker with the given code, first name and last name from the rubric.
     * @param code an integer value representing the speaker.
     * @param firstName the first name of the speaker.
     * @param lastName the last name of the speaker.
     */
    void removeSpeakerFromRubric(int code, String firstName, String lastName);

    /**
     * Gets all the speakers in the rubric.
     * @return a {@link List} of speakers.
     */
    List<Speaker> getSpeakers();

}

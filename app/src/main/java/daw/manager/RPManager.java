package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.ClipNotFoundException;
import daw.core.clip.RPClip;
import daw.core.mixer.RPMixer;
import planning.RPPart;
import planning.RPRole;
import planning.RPSection;
import planning.Speaker;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * This interface represents a manager that takes decisions for the user, simplifying its experience. It is used to
 * interact with the low level components.
 */
public interface RPManager {

    /**
     * This method creates a Channel of the given {@link daw.core.channel.RPChannel.Type} and all the corresponding
     * components.
     *
     * @param type the {@link planning.RPRole.RoleType} of {@link RPChannel} to be created
     * @param  title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     * @throws IllegalArgumentException if the given title is already in use
     */
    void addChannel(RPRole.RoleType type, String title, Optional<String> description) throws IllegalArgumentException;

    /**
     * This method removes the Channel with the given title.
     *
     * @param title the title of the Channel to remove
     * @throws NoSuchElementException if a Channel with the given title does not exist
     */
    void removeChannel(String title) throws NoSuchElementException;

    /**
     * This method adds a Channel to a group.
     *
     * @param role the Channel to add to a group
     * @param groupName the name of the group
     * @throws NoSuchElementException if a group with the given name does not exist
     */
    void addToGroup(RPRole role, String groupName) throws NoSuchElementException;

    /**
     * A method to create a group.
     *
     * @param groupName the name of the group to be created
     * @param type the type of group to be created
     * @throws IllegalArgumentException if a Group with the given name already exists
     */
    void createGroup(String groupName, RPRole.RoleType type) throws IllegalArgumentException;

    /**
     * This method creates Clip and all the corresponding components.
     *
     * @param type the type of clip that needs to be created
     * @param title the title to associate with the clip
     * @param description the optional description to associate with the clip
     * @param channel the channel where to insert the clip
     * @param time the starting time of the clip in the channel
     * @param duration the duration of the clip
     * @param content the Optional content of the clip
     * @throws ImportException if there were problems importing the file
     * @throws IllegalArgumentException if a Clip with the given title already exists
     */
    void addClip(RPPart.PartType type, String title, Optional<String> description, String channel, Double time,
                 Double duration,  Optional<File> content) throws ImportException, IllegalArgumentException;

    /**
     * This method adds a content to a Clip.
     *
     * @param title the title of the Clip
     * @param content the content to put into the Clip
     * @throws NoSuchElementException if no Clip with the given title exists
     * @throws ImportException if there was an error with the file
     */
    void addFileToClip(String title, File content) throws ImportException, ClipNotFoundException;

    /**
     * This method removes the content from a Clip.
     *
     * @param title the title of the Clip
     * @throws IllegalArgumentException if the Clip has no content
     * @throws NoSuchElementException if no Clip with the given title exists
     */
    void removeFileFromClip(String title) throws ClipNotFoundException;

    /**
     * This method removes the Clip with the given title.
     *
     * @param channel the Channel with the Clip to be removed
     * @param clip the tile of the Clip to be removed
     * @param time the time of the Clip in the Channel
     * @throws daw.core.clip.ClipNotFoundException if the Clip does not exist
     */
    void removeClip(String channel, String clip, double time) throws ClipNotFoundException;

    /**
     * A method to return the {@link RPRole} associated with a groupName.
     *
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     * @throws NoSuchElementException if the group does non exist
     */
    RPRole getGroup(String groupName);

    /**
     * A method to return the list of Channel associated to a group.
     *
     * @param groupName the name of the group
     * @return a List of {@link RPRole}
     * @throws NoSuchElementException if the group does not exist
     */
    List<RPRole> getGroupList(String groupName);

    /**
     *
     * @return the {@link ChannelLinker} of this Manager
     */
    RPChannelLinker getChannelLinker();

    /**
     *
     * @return the {@link RPClipLinker} of this manager
     */
    RPClipLinker getClipLinker();

    /**
     *
     * @return the {@link RPMixer} of this manager
     */
    RPMixer getMixer();

    /**
     *
     * @return the list of {@link RPRole}  of all channels excluded groups
     */
    List<RPRole> getRoles();

    /**
     *
     * @param channel the name of the channel
     * @return the list of clips associated with a channel
     */
    List<RPPart> getPartList(String channel);

    /**
     *
     * @param clip the name of the clip
     * @param channel the channel of a clip
     * @return the start time of a clip
     */
    Double getClipTime(String clip, String channel);

    /**
     *
     * @param clip the clip in question
     * @return the duration of a clip
     */
    Double getClipDuration(String clip);

    /**
     *
     * @param title the name of a role.
     * @return the channel corresponding to the given role.
     */
    RPChannel getChannelFromTitle(String title);

    /**
     * Adds a new {@link RPSection}.
     * @param title the title of the new section.
     * @param description the description of the new section.
     * @param initialTime the initial time in the timeline of the new section.
     * @param duration the duration of the new section.
     */
    void addSection(String title, Optional<String> description, Double initialTime, Double duration);

    /**
     * Removes a section.
     * @param time a position in the timeline that is associated with the section that needs removing.
     */
    void removeSection(Double time);

    /**
     * Gets all the sections.
     * @return a set of the {@link Map.Entry}s that represents the sections.
     */
    Set<Map.Entry<Double, RPSection>> getSections();

    /**
     * Updates the length of the project, that is the timeout of the last clip or a minimum length if the
     * project is shorter than that.
     */
    void updateProjectLength();

    /**
     * Gets the current project length. Project length is defined as the furthest timeout position of a clip, plus
     * an eventual minimum spacing for visual purposes.
     * @return the length of the projects in milliseconds.
     */
    double getProjectLength();

    /**
     * This method is used to get the time out of the last clip in the project.
     * @return the time out of the last clip
     */
    double getProjectTimeOut() throws ClipNotFoundException;

    /**
     * Moves a clip to a new position in the timeline.
     * @param clip the name of clip that has to be moved.
     * @param channel the channel that contains the given clip.
     * @param finalTimeIn the position in the timeline the clip has to be moved to.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void moveClip(String clip, String channel, Double finalTimeIn) throws ClipNotFoundException;

    /**
     * Sets the given clip's timeout, which is its beginning.
     * @param clip the name of a clip.
     * @param channel the name of the channel that contains the given clip.
     * @param finalTimeIn the position in the timeline where this clip must begin.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void setClipTimeIn(String clip, String channel, Double finalTimeIn) throws ClipNotFoundException;

    /**
     * Sets the given clip's timeout, which is its end.
     * @param clip the name of a clip.
     * @param channel the name of the channel that contains the given clip.
     * @param finalTimeOut the position in the timeline where this clip must end.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void setClipTimeOut(String clip, String channel, Double finalTimeOut) throws ClipNotFoundException;

    /**
     * Splits the clip that has the given title. Splitting means creating a duplicate of the clip and inserting
     * that duplicate into the same channel of the clip at the specified splitting time.
     * @param clip the name of a clip.
     * @param channel the channel that contains the given clip.
     * @param splittingTime the position in the timeline where to put the duplicate of the clip.
     * @throws ClipNotFoundException if there is no clip with the given name in the given channel.
     */
    void splitClip(String clip, String channel, Double splittingTime) throws ClipNotFoundException;

    /**
     * Gets the clip whose part has the given title.
     * @param title the name of a clip.
     * @return a {@link RPClip}.
     */
    RPClip<?> getClipFromTitle(String title);

    /**
     * Gets the name of the channel that contains the given clip. If there is no such channel,
     * this method returns null.
     * @param clip the name of a clip.
     * @return the name of a channel, or null.
     */
    String getClipChannel(String clip);

    /**
     * Creates a new {@link Speaker}.
     * @param id a numeric value representing the speaker.
     * @param firstName the speaker's first name.
     * @param lastName the speaker's last name.
     * @return a {@link Speaker}.
     */
    Speaker createSpeaker(int id, String firstName, String lastName);

    /**
     * Adds a speaker to the rubric
     * @param speaker a {@link Speaker}.
     */
    void addSpeakerToRubric(Speaker speaker);

    /**
     * Remove a speaker from the rubric.
     * @param speaker a {@link Speaker}.
     */
    void removeSpeakerFromRubric(Speaker speaker);

    /**
     * Gets all the speakers currently in the rubric.
     * @return a list of {@link Speaker}.
     */
    List<Speaker> getSpeakersInRubric();

}

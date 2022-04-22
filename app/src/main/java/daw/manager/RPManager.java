package daw.manager;

import daw.core.channel.RPChannel;
import planning.RPPart;
import planning.RPRole;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a {@link RPManager}
 */
public interface RPManager {

    //TODO
    /**
     * This method creates a Channel of the given {@link daw.core.channel.RPChannel.Type} and all the corresponding
     * components
     * @param type the {@link planning.RPRole.RoleType} of {@link RPChannel} to be created
     * @param  title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     */
    void addChannel(RPRole.RoleType type, String title, Optional<String> description);

    /**
     * This method adds a Channel to a group
     * @param role the Channel to add to a group
     * @param groupName the name of the group
     */
    void addToGroup(RPRole role, String groupName);

    /**
     * A method to create a group
     * @param groupName the name of the group to be created
     * @param type the type of group to be created
     */
    void createGroup(String groupName, RPRole.RoleType type);

    /**
     * This method creates a Clip and all the corresponding components
     */
    void addClip(RPPart.PartType type, String title, Optional<String> description);

    /**
     * A method to return the {@link RPRole} associated with a groupName
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     */
    RPRole getGroup(String groupName);

    /**
     * A method to return the list of Channel associated to a group
     * @param groupName the name of the group
     * @return a List of {@link RPRole}
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
}

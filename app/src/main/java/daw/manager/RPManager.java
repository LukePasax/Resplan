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

    /**
     * This method creates a Channel of the given {@link daw.core.channel.RPChannel.Type} and all the corresponding
     * components
     * @param type the {@link planning.RPRole.RoleType} of {@link RPChannel} to be created
     * @param  title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     */
    void addChannel(RPRole.RoleType type, String title, Optional<String> description);

    /**
     * This method creates a Sidechained Channel and all the corresponding components
     * @param channel the Channel to sidechain
     * @param title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     */
    void addSidechainedChannel(RPRole channel, String title, Optional<String> description);

    /**
     * This method adds a Channel to a group
     * @param role the Channel to add to a group
     * @param groupName the name of the group
     */
    void addToGroup(RPRole role, String groupName);

    /**
     * This method removes a Channel from a group
     * @param role the Channel to remove from a group
     * @param groupName the name of the Group
     */
    void removeFromGroup(RPRole role, String groupName);

    /**
     * This method moves a Channel from a group to another
     * @param role the Channel to move
     * @param oldGroup the current group of the Channel
     * @param newGroup the group where the Channel is going to be moved
     */
    void switchGroup(RPRole role, String oldGroup, String newGroup);

    /**
     * A method to create a group
     * @param groupName the name of the group to be created
     * @param type the type of group to be created
     */
    void createGroup(String groupName, RPRole.RoleType type);

    /**
     * A method to create a sidechained group
     * @param groupName the name of the group to be created
     * @param channel the Channel to sidechain
     */
    void createSidechainedGroup(String groupName, RPRole channel);

    /**
     * This method creates a Clip and all the corresponding components
     */
    void addClip(RPPart.PartType type);

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
}

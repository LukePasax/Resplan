package daw.manager;

import daw.core.channel.RPChannel;
import planning.RPRole;

import java.util.Optional;

/**
 * This interface represents a {@link RPManager}
 */
public interface RPManager {

    /**
     * This method creates a Channel of the given {@link daw.core.channel.RPChannel.Type} and all the corresponding
     * components
     * @param type the {@link daw.core.channel.RPChannel.Type} of {@link RPChannel} to be created
     * @param  title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     */
    void addChannel(RPChannel.Type type, String title, Optional<String> description);

    /**
     * This method creates a Sidechained Channel and all the corresponding components
     * @param channel the Channel to sidechain
     * @param title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     */
    void addSidechainedChannel(RPRole channel, String title, Optional<String> description);

    /**
     * This method adds a {@link RPChannel} to a group
     * @param channel the {@link RPChannel} to add to a group
     * @param groupName the name of the group
     */
    void addToGroup(RPChannel channel, String groupName);

    /**
     * A method to create a group
     * @param groupName the name of the group to be created
     */
    void createGroup(String groupName, RPChannel.Type type);

    /**
     * A method to create a sidechained group
     * @param groupName the name of the group to be created
     * @param channel the Channel to sidechain
     */
    void createSidechainedGroup(String groupName, RPRole channel);

    /**
     * This method creates a Clip and all the corresponding components
     */
    void addClip();

    /**
     * A method to return the {@link RPRole} associated with a groupName
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     */
    RPRole getGroup(String groupName);
}

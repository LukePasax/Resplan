package daw.manager;

import daw.core.channel.RPChannel;
import net.beadsproject.beads.ugens.Gain;
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
     * @param gain the optional {@link Gain} for a sidechained {@link RPChannel}
     */
    void addChannel(RPChannel.Type type, Optional<Gain> gain);

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
    void createGroup(String groupName);

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

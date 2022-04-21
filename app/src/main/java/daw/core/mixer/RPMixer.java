package daw.core.mixer;

import daw.core.channel.RPChannel;
import net.beadsproject.beads.ugens.Gain;

import java.util.List;
import java.util.Optional;

/**
 * This interface models a mixer,
 * which is a controller for {@link RPChannel}.
 */

public interface RPMixer {

    /**
     * A method to create a {@link RPChannel} in the mixer
     * and links its output to the Master channel.
     * @param type the {@link RPChannel} type that will be created
     * @return the {@link RPChannel} that is created
     */
    RPChannel createChannel(RPChannel.Type type);

    /**
     * This method is used to create a sidechained {@link RPChannel}
     * @param channel the {@link RPChannel} to sidechain
     * @return the sidechained {@link RPChannel}
     */
    RPChannel createSidechained(RPChannel channel);

    /**
     * A method that returns the Master channel of the mixer.
     * @return the Master channel contained in the mixer
     */
    RPChannel getMasterChannel();

    /**
     * A method to link the output of a {@link RPChannel}
     * to the Input of a Return channel.
     * @param channel the {@link RPChannel} which output is to be linked
     * @param returnChannel the Return channel which receives the input
     */
    void linkChannel(RPChannel channel, RPChannel returnChannel);

    /**
     * A method to add a {@link RPChannel} to a group
     * @param channel the {@link RPChannel} to be added
     * @param group the Group
     */
    void linkToGroup(RPChannel channel, RPChannel group);

    /**
     * A method to remove a {@link RPChannel} from a group
     * @param channel the {@link RPChannel} to be removed
     * @param group the group to remove the {@link RPChannel} from
     */
    void unlinkFromGroup(RPChannel channel, RPChannel group);

    /**
     * A method to link a sidechained {@link RPChannel}
     * @param channel the {@link RPChannel} to sidechain
     * @param sidechainedChannel the sidechained {@link RPChannel}
     */
    void linkToSidechained(RPChannel channel, RPChannel sidechainedChannel);

    /**
     * A method to unlink a sidechained {@link RPChannel}
     * @param channel the {@link RPChannel} to unlink
     * @param sidechainedChannel the sidechained {@link RPChannel}
     */
    void unlinkFromSidechained(RPChannel channel, RPChannel sidechainedChannel);

    /**
     * A method to link a sidechained {@link RPChannel} to a sidechained group
     * @param channel the {@link RPChannel} to link to the group
     * @param group the group to link the {@link RPChannel} into
     * @param sidechain the {@link RPChannel} sidechained by channel
     */
    void linkToSidechainedGroup(RPChannel channel, RPChannel group, RPChannel sidechain);

    /**
     * A method to unlink a sidechained {@link RPChannel} from a sidechained group
     * @param channel the {@link RPChannel} to unlink from the group
     * @param group the group to unlink the {@link RPChannel} from
     * @param sidechain the {@link RPChannel} sidechained by group
     */
    void unlinkToSidechainedGroup(RPChannel channel, RPChannel group, RPChannel sidechain);
}

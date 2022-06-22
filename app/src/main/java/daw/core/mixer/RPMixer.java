package daw.core.mixer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import daw.core.channel.RPChannel;

/**
 * This interface models a mixer,
 * which is a controller for {@link RPChannel}.
 */
@JsonDeserialize(as = Mixer.class)
public interface RPMixer {

    /**
     * A method to create a Basic {@link RPChannel} in the mixer
     * @return the {@link RPChannel} that is created
     */
    RPChannel createBasicChannel();

    /**
     * A method to create a Gated {@link RPChannel} in the mixer
     * @return the {@link RPChannel} that is created
     */
    RPChannel createGatedChannel();

    /**
     * A method to create a Return {@link RPChannel} in the mixer
     * @return the {@link RPChannel} that is created
     */
    RPChannel createReturnChannel();

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
     * A method to add a {@link RPChannel} to a group
     * @param channel the {@link RPChannel} to be added
     * @param group the Group
     */
    void linkToGroup(RPChannel channel, RPChannel group);

    void connectToSystem();

}

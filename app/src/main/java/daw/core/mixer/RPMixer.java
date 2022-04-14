package daw.core.mixer;

import daw.core.channel.RPChannel;

import java.util.List;

/**
 * This interface models a mixer,
 * which is a container and controller for {@link RPChannel}.
 */

public interface RPMixer {

    /**
     * Returns the list of {@link RPChannel} contained in the mixer.
     * @return a list of {@link RPChannel} in the mixer
     */
    List<RPChannel> getChannels();

    /**
     * A method to create a {@link RPChannel} in the mixer
     * and links its output to the Master channel.
     * @param type the {@link RPChannel} type that will be created
     */
    void createChannel(RPChannel.Type type);

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
    
}

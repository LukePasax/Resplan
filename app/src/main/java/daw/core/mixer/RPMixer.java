package daw.core.mixer;

import daw.core.channel.MasterChannel;
import daw.core.channel.RPChannel;
import daw.core.channel.ReturnChannel;
import daw.core.clip.RPClip;
import net.beadsproject.beads.core.AudioContext;

import java.util.List;

/**
 * This interface models a mixer,
 * which is a container and controller for {@link RPChannel}.
 */

public interface RPMixer {

    /**
     * Sets the {@link AudioContext} for the mixer.
     * @param context the {@link AudioContext} used in the mixer
     */
    void setAudioContext(AudioContext context);

    /**
     * Returns the {@link AudioContext} of the mixer.
     * @return the {@link AudioContext} for the mixer.
     */
    AudioContext getAudioContext();

    /**
     * Returns the list of {@link RPChannel} contained in the mixer.
     * @return a list of {@link RPChannel} in the mixer
     */
    List<RPChannel> getChannels();

    /**
     * A method to create a {@link RPChannel} in the mixer
     * and links its output to the {@link MasterChannel}.
     */
    void createChannel();

    /**
     * Return a {@link RPChannel} that contains the specified {@link RPClip}.
     * @param clip the {@link RPClip} contained in a {@link RPClip} in the mixer
     * @return the {@link RPChannel} that contains the {@link  RPClip}
     */
    RPChannel getChannel(RPClip clip);

    /**
     * A method that returns the {@link MasterChannel} of the mixer.
     * @return {@link MasterChannel} contained in the mixer
     */
    MasterChannel getMasterChannel();

    /**
     * A method to link the output of a {@link RPChannel}
     * to the Input of a {@link ReturnChannel}.
     * @param channel the {@link RPChannel} which output is to be linked
     * @param returnChannel the {@link ReturnChannel} which receives the input
     */
    void linkChannel(RPChannel channel, ReturnChannel returnChannel);

    /**
     * A method to create {@link ReturnChannel} in the mixer.
     */
    void createReturnChannel();
}

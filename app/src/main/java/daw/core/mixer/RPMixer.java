package daw.core.mixer;

import daw.core.channel.RPChannel;
import daw.core.clip.RPClip;
import net.beadsproject.beads.core.AudioContext;

import java.util.List;

/**
 * This interface models a Mixer,
 * which is a container and controller for channels.
 */

public interface RPMixer {

    /**
     * Sets the AudioContext for the mixer.
     * @param context the context used in the Mixer
     */
    void setAudioContext(AudioContext context);

    /**
     * @return the AudioContext for the mixer.
     */
    AudioContext getAudioContext();

    /**
     * @return a list of Channels in the Mixer
     */
    List<RPChannel> getChannels();

    /**
     * A method to create a Channel in the Mixer.
     */
    void createChannel();

    /**
     * @param clip the clip contained in a channel in the Mixer
     * @return the channel that contains the clip
     */
    RPChannel getChannel(RPClip clip);

    // wait for Return Channel interface

    /**
     * A method to link the output of a Channel to the Input of a ReturnChannel.
     * @param channel the channel which output is to be linked
     */
    void linkChannel(RPChannel channel);

    /**
     * A method to create a ReturnChannel in the Mixer.
     */
    void createReturnChannel();
}

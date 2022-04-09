package daw.core.channel;

import net.beadsproject.beads.ugens.Gain;

/**
 * This interface models a channel, which is a representation of sound coming from an input and going to an output.
 * Different forms of channel need to be supported by this interface.
 */
public interface RPChannel {

    /**
     * Identifies different forms of channels.
     */
    enum Type {
        GATED, SIDECHAINED, RETURN, MASTER;
    }

    /**
     * Adds an input.
     */
    void addInput(Gain g);

    /**
     * Sets the volume.
     * @param vol
     */
    void setVolume(int vol);

    /**
     * Gets the volume
     * @return the volume that has been gotten.
     */
    int getVolume();

    /**
     * Enables the audio of the channel.
     */
    void enable();

    /**
     * Disables the audio of the channel.
     */
    void disable();

    /**
     * Returns if the audio is enabled.
     */
    boolean isEnabled();

    /**
     *
     * @return the type of the channel.
     */
    RPChannel.Type getType();

    /**
     * Returns whether the channel has a {@link ProcessingUnit} attached or not.
     * @return true if the channel contains a {@link ProcessingUnit}.
     */
    boolean isProcessingUnitPresent();

}

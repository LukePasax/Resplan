package daw.core.channel;

import net.beadsproject.beads.core.UGen;
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
        BASIC, GATED, SIDECHAINED, RETURN, MASTER;
    }

    /**
     * Adds an audio input.
     * For a channel to properly work, a client should not add two inputs before getting an output.
     * This is because the {@link ProcessingUnit} manipulates audio on-the-fly, meaning that the processing
     * of an input may not be finished when another input is added.
     */
    void addInput(Gain g);

    /**
     * If the channel has any input, it tries to remove the connection to the specified {@link UGen},
     * otherwise it does nothing.
     */
    void removeInput(int inputChannel, UGen u);

    /**
     * Allows clients to obtain the audio coming from the channel.
     * Channel parameters are used in order to set the volume and the pan of the output.
     * Remember that the {@link ProcessingUnit} modifies the input data according to the sequence of the effects.
     * @return a {@link Gain} that represents the output.
     * @throws IllegalStateException if the channel is not enabled or if no input has been provided.
     */
    Gain getOutput();

    /**
     * Sets the volume.
     * @param vol
     */
    void setVolume(int vol);

    /**
     * Gets the volume
     * @return the volume.
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

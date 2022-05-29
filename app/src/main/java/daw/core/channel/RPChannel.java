package daw.core.channel;

import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;
import java.util.Optional;

/**
 * This interface models a channel, which is a representation of sound coming from an input and going to an output.
 * Different forms of channel need to be supported by this interface.
 */
public interface RPChannel {

    /**
     * Identifies different forms of channels.
     */
    enum Type {
        AUDIO, RETURN, MASTER
    }

    /**
     * Connects the given source to the input of the channel.
     * @param g the source that must be connected.
     */
    void connectSource(UGen g);

    /**
     * Disconnects the given source from the input of the channel.
     * @param u the source that must be disconnected.
     */
    void disconnectSource(UGen u);

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
     * @param vol the value that the volume must be set to.
     */
    void setVolume(int vol);

    /**
     * Gets the numerical value of the volume.
     * @return an integer representing the volume.
     */
    int getVolume();

    /**
     * Gets the panner, that is the tool which controls the distribution of an audio signal
     * into a new stereo or multichannel sound field.
     * @return a {@link Panner}.
     */
    Panner getPanner();

    /**
     * Enables the audio of the channel. After this operation is done,
     * the channel will be able to produce output.
     */
    void enable();

    /**
     * Disables the audio of the channel. After this operation is done,
     * the channel will not be able to produce any output.
     */
    void disable();

    /**
     * Allows knowing whether the channel will produce output or not.
     * @return true if the channel is enabled, that means it can produce output.
     */
    boolean isEnabled();

    /**
     * Adds the given {@link ProcessingUnit} to the channel.
     * @param pu the {@link ProcessingUnit} to be added.
     */
    void addProcessingUnit(ProcessingUnit pu);

    /**
     * Removes the {@link ProcessingUnit} if it is present, otherwise it does nothing.
     */
    void removeProcessingUnit();

    /**
     *
     * @return an {@link Optional} containing the {@link ProcessingUnit} if it is present,
     * otherwise an empty {@link Optional}.
     */
    Optional<ProcessingUnit> getProcessingUnit();

    /**
     * Allows knowing whether the channel has a {@link ProcessingUnit} attached or not.
     * @return true if the channel contains a {@link ProcessingUnit}.
     */
    boolean isProcessingUnitPresent();

    /**
     *
     * @return the type of the channel.
     */
    RPChannel.Type getType();

}

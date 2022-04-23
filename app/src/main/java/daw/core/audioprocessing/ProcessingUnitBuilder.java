package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;

/**
 * Makes the creation of a {@link ProcessingUnit} interactive, in that the client can specify any number
 * of the following effects in any order. The only method whose utility is different is the sidechain method,
 * which allows to sidechain the {@link ProcessingUnit} to an external sound source.
 * This implementation allows adding up to one {@link RPEffect} of each type. This means that if one method
 * has already been called, calling it again will produce no changes.
 * Remember that the order of the calls is relevant because, as specified in the {@link ProcessingUnit}
 * documentation, the ordering by which {@link RPEffect}s are stored influences how sound is processed.
 *
 * The only state which raises an {@link IllegalStateException} is the one where the method build is called
 * and no effect had been specified, since a {@link ProcessingUnit} can never be empty.
 */
public interface ProcessingUnitBuilder {

    /**
     * Makes the {@link ProcessingUnit} have a {@link Sidechaining} tied to the given {@link UGen}.
     * @param u the {@link UGen} which this {@link ProcessingUnit} is sidechained to.
     * @param channels the number of input and output channels for the underlying compressor.
     */
    ProcessingUnitBuilder sidechain(UGen u, int channels);

    /**
     * Adds a {@link LowPassFilter} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @param cutoffFrequency the frequency over which sound gets attenuated or eliminated.
     */
    ProcessingUnitBuilder lowPassFilter(int channels, float cutoffFrequency);

    /**
     * Adds a {@link HighPassFilter} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @param cutoffFrequency the frequency under which sound gets attenuated or eliminated.
     */
    ProcessingUnitBuilder highPassFilter(int channels, float cutoffFrequency);

    /**
     * Adds a {@link DigitalReverb} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder reverb(int channels);

    /**
     * Adds a {@link Gate} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder gate(int channels);

    /**
     * Adds a {@link Compression} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder compressor(int channels);

    /**
     * Allows to obtain the {@link ProcessingUnit}.
     * @return a {@link ProcessingUnit} with the ordered sequence of effects.
     * @throws IllegalStateException if no effects have been specified through this builder.
     * Remember that a {@link ProcessingUnit} can never be empty.
     */
    ProcessingUnit build() throws IllegalStateException;

}

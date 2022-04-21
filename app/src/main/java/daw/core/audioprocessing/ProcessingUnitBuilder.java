package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;

/**
 * Makes the creation of a {@link ProcessingUnit} interactive,
 * in that the client can specify any number of the following effects in any order.
 * The only state which raises an {@link IllegalStateException} is the one where the method build is called
 * and no effect had been specified, since a {@link ProcessingUnit} can never be empty.
 */
public interface ProcessingUnitBuilder {

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
     * Adds a {@link CompressorWithSidechaining} to the {@link ProcessingUnit} and immediately connects the
     * given {@link UGen} as a sidechain.
     * @param u the {@link UGen} which this {@link ProcessingUnit} is sidechained to.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder sidechain(UGen u, int channels);

    /**
     * Adds a {@link Gate} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder gate(int channels);

    /**
     * Adds a {@link CompressorWithSidechaining} to the {@link ProcessingUnit}
     * without connecting any sidechain to it.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder compressor(int channels);

    /**
     * Adds a {@link Limiter} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     */
    ProcessingUnitBuilder limiter(int channels);

    /**
     * Allows to obtain the {@link ProcessingUnit}.
     * @return a {@link ProcessingUnit} with the ordered sequence of effects.
     * @throws IllegalStateException if no effects have been specified through this builder.
     * Remember that a {@link ProcessingUnit} can never be empty.
     */
    ProcessingUnit build() throws IllegalStateException;

}

package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;

/**
 * Makes the creation of a {@link ProcessingUnit} interactive, in that the client can specify any number
 * of the following effects in any order. The only method whose function is different is the sidechain method,
 * which allows to sidechain the {@link ProcessingUnit} to an external sound source. Said method is also
 * the only one that produces changes to state of the builder only the first time it is called. This is
 * because there can only be one {@link Sidechaining}.
 * Remember that the order of the calls is relevant because, as specified in the {@link ProcessingUnit}
 * documentation, the ordering by which {@link RPEffect}s are stored influences how sound is processed.
 * The only state which surely raises an {@link IllegalStateException} is the one where the method build is called
 * and no {@link AudioElement} had been specified, since a {@link ProcessingUnit} can never be empty.
 * Nonetheless, other exceptions may be thrown by particular instantiations of this interface.
 */
public interface ProcessingUnitBuilder {

    /**
     * Makes the {@link ProcessingUnit} have a {@link Sidechaining} tied to the given {@link UGen}.
     * @param u the {@link UGen} which this {@link ProcessingUnit} is sidechained to.
     * @param channels the number of input and output channels for the underlying compressor.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder sidechain(UGen u, int channels);

    /**
     * Adds a {@link LowPassFilter} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder lowPassFilter(int channels);

    /**
     * Adds a {@link HighPassFilter} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder highPassFilter(int channels);

    /**
     * Adds a {@link DigitalReverb} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder reverb(int channels);

    /**
     * Adds a {@link Gate} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder gate(int channels);

    /**
     * Adds a {@link Compression} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder compressor(int channels);

    /**
     * Adds a {@link Limiter} to the {@link ProcessingUnit}.
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    ProcessingUnitBuilder limiter(int channels);

    /**
     * Allows to obtain the {@link ProcessingUnit}.
     * @return a {@link ProcessingUnit} with the ordered sequence of effects.
     * @throws IllegalStateException if no {@link AudioElement} has been added through this builder.
     * Remember that a {@link ProcessingUnit} can never be empty.
     */
    ProcessingUnit build() throws IllegalStateException;

}

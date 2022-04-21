package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;

/**
 * Makes the creation of a {@link ProcessingUnit} interactive,
 * in that the client can specify any number of the following effects in any order.
 * The only state which raises an {@link IllegalStateException} is the one where the method build is called
 * and no effect had been specified, since a {@link ProcessingUnit} can never be empty.
 */
public interface ProcessingUnitBuilder {

    ProcessingUnitBuilder lowPassFilter();

    ProcessingUnitBuilder highPassFilter();

    ProcessingUnitBuilder reverb();

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
     * Allows to obtain the {@link ProcessingUnit}.
     * @return a {@link ProcessingUnit} with the ordered sequence of effects.
     * @throws IllegalStateException if no effects have been specified through this builder.
     * Remember that a {@link ProcessingUnit} can never be empty.
     */
    ProcessingUnit build() throws IllegalStateException;

}

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
     * Allows to sidechain the {@link ProcessingUnit} to another {@link UGen}.
     * @param u the {@link UGen} which this {@link ProcessingUnit} is sidechained to.
     */
    ProcessingUnitBuilder sidechain(UGen u);

    ProcessingUnitBuilder gate();

    ProcessingUnit build() throws IllegalStateException;

}

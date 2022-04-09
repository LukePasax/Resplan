package daw.core.channel;

/**
 * Makes the creation of a {@link ProcessingUnit} interactive,
 * in that the client can specify any number of effects in any order.
 * The only state which raises an {@link IllegalStateException} is the one where the method build is called
 * and no effect had been specified, since a {@link ProcessingUnit} can never be empty.
 */
public interface ProcessingUnitBuilder {

    ProcessingUnitBuilder filter();

    ProcessingUnitBuilder compressor();

    ProcessingUnitBuilder reverb();

    ProcessingUnitBuilder sidechain();

    ProcessingUnitBuilder gate();

    ProcessingUnit build() throws IllegalStateException;

}

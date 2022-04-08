package daw.core.channel;

/**
 * Makes the creation of a {@link ProcessingUnit} interactive,
 * in that the client can specify any number of effects in any order.
 * The only state which raises an {@link IllegalStateException} when the method build is called
 * is the one does not contain any effect, because a {@link ProcessingUnit} can never be empty.
 */
public interface ProcessingUnitBuilder {

    ProcessingUnitBuilder filter();

    ProcessingUnitBuilder delay();

    ProcessingUnitBuilder compressor();

    ProcessingUnitBuilder reverb();

    ProcessingUnitBuilder sidechain();

    ProcessingUnitBuilder gate();

    ProcessingUnit build() throws IllegalStateException;

}

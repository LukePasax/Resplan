package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;

/**
 * Sidechaining is a compression technique that makes the sound coming in from a source
 * duck the output signal of another source. It is useful when the volume of a source needs to be tied
 * to the volume of another source, so that the sounds do not overlap.
 * Practically, sidechaining is a property that a {@link ProcessingUnit} may possess.
 * A {@link ProcessingUnit} that is sidechained gets an input from another audio source to calculate the
 * level of compression. This external audio source can always be changed after initialization, by calling
 * the method setSidechain.
 */
public interface Sidechaining {

    /**
     * After this method is called, the channel that uses this sidechaining will be sidechained to
     * the source given as input, regardless if it was already sidechained to another source or not.
     * @param u
     */
    void setSidechain(UGen u);

}

package daw.core.audioprocessing;

/**
 * Sidechaining is a compression technique that makes the sound coming in from a source
 * duck the output signal of another source. It is useful when the volume of a source needs to be tied
 * to the volume of another source, so that the sounds do not overlap.
 * Practically, sidechaining is a property that a {@link ProcessingUnit} may possess.
 * A {@link ProcessingUnit} that is sidechained gets an input from another audio source to calculate the
 * level of compression.
 */
public interface Sidechaining {
}

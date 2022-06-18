package daw.core.audioprocessing;

/**
 * Sidechaining is a compression technique that makes the sound coming in from a source
 * duck the output signal of another source. In other words, sidechaining behaves just like compression,
 * except for the fact that the source which the compression level is based upon is different.
 * It is useful when the volume of a source needs to be tied to the volume of another source,
 * so that the sounds do not overlap. Practically, sidechaining is a property that a {@link ProcessingUnit}
 * may or may not possess.
 */
public interface Sidechaining {
}

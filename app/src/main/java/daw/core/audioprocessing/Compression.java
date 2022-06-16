package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class models compression, that is an audio signal processing operation which reduces the volume of
 * loud sounds or amplifies quiet sounds, thus reducing or compressing an audio signal's dynamic range.
 * This effect does not allow for a sidechain to be connected.
 * A limiter is a compressor with a high ratio and, generally, a short attack time.
 */
public class Compression extends AbstractCompression {

    /**
     * Constructs a compressor and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public Compression(@JsonProperty("ins") int channels) {
        super(channels);
    }

}

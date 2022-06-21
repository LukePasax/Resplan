package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class models a limiter, which is a constructor with the ratio fixed at positive infinite.
 * This means all audio signal that crosses the specified volume threshold is not just attenuated,
 * rather it is completely eliminated.
 */
public class Limiter extends Dynamics {

    /**
     * Constructs a limiter and sets its parameters to the current default.
     *
     * @param channels the number of inputs and outputs of this effect.
     */
    public Limiter(@JsonProperty("ins") int channels) {
        super(channels);
        this.compressor.setRatio(Float.POSITIVE_INFINITY);
    }

}

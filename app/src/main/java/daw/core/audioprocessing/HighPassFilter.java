package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Filtering boosts or attenuates frequencies in a sound, thus modifying the frequency spectrum of that sound.
 * High pass filters allow the high frequencies to pass while reducing or even eliminating some lower
 * frequencies.
 */
public class HighPassFilter extends Equalization {

    /**
     * Constructs a high-pass filter and sets its cutoff frequency to the given value.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public HighPassFilter(@JsonProperty("ins") int channels) {
        super(channels, false);
    }

}

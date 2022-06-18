package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Filtering boosts or attenuates frequencies in a sound, thus modifying the frequency spectrum of that sound.
 * Low pass filters allow the low frequencies to pass while reducing or even eliminating some higher
 * frequencies.
 */
public class LowPassFilter extends Equalization {

    /**
     * Constructs a low-pass filter and sets its cutoff frequency to the given value.
     * @param channels the number of inputs and outputs of this effect.
     * @param cutoffFrequency the frequency of cutoff.
     */
    @JsonCreator
    public LowPassFilter(@JsonProperty("ins") int channels, @JsonProperty("frequency") float cutoffFrequency) {
        super(channels, cutoffFrequency, true);
    }

}

package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Filtering boosts or attenuates frequencies in a sound, thus modifying the frequency spectrum of that sound.
 * High pass filters allow the high frequencies to pass while reducing or even eliminating some of the lower
 * frequencies.
 */
public class HighPassFilter extends AbstractFilter {

    /**
     * Constructs a high-pass filter and sets its cutoff frequency to the given value.
     * @param channels the number of inputs and outputs of this effect.
     * @param cutoffFrequency the frequency of cutoff.
     */
    @JsonCreator
    public HighPassFilter(@JsonProperty("ins") int channels, @JsonProperty("frequency") float cutoffFrequency) {
        super(channels, cutoffFrequency, false);
    }

}

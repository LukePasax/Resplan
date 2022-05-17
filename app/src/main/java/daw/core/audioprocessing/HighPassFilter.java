package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.OnePoleFilter;

import java.util.Map;

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
    public HighPassFilter(int channels, float cutoffFrequency) {
        super(channels, cutoffFrequency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        // TODO
    }

}

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
public class HighPassFilter extends RPEffect {

    private final OnePoleFilter filter;

    /**
     * Constructs a high-pass filter and sets its cutoff frequency to the given value.
     * @param channels the number of inputs and outputs of this effect.
     * @param cutoffFrequency the frequency of cutoff.
     */
    public HighPassFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), cutoffFrequency);
    }

    /**
     * {@inheritDoc}
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("frequency", this.filter.getFrequency());
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.filter.sendData(new DataBead(parameters));
    }

    /**
     * {@inheritDoc}
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    @Override
    // TODO
    protected float getDefaultValue(String key) {
        return 0.0f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        // TODO
    }

}

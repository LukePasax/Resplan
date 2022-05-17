package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.OnePoleFilter;

import java.util.Map;

/**
 * Filtering boosts or attenuates frequencies in a sound, thus modifying the frequency spectrum of that sound.
 * Low pass filters allow the low frequencies to pass while reducing or even eliminating some of the higher
 * frequencies.
 */
public class LowPassFilter extends RPEffect {

    private final OnePoleFilter filter;

    /**
     * Constructs a low-pass filter and sets its cutoff frequency to the given value.
     * @param channels the number of inputs and outputs of this effect.
     * @param cutoffFrequency the frequency of cutoff.
     */
    public LowPassFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), cutoffFrequency);
    }

    /**
     * {@inheritDoc}
     * a {@link Map} where the keys are the parameters and the values are the
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
     * Allows to get the number of input channels of the effect.
     * @return an integer that represents the number of input.
     */
    @Override
    public int getIns() {
        return this.filter.getIns();
    }

    /**
     * Allows to get the number of output channels of the effect.
     * @return an integer that represents the number of outputs.
     */
    @Override
    public int getOuts() {
        return this.filter.getOuts();
    }

    /**
     * {@inheritDoc}
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    @Override
    // TODO
    public float getDefaultValue(String key) {
        return 0.0f;
    }

    /**
     * Sets a default value for the parameter specified by the given key. If there is no such parameter,
     * this method does nothing.
     *
     * @param key   the name of a parameter.
     * @param value the value that the key has as default after this method is called.
     */
    @Override
    public void setDefaultValue(String key, float value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.filter.calculateBuffer();
    }

}

package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.OnePoleFilter;
import java.util.Map;

public abstract class AbstractFilter extends RPEffect {

    protected final OnePoleFilter filter;

    protected AbstractFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), cutoffFrequency);
    }

    /**
     * Allows getting the value associated to each parameter of this particular effect.
     * Parameters influence how the sound is processed by this effect.
     *
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("frequency", this.filter.getFrequency());
    }

    /**
     * Modifies the value of all the parameters specified as keys in the given map. Those parameters
     * will then contain the value associated to them in the map.
     * The keys that do not match any of the effect's parameters are ignored
     * and so are the values associated to them.
     *
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.filter.sendData(new DataBead(parameters));
    }

    /**
     * Allows to get the number of input channels of the effect.
     *
     * @return an integer that represents the number of input.
     */
    @Override
    public int getIns() {
        return this.filter.getIns();
    }

    /**
     * Allows to get the number of output channels of the effect.
     *
     * @return an integer that represents the number of outputs.
     */
    @Override
    public int getOuts() {
        return this.filter.getOuts();
    }

    /**
     * Allows getting the default value of a certain parameter of this particular effect.
     *
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    @Override
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
    public abstract void calculateBuffer();

}

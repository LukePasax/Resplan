package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.OnePoleFilter;
import java.util.Map;

public abstract class AbstractFilter extends RPEffect {

    protected final OnePoleFilter filter;

    protected AbstractFilter(int channels, float cutoffFrequency) {
        super(channels);
        this.filter = new OnePoleFilter(cutoffFrequency);
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
        final DataBead db = new DataBead();
        parameters.entrySet().forEach(i -> db.put(i.getKey(), i.getValue()));
        this.filter.sendData(db);
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
     * {@inheritDoc}
     * @return the {@link Gain} that represents the audio processed by the element.
     */
    public Gain getOutput() {
        final var out = new Gain(AudioContextManager.getAudioContext(), 1);
        out.addInput(this.filter);
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateBuffer();

}

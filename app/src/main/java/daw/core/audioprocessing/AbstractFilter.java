package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.CrossoverFilter;
import java.util.Map;

public abstract class AbstractFilter extends RPEffect {

    protected CrossoverFilter filter;

    protected AbstractFilter(int channels, float cutoffFrequency, boolean low) {
        super(channels);
        this.filter = new CrossoverFilter(AudioContextManager.getAudioContext(), 1, cutoffFrequency);
        this.filter.addInput(this.getGainIn());
        if (low) {
            this.filter.drawFromLowOutput(this.getGainOut());
        } else {
            this.filter.drawFromHighOutput(this.getGainOut());
        }
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
        db.putAll(parameters);
        this.filter.sendData(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.filter.calculateBuffer();
    }

}

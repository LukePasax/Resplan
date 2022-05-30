package daw.core.audioprocessing;

import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCompression extends RPEffect {

    protected final Compressor compressor;

    public AbstractCompression(int channels) {
        super(channels);
        this.compressor = new Compressor(channels);
    }

    /**
     * {@inheritDoc}
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("threshold", this.compressor.getThreshold(), "ratio", this.compressor.getRatio(),
                "attack", this.compressor.getAttack(), "decay", this.compressor.getDecay(),
                "current compression", this.compressor.getCurrentCompression());
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        final DataBead db = new DataBead();
        parameters.entrySet().forEach(i -> db.put(i.getKey(), i.getValue()));
        this.compressor.sendData(db);
    }

    /**
     * Allows to get the number of input channels of the effect.
     * @return an integer that represents the number of input.
     */
    @Override
    public int getIns() {
        return this.compressor.getIns();
    }

    /**
     * Allows to get the number of output channels of the effect.
     * @return an integer that represents the number of outputs.
     */
    @Override
    public int getOuts() {
        return this.compressor.getOuts();
    }

    /**
     * {@inheritDoc}
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    // TODO
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

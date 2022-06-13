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
     */
    @Override
    public abstract void calculateBuffer();

}

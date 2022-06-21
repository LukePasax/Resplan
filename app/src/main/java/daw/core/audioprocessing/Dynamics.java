package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;
import java.util.Map;

/**
 * Abstract class for all the "dynamics" effects. Dynamics effects adjust volume based upon a volume limit
 * (called a “threshold”), that the user can set. When the sound crosses the threshold,
 * the effect adjusts the volume based upon the type of effect and its settings.
 * Implementations of this class are {@link Compression}, {@link Limiter} and {@link SidechainingImpl}.
 */
public abstract class Dynamics extends RPEffect {

    protected final Compressor compressor;

    /**
     * Sets up a dynamics effect.
     * @param channels the number of inputs and outputs of this effect.
     */
    public Dynamics(int channels) {
        super(channels);
        this.compressor = new Compressor(AudioContextManager.getAudioContext(), channels);
        this.compressor.addInput(this.getGainIn());
        this.getGainOut().addInput(this.compressor);
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
        db.putAll(parameters);
        this.compressor.sendData(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }

}

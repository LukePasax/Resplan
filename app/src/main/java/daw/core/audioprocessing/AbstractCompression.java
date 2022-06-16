package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;
import java.util.Map;

public abstract class AbstractCompression extends RPEffect {

    protected final Compressor compressor;

    public AbstractCompression(int channels) {
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

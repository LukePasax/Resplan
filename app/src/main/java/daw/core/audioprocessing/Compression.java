package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;

import java.util.Map;

/**
 * This class models compression, that is an audio signal processing operation which reduces the volume of
 * loud sounds or amplifies quiet sounds, thus reducing or compressing an audio signal's dynamic range.
 * This effect does not allow for a sidechain to be connected.
 * A limiter is a compressor with a high ratio and, generally, a short attack time.
 */
public class Compression extends RPEffect {

    private final Compressor compressor;

    /**
     * Constructs a compressor and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    public Compression(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContextManager.getAudioContext(), channels);
    }

    /**
     * {@inheritDoc}
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("threshold", this.compressor.getThreshold(), "ratio", this.compressor.getRatio(),
                "attack", this.compressor.getAttack(), "decay", this.compressor.getDecay());
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.compressor.sendData(new DataBead(parameters));
    }

    /**
     * {@inheritDoc}
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    // TODO
    @Override
    protected float getDefaultValue(String key) {
        return 0.0f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }

}

package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a gate, which is a tool that reduces or eliminates noise coming from an audio source.
 * Specifically, a gate parses the audio signal and reduces the volume of all the samples that do not reach
 * a certain volume threshold.
 */
public class Gate extends RPEffect {

    private final Compressor compressor;

    /**
     * Constructs a gate and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    public Gate(int channels) {
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
        // TODO
    }

}

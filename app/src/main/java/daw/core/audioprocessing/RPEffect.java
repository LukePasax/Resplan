package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.Map;

/**
 * This class is an extension of {@link UGen}. In the context of this software, this class is the one
 * all effects must extend. Non-abstract subclasses must provide an implementation for method calculateBuffer.
 * The extension of both {@link UGen} and {@link AudioElement} allows this class to be both powerful
 * at audio processing and easy to use for clients.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Compression.class, name = "compression"),
        @JsonSubTypes.Type(value = Limiter.class, name = "limiter"),
        @JsonSubTypes.Type(value = HighPassFilter.class, name = "high pass"),
        @JsonSubTypes.Type(value = LowPassFilter.class, name = "low pass"),
        @JsonSubTypes.Type(value = Gate.class, name = "gate"),
        @JsonSubTypes.Type(value = DigitalReverb.class, name = "reverb"),
        @JsonSubTypes.Type(value = SidechainingImpl.class, name = "sidechaining")
})
public abstract class RPEffect extends UGen implements AudioElement {

    private final Gain gainIn;
    private final Gain gainOut;

    /**
     * Base constructor for all the effects of this software.
     * @param channels the number of inputs and outputs of this effect.
     */
    protected RPEffect(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), channels, 1.0f);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), channels, 1.0f);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public abstract Map<String, Float> getParameters();

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    public abstract void setParameters(Map<String, Float> parameters);

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getIns() {
        return this.gainIn.getIns();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getOuts() {
        return this.gainOut.getOuts();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Gain getGainIn() {
        return this.gainIn;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Gain getGainOut() {
        return this.gainOut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateBuffer();

}

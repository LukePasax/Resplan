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
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Compression.class, name = "compression"),
        @JsonSubTypes.Type(value = HighPassFilter.class, name = "high pass"),
        @JsonSubTypes.Type(value = LowPassFilter.class, name = "low pass"),
        @JsonSubTypes.Type(value = Gate.class, name = "gate"),
        @JsonSubTypes.Type(value = DigitalReverb.class, name = "reverb"),
        @JsonSubTypes.Type(value = BasicSidechaining.class, name = "sidechaining")
})
public abstract class RPEffect extends UGen implements AudioElement {

    /**
     * Base constructor for all the effects of this software.
     * @param channels the number of inputs and outputs of this effect.
     */
    protected RPEffect(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
    }

    /**
     * Allows getting the value associated to each parameter of this particular effect.
     * Parameters influence how the sound is processed by this effect.
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    public abstract Map<String, Float> getParameters();

    /**
     * Modifies the value of all the parameters specified as keys in the given map. Those parameters
     * will then contain the value associated to them in the map.
     * The keys that do not match any of the effect's parameters are ignored
     * and so are the values associated to them.
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    public abstract void setParameters(Map<String, Float> parameters);

    /**
     * Allows to get the number of input channels of the effect.
     * @return an integer that represents the number of input.
     */
    public abstract int getIns();

    /**
     * Allows to get the number of output channels of the effect.
     * @return an integer that represents the number of outputs.
     */
    public abstract int getOuts();

    /**
     * {@inheritDoc}
     * @return the {@link Gain} that represents the audio processed by the element.
     */
    public abstract Gain getOutput();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateBuffer();

}

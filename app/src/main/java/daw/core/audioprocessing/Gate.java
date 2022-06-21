package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import java.util.Map;

/**
 * Gate is the term for the audio device designed to reduce background noise coming in from an audio source.
 * Specifically, a gate parses the audio signal and reduces the volume of all the samples that do not reach
 * a certain volume threshold.
 * Gates are usually put at the end of the dynamics section of a processing unit to remove
 * all the noise generated by compressors.
 */
public class Gate extends RPEffect {

    private final GateEffect gateEffect;

    /**
     * Creates a gate and sets its parameters to the default value.
     * @param channels the number of inputs and outputs of this effect.
     */
    public Gate(@JsonProperty("ins") final int channels) {
        super(channels);
        this.gateEffect = new GateEffect(AudioContextManager.getAudioContext(), channels);
        this.gateEffect.addInput(this.getGainIn());
        this.getGainOut().addInput(this.gateEffect);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final Map<String, Float> getParameters() {
        return Map.of("threshold", this.gateEffect.getThreshold(), "ratio", this.gateEffect.getRatio(),
                "attack", this.gateEffect.getAttack(), "decay", this.gateEffect.getDecay(),
                "current compression", this.gateEffect.getCurrentCompression());
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public final void setParameters(final Map<String, Float> parameters) {
        final DataBead db = new DataBead();
        db.putAll(parameters);
        this.gateEffect.sendData(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void calculateBuffer() {
        this.gateEffect.calculateBuffer();
    }

}

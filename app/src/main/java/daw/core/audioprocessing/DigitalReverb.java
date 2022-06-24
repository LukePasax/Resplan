package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Reverb;
import java.util.Map;

/**
 * Reverberation, in acoustics, is a persistence of sound (echo) after a sound is produced.
 * Reverberation is created when a sound or signal is reflected causing numerous reflections to build up
 * and then decay as the sound is absorbed by the surfaces of objects in the space.
 * A reverb effect, or digital reverb, is an audio effect applied to a sound signal to simulate reverberation.
 */
public class DigitalReverb extends RPEffect {

    private static final float DEFAULT_WET_VALUE = 1.0f;
    private static final float DEFAULT_DRY_VALUE = 0.0f;

    private final Reverb rev;
    private final Gain wet;
    private final Gain dry;
    private float dryWetValue;

    /**
     * Constructs a reverb and sets its parameters to the default value.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public DigitalReverb(@JsonProperty("ins") final int channels) {
        super(channels);
        this.rev = new Reverb(AudioContextManager.getAudioContext(), channels);
        this.wet = new Gain(AudioContextManager.getAudioContext(), 2, DEFAULT_WET_VALUE);
        this.dry = new Gain(AudioContextManager.getAudioContext(), 2, DEFAULT_DRY_VALUE);
        this.initializeStructure();
    }

    private void initializeStructure() {
        this.rev.addInput(this.getGainIn());
        this.dry.addInput(this.getGainIn());
        this.wet.addInput(this.rev);
        this.getGainOut().addInput(0, this.dry, 0);
        this.getGainOut().addInput(0, this.wet, 0);
        this.getGainOut().addInput(1, this.dry, 1);
        this.getGainOut().addInput(1, this.wet, 1);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final Map<String, Float> getParameters() {
        return Map.of("damping", this.rev.getDamping(), "roomSize", this.rev.getSize(),
                "earlyReflectionsLevel", this.rev.getEarlyReflectionsLevel(),
                "lateReverbLevel", this.rev.getLateReverbLevel(), "dryWet", this.dryWetValue);
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public final void setParameters(final Map<String, Float> parameters) {
        final DataBead db = new DataBead();
        db.putAll(parameters);
        this.dry.setGain((float) Math.cos(Math.PI * this.dryWetValue / 2));
        this.wet.setGain((float) Math.sin(Math.PI * this.dryWetValue / 2));
        this.rev.sendData(db);
        if (parameters.containsKey("dryWet")) {
            this.dryWetValue = parameters.get("dryWet");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void calculateBuffer() {
        this.rev.calculateBuffer();
    }

}

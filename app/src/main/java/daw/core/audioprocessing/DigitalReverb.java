package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Reverb;
import java.util.Map;

/**
 * Reverberation, in acoustics, is a persistence of sound, or echo after a sound is produced.
 * Reverberation is created when a sound or signal is reflected causing numerous reflections to build up
 * and then decay as the sound is absorbed by the surfaces of objects in the space.
 * A reverb effect, or digital reverb, is an audio effect applied to a sound signal to simulate reverberation.
 */
public class DigitalReverb extends RPEffect {

    private final Reverb rev;

    /**
     * Constructs a reverb and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public DigitalReverb(@JsonProperty("ins") int channels) {
        super(channels);
        this.rev = new Reverb(AudioContextManager.getAudioContext(), channels);
        this.rev.addInput(this.getGainIn());
        this.getGainOut().addInput(this.getGainIn());
        this.getGainOut().addInput(this.rev);
    }

    /**
     * {@inheritDoc}
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("damping", this.rev.getDamping(), "roomSize", this.rev.getSize());
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        final DataBead db = new DataBead();
        db.putAll(parameters);
        this.rev.sendData(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.rev.calculateBuffer();
    }

}

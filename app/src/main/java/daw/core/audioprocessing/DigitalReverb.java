package daw.core.audioprocessing;

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
    public DigitalReverb(int channels) {
        super(channels);
        this.rev = new Reverb(channels);
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
        parameters.entrySet().forEach(i -> db.put(i.getKey(), i.getValue()));
        this.rev.sendData(db);
    }

    /**
     * Allows to get the number of input channels of the effect.
     * @return an integer that represents the number of input.
     */
    @Override
    public int getIns() {
        return this.rev.getIns();
    }

    /**
     * Allows to get the number of output channels of the effect.
     * @return an integer that represents the number of outputs.
     */
    @Override
    public int getOuts() {
        return this.rev.getOuts();
    }

    /**
     * {@inheritDoc}
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    // TODO
    @Override
    public float getDefaultValue(String key) {
        return 0.0f;
    }

    /**
     * Sets a default value for the parameter specified by the given key. If there is no such parameter,
     * this method does nothing.
     *
     * @param key   the name of a parameter.
     * @param value the value that the key has as default after this method is called.
     */
    @Override
    public void setDefaultValue(String key, float value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.rev.calculateBuffer();
    }

}

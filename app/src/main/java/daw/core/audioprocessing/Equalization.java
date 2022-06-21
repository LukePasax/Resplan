package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.CrossoverFilter;
import java.util.Map;

/**
 * Equalization is the process of adjusting the volume of different frequency bands within an audio signal.
 * Different types of filters are used to achieve this result. These filters include high pass and low pass filter,
 * which are implemented in this software as subclasses of this abstract class.
 */
public abstract class Equalization extends RPEffect {

    private static final float LOW_PASS_DEFAULT_FREQUENCY = 18000.0f;
    private static final float HIGH_PASS_DEFAULT_FREQUENCY = 90.0f;

    protected CrossoverFilter filter;

    /**
     * Sets up an equalization effect, which can be either a low pass filter or a high pass filter.
     * @param channels the number of inputs and outputs of this effect.
     * @param low true if the requested filter is the low pass, false if it is the high pass.
     */
    protected Equalization(int channels, boolean low) {
        super(channels);
        this.filter = new CrossoverFilter(AudioContextManager.getAudioContext(), 1);
        this.filter.addInput(this.getGainIn());
        if (low) {
            this.filter.setFrequency(LOW_PASS_DEFAULT_FREQUENCY);
            this.filter.drawFromLowOutput(this.getGainOut());
        } else {
            this.filter.setFrequency(HIGH_PASS_DEFAULT_FREQUENCY);
            this.filter.drawFromHighOutput(this.getGainOut());
        }
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("frequency", this.filter.getFrequency());
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        final DataBead db = new DataBead();
        db.putAll(parameters);
        this.filter.sendData(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.filter.calculateBuffer();
    }

}

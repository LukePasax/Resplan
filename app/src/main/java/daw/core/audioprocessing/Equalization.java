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

    protected CrossoverFilter filter;

    /**
     * Sets up an equalization effect, which can be either a low pass filter or a high pass filter.
     * @param channels the number of inputs and outputs of this effect.
     * @param cutoffFrequency the frequency over (or under) which volume is attenuated.
     * @param low true if the requested filter is the low pass, false if it is the high pass.
     */
    protected Equalization(int channels, float cutoffFrequency, boolean low) {
        super(channels);
        this.filter = new CrossoverFilter(AudioContextManager.getAudioContext(), 1, cutoffFrequency);
        this.filter.addInput(this.getGainIn());
        if (low) {
            this.filter.drawFromLowOutput(this.getGainOut());
        } else {
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

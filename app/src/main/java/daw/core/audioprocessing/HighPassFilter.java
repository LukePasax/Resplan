package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.OnePoleFilter;

public class HighPassFilter extends RPEffect {

    private final OnePoleFilter filter;

    public HighPassFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), cutoffFrequency);
    }

    /**
     *
     * @param frequency
     */
    public void setFrequency(float frequency) {
        this.filter.setFrequency(frequency);
    }

    /**
     *
     * @return
     */
    public float getFrequency() {
        return this.filter.getFrequency();
    }

    @Override
    public void calculateBuffer() {
        // TODO
    }

}

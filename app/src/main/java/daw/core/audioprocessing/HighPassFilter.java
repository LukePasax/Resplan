package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.OnePoleFilter;

public class HighPassFilter extends RPEffect {

    private final OnePoleFilter filter;

    public HighPassFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), cutoffFrequency);
    }

    @Override
    public void calculateBuffer() {
        // TODO
    }

}

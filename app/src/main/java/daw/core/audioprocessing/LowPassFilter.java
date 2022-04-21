package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.OnePoleFilter;

public class LowPassFilter extends RPEffect {

    private final OnePoleFilter filter;

    public LowPassFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), 0);
    }

    @Override
    public void calculateBuffer() {
        this.filter.calculateBuffer();
    }

}

package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.Reverb;

public class DigitalReverb extends RPEffect {

    private final Reverb rev;

    public DigitalReverb(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.rev = new Reverb(AudioContextManager.getAudioContext(), channels);
    }

    @Override
    public void calculateBuffer() {
        this.rev.calculateBuffer();
    }
}

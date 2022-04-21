package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.Compressor;

public class Limiter extends RPEffect {

    private final Compressor compressor;

    public Limiter(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContextManager.getAudioContext(), channels)
                .setRatio(Float.POSITIVE_INFINITY);
    }

    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }
}

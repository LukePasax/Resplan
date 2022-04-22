package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.Compressor;

/**
 * This class models compression, that is an audio signal processing operation which reduces the volume of
 * loud sounds or amplifies quiet sounds, thus reducing or compressing an audio signal's dynamic range.
 * This effect does not allow for a sidechain to be connected.
 * A limiter is a compressor with a high ratio and, generally, a short attack time.
 */
public class Compression extends RPEffect {

    private final Compressor compressor;

    public Compression(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new net.beadsproject.beads.ugens.Compressor(AudioContextManager.getAudioContext(), channels);
    }

    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }
}

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

    /**
     * Constructs a compressor and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    public Compression(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContextManager.getAudioContext(), channels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }
}

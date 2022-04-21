package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.Compressor;

/**
 * This class represents a gate, which is a tool to reduce or eliminate noise from an audio source.
 * Specifically, a gate parses the audio signal and reduces the volume of all the samples that do not reach
 * a certain volume threshold.
 */
public class Gate extends RPEffect {

    private final Compressor compressor;

    public Gate(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContextManager.getAudioContext(), channels);
    }

    @Override
    public void calculateBuffer() {
        // TO DO
    }

}

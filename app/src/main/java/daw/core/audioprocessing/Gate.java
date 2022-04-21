package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.Compressor;

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

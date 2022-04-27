package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Compressor;

/**
 * Sidechaining is a compression technique that makes the sound coming in from a source
 * “duck” the output signal of another source. It is useful when the volume of a source needs to be tied
 * to the volume of another source, so that the sounds don't overlap.
 * Practically, sidechaining is a property that a {@link ProcessingUnit} may possess.
 * A {@link ProcessingUnit} that is sidechained gets an input from another audio source to calculate the
 * level of compression. This external audio source cannot be changed after this class has been instantiated,
 * neither can it be removed.
 */
public class Sidechaining extends UGen {

    private final Compressor compressor;

    public Sidechaining(UGen u, int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContext.getDefaultContext(), channels).setSideChain(u);
    }

    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }
}

package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;

import java.util.Map;

/**
 * Basic implementation of {@link Sidechaining}, that allows for the sidechained source to be changed after
 * initialization.
 * <p> NOTE: Due to how the Beads library is structured (namely the fact that UGen is an abstract class
 * instead of an interface), this implementation of {@link Sidechaining} is the only one currently supported.
 * This means all clients must use this class when sidechaining is needed. </p>
 */
public class BasicSidechaining extends AbstractCompression implements Sidechaining, AudioElement {

    /**
     * Constructs a {@link BasicSidechaining} object with the given parameters.
     * @param u the channel to be sidechained.
     * @param channels the number of inputs and outputs of the given channel.
     */
    public BasicSidechaining(UGen u, int channels) {
        super(channels);
        this.compressor.setSideChain(u);
    }

    /**
     * After this method is called, the channel that uses this sidechaining will be sidechained to
     * the source given as input, regardless if it was already sidechained to another source or not.
     * @param u
     */
    public void setSidechain(UGen u) {
        this.compressor.setSideChain(u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }

}

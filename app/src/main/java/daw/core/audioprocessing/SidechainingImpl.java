package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.beadsproject.beads.core.UGen;

/**
 * Basic implementation of {@link Sidechaining}, that allows for the sidechained source to be changed after
 * initialization.
 * <p> NOTE: Due to how the Beads library is structured (namely the fact that UGen is an abstract class
 * instead of an interface), this implementation of {@link Sidechaining} is the only one currently supported.
 * This means all clients must use this class when sidechaining is needed. </p>
 */
public class SidechainingImpl extends AbstractCompression implements Sidechaining, AudioElement {

    /**
     * Constructs a {@link SidechainingImpl} object with the given parameters.
     * @param u the channel to be sidechained.
     * @param channels the number of inputs and outputs of the given channel.
     */
    public SidechainingImpl(UGen u, int channels) {
        super(channels);
        this.compressor.setSideChain(u);
    }

    @JsonCreator
    private SidechainingImpl(@JsonProperty("ins") int channels) {
        super(channels);
    }

}

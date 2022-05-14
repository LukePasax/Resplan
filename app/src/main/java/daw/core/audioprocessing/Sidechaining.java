package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Compressor;

import java.util.Map;

/**
 * Sidechaining is a compression technique that makes the sound coming in from a source
 * duck the output signal of another source. It is useful when the volume of a source needs to be tied
 * to the volume of another source, so that the sounds do not overlap.
 * Practically, sidechaining is a property that a {@link ProcessingUnit} may possess.
 * A {@link ProcessingUnit} that is sidechained gets an input from another audio source to calculate the
 * level of compression. This external audio source can always be changed after initialization, by calling
 * the method setSidechain.
 */
public class Sidechaining extends UGen {

    private final Compressor compressor;

    /**
     * Constructs a {@link Sidechaining} object with the given parameters.
     * Remember to pass the correct sidechained channel, because it cannot be changed after the instantiation.
     * @param u the channel to be sidechained.
     * @param channels the number of inputs and outputs of the given channel.
     */
    public Sidechaining(UGen u, int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContext.getDefaultContext(), channels).setSideChain(u);
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
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    public Map<String, Float> getParameters() {
        return Map.of("threshold", this.compressor.getThreshold(), "ratio", this.compressor.getRatio(),
                "attack", this.compressor.getAttack(), "decay", this.compressor.getDecay());
    }

    /**
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    public void setParameters(Map<String, Float> parameters) {
        this.compressor.sendData(new DataBead(parameters));
    }

    /**
     * Allows to get the number of input channels of the effect.
     * @return an integer that represents the number of input.
     */
    @Override
    public int getIns() {
        return this.compressor.getIns();
    }

    /**
     * Allows to get the number of output channels of the effect.
     * @return an integer that represents the number of outputs.
     */
    @Override
    public int getOuts() {
        return this.compressor.getOuts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }
}

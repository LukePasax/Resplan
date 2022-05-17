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
public class BasicSidechaining extends UGen implements Sidechaining, AudioElement {

    private final Compressor compressor;

    /**
     * Constructs a {@link BasicSidechaining} object with the given parameters.
     * Remember to pass the correct sidechained channel, because it cannot be changed after the instantiation.
     * @param u the channel to be sidechained.
     * @param channels the number of inputs and outputs of the given channel.
     */
    public BasicSidechaining(UGen u, int channels) {
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
     * Allows getting the default value of a certain parameter of this particular effect.
     *
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    @Override
    public float getDefaultValue(String key) {
        return 0;
    }

    /**
     * Sets a default value for the parameter specified by the given key. If there is no such parameter,
     * this method does nothing.
     *
     * @param key   the name of a parameter.
     * @param value the value that the key has as default after this method is called.
     */
    @Override
    public void setDefaultValue(String key, float value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }
}

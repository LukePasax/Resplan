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
     *
     * @param attack
     */
    public void setAttack(float attack) {
        this.compressor.setAttack(attack);
    }

    /**
     *
     * @param threshold
     */
    public void setThreshold(float threshold) {
        this.compressor.setThreshold(threshold);
    }

    /**
     *
     * @param decay
     */
    public void setDecay(float decay) {
        this.compressor.setDecay(decay);
    }

    /**
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        this.compressor.setRatio(ratio);
    }

    /**
     *
     * @return
     */
    public float getAttack() {
        return this.compressor.getAttack();
    }

    /**
     *
     * @return
     */
    public float getThreshold() {
        return this.compressor.getThreshold();
    }

    /**
     *
     * @return
     */
    public float getDecay() {
        return this.compressor.getDecay();
    }

    /**
     *
     * @return
     */
    public float getRatio() {
        return this.compressor.getRatio();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }

}

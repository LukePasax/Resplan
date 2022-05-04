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

    @Override
    public void calculateBuffer() {
        // TODO
    }

}

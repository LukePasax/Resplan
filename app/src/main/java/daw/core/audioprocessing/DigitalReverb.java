package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.ugens.Reverb;

public class DigitalReverb extends RPEffect {

    private final Reverb rev;

    public DigitalReverb(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.rev = new Reverb(AudioContextManager.getAudioContext(), channels);
    }

    /**
     *
     * @param damping
     */
    public void setDamping(float damping) {
        this.rev.setDamping(damping);
    }

    /**
     *
     * @param roomSize
     */
    public void setRoomSize(float roomSize) {
        this.rev.setSize(roomSize);
    }

    /**
     *
     * @return
     */
    public float getDamping() {
        return this.rev.getDamping();
    }

    /**
     *
     * @return
     */
    public float getRoomSize() {
        return this.rev.getSize();
    }

    @Override
    public void calculateBuffer() {
        this.rev.calculateBuffer();
    }

}

package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Reverb;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigitalReverb extends RPEffect {

    private final Reverb rev;

    /**
     *
     * @param channels
     */
    public DigitalReverb(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.rev = new Reverb(AudioContextManager.getAudioContext(), channels);
    }

    /**
     *
     * @return
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("damping", this.rev.getDamping(), "roomSize", this.rev.getSize());
    }

    /**
     *
     * @param parameters
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.rev.sendData(new DataBead(parameters));
    }

    /**
     *
     * @param key
     * @return
     */
    // TODO
    @Override
    protected float getDefaultValue(String key) {
        return 0.0f;
    }

    /**
     *
     */
    @Override
    public void calculateBuffer() {
        this.rev.calculateBuffer();
    }

}

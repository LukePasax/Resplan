package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.OnePoleFilter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighPassFilter extends RPEffect {

    private final OnePoleFilter filter;

    public HighPassFilter(int channels, float cutoffFrequency) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.filter = new OnePoleFilter(AudioContextManager.getAudioContext(), cutoffFrequency);
    }

    /**
     *
     * @return
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("frequency", this.filter.getFrequency());
    }

    /**
     *
     * @param parameters
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.filter.sendData(new DataBead(parameters));
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    // TODO
    protected float getDefaultValue(String key) {
        return 0.0f;
    }

    /**
     *
     */
    @Override
    public void calculateBuffer() {
        // TODO
    }

}

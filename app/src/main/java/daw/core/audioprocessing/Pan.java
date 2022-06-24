package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.Map;

/**
 * Pan is the element that allows to accomplish audio panning.
 * Panning is the distribution of an audio signal into a new stereo or multichannel sound field,
 * as determined by pan's value.
 */
public class Pan extends UGen implements AudioElement {

    private final Gain gainIn;
    private final Gain gainOut;
    private final Gain left;
    private final Gain right;
    private float value;

    /**
     * Construct a Pan whose power remains constant regardless of its position.
     * This means that center-panning a signal will yield the input signal multiplied by 1 / sqrt(2)
     * in each output channel as a result. Setting the value to -1 pans completely to the left,
     * whereas 1 pans completely to the right and 0 (the default value) results in center panning.
     */
    public Pan() {
        super(AudioContextManager.getAudioContext());
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), 2, 1.0f);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), 2, 1.0f);
        this.left = new Gain(AudioContextManager.getAudioContext(), 1, (float) (1 / Math.sqrt(2.0)));
        this.right = new Gain(AudioContextManager.getAudioContext(), 1, (float) (1 / Math.sqrt(2.0)));
        this.initializeStructure();
    }

    private void initializeStructure() {
        this.left.addInput(0, this.gainIn, 0);
        this.right.addInput(0, this.gainIn, 1);
        this.gainOut.addInput(0, this.left, 0);
        this.gainOut.addInput(1,this.right,0);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Map<String, Float> getParameters() {
        return Map.of("value", this.value);
    }

    /**
     * {@inheritDoc}
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.value = parameters.get("value");
        this.left.setGain((float) Math.cos(Math.PI * (1 + this.value) / 4));
        this.right.setGain((float) Math.sin(Math.PI * (1 + this.value) / 4));
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getIns() {
        return this.gainIn.getIns();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getOuts() {
        return this.gainOut.getOuts();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Gain getGainIn() {
        return this.gainIn;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Gain getGainOut() {
        return this.gainOut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateBuffer() {
    }

}
